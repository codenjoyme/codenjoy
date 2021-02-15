package solver

import (
	"icancode/action"
	"icancode/game"
)

type Solver struct {
}

func New() Solver {
	return Solver{}
}

func (s Solver) GetNextAction(b *game.Board) action.Action {
	//todo: your code here

	return action.DoNothing()
}
