package com.codenjoy.dojo.spacerace.model.flyingitems;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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
import com.codenjoy.dojo.spacerace.model.Hero;
import com.codenjoy.dojo.spacerace.model.Player;
import com.codenjoy.dojo.spacerace.model.Spacerace;
import com.codenjoy.dojo.spacerace.model.Stone;
import com.codenjoy.dojo.spacerace.services.Events;

public class StoneController extends FlyingItemController<Stone> {

    public StoneController(Spacerace game, List<Stone> container, List<Explosion> explosions) {
        super(game, (x) -> new Stone(x, game.size()), container, Events.DESTROY_STONE, explosions, 3);
    }

    @Override
    protected void actWithHero() {
        Collection<Stone> badPlaces = new ArrayList<>(container);

        for (Player player : game.getPlayers()) {
            Hero hero = player.getHero();
            for (Stone item : badPlaces) {
                if (hero.itsMe(item)) {
                    heroDie(item, player);
                }
            }
        }
    }

    private void heroDie(Stone point, Player player) {
        container.remove(point);

        player.getHero().die();
    }

}
