package com.codenjoy.dojo.expansion.services;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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


import com.codenjoy.dojo.services.settings.Settings;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Oleksandr_Baglai on 2017-09-14.
 */
public class CommandParser { // TODO test this monster
    private GameRunner runner;

    public CommandParser(GameRunner runner) {
        this.runner = runner;
    }

    public void parse(Settings settings) {
        if (settings.whatChanged().toString().equals("[Command]")) {

            String command = SettingsWrapper.data.command();
            if (command.equals("lobby.letThemGo()")) {
//                if (runner.lobby instanceof WaitForAllPlayerLobby) {
//                    ((WaitForAllPlayerLobby) runner.lobby).letThemGo(false);
//                }
            }
//            else if (command.equals("any command that you want to run from admin")) {

//            }
            SettingsWrapper.data.command(StringUtils.EMPTY);
            settings.changesReacted();
        }
    }
}
