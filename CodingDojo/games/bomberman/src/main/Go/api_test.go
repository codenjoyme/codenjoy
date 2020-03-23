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

import (
	"sort"
	"strings"
	"testing"

	"github.com/stretchr/testify/assert"
)

func Test_GetBomberman(t *testing.T) {
	type tstruct struct {
		name          string
		board         *board
		expectedPoint Point
	}

	tests := []tstruct{
		{
			name: "Found",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			expectedPoint: Point{1, 28},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			assert.Equal(t, tt.expectedPoint, tt.board.GetBomberman())
		})
	}
}

func Test_GetOtherBombermans(t *testing.T) {
	type tstruct struct {
		name           string
		board          *board
		expectedPoints []Point
	}

	tests := []tstruct{
		{
			name: "Found",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			expectedPoints: []Point{{5, 28}, {17, 27}, {1, 24}, {20, 1}},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			assert.Equal(t, tt.expectedPoints, tt.board.GetOtherBombermans())
		})
	}
}

func Test_IsMyBombermanDead(t *testing.T) {
	type tstruct struct {
		name   string
		board  *board
		isDead bool
	}

	tests := []tstruct{
		{
			name: "Not dead",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			isDead: false,
		}, {
			name: "Dead",
			board: &board{
				boardContent: []rune(strings.Replace(testValidBoard, string(BOMBERMAN), string(DEAD_BOMBERMAN), 1)),
			},
			isDead: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			assert.Equal(t, tt.isDead, tt.board.IsMyBombermanDead())
		})
	}
}

func Test_IsAt(t *testing.T) {
	type tstruct struct {
		name    string
		board   *board
		point   Point
		element Element
		result  bool
	}

	tests := []tstruct{
		{
			name: "First cell",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			point:   Point{0, 0},
			element: WALL,
			result:  true,
		}, {
			name: "Other bomberman",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			point:   Point{1, 24},
			element: OTHER_BOMBERMAN,
			result:  true,
		}, {
			name: "Wrong wall",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			point:   Point{1, 1},
			element: WALL,
			result:  false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			assert.Equal(t, tt.result, tt.board.IsAt(tt.point, tt.element))
		})
	}
}

func Test_IsAtAny(t *testing.T) {
	type tstruct struct {
		name     string
		board    *board
		point    Point
		elements []Element
		result   bool
	}

	tests := []tstruct{
		{
			name: "Set #1",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			point:    Point{5, 1},
			elements: []Element{NONE, WALL},
			result:   false,
		}, {
			name: "Set #2",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			point:    Point{1, 2},
			elements: []Element{DESTROYABLE_WALL},
			result:   true,
		}, {
			name: "Set #3",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			point:    Point{20, 1},
			elements: []Element{BOMBERMAN, BOMB_BOMBERMAN, DEAD_BOMBERMAN, OTHER_BOMBERMAN},
			result:   true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			assert.Equal(t, tt.result, tt.board.IsAtAny(tt.point, tt.elements))
		})
	}
}

func Test_IsNear(t *testing.T) {
	type tstruct struct {
		name    string
		board   *board
		point   Point
		element Element
		result  bool
	}

	tests := []tstruct{
		{
			name: "First cell",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			point:   Point{1, 1},
			element: WALL,
			result:  true,
		}, {
			name: "Other bomberman",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			point:   Point{1, 28},
			element: DESTROYABLE_WALL,
			result:  true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			assert.Equal(t, tt.result, tt.board.IsNear(tt.point, tt.element))
		})
	}
}

func Test_IsBarrierAt(t *testing.T) {
	type tstruct struct {
		name   string
		board  *board
		point  Point
		result bool
	}

	tests := []tstruct{
		{
			name: "Destroyable wall under the bomberman",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			point:  Point{1, 27},
			result: true,
		}, {
			name: "None above the bomberman",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			point:  Point{1, 29},
			result: false,
		}, {
			name: "Wall to the right of bomberman",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			point:  Point{2, 28},
			result: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			assert.Equal(t, tt.result, tt.board.IsBarrierAt(tt.point))
		})
	}
}

func Test_CountNear(t *testing.T) {
	type tstruct struct {
		name    string
		board   *board
		point   Point
		element Element
		count   int
	}

	tests := []tstruct{
		{
			name: "First cell",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			point:   Point{1, 1},
			element: WALL,
			count:   2,
		}, {
			name: "Other bomberman",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			point:   Point{1, 28},
			element: DESTROYABLE_WALL,
			count:   1,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			assert.Equal(t, tt.count, tt.board.CountNear(tt.point, tt.element))
		})
	}
}

func Test_GetAt(t *testing.T) {
	type tstruct struct {
		name    string
		board   *board
		point   Point
		element Element
	}

	tests := []tstruct{
		{
			name: "First cell",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			point:   Point{1, 1},
			element: NONE,
		}, {
			name: "Wall under me",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			point:   Point{1, 27},
			element: DESTROYABLE_WALL,
		}, {
			name: "Bomberman",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			point:   Point{1, 28},
			element: BOMBERMAN,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			assert.Equal(t, tt.element, tt.board.GetAt(tt.point))
		})
	}
}

func Test_GetBarriers(t *testing.T) {
	type tstruct struct {
		name    string
		board   *board
		points  []Point
		element Element
	}

	tests := []tstruct{
		{
			name: "All barriers",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			points: []Point{
				{26, 29}, {5, 28}, {17, 27}, {1, 24}, {20, 1}, {29, 31}, {10, 29}, {23, 23}, {0, 32}, {1, 32}, {2, 32}, {3, 32}, {4, 32}, {5, 32}, {6, 32}, {7, 32}, {8, 32}, {9, 32}, {10, 32}, {11, 32}, {12, 32}, {13, 32}, {14, 32}, {15, 32}, {16, 32}, {17, 32}, {18, 32}, {19, 32}, {20, 32}, {21, 32}, {22, 32}, {23, 32}, {24, 32}, {25, 32}, {26, 32}, {27, 32}, {28, 32}, {29, 32}, {30, 32}, {31, 32}, {32, 32}, {0, 31}, {32, 31}, {0, 30}, {2, 30}, {4, 30}, {6, 30}, {8, 30}, {10, 30}, {12, 30}, {14, 30}, {16, 30}, {18, 30}, {20, 30}, {22, 30}, {24, 30}, {26, 30}, {28, 30}, {30, 30}, {32, 30}, {0, 29}, {32, 29}, {0, 28}, {2, 28}, {4, 28}, {6, 28}, {8, 28}, {10, 28}, {12, 28}, {14, 28}, {16, 28}, {18, 28}, {20, 28}, {22, 28}, {24, 28}, {26, 28}, {28, 28}, {30, 28}, {32, 28}, {0, 27}, {32, 27}, {0, 26}, {2, 26}, {4, 26}, {6, 26}, {8, 26}, {10, 26}, {12, 26}, {14, 26}, {16, 26}, {18, 26}, {20, 26}, {22, 26}, {24, 26}, {26, 26}, {28, 26}, {30, 26}, {32, 26}, {0, 25}, {32, 25}, {0, 24}, {2, 24}, {4, 24}, {6, 24}, {8, 24}, {10, 24}, {12, 24}, {14, 24}, {16, 24}, {18, 24}, {20, 24}, {22, 24}, {24, 24}, {26, 24}, {28, 24}, {30, 24}, {32, 24}, {0, 23}, {32, 23}, {0, 22}, {2, 22}, {4, 22}, {6, 22}, {8, 22}, {10, 22}, {12, 22}, {14, 22}, {16, 22}, {18, 22}, {20, 22}, {22, 22}, {24, 22}, {26, 22}, {28, 22}, {30, 22}, {32, 22}, {0, 21}, {32, 21}, {0, 20}, {2, 20}, {4, 20}, {6, 20}, {8, 20}, {10, 20}, {12, 20}, {14, 20}, {16, 20}, {18, 20}, {20, 20}, {22, 20}, {24, 20}, {26, 20}, {28, 20}, {30, 20}, {32, 20}, {0, 19}, {32, 19}, {0, 18}, {2, 18}, {4, 18}, {6, 18}, {8, 18}, {10, 18}, {12, 18}, {14, 18}, {16, 18}, {18, 18}, {20, 18}, {22, 18}, {24, 18}, {26, 18}, {28, 18}, {30, 18}, {32, 18}, {0, 17}, {32, 17}, {0, 16}, {2, 16}, {4, 16}, {6, 16}, {8, 16}, {10, 16}, {12, 16}, {14, 16}, {16, 16}, {18, 16}, {20, 16}, {22, 16}, {24, 16}, {26, 16}, {28, 16}, {30, 16}, {32, 16}, {0, 15}, {32, 15}, {0, 14}, {2, 14}, {4, 14}, {6, 14}, {8, 14}, {10, 14}, {12, 14}, {14, 14}, {16, 14}, {18, 14}, {20, 14}, {22, 14}, {24, 14}, {26, 14}, {28, 14}, {30, 14}, {32, 14}, {0, 13}, {32, 13}, {0, 12}, {2, 12}, {4, 12}, {6, 12}, {8, 12}, {10, 12}, {12, 12}, {14, 12}, {16, 12}, {18, 12}, {20, 12}, {22, 12}, {24, 12}, {26, 12}, {28, 12}, {30, 12}, {32, 12}, {0, 11}, {32, 11}, {0, 10}, {2, 10}, {4, 10}, {6, 10}, {8, 10}, {10, 10}, {12, 10}, {14, 10}, {16, 10}, {18, 10}, {20, 10}, {22, 10}, {24, 10}, {26, 10}, {28, 10}, {30, 10}, {32, 10}, {0, 9}, {32, 9}, {0, 8}, {2, 8}, {4, 8}, {6, 8}, {8, 8}, {10, 8}, {12, 8}, {14, 8}, {16, 8}, {18, 8}, {20, 8}, {22, 8}, {24, 8}, {26, 8}, {28, 8}, {30, 8}, {32, 8}, {0, 7}, {32, 7}, {0, 6}, {2, 6}, {4, 6}, {6, 6}, {8, 6}, {10, 6}, {12, 6}, {14, 6}, {16, 6}, {18, 6}, {20, 6}, {22, 6}, {24, 6}, {26, 6}, {28, 6}, {30, 6}, {32, 6}, {0, 5}, {32, 5}, {0, 4}, {2, 4}, {4, 4}, {6, 4}, {8, 4}, {10, 4}, {12, 4}, {14, 4}, {16, 4}, {18, 4}, {20, 4}, {22, 4}, {24, 4}, {26, 4}, {28, 4}, {30, 4}, {32, 4}, {0, 3}, {32, 3}, {0, 2}, {2, 2}, {4, 2}, {6, 2}, {8, 2}, {10, 2}, {12, 2}, {14, 2}, {16, 2}, {18, 2}, {20, 2}, {22, 2}, {24, 2}, {26, 2}, {28, 2}, {30, 2}, {32, 2}, {0, 1}, {32, 1}, {0, 0}, {1, 0}, {2, 0}, {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0}, {8, 0}, {9, 0}, {10, 0}, {11, 0}, {12, 0}, {13, 0}, {14, 0}, {15, 0}, {16, 0}, {17, 0}, {18, 0}, {19, 0}, {20, 0}, {21, 0}, {22, 0}, {23, 0}, {24, 0}, {25, 0}, {26, 0}, {27, 0}, {28, 0}, {29, 0}, {30, 0}, {31, 0}, {32, 0}, {2, 31}, {11, 31}, {12, 31}, {13, 31}, {14, 31}, {17, 31}, {19, 31}, {21, 31}, {22, 31}, {23, 31}, {24, 31}, {25, 31}, {26, 31}, {27, 31}, {28, 31}, {31, 31}, {5, 30}, {7, 30}, {15, 30}, {17, 30}, {23, 30}, {3, 29}, {5, 29}, {7, 29}, {12, 29}, {13, 29}, {18, 29}, {20, 29}, {21, 29}, {22, 29}, {23, 29}, {31, 29}, {3, 28}, {17, 28}, {31, 28}, {1, 27}, {3, 27}, {4, 27}, {5, 27}, {6, 27}, {21, 27}, {23, 27}, {27, 27}, {28, 27}, {30, 27}, {31, 27}, {3, 26}, {5, 26}, {7, 26}, {23, 26}, {1, 25}, {4, 25}, {5, 25}, {6, 25}, {9, 25}, {23, 25}, {24, 25}, {26, 25}, {30, 25}, {31, 25}, {3, 24}, {29, 24}, {1, 23}, {2, 23}, {31, 23}, {5, 22}, {23, 22}, {3, 21}, {6, 21}, {1, 20}, {1, 19}, {2, 19}, {26, 19}, {28, 17}, {25, 16}, {2, 15}, {19, 15}, {26, 15}, {11, 14}, {23, 14}, {27, 14}, {2, 13}, {7, 13}, {13, 13}, {26, 11}, {1, 10}, {3, 10}, {5, 10}, {17, 10}, {2, 9}, {11, 9}, {17, 8}, {23, 8}, {6, 7}, {7, 6}, {3, 5}, {26, 5}, {25, 4}, {31, 4}, {2, 3}, {4, 3}, {14, 3}, {1, 2}, {19, 2}, {25, 2}, {31, 2}, {8, 1}, {29, 1}, {20, 31}, {21, 14}, {1, 13}, {4, 11}, {27, 9}, {23, 7}, {5, 5}, {23, 2}, {5, 1}, {9, 1},
			},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			assert.Equal(t, tt.points, tt.board.GetBarriers())
		})
	}
}

func Test_GetMeatChoppers(t *testing.T) {
	type tstruct struct {
		name     string
		board    *board
		choppers []Point
	}

	tests := []tstruct{
		{
			name: "Get all choppers",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			choppers: []Point{{20, 31}, {21, 14}, {1, 13}, {4, 11}, {27, 9}, {23, 7}, {5, 5}, {23, 2}, {5, 1}, {9, 1}},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			assert.Equal(t, tt.choppers, tt.board.GetMeatChoppers())
		})
	}
}

func Test_GetWalls(t *testing.T) {
	type tstruct struct {
		name     string
		board    *board
		choppers []Point
	}

	tests := []tstruct{
		{
			name: "Get all walls",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			choppers: []Point{{0, 32}, {1, 32}, {2, 32}, {3, 32}, {4, 32}, {5, 32}, {6, 32}, {7, 32}, {8, 32}, {9, 32}, {10, 32}, {11, 32}, {12, 32}, {13, 32}, {14, 32}, {15, 32}, {16, 32}, {17, 32}, {18, 32}, {19, 32}, {20, 32}, {21, 32}, {22, 32}, {23, 32}, {24, 32}, {25, 32}, {26, 32}, {27, 32}, {28, 32}, {29, 32}, {30, 32}, {31, 32}, {32, 32}, {0, 31}, {32, 31}, {0, 30}, {2, 30}, {4, 30}, {6, 30}, {8, 30}, {10, 30}, {12, 30}, {14, 30}, {16, 30}, {18, 30}, {20, 30}, {22, 30}, {24, 30}, {26, 30}, {28, 30}, {30, 30}, {32, 30}, {0, 29}, {32, 29}, {0, 28}, {2, 28}, {4, 28}, {6, 28}, {8, 28}, {10, 28}, {12, 28}, {14, 28}, {16, 28}, {18, 28}, {20, 28}, {22, 28}, {24, 28}, {26, 28}, {28, 28}, {30, 28}, {32, 28}, {0, 27}, {32, 27}, {0, 26}, {2, 26}, {4, 26}, {6, 26}, {8, 26}, {10, 26}, {12, 26}, {14, 26}, {16, 26}, {18, 26}, {20, 26}, {22, 26}, {24, 26}, {26, 26}, {28, 26}, {30, 26}, {32, 26}, {0, 25}, {32, 25}, {0, 24}, {2, 24}, {4, 24}, {6, 24}, {8, 24}, {10, 24}, {12, 24}, {14, 24}, {16, 24}, {18, 24}, {20, 24}, {22, 24}, {24, 24}, {26, 24}, {28, 24}, {30, 24}, {32, 24}, {0, 23}, {32, 23}, {0, 22}, {2, 22}, {4, 22}, {6, 22}, {8, 22}, {10, 22}, {12, 22}, {14, 22}, {16, 22}, {18, 22}, {20, 22}, {22, 22}, {24, 22}, {26, 22}, {28, 22}, {30, 22}, {32, 22}, {0, 21}, {32, 21}, {0, 20}, {2, 20}, {4, 20}, {6, 20}, {8, 20}, {10, 20}, {12, 20}, {14, 20}, {16, 20}, {18, 20}, {20, 20}, {22, 20}, {24, 20}, {26, 20}, {28, 20}, {30, 20}, {32, 20}, {0, 19}, {32, 19}, {0, 18}, {2, 18}, {4, 18}, {6, 18}, {8, 18}, {10, 18}, {12, 18}, {14, 18}, {16, 18}, {18, 18}, {20, 18}, {22, 18}, {24, 18}, {26, 18}, {28, 18}, {30, 18}, {32, 18}, {0, 17}, {32, 17}, {0, 16}, {2, 16}, {4, 16}, {6, 16}, {8, 16}, {10, 16}, {12, 16}, {14, 16}, {16, 16}, {18, 16}, {20, 16}, {22, 16}, {24, 16}, {26, 16}, {28, 16}, {30, 16}, {32, 16}, {0, 15}, {32, 15}, {0, 14}, {2, 14}, {4, 14}, {6, 14}, {8, 14}, {10, 14}, {12, 14}, {14, 14}, {16, 14}, {18, 14}, {20, 14}, {22, 14}, {24, 14}, {26, 14}, {28, 14}, {30, 14}, {32, 14}, {0, 13}, {32, 13}, {0, 12}, {2, 12}, {4, 12}, {6, 12}, {8, 12}, {10, 12}, {12, 12}, {14, 12}, {16, 12}, {18, 12}, {20, 12}, {22, 12}, {24, 12}, {26, 12}, {28, 12}, {30, 12}, {32, 12}, {0, 11}, {32, 11}, {0, 10}, {2, 10}, {4, 10}, {6, 10}, {8, 10}, {10, 10}, {12, 10}, {14, 10}, {16, 10}, {18, 10}, {20, 10}, {22, 10}, {24, 10}, {26, 10}, {28, 10}, {30, 10}, {32, 10}, {0, 9}, {32, 9}, {0, 8}, {2, 8}, {4, 8}, {6, 8}, {8, 8}, {10, 8}, {12, 8}, {14, 8}, {16, 8}, {18, 8}, {20, 8}, {22, 8}, {24, 8}, {26, 8}, {28, 8}, {30, 8}, {32, 8}, {0, 7}, {32, 7}, {0, 6}, {2, 6}, {4, 6}, {6, 6}, {8, 6}, {10, 6}, {12, 6}, {14, 6}, {16, 6}, {18, 6}, {20, 6}, {22, 6}, {24, 6}, {26, 6}, {28, 6}, {30, 6}, {32, 6}, {0, 5}, {32, 5}, {0, 4}, {2, 4}, {4, 4}, {6, 4}, {8, 4}, {10, 4}, {12, 4}, {14, 4}, {16, 4}, {18, 4}, {20, 4}, {22, 4}, {24, 4}, {26, 4}, {28, 4}, {30, 4}, {32, 4}, {0, 3}, {32, 3}, {0, 2}, {2, 2}, {4, 2}, {6, 2}, {8, 2}, {10, 2}, {12, 2}, {14, 2}, {16, 2}, {18, 2}, {20, 2}, {22, 2}, {24, 2}, {26, 2}, {28, 2}, {30, 2}, {32, 2}, {0, 1}, {32, 1}, {0, 0}, {1, 0}, {2, 0}, {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0}, {8, 0}, {9, 0}, {10, 0}, {11, 0}, {12, 0}, {13, 0}, {14, 0}, {15, 0}, {16, 0}, {17, 0}, {18, 0}, {19, 0}, {20, 0}, {21, 0}, {22, 0}, {23, 0}, {24, 0}, {25, 0}, {26, 0}, {27, 0}, {28, 0}, {29, 0}, {30, 0}, {31, 0}, {32, 0}},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			assert.Equal(t, tt.choppers, tt.board.GetWalls())
		})
	}
}

func Test_GetDestroyableWalls(t *testing.T) {
	type tstruct struct {
		name     string
		board    *board
		choppers []Point
	}

	tests := []tstruct{
		{
			name: "Get all walls",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			choppers: []Point{{2, 31}, {11, 31}, {12, 31}, {13, 31}, {14, 31}, {17, 31}, {19, 31}, {21, 31}, {22, 31}, {23, 31}, {24, 31}, {25, 31}, {26, 31}, {27, 31}, {28, 31}, {31, 31}, {5, 30}, {7, 30}, {15, 30}, {17, 30}, {23, 30}, {3, 29}, {5, 29}, {7, 29}, {12, 29}, {13, 29}, {18, 29}, {20, 29}, {21, 29}, {22, 29}, {23, 29}, {31, 29}, {3, 28}, {17, 28}, {31, 28}, {1, 27}, {3, 27}, {4, 27}, {5, 27}, {6, 27}, {21, 27}, {23, 27}, {27, 27}, {28, 27}, {30, 27}, {31, 27}, {3, 26}, {5, 26}, {7, 26}, {23, 26}, {1, 25}, {4, 25}, {5, 25}, {6, 25}, {9, 25}, {23, 25}, {24, 25}, {26, 25}, {30, 25}, {31, 25}, {3, 24}, {29, 24}, {1, 23}, {2, 23}, {31, 23}, {5, 22}, {23, 22}, {3, 21}, {6, 21}, {1, 20}, {1, 19}, {2, 19}, {26, 19}, {28, 17}, {25, 16}, {2, 15}, {19, 15}, {26, 15}, {11, 14}, {23, 14}, {27, 14}, {2, 13}, {7, 13}, {13, 13}, {26, 11}, {1, 10}, {3, 10}, {5, 10}, {17, 10}, {2, 9}, {11, 9}, {17, 8}, {23, 8}, {6, 7}, {7, 6}, {3, 5}, {26, 5}, {25, 4}, {31, 4}, {2, 3}, {4, 3}, {14, 3}, {1, 2}, {19, 2}, {25, 2}, {31, 2}, {8, 1}, {29, 1}},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			assert.Equal(t, tt.choppers, tt.board.GetDestroyableWalls())
		})
	}
}

func Test_GetBombs(t *testing.T) {
	type tstruct struct {
		name     string
		board    *board
		choppers []Point
	}

	tests := []tstruct{
		{
			name: "Get all bombs",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			choppers: []Point{{26, 29}, {29, 31}, {10, 29}, {23, 23}},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			assert.Equal(t, tt.choppers, tt.board.GetBombs())
		})
	}
}

func Test_GetFutureBlasts(t *testing.T) {
	type tstruct struct {
		name   string
		board  *board
		blasts []Point
	}

	tests := []tstruct{
		{
			name: "Get all future blasts",
			board: &board{
				boardContent: []rune(testValidBoard),
			},
			blasts: []Point{{29, 29}, {8, 29}, {26, 23}, {27, 29}, {29, 30}, {29, 28}, {24, 23}, {11, 29}, {25, 23}, {21, 23}, {20, 23}, {28, 29}, {25, 29}, {24, 29}, {30, 31}, {23, 24}, {9, 29}, {22, 23}},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			blasts := tt.board.GetFutureBlasts()
			sort.Slice(blasts, func(i, j int) bool { return blasts[i].X+blasts[i].Y*33 < blasts[j].X+blasts[j].Y*33 })
			sort.Slice(tt.blasts, func(i, j int) bool { return tt.blasts[i].X+tt.blasts[i].Y*33 < tt.blasts[j].X+tt.blasts[j].Y*33 })
			assert.Equal(t, tt.blasts, blasts)
		})
	}
}
