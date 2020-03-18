package bomberman

import (
	"errors"
	"fmt"
	"github.com/gorilla/websocket"
	"log"
	"net/url"
	"os"
	"strings"
	"sync"
)

var stopPtr = STOP

type board struct {
	rawBoard     string // to avoid transformation from
	boardContent []rune
	command      *Action
	sync.Mutex
}

type Game interface {
	Move(move Action)
	Show() string
	CommonAPI
}

const (
	gamePath          = "/codenjoy-contest/ws"
	gameQueryTemplate = "user=%s&code=%s&gameName=bomberman"
)

// It's var for testing purposes
var gameProtocol = "wss"

func (b *board) Show() string {
	repr := strings.Builder{}
	for i := 0; i < BoardSize; i++ {
		for j := 0; j < BoardSize; j++ {
			repr.Write([]byte(fmt.Sprintf("%c", b.boardContent[i*33+j])))
		}
		repr.Write([]byte("\n"))
	}
	return repr.String()
}

// Move allows client to manipulate bomberman, client should
// assign new action every second or STOP action will be used as a default
func (b *board) Move(move Action) {
	if move.IsValid() {
		b.Lock()
		b.command = &move
		b.Unlock()
	}
}

// getAction returns last action that client assigned to board command
// or STOP if client didn't assigned any action for this move
func (b *board) getAction() Action {
	b.Lock()
	defer b.Unlock()
	// Every time we clean next command
	defer func() {
		b.command = &stopPtr
	}()
	if b.command == nil {
		return STOP
	}
	return *b.command
}

func updateBoard(msg string, b *board) error {
	boardContent := strings.Replace(msg, "board=", "", 1)
	if len([]rune(boardContent)) != BoardSize*BoardSize {
		return errors.New("Invalid input, board size is not valid, input msg: " + msg)
	}
	b.rawBoard = boardContent
	b.boardContent = []rune(boardContent)
	//b.Show() // Use this to display game board in console output
	return nil
}

func getConnection(u url.URL) (*websocket.Conn, error) {
	log.Printf("connecting to %s", u.String())
	conn, _, err := websocket.DefaultDialer.Dial(u.String(), nil)
	return conn, err
}

// createURL creates valid connection URL from raw url copied from browser url input in the game window
// example browserURL: "https://dojorena.io/codenjoy-contest/board/player/793wdxskw521spo4mn1y?code=531459153668826800&gameName=bomberman"
func createURL(browserURL string) (url.URL, error) {
	// try params from env
	envHost := os.Getenv("HOST")
	envPlayer := os.Getenv("PLAYER")
	envCode := os.Getenv("CODE")
	if len(envHost) != 0 && len(envPlayer) != 0 && len(envCode) != 0 {
		log.Println("Took HOST, PLAYER and CODE values from environment")
		return url.URL{
			Scheme:   gameProtocol,
			Host:     envHost,
			Path:     gamePath,
			RawQuery: fmt.Sprintf(gameQueryTemplate, envPlayer, envCode),
		}, nil
	} else {
		if len(envHost) == 0 {
			log.Println("Can't get env variable HOST")
		}
		if len(envPlayer) == 0 {
			log.Println("Can't get env variable PLAYER")
		}
		if len(envCode) == 0 {
			log.Println("Can't get env variable CODE")
		}
	}

	// get host name
	mutatedUrl := strings.Replace(browserURL, "https://", "", 1)
	urlParts := strings.Split(mutatedUrl, "/")
	if len(urlParts) != 5 {
		return url.URL{}, errors.New("Invalid URL, can't get host name, url: " + browserURL)
	}
	host := urlParts[0]

	// get player id
	mutatedUrl = strings.Replace(mutatedUrl, host+"/codenjoy-contest/board/player/", "", 1)
	urlParts = strings.Split(mutatedUrl, "?")
	if len(urlParts) != 2 {
		return url.URL{}, errors.New("Invalid URL, can't get player ID, url: " + browserURL)
	}
	player := urlParts[0]

	// get game code
	mutatedUrl = strings.Replace(mutatedUrl, player+"?code=", "", 1)
	urlParts = strings.Split(mutatedUrl, "&")
	if len(urlParts) != 2 {
		return url.URL{}, errors.New("Invalid URL, can't get game code, url: " + browserURL)
	}
	code := urlParts[0]

	u := url.URL{
		Scheme:   gameProtocol,
		Host:     host,
		Path:     gamePath,
		RawQuery: fmt.Sprintf(gameQueryTemplate, player, code),
	}
	return u, nil
}

type Communication struct {
	Done  chan struct{}
	Read  chan struct{}
	Write chan struct{}
}

// readWriteSocket reads socket (that should send a message once per second)
// and responds with move from board command
func readWriteSocket(brd *board, conn *websocket.Conn, c Communication) {
	for {
		select {
		case <-c.Done:
			log.Println("Client closed connection")
			return
		default:
			_, message, err := conn.ReadMessage()
			if err != nil {
				log.Println("Server closed connection, err:", err)
				close(c.Done)
				return
			}
			err = updateBoard(string(message), brd)
			if err != nil {
				log.Println("Invalid message from server, err:", err)
				close(c.Done)
				return
			}
			c.Read <- struct{}{} // Make a signal to the client that it's time to make a move
			<-c.Write            // Wait for client response
			//time.Sleep(time.Millisecond * 500) // 0.5 second to think what to respond
			move := brd.getAction()
			if err := conn.WriteMessage(websocket.TextMessage, []byte(move)); err != nil {
				log.Println("Failed to write command to the game server, err: ", err)
				close(c.Done)
				return
			}
		}
	}
}

// StartGame creates connection to the game and returns game interface to play and channel to close the connection.
// Game will be played without command from client, default action is STOP
func StartGame(browserURL string) (Game, Communication) {
	u, err := createURL(browserURL)
	if err != nil {
		log.Panicln("Failed to create valid game url, err: ", err)
	}

	conn, err := getConnection(u)
	if err != nil {
		log.Panicln("Failed to create connection to game, err: ", err)
	}

	c := Communication{
		Done:  make(chan struct{}),
		Read:  make(chan struct{}),
		Write: make(chan struct{}),
	}
	brd := &board{}

	// Constantly read data from socket
	go readWriteSocket(brd, conn, c)

	return brd, c
}
