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
	"fmt"
	"icancode/action"
	"strings"
	"sync"
)

const boardSize = 20

type msg struct {
	HeroPosition     Point
	ShowName         bool
	Offset           Point
	LevelFinished    bool
	LayersContentRaw rawLayers `json:"layers"`
	LayersContentMap mapLayers
	LevelProgress    levelProgress
}

type rawLayers []string

type mapLayers map[int][]rune

type levelProgress struct {
	Total      int
	Current    int
	LastPassed int
}

type Point struct {
	X, Y int
}

type Board struct {
	msg     *msg
	command *action.Action
	m       sync.Mutex
}

//Show returns string representation of current state of board
func (b *Board) Show() string {
	if len(b.msg.LayersContentMap) == 0 {
		return ""
	}
	repr := strings.Builder{}
	for i := 0; i < boardSize; i++ {
		for j := 0; j < boardSize; j++ {
			repr.Write([]byte(fmt.Sprintf("%c", b.msg.LayersContentMap[0][i*boardSize+j])))
		}
		repr.Write([]byte("\n"))
	}

	repr.Write([]byte("\n"))

	for i := 0; i < boardSize; i++ {
		for j := 0; j < boardSize; j++ {
			repr.Write([]byte(fmt.Sprintf("%c", b.msg.LayersContentMap[1][i*boardSize+j])))
		}
		repr.Write([]byte("\n"))
	}

	repr.Write([]byte("\n"))

	for i := 0; i < boardSize; i++ {
		for j := 0; j < boardSize; j++ {
			repr.Write([]byte(fmt.Sprintf("%c", b.msg.LayersContentMap[2][i*boardSize+j])))
		}
		repr.Write([]byte("\n"))
	}
	return repr.String()
}

func (b *Board) IsAt(point Point, element Element) bool {
	index := b.pointToIndex(point)
	if index < 0 || index >= len(b.msg.LayersContentMap[element.getLayer()]) {
		return false
	}
	return b.msg.LayersContentMap[element.getLayer()][index] == rune(element)
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

func (b *Board) GetAt(p Point) []Element {
	elements := make([]Element, 0)
	for _, c := range b.msg.LayersContentMap {
		elements = append(elements, Element(c[b.pointToIndex(p)]))
	}
	return elements
}

func (b *Board) GetAllPoints(elements ...Element) []Point {
	p := make([]Point, 0)
	for _, e := range elements {
		p = append(p, b.GetElementPoints(e)...)
	}
	return p
}

func (b *Board) GetElementPoints(element Element) []Point {
	l := make([]Point, 0)
	for _, i := range findAll(b.msg.LayersContentMap[element.getLayer()], rune(element)) {
		l = append(l, b.indexToPoint(i))
	}
	return l
}

func (b *Board) IsBarrierAt(point Point) bool {
	return b.IsAnyAt(point, []Element{
		FLOOR, START, EXIT, GOLD, HOLE, //layer1
		EMPTY, GOLD,
		LASER_DOWN, LASER_UP, LASER_LEFT, LASER_RIGHT,
		ROBO_OTHER, ROBO_OTHER_FLYING, ROBO_OTHER_FALLING, ROBO_OTHER_LASER,
		ROBO, ROBO_FLYING, ROBO_FALLING, ROBO_LASER, //layer 2
	})
}

func (b *Board) GetBarriers() []Point {
	return b.GetAllPoints(
		FLOOR, START, EXIT, GOLD, HOLE, //layer1
		EMPTY, GOLD,
		LASER_DOWN, LASER_UP, LASER_LEFT, LASER_RIGHT,
		ROBO_OTHER, ROBO_OTHER_FLYING, ROBO_OTHER_FALLING, ROBO_OTHER_LASER,
		ROBO, ROBO_FLYING, ROBO_FALLING, ROBO_LASER, //layer 2
	)
}

func (b *Board) IsHoleAt(point Point) bool {
	return !b.IsAt(point, HOLE)
}

func (b *Board) GetMe() Point {
	return b.GetAllPoints(
		ROBO_FALLING, //layer2
		ROBO_LASER,
		ROBO,
		ROBO_FLYING, //layer3
	)[0]
}

func (b *Board) GetOtherHeroes() []Point {
	return b.GetAllPoints(
		ROBO_OTHER_FALLING, //layer2
		ROBO_OTHER_LASER,
		ROBO_OTHER,
		ROBO_OTHER_FLYING, //layer3
	)
}

func (b *Board) GetLaserMachines() []Point {
	return b.GetAllPoints(
		LASER_MACHINE_CHARGING_LEFT, //layer1
		LASER_MACHINE_CHARGING_RIGHT,
		LASER_MACHINE_CHARGING_UP,
		LASER_MACHINE_CHARGING_DOWN,

		LASER_MACHINE_READY_LEFT,
		LASER_MACHINE_READY_RIGHT,
		LASER_MACHINE_READY_UP,
		LASER_MACHINE_READY_DOWN,
	)
}

func (b *Board) GetLasers() []Point {
	return b.GetAllPoints(
		LASER_LEFT, //layer2
		LASER_RIGHT,
		LASER_UP,
		LASER_DOWN,
	)
}

func (b *Board) GetWalls() []Point {
	return b.GetAllPoints(
		ANGLE_IN_LEFT, //layer1
		WALL_FRONT,
		ANGLE_IN_RIGHT,
		WALL_RIGHT,
		ANGLE_BACK_RIGHT,
		WALL_BACK,
		ANGLE_BACK_LEFT,
		WALL_LEFT,
		WALL_BACK_ANGLE_LEFT,
		WALL_BACK_ANGLE_RIGHT,
		ANGLE_OUT_RIGHT,
		ANGLE_OUT_LEFT,
		SPACE,
	)
}

func (b *Board) IsWallAt(point Point) bool {
	return b.IsAnyAt(point, []Element{
		ANGLE_IN_LEFT, //layer1
		WALL_FRONT,
		ANGLE_IN_RIGHT,
		WALL_RIGHT,
		ANGLE_BACK_RIGHT,
		WALL_BACK,
		ANGLE_BACK_LEFT,
		WALL_LEFT,
		WALL_BACK_ANGLE_LEFT,
		WALL_BACK_ANGLE_RIGHT,
		ANGLE_OUT_RIGHT,
		ANGLE_OUT_LEFT,
		SPACE,
	})
}

func (b *Board) GetBoxes() []Point {
	return b.GetAllPoints(
		BOX, //layer2
	)
}

func (b *Board) GetHoles() []Point {
	return b.GetAllPoints(
		HOLE, //layer1
		ROBO_FALLING,
		ROBO_OTHER_FALLING,
	)
}

func (b *Board) GetExits() []Point {
	return b.GetAllPoints(
		EXIT, //layer1
	)
}

func (b *Board) GetStarts() []Point {
	return b.GetAllPoints(
		START, //layer1
	)
}

func (b *Board) GetGold() []Point {
	return b.GetAllPoints(
		GOLD, //layer1
	)
}

func (b *Board) GetZombies() []Point {
	return b.GetAllPoints(
		FEMALE_ZOMBIE, //layer2
		MALE_ZOMBIE,
		ZOMBIE_DIE,
	)
}

func (b *Board) GetPerks() []Point {
	return b.GetAllPoints(
		DEATH_RAY_PERK, //layer1
		UNLIMITED_FIRE_PERK,
		UNSTOPPABLE_LASER_PERK,
		FIRE_PERK,
		JUMP_PERK,
		MOVE_BOXES_PERK,
	)
}

func (b *Board) AreMyHeroesDead() bool {
	return 0 == len(b.GetAllPoints(
		ROBO, ROBO_FALLING, ROBO_LASER,
	))
}

func (b *Board) BoardSize() int {
	return boardSize
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

func (r rawLayers) toMap() mapLayers {
	m := make(map[int][]rune)
	for i, l := range []string(r) {
		m[i] = []rune(l)
	}
	return m
}
