package com.codenjoy.dojo.loderunner.model.items;

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


import com.codenjoy.dojo.games.loderunner.Element;
import org.junit.Test;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;

public class BrickTest {

    @Test
    public void test() {
        Brick brick = new Brick(pt(0, 0));

        assertEquals(Element.BRICK, brick.state(null));

        brick.drill(null);
        brick.tick();

        assertEquals(Element.DRILL_PIT, brick.state(null));

        brick.tick();

        assertEquals(Element.NONE, brick.state(null));

        brick.tick();

        assertEquals(Element.NONE, brick.state(null));

        brick.tick();
        brick.tick();
        brick.tick();
        brick.tick();
        brick.tick();

        assertEquals(Element.NONE, brick.state(null));

        brick.tick();

        assertEquals(Element.PIT_FILL_4, brick.state(null));

        brick.tick();

        assertEquals(Element.PIT_FILL_3, brick.state(null));

        brick.tick();

        assertEquals(Element.PIT_FILL_2, brick.state(null));

        brick.tick();

        assertEquals(Element.PIT_FILL_1, brick.state(null));

        brick.tick();

        assertEquals(Element.BRICK, brick.state(null));
    }
}
