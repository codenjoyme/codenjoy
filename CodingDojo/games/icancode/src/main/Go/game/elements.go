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
	// empty space where player can go
	EMPTY = '-'
	FLOOR = '.'

	// walls
	ANGLE_IN_LEFT         = '╔'
	WALL_FRONT            = '═'
	ANGLE_IN_RIGHT        = '┐'
	WALL_RIGHT            = '│'
	ANGLE_BACK_RIGHT      = '┘'
	WALL_BACK             = '─'
	ANGLE_BACK_LEFT       = '└'
	WALL_LEFT             = '║'
	WALL_BACK_ANGLE_LEFT  = '┌'
	WALL_BACK_ANGLE_RIGHT = '╗'
	ANGLE_OUT_RIGHT       = '╝'
	ANGLE_OUT_LEFT        = '╚'
	SPACE                 = ' '

	// laser machine
	LASER_MACHINE_CHARGING_LEFT  = '˂'
	LASER_MACHINE_CHARGING_RIGHT = '˃'
	LASER_MACHINE_CHARGING_UP    = '˄'
	LASER_MACHINE_CHARGING_DOWN  = '˅'

	// lase machine ready
	LASER_MACHINE_READY_LEFT  = '◄'
	LASER_MACHINE_READY_RIGHT = '►'
	LASER_MACHINE_READY_UP    = '▲'
	LASER_MACHINE_READY_DOWN  = '▼'

	// other stuff
	START        = 'S'
	EXIT         = 'E'
	HOLE         = 'O'
	BOX          = 'B'
	ZOMBIE_START = 'Z'
	GOLD         = '$'

	// your robot
	ROBO         = '☺'
	ROBO_FALLING = 'o'
	ROBO_FLYING  = '*'
	ROBO_LASER   = '☻'

	// other robot
	ROBO_OTHER         = 'X'
	ROBO_OTHER_FALLING = 'x'
	ROBO_OTHER_FLYING  = '^'
	ROBO_OTHER_LASER   = '&'

	// laser
	LASER_LEFT  = '←'
	LASER_RIGHT = '→'
	LASER_UP    = '↑'
	LASER_DOWN  = '↓'

	// zombie
	FEMALE_ZOMBIE = '♀'
	MALE_ZOMBIE   = '♂'
	ZOMBIE_DIE    = '✝'

	// perks
	UNSTOPPABLE_LASER_PERK = 'l'
	DEATH_RAY_PERK         = 'r'
	UNLIMITED_FIRE_PERK    = 'f'
	FIRE_PERK              = 'a'
    JUMP_PERK              = 'j'
    MOVE_BOXES_PERK        = 'm'

	// system elements, don't touch it
	FOG        = 'F'
	BACKGROUND = 'G'
)

type Element rune

func (e Element) getLayer() int {
	switch e {
	case START, EXIT, HOLE, ZOMBIE_START, GOLD, FLOOR, SPACE,
		ANGLE_BACK_LEFT, ANGLE_BACK_RIGHT, ANGLE_IN_LEFT, ANGLE_IN_RIGHT, ANGLE_OUT_LEFT, ANGLE_OUT_RIGHT,
		WALL_BACK, WALL_BACK_ANGLE_LEFT, WALL_BACK_ANGLE_RIGHT, WALL_FRONT, WALL_LEFT, WALL_RIGHT,
		LASER_MACHINE_CHARGING_DOWN, LASER_MACHINE_CHARGING_LEFT, LASER_MACHINE_CHARGING_RIGHT, LASER_MACHINE_CHARGING_UP,
		LASER_MACHINE_READY_DOWN, LASER_MACHINE_READY_LEFT, LASER_MACHINE_READY_RIGHT, LASER_MACHINE_READY_UP,
		DEATH_RAY_PERK, UNLIMITED_FIRE_PERK, UNSTOPPABLE_LASER_PERK, FIRE_PERK, JUMP_PERK, MOVE_BOXES_PERK,
		FOG:
		return 0
	case ROBO, ROBO_FALLING, ROBO_LASER, ROBO_OTHER, ROBO_OTHER_FALLING, ROBO_OTHER_LASER,
		LASER_DOWN, LASER_LEFT, LASER_RIGHT, LASER_UP,
		FEMALE_ZOMBIE, MALE_ZOMBIE, ZOMBIE_DIE,
		EMPTY, BOX,
		BACKGROUND:
		return 1
	case ROBO_FLYING, ROBO_OTHER_FLYING:
		return 2
	}
	return 0 //prob there is no such element, at least try to find on 1st layer
}
