package com.codenjoy.dojo.icancode.model;

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
        assertEquals(LAYER1, new LaserMachine(Elements.LASER_MACHINE_CHARGING_DOWN).layer());
        assertEquals(LAYER1, new Start(Elements.START).layer());
        assertEquals(LAYER1, new Wall(Elements.WALL_BACK).layer());
        assertEquals(LAYER2, new Zombie(true).layer());
        assertEquals(LAYER1, new ZombiePot(Elements.ZOMBIE_START).layer());
    }
}
