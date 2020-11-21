package solver

import (
	"icancode/action"
	"icancode/api"
)

type Solver struct {
}

func New() Solver {
	return Solver{}
}

func (s Solver) GetNextAction(b *api.Board) action.Action {
	//todo: your code here

	return action.DoNothing()
}
