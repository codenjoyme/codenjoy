package com.codenjoy.dojo.kata.model;

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


import com.codenjoy.dojo.kata.services.events.NextAlgorithmEvent;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {

    @Test
    @Ignore // TODO валится в maven cmd
    public void shouldWork() throws InterruptedException {
        Timer timer = new Timer();
        timer.start();

        Thread.sleep(5000);

        double end = timer.end();
        assertEquals(5D/60D, end,0.001);
    }
}

