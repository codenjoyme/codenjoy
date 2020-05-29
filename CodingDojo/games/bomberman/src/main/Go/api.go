/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

package bomberman

type CommonAPI interface {
	GetBomberman() Point
	GetOtherBombermans() []Point
	IsMyBombermanDead() bool
	IsAt(point Point, element Element) bool
	IsAtAny(point Point, element []Element) bool // boolean isAt(Point point, Collection<Element> elements)
	IsNear(point Point, element Element) bool
	IsBarrierAt(point Point) bool
	CountNear(point Point, element Element) int
	GetAt(point Point) Element
	boardSize() int
	GetBarriers() []Point
	GetMeatChoppers() []Point
	GetWalls() []Point
	GetDestroyableWalls() []Point
	GetBombs() []Point
	GetFutureBlasts() []Point
	GetPerks() []Point
}

type Point struct {
	X, Y int
}

type Element rune

func findOne(content []rune, symbol rune) int {
	for i, el := range content {
		if el == symbol {
			return i
		}
	}
	return -1
}

func findAll(content []rune, symbol rune) []int {
	indexes := []int{}
	for i, el := range content {
		if el == symbol {
			indexes = append(indexes, i)
		}
	}
	return indexes
}

func (b *board) indexToPoint(index int) Point {
	return Point{
		X: index % b.boardSize(),
		Y: b.boardSize() - 1 - index/b.boardSize(), // -1 because we start from 0, not from 1
	}
}

func (b *board) pointToIndex(p Point) int {
	index := (b.boardSize() - 1 - p.Y) * b.boardSize()
	index += p.X
	return index
}

func (b *board) GetBomberman() Point {
	index := findOne(b.boardContent, BOMBERMAN)
	return b.indexToPoint(index)
}

func (b *board) GetOtherBombermans() []Point {
	indexes := findAll(b.boardContent, OTHER_BOMBERMAN)
	others := []Point{}
	for _, i := range indexes {
		others = append(others, b.indexToPoint(i))
	}
	return others
}

func (b *board) IsMyBombermanDead() bool {
	index := findOne(b.boardContent, DEAD_BOMBERMAN)
	return index != -1
}

func (b *board) IsAt(point Point, element Element) bool {
	index := b.pointToIndex(point)
	if index < 0 || index >= len(b.boardContent) {
		return false
	}
	return b.boardContent[index] == rune(element)
}

func (b *board) IsAtAny(point Point, element []Element) bool {
	for _, el := range element {
		if b.IsAt(point, el) {
			return true
		}
	}
	return false
}

func (b *board) IsNear(p Point, element Element) bool {
	return b.IsAt(Point{p.X, p.Y - 1}, element) ||
		b.IsAt(Point{p.X, p.Y + 1}, element) ||
		b.IsAt(Point{p.X - 1, p.Y}, element) ||
		b.IsAt(Point{p.X + 1, p.Y}, element)
}

func (b *board) IsBarrierAt(point Point) bool {
	return b.IsAtAny(point, []Element{
		BOMBERMAN, BOMB_BOMBERMAN, OTHER_BOMBERMAN, OTHER_BOMB_BOMBERMAN,
		BOMB_TIMER_5, BOMB_TIMER_4, BOMB_TIMER_3, BOMB_TIMER_2, BOMB_TIMER_1,
		WALL, DESTROYABLE_WALL, MEAT_CHOPPER,
	})
}

func (b *board) CountNear(p Point, element Element) int {
	counter := 0
	if b.IsAt(Point{p.X, p.Y - 1}, element) {
		counter++
	}
	if b.IsAt(Point{p.X, p.Y + 1}, element) {
		counter++
	}
	if b.IsAt(Point{p.X - 1, p.Y}, element) {
		counter++
	}
	if b.IsAt(Point{p.X + 1, p.Y}, element) {
		counter++
	}
	return counter
}

func (b *board) GetAt(p Point) Element {
	return Element(b.boardContent[b.pointToIndex(p)])
}

func (b *board) boardSize() int {
	return BoardSize
}

func (b *board) GetBarriers() []Point {
	points := []Point{}
	barrierElements := []rune{BOMB_BOMBERMAN, OTHER_BOMBERMAN, OTHER_BOMB_BOMBERMAN, OTHER_DEAD_BOMBERMAN,
		BOMB_TIMER_5, BOMB_TIMER_4, BOMB_TIMER_3, BOMB_TIMER_2, BOMB_TIMER_1, BOOM,
		WALL, DESTROYABLE_WALL, DESTROYED_WALL, MEAT_CHOPPER, DEAD_MEAT_CHOPPER}

	for _, barier := range barrierElements {
		for _, i := range findAll(b.boardContent, barier) {
			points = append(points, b.indexToPoint(i))
		}

	}
	return points
}

func (b *board) GetMeatChoppers() []Point {
	points := []Point{}

	for _, i := range findAll(b.boardContent, MEAT_CHOPPER) {
		points = append(points, b.indexToPoint(i))
	}
	return points
}

func (b *board) GetWalls() []Point {
	points := []Point{}

	for _, i := range findAll(b.boardContent, WALL) {
		points = append(points, b.indexToPoint(i))
	}
	return points
}

func (b *board) GetDestroyableWalls() []Point {
	points := []Point{}

	for _, i := range findAll(b.boardContent, DESTROYABLE_WALL) {
		points = append(points, b.indexToPoint(i))
	}
	return points
}

func (b *board) GetBombs() []Point {
	points := []Point{}
	barrierElements := []rune{BOMB_BOMBERMAN, OTHER_BOMB_BOMBERMAN,
		BOMB_TIMER_5, BOMB_TIMER_4, BOMB_TIMER_3, BOMB_TIMER_2, BOMB_TIMER_1}

	for _, barier := range barrierElements {
		for _, i := range findAll(b.boardContent, barier) {
			points = append(points, b.indexToPoint(i))
		}

	}
	return points
}

func (b *board) GetPerks() []Point {
	points := []Point{}
	perks := []rune{BOMB_BLAST_RADIUS_INCREASE, BOMB_COUNT_INCREASE, BOMB_IMMUNE, BOMB_REMOTE_CONTROL}

	for _, perk := range perks {
		for _, i := range findAll(b.boardContent, perk) {
			points = append(points, b.indexToPoint(i))
		}

	}
	return points
}

func (b *board) GetFutureBlasts() []Point {
	bombs := []Point{}
	barrierElements := []rune{BOMB_BOMBERMAN, OTHER_BOMB_BOMBERMAN,
		BOMB_TIMER_5, BOMB_TIMER_4, BOMB_TIMER_3, BOMB_TIMER_2, BOMB_TIMER_1}

	// Get all bombs
	for _, barier := range barrierElements {
		for _, i := range findAll(b.boardContent, barier) {
			bombs = append(bombs, b.indexToPoint(i))
		}
	}
	futureBlasts := []Point{}
	// Get all blasts
	for _, bomb := range bombs {
		// Check all 4 directions
		for i := 1; i <= BLAST_SIZE; i++ {
			fBlast := Point{
				X: bomb.X + i,
				Y: bomb.Y,
			}
			if b.IsBarrierAt(fBlast) {
				break
			}
			futureBlasts = append(futureBlasts, fBlast)
		}
		for i := 1; i <= BLAST_SIZE; i++ {
			fBlast := Point{
				X: bomb.X - i,
				Y: bomb.Y,
			}
			if b.IsBarrierAt(fBlast) {
				break
			}
			futureBlasts = append(futureBlasts, fBlast)
		}
		for i := 1; i <= BLAST_SIZE; i++ {
			fBlast := Point{
				X: bomb.X,
				Y: bomb.Y + i,
			}
			if b.IsBarrierAt(fBlast) {
				break
			}
			futureBlasts = append(futureBlasts, fBlast)
		}
		for i := 1; i <= BLAST_SIZE; i++ {
			fBlast := Point{
				X: bomb.X,
				Y: bomb.Y - i,
			}
			if b.IsBarrierAt(fBlast) {
				break
			}
			futureBlasts = append(futureBlasts, fBlast)
		}
	}

	return removeDuplicates(futureBlasts)
}

func removeDuplicates(points []Point) []Point {
	set := make(map[Point]struct{})
	for _, p := range points {
		if _, ok := set[p]; !ok {
			set[p] = struct{}{}
		}
	}
	res := make([]Point, 0, len(set))
	for p, _ := range set {
		res = append(res, p)
	}
	return res
}
