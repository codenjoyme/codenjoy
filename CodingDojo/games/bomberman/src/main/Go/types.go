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

const (
	BOMBERMAN      = '☺'             // this is what he usually looks like
	BOMB_BOMBERMAN = '☻'             // this is if he is sitting on own bomb
	DEAD_BOMBERMAN = 'Ѡ'             // oops your Bomberman is dead (don`t worry he will appear somewhere in next move)
	                                 // you`re getting -200 for each death

	/// this is other players Bombermans
	OTHER_BOMBERMAN      = '♥'       // this is what other Bombermans looks like
	OTHER_BOMB_BOMBERMAN = '♠'       // this is if player just set the bomb
	OTHER_DEAD_BOMBERMAN = '♣'       // enemy corpse (it will disappear shortly right on the next move)
	                                 // if you`ve done it you`ll get +1000

	/// the bombs
	BOMB_TIMER_5 = '5'               // after bomberman set the bomb the timer starts (5 tacts)
	BOMB_TIMER_4 = '4'               // this will blow up after 4 tacts
	BOMB_TIMER_3 = '3'               // this after 3
	BOMB_TIMER_2 = '2'               // two
	BOMB_TIMER_1 = '1'               // one
	BOOM         = '҉'               // Boom! this is what is bomb does everything that is destroyable got destroyed

	/// walls
	WALL             = '☼'           // indestructible wall - it will not fall from bomb
	DESTROYABLE_WALL = '#'           // this wall could be blowed up
	DESTROYED_WALL   = 'H'           // this is how broken wall looks like it will dissapear on next move
	                                 // if it`s you did it - you`ll get +10 points.

	/// meatchoppers
	MEAT_CHOPPER = '&'               // this guys runs over the board randomly and gets in the way all the time
                                     // if it will touch bomberman - it will die
    DEAD_MEAT_CHOPPER = 'x'          // you`d better kill this piece of ... meat you`ll get +100 point for it
	                                 // this is chopper corpse

	/// perks
	BOMB_BLAST_RADIUS_INCREASE = '+' // Bomb blast radius increase. Applicable only to new bombs. The perk is temporary.
	BOMB_COUNT_INCREASE        = 'c' // Increase available bombs count. Number of extra bombs can be set in settings. Temporary.
	BOMB_REMOTE_CONTROL        = 'r' // Bomb blast not by timer but by second act. Number of RC triggers is limited and can be set in settings.
	BOMB_IMMUNE                = 'i' // Do not die after bomb blast (own bombs and others as well). Temporary.

	/// a void
	NONE = ' '                       // this is the only place where you can move your Bomberman

	BoardSize  = 33
	BLAST_SIZE = 3 // Blast size
)

type Action string

const (
	UP    Action = "UP"
	DOWN  Action = "DOWN"
	RIGHT Action = "RIGHT"
	LEFT  Action = "LEFT"
	ACT   Action = "ACT"
	STOP  Action = "STOP"
	// Combinations
	UPA    Action = "UP,ACT"
	DOWNA  Action = "DOWN,ACT"
	RIGHTA Action = "RIGHT,ACT"
	LEFTA  Action = "LEFT,ACT"
	// Vice versa, order matters
	AUP    Action = "ACT,UP"
	ADOWN  Action = "ACT,DOWN"
	ARIGHT Action = "ACT,RIGHT"
	ALEFT  Action = "ACT,LEFT"
)

func (m Action) IsValid() bool {
	switch m {
	case UP, DOWN, RIGHT, LEFT, ACT, STOP, UPA, DOWNA, RIGHTA, LEFTA, AUP, ADOWN, ARIGHT, ALEFT:
		return true
	default:
		return false
	}
}
