package com.codenjoy.dojo.icancode.model;

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

import com.codenjoy.dojo.icancode.model.items.*;
import org.junit.Test;

import static com.codenjoy.dojo.icancode.model.Elements.Layers.*;
import static org.junit.Assert.assertEquals;

public class ItemsTest {

    @Test
    public void testLayers() {
        assertEquals(LAYER2, new Air().layer());
        assertEquals(LAYER2, new Box(Elements.BOX).layer());
        assertEquals(LAYER1, new Exit(Elements.EXIT).layer());
        assertEquals(LAYER1, new Floor(Elements.FLOOR).layer());
        assertEquals(LAYER1, new Gold(Elements.GOLD).layer());

        assertEquals(LAYER2, new Hero(Elements.ROBO).getItem().layer());
        assertEquals(LAYER3, new Hero(Elements.ROBO){{ jump(); tick(); }}.getItem().layer());

        assertEquals(LAYER1, new Hole(Elements.HOLE).layer());
        assertEquals(LAYER2, new Laser(Elements.LASER_UP).layer());
        assertEquals(LAYER2, new Laser(Elements.LASER_DOWN).layer());
        assertEquals(LAYER2, new Laser(Elements.LASER_LEFT).layer());
        assertEquals(LAYER2, new Laser(Elements.LASER_RIGHT).layer());
        assertEquals(LAYER1, new LaserMachine(Elements.LASER_MACHINE_CHARGING_DOWN).layer());
        assertEquals(LAYER1, new Start(Elements.START).layer());
        assertEquals(LAYER1, new Wall(Elements.WALL_BACK).layer());
        assertEquals(LAYER2, new Zombie(true).layer());
        assertEquals(LAYER1, new ZombiePot(Elements.ZOMBIE_START).layer());
    }
}
