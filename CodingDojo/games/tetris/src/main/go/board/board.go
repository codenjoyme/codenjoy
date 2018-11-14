/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

package board

import (
	"bytes"
	"math"
	"sort"
	"strings"
)

type Board struct {
	Size  			   int
	Glass 			   string
	CurrentFigureType  string
	FutureFigures      []string
	CurrentFigurePoint Point
}

// NewBoard instance creation
func NewBoard(t *Question) *Board {
	b := new(Board)
	b.parse(t)
	return b
}

func (b *Board) ToString() string {
	return "-------------------------------------------------------------------------------------------" + "\n" +
			 "currentFigure: \"" + b.CurrentFigureType + "\" at: " + b.CurrentFigurePoint.String() + "\n" +
			"futureFigures: [\"" + strings.Join(b.FutureFigures, "\",\"") + "\"]" + "\n" +
			"board:" + "\n" +
			insertNth(b.Glass, b.Size, '\n')
}

// Thanks to https://stackoverflow.com/a/33633451/4728425
func insertNth(s string, n int, r rune) string {
	var buffer bytes.Buffer
	var n_1 = n - 1
	var l_1 = len(s) - 1
	for i,rune := range s {
		buffer.WriteRune(rune)
		if i % n == n_1 && i != l_1  {
			buffer.WriteRune(r)
		}
	}
	return buffer.String()
}

func (b *Board) size() int {
	return b.Size
}

// parse all data from server package
func (b *Board) parse(t *Question) {
	b.Glass = t.Layers[0]
	b.Size = int(math.Sqrt(float64(len(b.Glass))))
	b.CurrentFigurePoint = t.CurrentFigurePoint
	b.CurrentFigureType = t.CurrentFigureType
	b.FutureFigures = t.FutureFigures
}

func (b *Board) CordsToPos(x int, y int) int {
	return (b.Size - 1 - y) * b.Size + x
}

func (b *Board) PosToCords(pos int) Point {
	x := (pos % b.Size)
	y := b.Size - 1 - (pos / b.Size)
	return Point{X : x, Y : y}
}

func (b *Board) GetAt(x int, y int) string {
	return string(b.Glass[b.CordsToPos(x, y)])
}

func (b *Board) IsAt(x int, y int, elements []string) bool {
	found := false
	for _, element := range elements {
		found = found || (element == b.GetAt(x, y))
	}
	return found
}

func (b *Board) IsFree(x int, y int) bool {
	return b.IsAt(x, y, []string{ NONE })
}

func (b *Board) Get(elements []string) []Point {
	result := []Point{}
	for pos, char := range b.Glass {
		for _, element := range elements {
		if (string(char) == element) {
			result = append(result, b.PosToCords(pos))
		}
	}
}
	return b.Sort(result);
}

func (b *Board) GetFigures() []Point {
	return b.Get(FIGURES)
}

func (b *Board) GetFreeSpace() []Point {
	return b.Get([]string { NONE })
}

func (b *Board) GetNear(x int, y int) []string {
	result := []string{}
	p := Point{X : x, Y : y}
	for dx := -1; dx <= 1; dx++ {
		for dy := -1; dy <= 1; dy++ {
			if dx == 0 && dy == 0 {
				continue
			}
			pos := p.Add(dx, dy)
			if pos.X < 0 || pos.Y < 0 || pos.X >= b.Size || pos.Y >= b.Size {
				continue
			}
			result = append(result, b.GetAt(pos.X, pos.Y))
		}
	}
	return result
}

func (b *Board) CountNear(x int, y int, elements []string) int {
	result := 0
	near := b.GetNear(x, y)
	for _, e1 := range near {
		for _, e2 := range elements {
			if (e1 == e2) {
				result ++
			}
		}
	}
	return result
}

func (b *Board) IsNear(x int, y int, elements []string) bool {
	return b.CountNear(x, y, elements) > 0
}

// soring array of Point

type byPoint []Point

func (s byPoint) Len() int {
	return len(s)
}

func (s byPoint) Swap(i, j int) {
	s[i], s[j] = s[j], s[i]
}

func (s byPoint) Less(i, j int) bool {
	return Index(s[i]) < Index(s[j])
}

func Index(pt Point) int {
	return pt.X * 1000 + pt.Y;
}

func (b *Board) Sort(array []Point) []Point {
	sort.Sort(byPoint(array))
	return array;
}
