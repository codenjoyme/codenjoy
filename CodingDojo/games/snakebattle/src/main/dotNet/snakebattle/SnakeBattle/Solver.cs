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
﻿using System;
using SnakeBattle.Enums;
using SnakeBattle.Interfaces;
using SnakeBattle.Interfaces.Services;

namespace SnakeBattle
{
    public class Solver : ISolver
    {
        public PlayerCommand Decide(IBoardNavigator boardNavigator)
        {
            // Todo Make your magic here!

            var values = Enum.GetValues(typeof(PlayerCommand));
            var random = new Random();
            var randomBotCommand = (PlayerCommand) values.GetValue(random.Next(values.Length));

            return randomBotCommand;
        }
    }
}
