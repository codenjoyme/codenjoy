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

package game

import (
	"battlecity/action"
	"fmt"
	"strings"
	"sync"
)

type msg struct {
	HeroPosition Point
	ShowName     bool
	ContentRune  []rune
}

type Point struct {
	X, Y int
}

type Board struct {
	msg     *msg
	command *action.Action
	m       sync.Mutex
	size    int
}

//Show returns string representation of current state of board
func (b *Board) Show() string {
	if len(b.msg.ContentRune) == 0 {
		return ""
	}
	repr := strings.Builder{}
	for i := 0; i < b.BoardSize(); i++ {
		for j := 0; j < b.BoardSize(); j++ {
			repr.Write([]byte(fmt.Sprintf("%c", b.msg.ContentRune[i*b.BoardSize()+j])))
		}
		repr.Write([]byte("\n"))
	}

	repr.Write([]byte("\n"))

	return repr.String()
}

func (b *Board) IsAt(point Point, element Element) bool {
	index := b.pointToIndex(point)
	if index < 0 || index >= len(b.msg.ContentRune) {
		return false
	}
	return b.msg.ContentRune[index] == rune(element)
}

func (b *Board) IsAnyAt(point Point, element []Element) bool {
	for _, el := range element {
		if b.IsAt(point, el) {
			return true
		}
	}
	return false
}

func (b *Board) IsNear(p Point, element Element) bool {
	return b.IsAt(Point{p.X, p.Y - 1}, element) ||
		b.IsAt(Point{p.X, p.Y + 1}, element) ||
		b.IsAt(Point{p.X - 1, p.Y}, element) ||
		b.IsAt(Point{p.X + 1, p.Y}, element)
}

func (b *Board) GetAt(p Point) Element {
	if b.isOutOfField(p) {
		return BATTLE_WALL
	}
	return Element(b.msg.ContentRune[b.pointToIndex(p)])
}

func (b *Board) GetAllPoints(elements ...Element) []Point {
	p := make([]Point, 0)
	for _, e := range elements {
		p = append(p, b.getElementPoints(e)...)
	}
	return p
}

func (b *Board) getElementPoints(element Element) []Point {
	l := make([]Point, 0)
	for _, i := range findAll(b.msg.ContentRune, rune(element)) {
		l = append(l, b.indexToPoint(i))
	}
	return l
}

func (b *Board) IsBarrierAt(point Point) bool {
	return b.IsAnyAt(point, []Element{
		BATTLE_WALL,
		WALL,
		WALL_DESTROYED_DOWN,
		WALL_DESTROYED_UP,
		WALL_DESTROYED_LEFT,
		WALL_DESTROYED_RIGHT,
		WALL_DESTROYED_DOWN_TWICE,
		WALL_DESTROYED_UP_TWICE,
		WALL_DESTROYED_LEFT_TWICE,
		WALL_DESTROYED_RIGHT_TWICE,
		WALL_DESTROYED_LEFT_RIGHT,
		WALL_DESTROYED_UP_DOWN,
		WALL_DESTROYED_UP_LEFT,
		WALL_DESTROYED_RIGHT_UP,
		WALL_DESTROYED_DOWN_LEFT,
		WALL_DESTROYED_DOWN_RIGHT,
	})
}

func (b *Board) GetBarriers() []Point {
	return b.GetAllPoints(
		BATTLE_WALL,
		WALL,
		WALL_DESTROYED_DOWN,
		WALL_DESTROYED_UP,
		WALL_DESTROYED_LEFT,
		WALL_DESTROYED_RIGHT,
		WALL_DESTROYED_DOWN_TWICE,
		WALL_DESTROYED_UP_TWICE,
		WALL_DESTROYED_LEFT_TWICE,
		WALL_DESTROYED_RIGHT_TWICE,
		WALL_DESTROYED_LEFT_RIGHT,
		WALL_DESTROYED_UP_DOWN,
		WALL_DESTROYED_UP_LEFT,
		WALL_DESTROYED_RIGHT_UP,
		WALL_DESTROYED_DOWN_LEFT,
		WALL_DESTROYED_DOWN_RIGHT,
	)
}

func (b *Board) IsBulletAt(point Point) bool {
	return !b.IsAt(point, BULLET)
}

func (b *Board) GetBullets() []Point {
	return b.GetAllPoints(
		BULLET,
	)
}

func (b *Board) IsRiverAt(point Point) bool {
	return !b.IsAt(point, RIVER)
}

func (b *Board) GetRivers() []Point {
	return b.GetAllPoints(
		RIVER,
	)
}

func (b *Board) IsTreeAt(point Point) bool {
	return !b.IsAt(point, TREE)
}

func (b *Board) GetTrees() []Point {
	return b.GetAllPoints(
		TREE,
	)
}

func (b *Board) IsIceAt(point Point) bool {
	return !b.IsAt(point, ICE)
}

func (b *Board) GetIces() []Point {
	return b.GetAllPoints(
		ICE,
	)
}

func (b *Board) IsPrizeAt(point Point) bool {
	return !b.IsAt(point, PRIZE)
}

func (b *Board) GetPrizes() []Point {
	return b.GetAllPoints(
		PRIZE,
	)
}

func (b *Board) IsImmortalityPrizeAt(point Point) bool {
	return !b.IsAt(point, PRIZE_IMMORTALITY)
}

func (b *Board) GetImmortalityPrizes() []Point {
	return b.GetAllPoints(
		PRIZE_IMMORTALITY,
	)
}

func (b *Board) IsBreakingWallsPrizeAt(point Point) bool {
	return !b.IsAt(point, PRIZE_BREAKING_WALLS)
}

func (b *Board) GetBreakingWallsPrizes() []Point {
	return b.GetAllPoints(
		PRIZE_BREAKING_WALLS,
	)
}

func (b *Board) IsWalkingOnWaterPrizeAt(point Point) bool {
	return !b.IsAt(point, PRIZE_WALKING_ON_WATER)
}

func (b *Board) GetWalkingOnWaterPrizes() []Point {
	return b.GetAllPoints(
		PRIZE_WALKING_ON_WATER,
	)
}

func (b *Board) IsNoSlidingPrizeAt(point Point) bool {
	return !b.IsAt(point, PRIZE_NO_SLIDING)
}

func (b *Board) GetNoSlidingPrizes() []Point {
	return b.GetAllPoints(
		PRIZE_NO_SLIDING,
	)
}

func (b *Board) IsVisibilityPrizeAt(point Point) bool {
	return !b.IsAt(point, PRIZE_VISIBILITY)
}

func (b *Board) GetVisibilityPrizes() []Point {
	return b.GetAllPoints(
		PRIZE_VISIBILITY,
	)
}

func (b *Board) GetMe() Point {
	ap := b.GetAllPoints(
		TANK_UP,
		TANK_DOWN,
		TANK_LEFT,
		TANK_RIGHT,
	)
	if len(ap) == 0 {
		return Point{}
	}
	return ap[0]
}

func (b *Board) GetEnemies() []Point {
	return b.GetAllPoints(
		AI_TANK_UP,
		AI_TANK_DOWN,
		AI_TANK_LEFT,
		AI_TANK_RIGHT,
		OTHER_TANK_UP,
		OTHER_TANK_DOWN,
		OTHER_TANK_LEFT,
		OTHER_TANK_RIGHT,
		AI_TANK_PRIZE,
	)
}

func (b *Board) IsGameOver() bool {
	return 0 == len(b.GetAllPoints(
		TANK_UP,
		TANK_DOWN,
		TANK_LEFT,
		TANK_RIGHT,
	))
}

func (b *Board) BoardSize() int {
	return b.size
}

func findAll(content []rune, symbol rune) []int {
	var indexes []int
	for i, el := range content {
		if el == symbol {
			indexes = append(indexes, i)
		}
	}
	return indexes
}

func (b *Board) indexToPoint(index int) Point {
	return Point{
		X: index % b.BoardSize(),
		Y: b.BoardSize() - 1 - index/b.BoardSize(), // -1 because we start from 0, not from 1
	}
}

func (b *Board) pointToIndex(p Point) int {
	index := (b.BoardSize() - 1 - p.Y) * b.BoardSize()
	index += p.X
	return index
}

// getAction returns last action that client assigned to board command
// or NOTHING if client didn't assigned any action for this Move
func (b *Board) getAction() action.Action {
	b.m.Lock()
	defer b.m.Unlock()
	// Every time we clean next command
	defer func() {
		b.command = &nothingPtr
	}()
	if b.command == nil {
		return action.NOTHING
	}
	return *b.command
}

func (b *Board) isOutOfField(p Point) bool {
	return p.X > b.BoardSize()-1 || p.X < 0 || p.Y > b.BoardSize()-1 || p.Y < 0
}

//
//func toRunes(s string) []rune {
//	for i, l := range []string(r) {
//		//j := i
//		if i == boardSize {
//			//j = 0
//		}
//		m[i] = []rune(l)
//	}
//	return []rune{}
//}
