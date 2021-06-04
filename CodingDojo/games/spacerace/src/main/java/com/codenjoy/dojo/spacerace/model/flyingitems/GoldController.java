package com.codenjoy.dojo.spacerace.model.flyingitems;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.codenjoy.dojo.spacerace.model.Explosion;
import com.codenjoy.dojo.spacerace.model.Gold;
import com.codenjoy.dojo.spacerace.model.Hero;
import com.codenjoy.dojo.spacerace.model.Player;
import com.codenjoy.dojo.spacerace.model.Spacerace;
import com.codenjoy.dojo.spacerace.services.Events;

public class GoldController extends FlyingItemController<Gold> {

    public GoldController(Spacerace game, List<Gold> container, List<Explosion> explosions) {
        super(game, (x) -> new Gold(x, game.size()), container, Events.GET_GOLD, explosions, 5);
    }

    @Override
    protected void actWithHero() {
        Collection<Gold> items = new ArrayList<>(container);

        for (Player player : game.getPlayers()) {
            Hero hero = player.getHero();
            for (Gold item : items) {
                if (hero.itsMe(item)) {
                    container.remove(item);
                    player.event(Events.GET_GOLD);
                }
            }
        }
    }

}
