package com.codenjoy.dojo.battlecity.services.rooms;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import com.codenjoy.dojo.battlecity.services.GameRunner;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.settings.Parameter;

public class GameRunner4 extends GameRunner {

    private final Parameter<Integer> playersPerRoom;

    @Override
    public String name() {
        return "battlecity4";
    }

    public GameRunner4() {
        super();

        playersPerRoom = settings.addEditBox("Players per room").type(Integer.class).def(5);
    }

    @Override
    public MultiplayerType getMultiplayerType() {
        return MultiplayerType.TEAM.apply(playersPerRoom.getValue(), MultiplayerType.DISPOSABLE);
    }

    public String getMap() {
        return  "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼  ¿   ¿   ¿    ¿   ¿   ¿  ☼" +
                "☼                          ☼" +
                "☼ ╬ ╬╬╬ ╬╬╬ ╬╬╬╬ ╬╬╬ ╬╬╬ ╬ ☼" +
                "☼                          ☼" +
                "☼  ☼☼☼☼     ☼     ☼     ☼  ☼" +
                "☼  ☼   ☼    ☼ ☼   ☼☼    ☼  ☼" +
                "☼  ☼    ☼  ☼   ☼  ☼ ☼   ☼  ☼" +
                "☼  ☼    ☼  ☼    ☼ ☼  ☼  ☼  ☼" +
                "☼  ☼    ☼  ☼ ☼☼☼☼ ☼   ☼    ☼" +
                "☼  ☼   ☼  ☼     ☼ ☼    ☼   ☼" +
                "☼  ☼ ☼☼   ☼     ☼ ☼     ☼  ☼" +
                "☼                          ☼" +
                "☼          ╬    ╬          ☼" +
                "☼☼☼☼☼ ╬╬╬ ╬╬    ╬╬ ╬╬╬ ☼☼☼☼☼" +
                "☼          ╬╬  ╬╬          ☼" +
                "☼   ╬                  ╬   ☼" +
                "☼   ╬   ☼☼☼     ☼☼☼☼☼  ╬   ☼" +
                "☼   ╬╬   ☼        ☼   ╬╬   ☼" +
                "☼    ╬   ☼  ╬╬╬╬  ☼   ╬    ☼" +
                "☼    ╬   ☼   ╬╬   ☼   ╬    ☼" +
                "☼    ╬  ☼☼☼  ╬╬   ☼   ╬    ☼" +
                "☼                          ☼" +
                "☼    ╬╬╬  ╬╬╬╬╬╬╬╬  ╬╬╬    ☼" +
                "☼                          ☼" +
                "☼╬╬     ╬╬        ╬╬     ╬╬☼" +
                "☼╬╬╬    ╬╬        ╬╬    ╬╬╬☼" +
                "☼╬╬╬╬╬    ╬      ╬    ╬╬╬╬╬☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼";
    }
}
