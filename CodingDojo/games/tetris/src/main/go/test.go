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

package main

import (
	b "./board"
	"encoding/json"
	"fmt"
	"log"
	"strings"
)

type XUnit struct {
	Failures  []string
	Index     int
}

func NewTest() *XUnit {
	t := new(XUnit)
	t.Failures = []string{}
	t.Index = 0
	return t
}

func (t *XUnit) assert(expected interface{}, actual interface{}) {
	t.Index += 1
	if expected == actual {
	} else {
		message := fmt.Sprintf("[%v] \"%v\" != \"%v\"", t.Index, expected, actual)
		t.Failures = append(t.Failures, message)
	}
}

func (t *XUnit) print() {
	log.Println("------------------------------------")
	if len(t.Failures) > 0 {
		for _, it := range t.Failures {
			log.Println("FAIL " + it)
		}
	} else {
		log.Println("SUCCESS!!!")
	}
	log.Println("------------------------------------")
}

func main() {

	test := NewTest()

	question := b.Question{}
	question.FutureFigures = []string{"I", "O", "L", "Z"}
	question.CurrentFigurePoint = b.Point{1, 2}
	question.CurrentFigureType = "T"
	question.Layers = []string{
		"......." +
		"......I" +
		"..LL..I" +
		"...LI.I" +
		".SSLI.I" +
		"SSOOIOO" +
		"..OOIOO"}

	board := b.NewBoard(&question)
	board.ToString()

	test.assert(b.T_PURPLE, board.CurrentFigureType)
	test.assert("[1,2]", board.CurrentFigurePoint.String())
	test.assert("I,O,L,Z", strings.Join(board.FutureFigures, ","))

	test.assert(b.NONE, board.GetAt(0, 0))
	test.assert(false, board.IsAt(0, 0, []string { b.O_YELLOW }))
	test.assert(true, board.IsAt(0, 0, []string { b.NONE }))
	test.assert(true, board.IsAt(0, 0, []string { b.L_ORANGE, b.NONE }))
	test.assert(true, board.IsFree(0, 0))

	test.assert(b.O_YELLOW, board.GetAt(2, 0))
	test.assert(true, board.IsAt(2, 0, []string { b.O_YELLOW }))
	test.assert(false, board.IsAt(2, 0, []string { b.NONE }))
	test.assert(false, board.IsAt(2, 0, []string { b.L_ORANGE, b.NONE }))
	test.assert(false, board.IsFree(2, 0))

	test.assert(b.S_GREEN, board.GetAt(2, 2))
	test.assert(false, board.IsAt(2, 2, []string { b.O_YELLOW }))
	test.assert(false, board.IsAt(2, 2, []string { b.NONE }))
	test.assert(false, board.IsAt(2, 2, []string { b.L_ORANGE, b.NONE }))
	test.assert(false, board.IsFree(2, 2))

	test.assert(b.L_ORANGE, board.GetAt(3, 4))
	test.assert(false, board.IsAt(3, 4, []string { b.O_YELLOW }))
	test.assert(false, board.IsAt(3, 4, []string { b.NONE }))
	test.assert(true, board.IsAt(3, 4, []string { b.L_ORANGE, b.NONE }))
	test.assert(false, board.IsFree(3, 4))

	test.assert("[[0,1], [1,1], [1,2], [2,2]]",
		ToString(board.Get([]string { b.S_GREEN })))

	test.assert("[[2,4], [3,2], [3,3], [3,4]]",
		ToString(board.Get([]string { b.L_ORANGE })))

	test.assert("[[0,1], [1,1], [1,2], [2,2], [2,4], [3,2], [3,3], [3,4]]",
		ToString(board.Get([]string { b.L_ORANGE, b.S_GREEN } )))

	test.assert(".,L,.,L,.,I,.,.", strings.Join(board.GetNear(3, 4), ","))
	test.assert(false, board.IsNear(3, 4, []string{ b.T_PURPLE }))
	test.assert(true, board.IsNear(3, 4, []string{ b.I_BLUE }))
	test.assert(1, board.CountNear(3, 4, []string{ b.I_BLUE }))
	test.assert(2, board.CountNear(3, 4, []string{ b.L_ORANGE }))

	test.assert("S,S,.,O,.,O,L,L", strings.Join(board.GetNear(2, 2), ","))
	test.assert(true, board.IsNear(2, 2, []string{ b.L_ORANGE }))
	test.assert(false, board.IsNear(2, 2, []string{ b.I_BLUE }))
	test.assert(2, board.CountNear(2, 2, []string{ b.L_ORANGE }))
	test.assert(2, board.CountNear(2, 2, []string{ b.S_GREEN }))
	
	test.assert("[[0,1], [1,1], [1,2], [2,0], " +
		"[2,1], [2,2], [2,4], [3,0], " +
		"[3,1], [3,2], [3,3], [3,4], " +
		"[4,0], [4,1], [4,2], [4,3], " +
		"[5,0], [5,1], [6,0], [6,1], " +
		"[6,2], [6,3], [6,4], [6,5]]",
		ToString(board.GetFigures()))

	test.assert("[[0,0], [0,2], [0,3], [0,4], " +
		"[0,5], [0,6], [1,0], [1,3], " +
		"[1,4], [1,5], [1,6], [2,3], " +
		"[2,5], [2,6], [3,5], [3,6], " +
		"[4,4], [4,5], [4,6], [5,2], " +
		"[5,3], [5,4], [5,5], [5,6], " +
		"[6,6]]",
		ToString(board.GetFreeSpace()))

	test.print()
}

func ToString(array []b.Point) string {
	b, _ := json.Marshal(array);
	s := string(b)
	s = strings.Replace(s, "\"x\":", "", -1)
	s = strings.Replace(s, "\"y\":", "", -1)
	s = strings.Replace(s, "{", "[", -1)
	s = strings.Replace(s, "}", "]", -1)
	s = strings.Replace(s, "],[", "], [", -1)
	return s
}