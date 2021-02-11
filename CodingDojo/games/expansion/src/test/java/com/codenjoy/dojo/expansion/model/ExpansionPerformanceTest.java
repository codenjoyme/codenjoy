package com.codenjoy.dojo.expansion.model;

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


import com.codenjoy.dojo.profile.Profiler;
import com.codenjoy.dojo.services.Game;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by Sanja on 15.02.14.
 */
public class ExpansionPerformanceTest extends AbstractGameRunnerTest {

    public static final int COUNT_USERS = 100;

    @Test
    public void test() {
        for (int index = 0; index < COUNT_USERS; index++) {
            createNewGame();
        }

        Profiler profiler = new Profiler();

        profiler.start();
        for (int index = 0; index < 10; index++) {
            for (Game game : games) {
                game.getBoardAsString();
                profiler.done("getBoardAsString");
            }
        }
        profiler.print();
    }
}
