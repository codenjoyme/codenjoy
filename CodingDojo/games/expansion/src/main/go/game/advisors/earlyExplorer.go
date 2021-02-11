package advisors

import (
	linq "github.com/ahmetb/go-linq"
	"github.com/arukim/expansion/game"
	m "github.com/arukim/expansion/models"
)

// EarlyExplorer is advisor with main focus to capture new territories
type EarlyExplorer struct {
}

type pointEx struct {
	P m.Point
	V int
}

type Move struct {
	Point     m.Point
	Direction string
}

func NewEarlyExplorer() *EarlyExplorer {
	return &EarlyExplorer{}
}

func (g *EarlyExplorer) MakeTurn(b *game.Board, t *m.Turn) {
	// build reversed-flood fill
	// clone outside map
	reverseMap := b.OutsideMap.Clone()

	pointsMap := make(map[m.Point]int)
	reverseMap.Modify(func(p m.Point, v int) int {
		// remove all enemies from map
		if _, ok := b.Enemies[p]; ok {
			return -1
		}
		// remove all internal territory from map
		if b.InsideMap.Get(p) > 2 {
			return -1
		}
		return v
	})

	max := 0
	// for every free cell add 1 point into pointsMap
	reverseMap.IterateP(func(p m.Point, v int) {
		if v > 0 {
			pointsMap[p] = 1
		}

		if v > max {
			max = v
		}
	})

	// reverse flood fill, starting from max point
	for i := max; i > 0; i-- {
		reverseMap.IterateP(func(p m.Point, v int) {
			if v == i {
				b.Neighbours(p, func(i1 int, p1 m.Point) bool {
					v := reverseMap.Data[i1]
					if v > 0 && v < i {
						if b.FreeMines[p] == true {
							pointsMap[p1] += earlyExplorerMineConst
						}
						pointsMap[p1] += pointsMap[p]
						return false
					}
					return true
				})
			}
		})
	}

	// order border cells by required forces, and translate into pointEx
	borderOrdered := []pointEx{}
	// get all border-cells
	linq.From(reverseMap.Filter(func(p m.Point, v int) bool {
		return v == 1
		// Required forces is diff between required and presented force
	})).SelectT(func(x linq.KeyValue) pointEx {
		p := x.Key.(m.Point)
		return pointEx{P: p, V: pointsMap[p] - b.MyInfo.Forces[p]}
	}).OrderByDescendingT(func(x pointEx) int {
		return x.V
	}).ToSlice(&borderOrdered)

	moves := map[Move]int{}
	// !!! calculate mines directions first:
	// - get all free mines.
	// - Take free forces, find nearest way to free mine and how many forces required, send forces
	// - repeat

	// fill-up borders with internal forces movements
	changes := 1
	for changes > 0 {
		changes = 0
		for i := range borderOrdered {
			cell := &borderOrdered[i]

			if cell.V > 0 {
				var dir *string
				var from *m.Point
				b.Neighbours(cell.P, func(i1 int, p1 m.Point) bool {
					// use only near-border inside cells
					if b.InsideMap.Data[i1] != 3 {
						return true
					}
					available := b.ForcesMap.Data[i1]
					if available > 1 {
						b.ForcesMap.Data[i1]--
						d := p1.GetDirection(cell.P)
						dir = &d
						from = &p1
						return false
					}
					return true
				})

				if dir == nil {
					continue
				}

				move := Move{Direction: *dir, Point: *from}
				moves[move]++
				changes++
				cell.V--
			}
		}
	}
	// fill-up borders with force increase
	changes = 1
	increaseMap := map[m.Point]int{}
	for b.ForcesAvailable > 0 && changes > 0 {
		changes = 0
		for i := range borderOrdered {
			cell := &borderOrdered[i]

			if cell.V > 0 {
				cell.V--
				increaseMap[cell.P]++
				b.ForcesMap.Data[cell.P.GetPos(b.Width)]++

				b.ForcesAvailable--
				changes++
				if b.ForcesAvailable == 0 {
					break
				}
			}
		}
		// Could remove empty cells here (cell.V <= 0)
	}

	nearbyOrdered := []pointEx{}
	// get all nearBy cells
	linq.From(reverseMap.Filter(func(p m.Point, v int) bool {
		return v == 2
		// select forces from them
	})).SelectT(func(x linq.KeyValue) pointEx {
		p := x.Key.(m.Point)
		return pointEx{P: p, V: pointsMap[p]}
	}).OrderByDescendingT(func(x pointEx) int {
		return x.V
	}).ToSlice(&nearbyOrdered)

	changes = 1
	for changes > 0 {
		changes = 0
		for i := range nearbyOrdered {
			cell := &nearbyOrdered[i]

			if cell.V > 0 {
				var dir *string
				var from *m.Point
				// try to find neighbour with forces
				b.Neighbours(cell.P, func(i1 int, p1 m.Point) bool {
					available := b.ForcesMap.Data[i1]
					if available > 1 {
						b.ForcesMap.Data[i1]--
						d := p1.GetDirection(cell.P)
						dir = &d
						from = &p1
						return false
					}
					return true
				})

				if dir == nil {
					continue
				}

				move := Move{Direction: *dir, Point: *from}
				moves[move]++
				changes++
			}
		}
		// Could remove empty cells here (cell.V <= 0)
	}

	// count forces movements

	//log.Printf("increase map %+v\n", increaseMap)
	//log.Printf("moves map %+v\n", moves)
	inc := []m.Increase{}
	linq.From(increaseMap).SelectT(func(kv linq.KeyValue) m.Increase {
		return m.Increase{Region: kv.Key.(m.Point), Count: kv.Value.(int)}
	}).ToSlice(&inc)

	mov := []m.Movement{}
	linq.From(moves).SelectT(func(kv linq.KeyValue) m.Movement {
		move := kv.Key.(Move)
		v := kv.Value.(int)
		return m.Movement{Count: v, Region: move.Point, Direction: move.Direction}
	}).ToSlice(&mov)

	t.Increase = append(t.Increase, inc...)
	t.Movements = append(t.Movements, mov...)
}
