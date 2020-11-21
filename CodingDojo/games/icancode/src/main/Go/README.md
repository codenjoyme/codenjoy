# icancode client
Client for the icancode game

Check details at: https://epam-botchallenge.com/rules

####Dev setup:

Install Go 1.11 or higher.

Implement your bot logic in solver package.

Use next code snippet to run your bot:
```
package main

import (
	"log"

	"icancode/api"
)

func main() {
	browserURL := "https://epam-botchallenge.com/codenjoy-contest/board/player/{player-id}?code={code}&gameName=bomberman"
        game, c := api.StartGame(browserURL)
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

```
