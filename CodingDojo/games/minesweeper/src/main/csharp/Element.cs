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
namespace MinesweeperClient
{
	public enum Element : short
	{

		HIDDEN = (short)'*',
		MINE_1 = (short)'1',
		MINE_2 = (short)'2',
		MINE_3 = (short)'3',
		MINE_4 = (short)'4',
		MINE_5 = (short)'5',
		MINE_6 = (short)'6',
		MINE_7 = (short)'7',
		MINE_8 = (short)'8',

		HERO = (short)'☺',

		FLAG = (short)'‼',
		DEAD_BODY = (short)'Ѡ',
		HERE_WAS_BOMB = (short)'☻',
		DESTROYED_BOMB = (short)'x',

		NONE = (short)' ',
		BORDER = (short)'☼',
	}

}
