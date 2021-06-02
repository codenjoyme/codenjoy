/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

package bomberman

import (
	"errors"
	"fmt"
	"log"
	"net/url"
	"regexp"
	"strings"
	"sync"

	"github.com/gorilla/websocket"
)

var stopPtr = Stop()

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

func (b *board) Show() string {
	repr := strings.Builder{}
	for i := 0; i < b.boardSize(); i++ {
		for j := 0; j < b.boardSize(); j++ {
			repr.Write([]byte(fmt.Sprintf("%c", b.boardContent[i*b.boardSize()+j])))
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
	r := regexp.MustCompile("board=.*")
	if !r.MatchString(msg) {
		return errors.New("Invalid input, input msg: " + msg)
	}
	boardContent := strings.Replace(msg, "board=", "", 1)
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
// example browserURL: "https://codenjoy.com/codenjoy-contest/board/player/793wdxskw521spo4mn1y?code=531459153668826800&gameName=bomberman"
func createURL(browserURL string) (url.URL, error) {
	r := regexp.MustCompile("(http|https)://(.*)/codenjoy-contest/board/player/(\\w*)\\?code=(\\d*)(&game=bomberman)?")
	params := r.FindStringSubmatch(browserURL)
	if params == nil {
		return url.URL{}, errors.New("Invalid URL, url: " + browserURL)
	}

	var gameProtocol string
	protocol := params[1]
	if strings.EqualFold(protocol, "http") {
		gameProtocol = "ws"
	} else {
		gameProtocol = "wss"
	}

	host := params[2]
	player := params[3]
	code := params[4]

	u := url.URL{
		Scheme:   gameProtocol,
		Host:     host,
		Path:     "/codenjoy-contest/ws",
		RawQuery: fmt.Sprintf("user=%s&code=%s&game=bomberman", player, code),
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
