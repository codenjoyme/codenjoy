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

package action

import "icancode/direction"

type Action string

const (
	NOTHING Action = ""
	DIE     Action = "Act(0)"
	JUMP    Action = "Act(1)"
	FIRE    Action = "Act(3)"
	PULL    Action = "Act(2)"
	RESET   Action = "Act(0)"
)

//Resets current level
func Reset() Action {
	return RESET
}

//Says to Hero jump to direction
func JumpTo(d direction.Direction) Action {
	return Action(string(JUMP) + "," + d.String())
}

//Says to Hero jump in place
func Jump() Action {
	return JUMP
}

//Says to Hero Move to direction
func Move(d direction.Direction) Action {
	return Action(d.String())
}

//Says to Hero fire on this direction
func Fire(d direction.Direction) Action {
	return Action(string(FIRE) + "," + d.String())
}

//Says to Hero pull box on this direction
func Pull(d direction.Direction) Action {
	return Action(string(PULL) + "," + d.String())
}

//Says to Hero to do nothing
func DoNothing() Action {
	return NOTHING
}

//Says to Hero to die
func Die() Action {
	return DIE
}
