package main

import (
	"icancode/game"
	"icancode/solver"
	"log"
)

func main() {
	// you can get this code after registration on the server with your email
	browserURL := "http://codenjoy.com:80/codenjoy-contest/board/player/{player-id}?code={code}"

	game, c := game.StartGame(browserURL)
	b := game.GetBoard()
	s := solver.New()

	for {
		select {
		case <-c.Done:
			log.Fatal("It's done")
		case <-c.Read:
			// Set next move
			game.SetNextAction(s.GetNextAction(b))
			c.Write <- struct{}{}
		}
	}
}
