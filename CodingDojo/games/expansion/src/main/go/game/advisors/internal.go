package advisors

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

import "github.com/arukim/expansion/game"
import m "github.com/arukim/expansion/models"

// General contains game logic
type Internal struct {
}

func NewInternal() *Internal {
	return &Internal{}
}

func (g *Internal) MakeTurn(b *game.Board, t *m.Turn) {

	//b.InsideMap.Print()
	for i := 0; i < b.Size; i++ {
		if b.InsideMap.Data[i] > 2 && b.ForcesMap.Data[i] > 1 {
			p1 := m.NewPoint(i, b.Width)
			move := b.GetDirection(p1, b.InsideMap)
			move.Count = b.ForcesMap.Data[i] - 1

			t.Movements = append(t.Movements, *move)
		}
	}
}
