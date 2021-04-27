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


import com.codenjoy.dojo.icancode.services.GameSettings;
import com.codenjoy.dojo.icancode.services.Levels;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.layeredview.LayeredViewPrinter;
import com.codenjoy.dojo.services.printer.layeredview.PrinterData;
import org.json.JSONObject;

public class Player extends GamePlayer<Hero, Field> {

    private Printer<PrinterData> printer;

    public Player(EventListener listener, GameSettings settings) {
        super(listener, settings);
        setupPrinter();
    }

    private void setupPrinter() {
        printer = new LayeredViewPrinter(
                () -> field.layeredReader(),
                () -> this,
                Levels.COUNT_LAYERS);
    }

    @Override
    protected boolean shouldCreate() {
        return hero == null;
    }

    @Override
    public Hero createHero(Point pt) {
        return new Hero();
    }

    @Override
    public boolean isWin() {
        return hero != null && hero.isWin();
    }

    @Override
    public HeroData getHeroData() {
        return new ICanCodeHeroData();
    }

    // TODO test me
    public boolean isLevelFinished() {
        Hero hero = getHero();
        Point exit = field.getEndPosition();
        return hero.getPosition().equals(exit) && !hero.isFlying();
    }

    // TODO test me
    public Point getHeroOffset(Point offset) {
        return getHero().getPosition().relative(offset);
    }

    public Printer<PrinterData> getPrinter() {
        return printer;
    }

    public class ICanCodeHeroData implements HeroData {
        @Override
        public Point getCoordinate() {
            return Player.this.getHero().getPosition().copy();
        }

        @Override
        public boolean isMultiplayer() {
            return Player.this.field.isContest();
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
    }
}