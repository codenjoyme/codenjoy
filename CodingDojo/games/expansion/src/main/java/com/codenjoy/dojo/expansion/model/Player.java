package com.codenjoy.dojo.expansion.model;

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


import com.codenjoy.dojo.expansion.model.levels.Levels;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.layeredview.LayeredViewPrinter;
import com.codenjoy.dojo.services.printer.layeredview.PrinterData;
import com.codenjoy.dojo.utils.JsonUtils;
import com.codenjoy.dojo.expansion.model.levels.items.Hero;
import com.codenjoy.dojo.expansion.services.Events;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class Player extends GamePlayer<Hero, IField> {

    private static Logger logger = DLoggerFactory.getLogger(Player.class);

    Hero hero;
    private String name;
    private IField field;
    private Printer<PrinterData> printer;
    private boolean isWin;

    public Player(EventListener listener, String name) {
        super(listener);
        this.name = name;
        isWin = false;
        setupPrinter();
    }

    private void setupPrinter() {
        printer = new LayeredViewPrinter(
                () -> field.layeredReader(),
                () -> this,
                Levels.COUNT_LAYERS);
    }

    public void event(Events event) {
        if (logger.isDebugEnabled()) {
            logger.debug("Player {} fired event {}", lg.id(), event);
        }
        super.event(event);

        //if (listener != null /* TODO что это было тут? && progressBar.enableWinScore()*/) {
        //    listener.event(event);
        //}
    }

    public Hero getHero() {
        return hero;
    }

    public void newHero(IField field) {
        this.field = field;
        if (hero == null) {
            hero = new Hero();
        }

        hero.init(field);
    }

    @Override
    public boolean isAlive() {
        return hero.isAlive();
    }

    @Override
    public boolean isWin() {
        return isWin;
    }

    public void goToNextLevel() {
        isWin = true;
    }

    public int getForcesColor() {
        return hero.getBase().element().getIndex();
    }

    public IField getField() {
        return field;
    }

    @Override
    public HeroData getHeroData() {
        return new GameHeroData();
    }

    public int getRoundTicks() {
        return field.getRoundTicks();
    }

    public class GameHeroData implements HeroData {
        @Override
        public Point getCoordinate() {
            return new PointImpl(getHero().getBasePosition());
        }

        @Override
        public boolean isMultiplayer() {
            return Player.this.field.isMultiplayer();
        }

        @Override
        public int getLevel() {
            return 0;
        }

        @Override
        public Object getAdditionalData() {
            JSONObject result = new JSONObject();
            result.put("lastAction", getCurrentAction());

            if (logger.isDebugEnabled()) {
                logger.debug("getAdditionalData for game {} prepare {}", lg.id(), JsonUtils.toStringSorted(result));
            }

            return result;
        }

    };

    public Point getBasePosition() {
        return hero.getBasePosition();
    }

    public JSONObject getCurrentAction() {
        return hero.getCurrentAction();
    }

    public int getForcesPerTick() {
        return hero.getForcesPerTick();
    }

    public void destroyHero() {
        if (hero != null) {
            hero.destroy();
        }
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public String getName() {
        return name;
    }

    public class LogState {
        public JSONObject json() {
            return new JSONObject(){{
                put("id", id());
                put("name", name);
                put("hero", (hero != null) ? hero.lg.json() : "null");
                put("field", (field != null) ? field.id() : "null");
            }};
        }

        public String id() {
            return "P@" + Integer.toHexString(Player.this.hashCode());
        }
    }

    public static List<String> lg(List<Player> players) {
        return players.stream().map(p -> (p != null) ? p.lg.id() : "null").collect(toList());
    }

    public LogState lg = new LogState();

    public Printer<PrinterData> getPrinter() {
        return printer;
    }

    @Override
    public String toString() {
        return JsonUtils.toStringSorted(lg.json());
    }
}