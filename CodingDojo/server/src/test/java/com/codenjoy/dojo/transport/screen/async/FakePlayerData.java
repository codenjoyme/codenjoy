package com.codenjoy.dojo.transport.screen.async;

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


import com.codenjoy.dojo.transport.screen.ScreenData;

import java.util.List;

public class FakePlayerData implements ScreenData {
    FakePlayerData(List<SomePlot> plots, int score, int linesRemoved,
               String nextLevelIngoingCriteria, int level, String info) {
        this.plots = plots;
        this.score = score;
        this.linesRemoved = linesRemoved;
        this.nextLevelIngoingCriteria = nextLevelIngoingCriteria;
        this.level = level;
        this.info = info;
    }

    private List<SomePlot> plots;
    private int score;
    private int linesRemoved;
    private String nextLevelIngoingCriteria;
    private int level;
    private String info;

    public List<SomePlot> getPlots() {
        return plots;
    }

    public int getScore() {
        return score;
    }

    public int getLinesRemoved() {
        return linesRemoved;
    }

    public String getNextLevelIngoingCriteria() {
        return nextLevelIngoingCriteria;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return String.format(
                "PlayerData[Plots:%s, " +
                        "Score:%s, " +
                        "LinesRemoved:%s, " +
                        "NextLevelIngoingCriteria:'%s', " +
                        "CurrentLevel:%s, " +
                        "Info:'%s']",
                plots.toString(),
                score,
                linesRemoved,
                nextLevelIngoingCriteria,
                level,
                getInfo());
    }

    public String getInfo() {
        return (info == null)?"":info;
    }
}
