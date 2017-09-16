package models

type Point struct {
	X int `json:"x"`
	Y int `json:"y"`
}

func NewPoint(pos int, width int) Point {
	return Point{X: pos % width, Y: pos / width}
}

func (p *Point) Add(x, y int) Point {
	return Point{X: p.X + x, Y: p.Y + y}
}

func (p *Point) GetPos(width int) int {
	return p.X + p.Y*width
}

func (p1 *Point) GetDirection(p2 Point) string {
	diffX := p2.X - p1.X
	diffY := p2.Y - p1.Y
	if diffX == -1 {
		if diffY == -1 {
			return "LEFT_DOWN"
		} else if diffY == 1 {
			return "LEFT_UP"
		} else {
			return "LEFT"
		}
	} else if diffX == 1 {
		if diffY == 1 {
			return "RIGHT_UP"
		} else if diffY == -1 {
			return "RIGHT_DOWN"
		} else {
			return "RIGHT"
		}
	} else {
		if diffY == 1 {
			return "UP"
		} else if diffY == -1 {
			return "DOWN"
		} else {
			return "NONE"
		}
	}
}
