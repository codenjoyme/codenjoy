package network

import (
	"encoding/json"
	"fmt"
	"log"
	"net/url"

	"github.com/arukim/expansion/models"
	"github.com/arukim/expansion/player"
	"github.com/gorilla/websocket"
)

type Client struct {
	Url string
	Id  int
}

func NewClient(id int, url string) *Client {
	c := &Client{Url: url, Id: id}

	go c.run()

	return c
}

func (c *Client) run() {

	u, err := url.Parse(c.Url)
	if err != nil {
		panic(err)
	}

	log.Printf("connecting to %s", u.String())

	conn, _, err := websocket.DefaultDialer.Dial(u.String(), nil)
	if err != nil {
		log.Fatal("dial:", err)
	}

	player := player.NewPlayer(c.Id)
	go func() {
		defer conn.Close()
		for {
			_, message, err := conn.ReadMessage()
			if err != nil {
				log.Println("read:", err)
				return
			}

			msg := ""

			turnInfo := models.TurnInfo{}
			//fmt.Printf("%s", message)
			json.Unmarshal(message[6:], &turnInfo)

			t := player.MakeTurn(&turnInfo)
			if t != nil {
				payload, _ := json.Marshal(t)
				msg = fmt.Sprintf("message('%s')", payload)
			} else {
				//log.Println("Sending act(0)!")
				//msg = "act(0)"
			}
			//log.Printf("%s\n", msg)
			//time.Sleep(100 * time.Millisecond)
			conn.WriteMessage(websocket.TextMessage, []byte(msg))
			//log.Printf("recv: %s", message)
			//log.Printf("turn info: %+v", turnInfo)
		}
	}()
}
