package com.epam.dojo.icancode.model;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.epam.dojo.icancode.model.interfaces.IField;
import org.json.JSONObject;

public class Player extends GamePlayer<Hero, IField> {

    Hero hero;
    private IField field;

    public Player(EventListener listener) {
        super(listener);
    }

    public Hero getHero() {
        return hero;
    }

    public void newHero(IField field) {
        this.field = field;
        if (hero == null) {
            hero = new Hero(Elements.ROBO);
        }

        hero.init(field);
    }

    @Override
    public boolean isAlive() {
        return hero != null && hero.isAlive();
    }

    public void tick() {
        hero.tick();
    }

    @Override
    public boolean isWin() {
        return hero != null && hero.isWin();
    }

    @Override
    public HeroData getHeroData() {
        return new ICanCodeHeroData();
    }

    public IField getField() {
        return field;
    }

    public class ICanCodeHeroData implements HeroData {
        @Override
        public Point getCoordinate() {
            return new PointImpl(Player.this.getHero().getPosition());
        }

        @Override
        public boolean isMultiplayer() {
            return Player.this.field.isMultiplayer();
        }

        @Override
        public int getLevel() {
            return 0; // TODO а тут что вернуть?
        }

        @Override
        public Object getAdditionalData() {
            JSONObject result = new JSONObject();
            result.put("hello", "world"); // TODO remove me :)
            return result;
        }

    };
}