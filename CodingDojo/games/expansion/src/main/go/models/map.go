package models

import "fmt"

type Map struct {
	Size  int
	Width int
	Data  []int
}

// NewMap creates new map instance
func NewMap(width int) *Map {
	size := width * width
	m := Map{
		Width: width,
		Size:  size,
		Data:  make([]int, size),
	}
	return &m
}

func (m *Map) Clone() *Map {
	newMap := NewMap(m.Width)
	for i := 0; i < m.Size; i++ {
		newMap.Data[i] = m.Data[i]
	}
	return newMap
}

func (m *Map) CloneF(f func(int) int) *Map {
	newMap := NewMap(m.Width)
	for i := 0; i < m.Size; i++ {
		newMap.Data[i] = f(m.Data[i])
	}
	return newMap
}

func (m *Map) Print() {
	for i := m.Width - 1; i >= 0; i-- {
		fmt.Printf("%v\n", m.Data[i*m.Width:i*m.Width+m.Width])
	}
}

func (m *Map) Iterate(f func(int, int)) {
	for i := 0; i < m.Size; i++ {
		f(i, m.Data[i])
	}
}

func (m *Map) IterateP(f func(Point, int)) {
	for i := 0; i < m.Size; i++ {
		f(NewPoint(i, m.Width), m.Data[i])
	}
}

func (m *Map) Modify(f func(p Point, v int) int) {
	for i := 0; i < m.Size; i++ {
		m.Data[i] = f(NewPoint(i, m.Width), m.Data[i])
	}
}

func (m *Map) Filter(f func(p Point, v int) bool) map[Point]int {
	res := make(map[Point]int)
	for i := 0; i < m.Size; i++ {
		p := NewPoint(i, m.Width)
		v := m.Data[i]
		if f(p, v) {
			res[p] = v
		}
	}
	return res
}

func (m *Map) Get(p Point) int {
	return m.Data[p.GetPos(m.Width)]
}

func (m *Map) Set(p Point, value int) {
	m.Data[p.GetPos(m.Width)] = value
}
