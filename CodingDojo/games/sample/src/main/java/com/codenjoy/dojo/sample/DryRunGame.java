package com.codenjoy.dojo.sample;

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

import com.codenjoy.dojo.client.KeyboardSolver;
import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.sample.client.Board;
import com.codenjoy.dojo.sample.services.GameRunner;

/**
 * Created by Oleksandr_Baglai on 2019-07-12.
 */
public class DryRunGame {

    public static void main(String[] args) {
        LocalGameRunner.run(new GameRunner(),
                new KeyboardSolver(),
                // new AISolver(new RandomDice()),
                new Board());
    }
}
