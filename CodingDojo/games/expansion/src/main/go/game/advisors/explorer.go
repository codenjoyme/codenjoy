package advisors

import (
	"math"

	linq "github.com/ahmetb/go-linq"
	"github.com/arukim/expansion/game"
	m "github.com/arukim/expansion/models"
)

// General contains game logic
type Explorer struct {
}

func NewExplorer() *Explorer {
	return &Explorer{}
}

func (g *Explorer) MakeTurn(b *game.Board, t *m.Turn) {
	for i := 0; i < b.Size; i++ {
		if b.OutsideMap.Data[i] == 2 {
			p1 := m.NewPoint(i, b.Width)
			targetForces := b.ForcesMap.Get(p1)
			moves := []m.Movement{}
			linq.From(b.GetDirectionTo(p1, b.OutsideMap)).OrderByDescendingT(func(m m.Movement) int {
				return b.ForcesMap.Get(m.Region)
			}).ToSlice(&moves)

			movesL := linq.From(moves)
			// check if zerg atack is available
			myTotalForces := int(movesL.SelectT(func(m m.Movement) int {
				return b.ForcesMap.Get(m.Region) - 1
			}).SumInts())

			if (float64(myTotalForces) > (float64)(targetForces)*explorerAttackRate) || targetForces == 0 {

				total := targetForces
				if total > 0 {
					total = (int)(math.Ceil(math.Min(float64(total+20), float64(total)*explorerAttackRate))) + 1
				}
				for _, m := range moves {
					availableToAttack := b.ForcesMap.Get(m.Region) - 1

					if availableToAttack == 0 {
						continue
					}

					m.Count = availableToAttack

					total -= m.Count
					b.ForcesMap.Data[m.Region.GetPos(b.Width)] -= m.Count

					t.Movements = append(t.Movements, m)

					if total <= 0 {
						break
					}
				}
			}
		}
	}
}
