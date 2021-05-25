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

package game

import (
	"battlecity/action"
	"fmt"
	"github.com/gorilla/websocket"
	"log"
	"math"
	"net/url"
	"strings"
)

const (
	gameQueryTemplate = "user=%s&code=%s"
)

var (
	gameProtocolS = "wss"
	gameProtocol  = "ws"
)

var nothingPtr = action.DoNothing()

type game struct {
	brd *Board
}

type communication struct {
	Done  chan struct{}
	Read  chan struct{}
	Write chan struct{}
}

// StartGame creates connection to the game and returns game interface to play and channel to close the connection.
// Game will be played without command from client, default action is STOP
func StartGame(browserURL string) (game, communication) {
	u, err := createURL(browserURL)
	if err != nil {
		log.Panicln("Failed to create valid game url, err: ", err)
	}

	conn, err := getConnection(u)
	if err != nil {
		log.Panicln("Failed to create connection to game, err: ", err)
	}

	c := communication{
		Done:  make(chan struct{}),
		Read:  make(chan struct{}),
		Write: make(chan struct{}),
	}
	brd := &Board{}

	// Constantly read data from socket
	go readWriteSocket(brd, conn, c)

	return game{
		brd: brd,
	}, c
}

// GetBoard returns current state of board
func (g game) GetBoard() *Board {
	return g.brd
}

// Move allows client to manipulate bomberman, client should
// assign new action every second or STOP action will be used as a default
func (g game) SetNextAction(move action.Action) {
	g.brd.m.Lock()
	g.brd.command = &move
	g.brd.m.Unlock()
}

// readWriteSocket reads socket (that should send a message once per second)
// and responds with Move from board command
func readWriteSocket(brd *Board, conn *websocket.Conn, c communication) {
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
			c.Read <- struct{}{} // Make a signal to the client that it's time to make an action
			<-c.Write            // Wait for client response
			action := brd.getAction()
			log.Println(action)
			if err := conn.WriteMessage(websocket.TextMessage, []byte(action)); err != nil {
				log.Println("Failed to write command to the game server, err: ", err)
				close(c.Done)
				return
			}
		}
	}
}

func updateBoard(m string, b *Board) error {
	boardContent := strings.Replace(m, "board=", "", 1)
	b.msg = &msg{}
	b.msg.ContentRune = []rune(boardContent)
	b.size = int(math.Sqrt(float64(len(b.msg.ContentRune))))
	fmt.Println(b.Show()) // Use this to display game board in console output
	return nil
}

func getConnection(u url.URL) (*websocket.Conn, error) {
	log.Printf("connecting to %s", u.String())
	conn, _, err := websocket.DefaultDialer.Dial(u.String(), nil)
	return conn, err
}

// createURL creates valid connection URL from raw url copied from browser url input in the game window
// example browserURL: "https://epam-botchallenge.com/codenjoy-contest/board/player/793wdxskw521spo4mn1y?code=531459153668826800&gameName=battlecity"
func createURL(browserURL string) (url.URL, error) {
	gURL, err := url.Parse(browserURL)
	if err != nil {
		return url.URL{}, err
	}
	pp := strings.Split(gURL.Path, "/")

	u := url.URL{
		Path:     pp[1] + "/ws",
		RawQuery: fmt.Sprintf(gameQueryTemplate, pp[4], strings.Split(gURL.RawQuery, "=")[1]),
		Scheme:   gameProtocolS,
		Host:     gURL.Host,
	}

	if u.Scheme == "https" {
		u.Scheme = gameProtocolS
	}

	return u, nil
}
