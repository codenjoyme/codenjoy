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
	"strings"
)

type Board struct {
	Size     int
	Width    int

	Glass    string

	CurrentFigureType	  string
	FutureFigures         []string
	CurrentFigurePoint    Point
}

// NewBoard instance creation
func NewBoard(t *Question) *Board {
	b := new(Board)
	b.parse(t)
	return b
}

func (b *Board) ToString() string {
	return "-------------------------------------------------------------------------------------------" + "\n" +
			 "currentFigure: \"" + b.CurrentFigureType + "\" at: " + b.CurrentFigurePoint.ToString() + "\n" +
			"futureFigures: [\"" + strings.Join(b.FutureFigures, "\",\"") + "\"]" + "\n" +
			"board:" + "\n" +
			insertNth(b.Glass, b.Width, '\n')
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

func (b *Board) rotate(i int) int {
	return (i % b.Width) + (b.Width-1-i/b.Width)*b.Width
}

// parse all data from server package
func (b *Board) parse(t *Question) {
	b.Glass = t.Layers[0]

	mapSize := len(b.Glass)
	mapWidth := int(math.Sqrt(float64(mapSize)))

	b.Size = mapSize
	b.Width = mapWidth

	b.CurrentFigurePoint = t.CurrentFigurePoint
	b.CurrentFigureType = t.CurrentFigureType
	b.FutureFigures = t.FutureFigures
}

// Neighbours enumerates all neighbours cells with f(func)
// if func failed - returns
func (b *Board) Neighbours(p Point, f func(int, Point) bool) {
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
