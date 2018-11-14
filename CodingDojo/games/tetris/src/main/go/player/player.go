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
package player

import (
	"../game"
	"../models"
)

type Player struct {
	id        int
}

// NewPlayer const
func NewPlayer(id int) *Player {
	p := &Player{id: id}

	return p
}

func (p *Player) MakeTurn(turnInfo *models.TurnInfo) string {

	b := game.NewBoard(turnInfo)

	if b.CurrentFigureType == "O" {
		return "DOWN"
	}
	return "DOWN"

	//if b.Turn == -1 {
	//	log.Println("waiting for game in lobby...")
	//	return &models.Turn{}
	//}
	//if b.Turn == 0 {
	//	p.NewGame()
	//	log.Printf("total mines is %d\n", b.TotalMines)
	//}

	//if b.TotalWalkCells == b.MyInfo.TerritorySize {
	//	log.Println("won game")
	//	return nil
	//}

	//playerTurn := &models.Turn{
	//	Increase:  []models.Increase{},
	//	Movements: []models.Movement{},
	//}

	//fmt.Printf("T[%d] inc: %d space: %d freeForces: %d occup: %f stage: %+v\n",
	//	b.Turn, b.ForcesAvailable, b.MyInfo.TerritorySize,
	//	b.MyInfo.ForcesFree, b.OccupationRate, p.gameStage)
	//
	//if b.OccupationRate > 0.9 {
	//	p.gameStage = lateGame
	//}

	//if b.MyInfo.ForcesTotal > 0 {
	//	var advSet = p.advisors[p.gameStage]
	//
	//	for _, adv := range advSet {
	//		adv.MakeTurn(b, playerTurn)
	//	}
	//} else {
	//	fmt.Println("I've done")
	//}
	//fmt.Printf("P%d making turn\n", p.id)
	//fmt.Printf("player turn is %+v\n", playerTurn)

}
