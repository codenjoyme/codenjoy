package main

import (
	"icancode/game"
	"icancode/solver"
	"log"
)

func main() {
	browserURL := "https://epam-botchallenge.com/codenjoy-contest/board/player/{player-id}?code={code}&gameName=icancode"

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
