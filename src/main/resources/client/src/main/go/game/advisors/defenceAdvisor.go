package advisors

import (
	"math/rand"

	"github.com/ahmetb/go-linq"
	"github.com/arukim/expansion/game"
	m "github.com/arukim/expansion/models"
)

// General contains game logic
type DefenceAdvisor struct {
}

func NewDefenceAdvisor() *DefenceAdvisor {
	return &DefenceAdvisor{}
}

func (g *DefenceAdvisor) MakeTurn(b *game.Board, t *m.Turn) {

	borderLands := b.InsideMap.Filter(func(p m.Point, v int) bool {
		return v == 2
	})

	threatList := []pointEx{}

	linq.From(borderLands).SelectT(func(kv linq.KeyValue) pointEx {
		p := kv.Key.(m.Point)
		forces := make([]int, 4)
		freeCells := 0
		mineAdd := 0
		b.Neighbours(p, func(idx int, p1 m.Point) bool {
			pForce := b.ForcesMap.Data[idx]
			if pForce > 1 {
				forces[b.PlayersMap.Data[idx]] += pForce - 1
			} else if pForce == 0 {
				freeCells++
			}
			return true
		})
		maxForce := linq.From(forces).Max().(int)
		myForce := forces[b.TurnInfo.MyColor]

		if _, ok := b.MyInfo.Mines[p]; ok {
			mineAdd = defenceAdvisorMineBonus
		}
		return pointEx{
			P: p,
			V: freeCells + maxForce - myForce + mineAdd,
		}
	}).OrderByT(func(p pointEx) int {
		return p.V
	}).ToSlice(&threatList)

	increaseMap := map[m.Point]int{}
	changed := true
	for b.ForcesAvailable > 0 && changed {
		changed = false
		for i := range threatList {
			t := &threatList[i]

			if t.V > 0 {
				changed = true
				t.V--
				increaseMap[t.P]++
				b.ForcesMap.Data[t.P.GetPos(b.Width)]++

				b.ForcesAvailable--

				if b.ForcesAvailable <= 0 {
					break
				}
			}
		}
	}

	if b.ForcesAvailable > 0 {
		t := threatList[rand.Intn(len(threatList))]

		increaseMap[t.P]++
		b.ForcesMap.Data[t.P.GetPos(b.Width)]++
		b.ForcesAvailable--

	}

	//log.Printf("DA: increase map %+v\n", increaseMap)
	inc := []m.Increase{}
	linq.From(increaseMap).SelectT(func(kv linq.KeyValue) m.Increase {
		return m.Increase{Region: kv.Key.(m.Point), Count: kv.Value.(int)}
	}).ToSlice(&inc)
	t.Increase = append(t.Increase, inc...)
}
