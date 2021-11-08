package models

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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
