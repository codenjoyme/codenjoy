package main

import (
	"./board"
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

	question := board.Question{}
	question.FutureFigures = []string{"I", "O", "L", "Z"}
	question.CurrentFigurePoint = board.Point{1, 2}
	question.CurrentFigureType = "T"
	question.Layers = []string{
		"......." +
		"......I" +
		"..LL..I" +
		"...LI.I" +
		".SSLI.I" +
		"SSOOIOO" +
		"..OOIOO"}

	board := board.NewBoard(&question)
	board.ToString()

	test.assert("T", board.CurrentFigureType)
	test.assert("[1,2]", board.CurrentFigurePoint.String())
	test.assert("I,O,L,Z", strings.Join(board.FutureFigures, ","))
	test.assert(".", board.GetAt(0, 0))

	test.print()
}