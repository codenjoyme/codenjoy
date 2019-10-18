package advisors

import (
	linq "github.com/ahmetb/go-linq"
	"github.com/arukim/expansion/game"
	m "github.com/arukim/expansion/models"
)

// General contains game logic
type General struct {
	board *game.Board
}

func NewGeneral() *General {
	return &General{}
}

func (g *General) MakeTurn(b *game.Board, t *m.Turn) {
	g.board = b

	moves := g.findMove()

	if len(moves) == 0 {
		return
	}

	g.borderSpawn(b, t)

	t.Movements = append(t.Movements, moves...)
}

func (g *General) borderSpawn(b *game.Board, t *m.Turn) {
	if b.ForcesAvailable == 0 {
		return
	}

	pointsMap := []m.Point{}
	linq.From(b.InsideMap.Filter(func(p m.Point, v int) bool {
		return v == 1
	})).SelectT(func(kv linq.KeyValue) m.Point {
		return kv.Key.(m.Point)
	}).ToSlice(&pointsMap)

	if len(pointsMap) == 0 {
		return
	}

	increaseMap := map[m.Point]int{}
	for b.ForcesAvailable > 0 {
		for _, p := range pointsMap {
			increaseMap[p]++
			b.ForcesAvailable--
			if b.ForcesAvailable == 0 {
				break
			}
		}
	}

	inc := []m.Increase{}
	linq.From(increaseMap).SelectT(func(kv linq.KeyValue) m.Increase {
		return m.Increase{Region: kv.Key.(m.Point), Count: kv.Value.(int)}
	}).ToSlice(&inc)

	t.Increase = append(t.Increase, inc...)
}

func (g *General) findMove() []m.Movement {
	var b = g.board

	p := m.Point{}

	if len(b.FreeMines) > 0 {
		p = linq.From(b.FreeMines).SelectT(func(x linq.KeyValue) m.Point {
			return x.Key.(m.Point)
		}).OrderByT(func(x m.Point) int {
			return b.OutsideMap.Get(x) - 1
		}).First().(m.Point)
	} else if len(b.Enemies) > 0 {
		// p = linq.From(b.Enemies).OrderByT(func(x m.Point) int {
		// 	return b.OutsideMap.Get(x) - 1
		// }).First().(m.Point)
		return []m.Movement{}
	} else {
		return []m.Movement{}
	}

	moves := b.GetDirectionTo(p, b.OutsideMap)

	// don't forget to move largest force!
	maxForce := 0
	maxForcePos := 0
	for i, v := range b.ForcesMap.Data {
		// check only my force
		if b.PlayersMap.Data[i] == b.TurnInfo.MyColor && v > maxForce {
			maxForce = v
			maxForcePos = i
		}
	}

	moves = append(moves, *b.GetDirectionFromTo(m.NewPoint(maxForcePos, b.Width), p))

	for i := range moves {
		moves[i].Count = b.ForcesMap.Get(moves[i].Region) - 1
		b.ForcesMap.Data[moves[i].Region.GetPos(b.Width)] -= moves[i].Count
	}

	//fmt.Printf("General moves: %+v\n", moves)
	return moves
}
