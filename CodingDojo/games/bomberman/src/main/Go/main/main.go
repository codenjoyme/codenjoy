package main

import (
	"log"

	"github.com/dnahurnyi/bomberman"
)

func main() {
	// paste here board page url from browser after registration
	// or put it as command line parameter
	browserURL := "http://codenjoy.com:80/codenjoy-contest/board/player/3edq63tw0bq4w4iem7nb?code=1234567890123456789"
	game, c := bomberman.StartGame(browserURL)

	for {
		select {
		case <-c.Done:
			log.Fatal("It's done")
		case <-c.Read:
			// Make your move

			game.Move(bomberman.MoveFire(bomberman.UP))
			c.Write <- struct{}{}
		}
	}
}
