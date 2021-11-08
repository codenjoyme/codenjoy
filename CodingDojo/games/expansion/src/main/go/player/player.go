package player

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
	"fmt"
	"log"

	"github.com/arukim/expansion/game"
	"github.com/arukim/expansion/game/advisors"
	"github.com/arukim/expansion/models"
)

type gameStage int

const (
	earlyGame gameStage = iota
	lateGame
)

type Player struct {
	id        int
	advisors  map[gameStage][]advisors.Advisor
	gameStage gameStage
}

// NewPlayer const
func NewPlayer(id int) *Player {
	p := &Player{id: id}

	p.NewGame()

	return p
}

func (p *Player) NewGame() {
	p.gameStage = earlyGame
	p.advisors = make(map[gameStage][]advisors.Advisor)

	p.advisors[earlyGame] = []advisors.Advisor{
		//advisors.NewGoldHunter(),
		advisors.NewEarlyExplorer(),
		advisors.NewDefenceAdvisor(),
		advisors.NewInternal(),
	}

	p.advisors[lateGame] = []advisors.Advisor{
		advisors.NewDefenceAdvisor(),
		advisors.NewExplorer(),
		advisors.NewGeneral(),
		advisors.NewInternal(),
	}
}

func (p *Player) MakeTurn(turnInfo *models.TurnInfo) *models.Turn {

	b := game.NewBoard(turnInfo)

	if b.Turn == -1 {
		log.Println("waiting for game in lobby...")
		return &models.Turn{}
	}
	if b.Turn == 0 {
		p.NewGame()
	}

	if b.TotalWalkCells == b.MyInfo.TerritorySize {
		log.Println("won game")
		return nil
	}

	playerTurn := &models.Turn{
		Increase:  []models.Increase{},
		Movements: []models.Movement{},
	}

	fmt.Printf("T[%d] inc: %d space: %d freeForces: %d occup: %f stage: %+v\n",
		b.Turn, b.ForcesAvailable, b.MyInfo.TerritorySize,
		b.MyInfo.ForcesFree, b.OccupationRate, p.gameStage)

	if b.OccupationRate > 0.9 {
		p.gameStage = lateGame
	}

	if b.MyInfo.ForcesTotal > 0 {
		var advSet = p.advisors[p.gameStage]

		for _, adv := range advSet {
			adv.MakeTurn(b, playerTurn)
		}
	} else {
		fmt.Println("I've done")
	}
	//fmt.Printf("P%d making turn\n", p.id)
	//fmt.Printf("player turn is %+v\n", playerTurn)
	return playerTurn
}
