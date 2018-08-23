package com.codenjoy.dojo.lunolet.model;

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


import com.codenjoy.dojo.services.EventListener;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class HeroTest {

    @Test
    public void initAndThenUp() {
        EventListener listener = mock(EventListener.class);
        Player player = new Player(listener);

        Hero hero = new Hero(player);
        hero.init(new LevelManager().getLevel(0));

        Assert.assertEquals(0, player.getCurrentLevel());
        Assert.assertTrue(hero.isAlive());
        Assert.assertNotNull(hero.getLevelRelief());
        Assert.assertNotNull(hero.getVesselStatus());
        Assert.assertEquals(0.0, hero.getVesselStatus().Time, 1e-5);
        Assert.assertNotNull(hero.getTarget());
        Assert.assertNotNull(hero.getVesselHistory());
        Assert.assertEquals(0, hero.getVesselHistory().size());

        hero.up();

        Assert.assertEquals(0, player.getCurrentLevel());
        Assert.assertTrue(hero.isAlive());
        Assert.assertNotNull(hero.getLevelRelief());
        Assert.assertNotNull(hero.getVesselStatus());
        Assert.assertEquals(VesselState.FLIGHT, hero.getVesselStatus().State);
        Assert.assertEquals(1.0, hero.getVesselStatus().Time, 1e-5);
        Assert.assertEquals(0.0, hero.getLastAngle(), 1e-5);
        Assert.assertNotNull(hero.getTarget());
        Assert.assertNotNull(hero.getVesselHistory());
        Assert.assertEquals(2, hero.getVesselHistory().size());
    }
}
