package game

import (
	"math"
	"math/rand"
	"strconv"

	m "github.com/arukim/expansion/models"
)

const totalPlayers = 4

type Board struct {
	TurnInfo *m.TurnInfo
	Size     int
	Width    int

	ForcesAvailable int
	Turn            int
	Tick            int
	TotalWalkCells  int
	TotalFreeCells  int
	OccupationRate  float64

	WalkMap    *m.Map
	PlayersMap *m.Map
	OutsideMap *m.Map
	InsideMap  *m.Map
	ForcesMap  *m.Map

	MinesList   []m.Point
	PlayerInfos []m.PlayerInfo
	Enemies     map[m.Point]bool
	FreeMines   map[m.Point]bool

	MyInfo *m.PlayerInfo
}

// NewBoard instance creation
func NewBoard(t *m.TurnInfo) *Board {
	b := new(Board)

	b.PlayerInfos = make([]m.PlayerInfo, 4)
	b.FreeMines = make(map[m.Point]bool)
	b.Enemies = make(map[m.Point]bool)

	b.parse(t)
	b.fillPlayersInfos()
	b.buildOutsideMap()
	b.buildInsideMap()

	return b
}

func (b *Board) rotate(i int) int {
	return (i % b.Width) + (b.Width-1-i/b.Width)*b.Width
}

// parse all data from server package
func (b *Board) parse(t *m.TurnInfo) {
	b.TurnInfo = t

	walkLayer := []rune(t.Layers[0])
	playersLayer := []rune(t.Layers[1])

	mapSize := len(walkLayer)
	mapWidth := int(math.Sqrt(float64(mapSize)))

	b.Size = mapSize
	b.Width = mapWidth
	b.ForcesAvailable = t.Available
	b.Turn = t.Round
	b.Tick = t.Tick

	//fmt.Printf("Map size: %v, side: %v", mapSize, mapWidth)

	b.WalkMap = m.NewMap(mapWidth)
	b.MinesList = []m.Point{}

	for i := 0; i < mapSize; i++ {
		switch walkLayer[b.rotate(i)] {
		case '$':
			b.MinesList = append(b.MinesList, m.NewPoint(i, b.Width))
			fallthrough
		case '1', '2', '3', '4', '.':
			b.WalkMap.Data[i] = 0
			b.TotalWalkCells++
		default:
			b.WalkMap.Data[i] = -1
		}
	}

	b.PlayersMap = m.NewMap(mapWidth)
	for i := 0; i < mapSize; i++ {
		switch playersLayer[b.rotate(i)] {
		case '♥':
			b.PlayersMap.Data[i] = 0
		case '♦':
			b.PlayersMap.Data[i] = 1
		case '♣':
			b.PlayersMap.Data[i] = 2
		case '♠':
			b.PlayersMap.Data[i] = 3
		case 45:
			b.PlayersMap.Data[i] = -1
		}
		if b.PlayersMap.Data[i] != -1 && b.PlayersMap.Data[i] != t.MyColor {
			b.Enemies[m.NewPoint(i, b.Width)] = true
		}
		if b.PlayersMap.Data[i] == -1 && b.WalkMap.Data[i] == 0 {
			b.TotalFreeCells++
		}
	}

	b.OccupationRate = float64(b.TotalWalkCells-b.TotalFreeCells) / float64(b.TotalWalkCells)

	b.ForcesMap = m.NewMap(mapWidth)
	for i := 0; i < mapSize; i++ {
		value, _ := strconv.ParseInt(t.Forces[i*3:i*3+3], 36, 32)
		if value != 0 {
			b.ForcesMap.Data[b.rotate(i)] = int(value)
		}
	}
	// fmt.Println("Walk map")
	// b.WalkMap.Print()
	// fmt.Println("Players map")
	// b.PlayersMap.Print()
	// fmt.Println("Forces map")
	// b.ForcesMap.Print()

}

func (b *Board) floodFill(pmap *m.Map, points map[m.Point]bool) {
	turn := 0
	for len(points) > 0 {
		turn++
		changes := make(map[m.Point]bool)
		for f := range points {
			if pmap.Get(f) == 0 {
				pmap.Set(f, turn)
			}

			b.Neighbours(f, func(pos int, p m.Point) bool {
				moveV := pmap.Data[pos]

				if moveV == 0 {
					changes[p] = true
				}

				return true
			})
		}
		points = changes
	}
}

func (b *Board) fillPlayersInfos() {

	for i := range b.PlayerInfos {
		p := &b.PlayerInfos[i]
		p.Mines = make(map[m.Point]bool)
		p.Forces = make(map[m.Point]int)
	}

	// fill map with current territory
	b.PlayersMap.Iterate(func(i, v int) {
		if v == -1 {
			return
		}

		p := &b.PlayerInfos[v]
		pos := m.NewPoint(i, b.Size)

		forceCount := b.ForcesMap.Data[i]
		p.TerritorySize++

		p.ForcesTotal += forceCount
		if forceCount > 0 {
			p.ForcesFree += forceCount - 1
			p.Forces[pos] = forceCount
		}
	})

	for _, p := range b.MinesList {
		pNum := b.PlayersMap.Get(p)
		if pNum == -1 {
			b.FreeMines[p] = true
			continue
		}

		b.PlayerInfos[pNum].MinesCount++
		b.PlayerInfos[pNum].Mines[p] = true
	}

	b.MyInfo = &b.PlayerInfos[b.TurnInfo.MyColor]
	//fmt.Printf("playersInfos: %+v\n", b.PlayerInfos)
}

// build-up map of outside territories
// 1 - my territory, -1 not passable, >2 - outside territory
func (b *Board) buildOutsideMap() {

	points := make(map[m.Point]bool)

	b.PlayersMap.Iterate(func(i, v int) {
		if v == b.TurnInfo.MyColor {
			points[m.NewPoint(i, b.Width)] = true
		}
	})

	//  start with walk map
	b.OutsideMap = b.WalkMap.Clone()

	b.floodFill(b.OutsideMap, points)
	/*
		b.MoveMap.Print()
	*/
}

// build-up map of inside territories
// 1 - external border, >1 - internal territory. -1 - external territory
func (b *Board) buildInsideMap() {

	b.InsideMap = b.OutsideMap.CloneF(func(v int) int {
		switch v {
		case 2:
			return 1
		case 1:
			return 0
		default:
			return -1
		}
	})

	points := make(map[m.Point]bool)
	b.InsideMap.Iterate(func(i, v int) {
		if v == 1 {
			points[m.NewPoint(i, b.Width)] = true
		}
	})

	b.floodFill(b.InsideMap, points)
	//b.InsideMap.Print()
}

// GetDirection finds single turn towards target using existing path-map
func (b *Board) GetDirection(p m.Point, pmap *m.Map) *m.Movement {
	pos := pmap.Get(p)
	dirs := []string{}

	b.Neighbours(p, func(n_pos int, neighbour m.Point) bool {
		//found
		if pmap.Data[n_pos] < pos && pmap.Data[n_pos] > 0 {
			dirs = append(dirs, p.GetDirection(neighbour))
		}
		return true
	})

	dir := dirs[rand.Intn(len(dirs))]

	return &m.Movement{
		Direction: dir,
		Region:    p,
	}
}

// GetDirectionTo search direction to point using existing path-map.
// If there are several last turn - will return slice of turns
func (b *Board) GetDirectionTo(p m.Point, pmap *m.Map) []m.Movement {
	pos := pmap.Get(p)

	if pos == 0 {
		return []m.Movement{}
	}

	moves := []m.Movement{}
	for len(moves) == 0 {
		b.Neighbours(p, func(n_pos int, neighbour m.Point) bool {
			//found
			if pmap.Data[n_pos] == 1 {
				moves = append(moves, m.Movement{
					Direction: neighbour.GetDirection(p),
					Region:    neighbour,
				})
			} else if pmap.Data[n_pos] == pos-1 {
				p = neighbour
				pos--
				return false
			}
			return true
		})
	}

	return moves
}

// GetDirectionFromTo find path from A to B, creates new temp path map
func (b *Board) GetDirectionFromTo(pa m.Point, pb m.Point) *m.Movement {

	pathMap := b.WalkMap.Clone()

	points := make(map[m.Point]bool)
	points[pb] = true

	found := false

	turn := 0
	for !found {
		turn++
		changes := make(map[m.Point]bool)
		for f := range points {
			if pathMap.Get(f) == 0 {
				pathMap.Set(f, turn)
			}

			b.Neighbours(f, func(pos int, p m.Point) bool {
				moveV := pathMap.Data[pos]

				if moveV == 0 {
					changes[p] = true
				}

				if p.X == pa.X && p.Y == pa.Y {
					pathMap.Set(p, turn+1)
					found = true
					return false
				}

				return true
			})
		}
		points = changes
	}
	move := b.GetDirection(pa, pathMap)
	return move
}

// Neighbours enumerates all neighbours cells with f(func)
// if func failed - returns
func (b *Board) Neighbours(p m.Point, f func(int, m.Point) bool) {
	for i := -1; i <= 1; i++ {
		for j := -1; j <= 1; j++ {
			if i == 0 && j == 0 {
				continue
			}
			p1 := p.Add(i, j)
			if p1.X < 0 || p1.Y < 0 || p1.X >= b.Width || p1.Y >= b.Width {
				continue
			}
			pos := p1.GetPos(b.Width)
			if !f(pos, p1) {
				return
			}
		}
	}
}
