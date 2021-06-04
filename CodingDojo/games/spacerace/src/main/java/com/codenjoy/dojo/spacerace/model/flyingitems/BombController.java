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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.spacerace.model.Bomb;
import com.codenjoy.dojo.spacerace.model.BombWave;
import com.codenjoy.dojo.spacerace.model.Bullet;
import com.codenjoy.dojo.spacerace.model.Explosion;
import com.codenjoy.dojo.spacerace.model.Hero;
import com.codenjoy.dojo.spacerace.model.Player;
import com.codenjoy.dojo.spacerace.model.Spacerace;
import com.codenjoy.dojo.spacerace.services.Events;

public class BombController extends FlyingItemController<Bomb> {

    public BombController(Spacerace game, List<Bomb> container, List<Explosion> explosions) {
        super(game, (x) -> new Bomb(x, game.size()), container, Events.DESTROY_BOMB, explosions, 4);
    }

    @Override
    protected void actWithHero() {
        Collection<BombWave> badPlaces = getBombWaves(container);

        for (Player player : game.getPlayers()) {
            Hero hero = player.getHero();
            for (BombWave wave : badPlaces) {
                if (hero.itsMe(wave)) {
                    heroDie(wave.getBomb(), player);
                }
            }
        }
    }

    public static Collection<BombWave> getBombWaves(List<Bomb> container) {
        Collection<BombWave> badPlaces = new LinkedList<>();
        for (Bomb bomb : container) {
            for (int x = bomb.getX() - 1; x < bomb.getX() + 2; x++) {
                for (int y = bomb.getY() - 1; y < bomb.getY() + 2; y++) {
                    badPlaces.add(new BombWave(x, y, bomb));
                }
            }
        }
        return badPlaces;
    }

    private void heroDie(Bomb point, Player player) {

        bombExplosion(point);
        container.remove(point);

        player.getHero().die();
    }

    private void bombExplosion(Point pt) {
        for (int x = pt.getX() - 1; x < pt.getX() + 2; x++) {
            for (int y = pt.getY() - 1; y < pt.getY() + 2; y++) {
                if (y != game.size()) {
                    explosions.add(new Explosion(x, y));
                }
            }
        }
    }

    @Override
    protected void bulletAction(Bullet bullet) {
        bombExplosion(bullet);
    }
}
