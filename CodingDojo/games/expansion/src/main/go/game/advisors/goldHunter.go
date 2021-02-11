package advisors

import (
	"log"

	"github.com/ahmetb/go-linq"
	"github.com/arukim/expansion/game"
	m "github.com/arukim/expansion/models"
)

// GoldHunter is advisor with main focus to get gold mines ASAP
type GoldHunter struct {
	target   *m.Point
	forces   *m.Point
	finished bool
}

func NewGoldHunter() *GoldHunter {
	return &GoldHunter{}
}

func (gh *GoldHunter) MakeTurn(b *game.Board, t *m.Turn) {
	if gh.finished {
		return
	}

findTarget:
	// it there is no target, find new one
	if gh.target == nil {
		if len(b.FreeMines) == 0 {
			log.Println("GH: no new targets, I'm done")
			gh.finished = true
			return
		}

		p := linq.From(b.FreeMines).SelectT(func(x linq.KeyValue) m.Point {
			return x.Key.(m.Point)
		}).OrderByT(func(x m.Point) int {
			return b.OutsideMap.Get(x) - 1
		}).First().(m.Point)
		log.Printf("GH: found new target at %+v\n", p)
		gh.target = &p
	}

	if b.PlayersMap.Get(*gh.target) != -1 {
		gh.target = nil
		goto findTarget
	}

	maxForce := 0
	maxForcePos := 0
	for i, v := range b.ForcesMap.Data {
		// check only my force
		if b.PlayersMap.Data[i] == b.TurnInfo.MyColor && v > maxForce {
			maxForce = v
			maxForcePos = i
		}
	}

	from := m.NewPoint(maxForcePos, b.Width)
	move := *b.GetDirectionFromTo(from, *gh.target)

	forces := b.ForcesMap.Get(from)
	dist := b.OutsideMap.Get(*gh.target) - 1

	if dist > forces {
		move.Count = forces
	} else {
		move.Count = dist
	}

	b.ForcesMap.Data[maxForce] -= move.Count
	log.Printf("GH: moving %d forces to target %+v\n", move.Count, *gh.target)
	t.Movements = append(t.Movements, move)
}
