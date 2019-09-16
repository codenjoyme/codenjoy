package advisors

import "github.com/arukim/expansion/game"
import m "github.com/arukim/expansion/models"

// General contains game logic
type Internal struct {
}

func NewInternal() *Internal {
	return &Internal{}
}

func (g *Internal) MakeTurn(b *game.Board, t *m.Turn) {

	//b.InsideMap.Print()
	for i := 0; i < b.Size; i++ {
		if b.InsideMap.Data[i] > 2 && b.ForcesMap.Data[i] > 1 {
			p1 := m.NewPoint(i, b.Width)
			move := b.GetDirection(p1, b.InsideMap)
			move.Count = b.ForcesMap.Data[i] - 1

			t.Movements = append(t.Movements, *move)
		}
	}
}
