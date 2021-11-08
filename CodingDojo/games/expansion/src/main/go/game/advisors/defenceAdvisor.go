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

import (
	"math/rand"

	"github.com/ahmetb/go-linq"
	"github.com/arukim/expansion/game"
	m "github.com/arukim/expansion/models"
)

// General contains game logic
type DefenceAdvisor struct {
}

func NewDefenceAdvisor() *DefenceAdvisor {
	return &DefenceAdvisor{}
}

func (g *DefenceAdvisor) MakeTurn(b *game.Board, t *m.Turn) {

	borderLands := b.InsideMap.Filter(func(p m.Point, v int) bool {
		return v == 2
	})

	threatList := []pointEx{}

	linq.From(borderLands).SelectT(func(kv linq.KeyValue) pointEx {
		p := kv.Key.(m.Point)
		forces := make([]int, 4)
		freeCells := 0
		mineAdd := 0
		b.Neighbours(p, func(idx int, p1 m.Point) bool {
			pForce := b.ForcesMap.Data[idx]
			if pForce > 1 {
				forces[b.PlayersMap.Data[idx]] += pForce - 1
			} else if pForce == 0 {
				freeCells++
			}
			return true
		})
		maxForce := linq.From(forces).Max().(int)
		myForce := forces[b.TurnInfo.MyColor]

		if _, ok := b.MyInfo.Mines[p]; ok {
			mineAdd = defenceAdvisorMineBonus
		}
		return pointEx{
			P: p,
			V: freeCells + maxForce - myForce + mineAdd,
		}
	}).OrderByT(func(p pointEx) int {
		return p.V
	}).ToSlice(&threatList)

	increaseMap := map[m.Point]int{}
	changed := true
	for b.ForcesAvailable > 0 && changed {
		changed = false
		for i := range threatList {
			t := &threatList[i]

			if t.V > 0 {
				changed = true
				t.V--
				increaseMap[t.P]++
				b.ForcesMap.Data[t.P.GetPos(b.Width)]++

				b.ForcesAvailable--

				if b.ForcesAvailable <= 0 {
					break
				}
			}
		}
	}

	if b.ForcesAvailable > 0 {
		t := threatList[rand.Intn(len(threatList))]

		increaseMap[t.P]++
		b.ForcesMap.Data[t.P.GetPos(b.Width)]++
		b.ForcesAvailable--

	}

	//log.Printf("DA: increase map %+v\n", increaseMap)
	inc := []m.Increase{}
	linq.From(increaseMap).SelectT(func(kv linq.KeyValue) m.Increase {
		return m.Increase{Region: kv.Key.(m.Point), Count: kv.Value.(int)}
	}).ToSlice(&inc)
	t.Increase = append(t.Increase, inc...)
}
