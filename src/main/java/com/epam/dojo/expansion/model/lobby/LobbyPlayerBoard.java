package com.epam.dojo.expansion.model.lobby;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2017 EPAM
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


import com.codenjoy.dojo.services.DLoggerFactory;
import com.codenjoy.dojo.services.Point;
import com.epam.dojo.expansion.model.*;
import com.epam.dojo.expansion.model.levels.CellImpl;
import com.epam.dojo.expansion.model.levels.Level;
import com.epam.dojo.expansion.model.levels.LevelImpl;
import com.epam.dojo.expansion.model.levels.Levels;
import com.epam.dojo.expansion.model.levels.items.Hero;
import com.epam.dojo.expansion.model.levels.items.HeroForces;
import com.epam.dojo.expansion.model.levels.items.Start;
import com.epam.dojo.expansion.services.SettingsWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * Created by Oleksandr_Baglai on 2017-09-11.
 */
public abstract class LobbyPlayerBoard implements PlayerBoard, Field {

    private static Logger logger = DLoggerFactory.getLogger(LobbyPlayerBoard.class);

    public static final int BOARD_SIZE = 20;
    public static final Point NULL_POINT = pt(-1, -1);
    private List<Player> waiting;
    private LevelImpl level;

    public LobbyPlayerBoard(List<Player> waiting) {
        this.waiting = waiting;
        level = Levels.getLevel(size(), "MULTILOBBY");
    }

    @Override
    public int getViewSize() {
        return BOARD_SIZE;
    }

    @Override
    public int size() {
        return BOARD_SIZE;
    }

    @Override
    public Level getCurrentLevel() {
        return level;
    }

    @Override
    public boolean isMultiple() {
        return true;
    }

    @Override
    public int levelsCount() {
        return 0;
    }

    @Override
    public void loadLevel(int level) {
        // do nothing
    }

    @Override
    public List<Player> getPlayers() {
        return waiting;
    }

    @Override
    public int getRoundTicks() {
        return SettingsWrapper.UNLIMITED;
    }

    @Override
    public String id() {
        return "ELB@" + Integer.toHexString(this.hashCode());
    }

    @Override
    public void increase(Hero hero, List<ForcesMoves> increase) {
        // do nothing
    }

    @Override
    public void move(Hero hero, List<ForcesMoves> movements) {
        // do nothing
    }

    @Override
    public Start getBaseOf(Hero hero) {
        return getStart(hero.getPosition());
    }

    @Nullable
    @Override
    public Start getFreeBase() {
        return getStart(NULL_POINT);
    }

    @NotNull
    private Start getStart(Point pt) {
        Start start = new Start(Elements.BASE1);
        start.setCell(new CellImpl(pt));
        return start;
    }

    @Override
    public HeroForces startMoveForces(Hero item, int x, int y, int count) {
        return HeroForces.EMPTY;
    }

    @Override
    public void removeForces(Hero hero, int x, int y) {
        // do nothing
    }

    @Override
    public void reset() {
        // do nothing
    }

    @Override
    public void removeFromCell(Hero hero) {
        // do nothing
    }

    @Override
    public int totalRegions() {
        return 1;
    }

    @Override
    public int regionsCount(Hero hero) {
        return 0;
    }

    @Override
    public void tick() {
        // do nothing
    }

    @Override
    public boolean isFree() {
        return true;
    }

    @Override
    public int freeBases() {
        return Integer.MAX_VALUE;
    }
}
