/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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
﻿namespace LemonadeClient
{
    public class History
    {
        public int Day { get; set; }
        public decimal AssetsBefore { get; set; }
        public decimal AssetsAfter { get; set; }
        public int LemonadeMade { get; set; }
        public int SignsMade { get; set; }
        public decimal LemonadePrice { get; set; }
        public int LemonadeSold { get; set; }
        public decimal Expenses { get; set; }
        public decimal Income { get; set; }
        public decimal Profit { get; set; }
    }
}
