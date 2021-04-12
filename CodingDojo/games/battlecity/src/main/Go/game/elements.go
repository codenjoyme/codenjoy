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

const (
	NONE        = ' '
	BATTLE_WALL = '☼'
	BANG        = 'Ѡ'

	ICE   = '#'
	TREE  = '%'
	RIVER = '~'

	WALL = '╬'

	WALL_DESTROYED_DOWN  = '╩'
	WALL_DESTROYED_UP    = '╦'
	WALL_DESTROYED_LEFT  = '╠'
	WALL_DESTROYED_RIGHT = '╣'

	WALL_DESTROYED_DOWN_TWICE  = '╨'
	WALL_DESTROYED_UP_TWICE    = '╥'
	WALL_DESTROYED_LEFT_TWICE  = '╞'
	WALL_DESTROYED_RIGHT_TWICE = '╡'

	WALL_DESTROYED_LEFT_RIGHT = '│'
	WALL_DESTROYED_UP_DOWN    = '─'

	WALL_DESTROYED_UP_LEFT    = '┌'
	WALL_DESTROYED_RIGHT_UP   = '┐'
	WALL_DESTROYED_DOWN_LEFT  = '└'
	WALL_DESTROYED_DOWN_RIGHT = '┘'

	WALL_DESTROYED = ' '

	BULLET = '•'

	TANK_UP    = '▲'
	TANK_RIGHT = '►'
	TANK_DOWN  = '▼'
	TANK_LEFT  = '◄'

	OTHER_TANK_UP    = '˄'
	OTHER_TANK_RIGHT = '˃'
	OTHER_TANK_DOWN  = '˅'
	OTHER_TANK_LEFT  = '˂'

	AI_TANK_UP    = '?'
	AI_TANK_RIGHT = '»'
	AI_TANK_DOWN  = '¿'
	AI_TANK_LEFT  = '«'

	AI_TANK_PRIZE = '◘'

	PRIZE                  = '!'
	PRIZE_IMMORTALITY      = '1'
	PRIZE_BREAKING_WALLS   = '2'
	PRIZE_WALKING_ON_WATER = '3'
	PRIZE_VISIBILITY       = '4'
	PRIZE_NO_SLIDING       = '5'
)

type Element rune
