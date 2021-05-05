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
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import com.codenjoy.dojo.spacerace.model.Bullet;
import com.codenjoy.dojo.spacerace.model.Explosion;
import com.codenjoy.dojo.spacerace.model.FlyingItem;
import com.codenjoy.dojo.spacerace.model.Hero;
import com.codenjoy.dojo.spacerace.model.Spacerace;
import com.codenjoy.dojo.spacerace.services.Events;

public abstract class FlyingItemController<T extends FlyingItem> {

    private final int appearPeriod;

    protected final Spacerace game;
    private final Function<Integer, T> supplier;
    protected final List<T> container;
    private final Events bulletEvent;
    protected final List<Explosion> explosions;

    private int counter;

    public FlyingItemController(
            Spacerace game,
            Function<Integer, T> supplier,
            List<T> container,
            Events bulletEvent,
            List<Explosion> explosions,
            int appearPeriod) {
        this.game = game;
        this.supplier = supplier;
        this.container = container;
        this.bulletEvent = bulletEvent;
        this.explosions = explosions;
        this.appearPeriod = appearPeriod;
    }

    public void create(List<Integer> emptyFrontPoints) {
        counter++;
        if (counter >= appearPeriod) {
            counter = 0;
            if (emptyFrontPoints.size() == 0) {
                throw new RuntimeException("Извините не нашли пустого места");
            }

            int i = game.dice(emptyFrontPoints.size());
            if (i == -1)
                return;
            Integer x = emptyFrontPoints.remove(i);
            T newItem = supplier.apply(x);
            container.add(newItem);


        }
    }

    public void tick(List<Bullet> bullets) {
        removeDestroyedByBullet(bullets);
        actWithHero();
        for (T item : container) {
            item.tick();
        }
        removeDestroyedByBullet(bullets);
        actWithHero();
    }

    public void removeOutOfBoard() {
        for (Iterator<T> item = container.iterator(); item.hasNext();) {
            if (item.next().isOutOf(game.size())) {
                item.remove();
            }
        }
    }

    @SuppressWarnings("unlikely-arg-type")
    private void removeDestroyedByBullet(Collection<Bullet> bullets) {

        for (Iterator<Bullet> iterator = bullets.iterator(); iterator.hasNext();) {
            Bullet bullet = iterator.next();
            if (container.remove(bullet)) {
                iterator.remove();
                bulletAction(bullet);
                fireWinScoresFor(bullet, bulletEvent);
            }
        }
    }

    private void fireWinScoresFor(Bullet bullet, Events event) {
        Hero hero = bullet.getOwner();
        game.getPlayerFor(hero).ifPresent(p -> p.event(event));
    }

    protected void bulletAction(Bullet bullet) {
        explosions.add(new Explosion(bullet));
    }

    protected abstract void actWithHero();
}
