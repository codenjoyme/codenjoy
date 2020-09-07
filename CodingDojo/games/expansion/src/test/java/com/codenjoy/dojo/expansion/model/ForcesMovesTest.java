package com.codenjoy.dojo.expansion.model;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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


import com.codenjoy.dojo.services.QDirection;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Oleksandr_Baglai on 2017-08-29.
 */
public class ForcesMovesTest {

    @Test
    public void testParse() {
        JSONObject json = new JSONObject("{region:{x:3, y:4}, direction:'left_down', count:2}");

        // when
        ForcesMoves forces = new ForcesMoves(json);

        // then
        assertEquals(2, forces.getCount());
        assertEquals(QDirection.LEFT_DOWN.name(), forces.getDirection());
        assertEquals("[3,4]", forces.getRegion().toString());
    }

    @Test
    public void testParse_withoutDirection() {
        JSONObject json = new JSONObject("{region:{x:-1, y:-5}, count:2}");

        // when
        ForcesMoves forces = new ForcesMoves(json);

        // then
        assertEquals(2, forces.getCount());
        assertEquals(QDirection.NONE.name(), forces.getDirection());
        assertEquals("[-1,-5]", forces.getRegion().toString());
    }

    @Test
    public void testParse_upperCaseDirection() {
        JSONObject json = new JSONObject("{region:{x:3, y:4}, direction:'LEFT_DOWN', count:2}");

        // when
        ForcesMoves forces = new ForcesMoves(json);

        // then
        assertEquals(2, forces.getCount());
        assertEquals(QDirection.LEFT_DOWN.name(), forces.getDirection());
        assertEquals("[3,4]", forces.getRegion().toString());
    }

}