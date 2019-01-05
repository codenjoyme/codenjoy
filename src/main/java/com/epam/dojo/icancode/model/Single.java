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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.layeredview.PrinterData;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * А вот тут немного хак :) Дело в том, что фреймворк изначально не поддерживал игры типа "все на однмо поле", а потому
 * пришлось сделать этот декоратор. Борда (@see Sample) - одна на всех, а игры (@see Single) у каждого своя. Кода тут не много.
 */
public class Single implements Game {

    private ProgressBar progressBar;
    private Player player;

    public Single(ICanCode single, ICanCode multiple, EventListener listener, PrinterFactory factory, String save) {
        progressBar = new ProgressBar(single, multiple);
        player = new Player(listener, progressBar);
        progressBar.setPlayer(player);
        if (!StringUtils.isEmpty(save)) {
            progressBar.loadProgress(save);
        }
    }

    @Override
    public Joystick getJoystick() {
        return player.getHero();
    }

    @Override
    public int getMaxScore() {
        return player.getMaxScore();
    }

    @Override
    public int getCurrentScore() {
        return player.getScore();
    }

    @Override
    public boolean isGameOver() {
        return !player.hero.isAlive();
    }

    @Override
    public void newGame() {
        progressBar.newGame(player);
    }

    @Override
    public JSONObject getBoardAsString() {
        PrinterData data = getPrinter().print();

        JSONObject result = new JSONObject();
        result.put("layers", data.getLayers());
        result.put("offset", data.getOffset());
        JSONObject progress = progressBar.printProgress();
        result.put("showName", true);
        result.put("onlyMyName", !progress.getBoolean("multiple"));
        result.put("levelProgress", progress);
        return result;
    }

    @Override
    public void destroy() {
        progressBar.remove(player);
    }

    @Override
    public void clearScore() {
        player.clearScore();
    }

    @Override
    public HeroData getHero() {
        return new ICanCodeHeroData();
    }

    public class ICanCodeHeroData implements HeroData {
        @Override
        public Point getCoordinate() {
            return new PointImpl(player.getHero().getPosition());
        }

        @Override
        public boolean isMultiplayer() {
            return progressBar.isMultiple();
        }

        @Override
        public int getLevel() {
            return 0;
        }

        @Override
        public Object getAdditionalData() {
            JSONObject result = new JSONObject();
            result.put("hello", "world"); // TODO remove me :)
            return result;
        }

        @Override
        public List<Game> playersGroup() {
            return new LinkedList<Game>(){{
                add(Single.this);
            }};
        }
    };

    @Override
    public String getSave() {
        return progressBar.printProgress().toString();
    }

    @Override
    public void tick() {
        progressBar.tick();
    }

    public Player getPlayer() {
        return player;
    }

    public Printer<PrinterData> getPrinter() {
        return progressBar.getPrinter();
    }
}
