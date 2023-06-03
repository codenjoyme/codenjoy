package com.codenjoy.dojo.services.helper;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import com.codenjoy.dojo.services.DealsService;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.GameServiceImpl;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.level.LevelsSettings;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.settings.SettingsReader;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@Lazy
@Component
@AllArgsConstructor
public class RoomHelper {

    private RoomService rooms;
    private GameServiceImpl games;
    private DealsService dealsService;

    public SettingsReader settings(String room, String game) {
        GameType type = rooms.create(room, games.getGameType(game));
        SettingsReader settings = (SettingsReader) type.getSettings();
        return settings;
    }

    public LevelsSettings levelsSettings(String room, String game) {
        return (LevelsSettings) settings(room, game);
    }

    public void removeAll() {
        games.init(); // тут чистятся rooms и связанные с ними сеттинги
    }

    public void mockDice(String game, Dice dice) {
        games.replace(game, real -> {
            GameType spy = spy(real);
            when(spy.getDice()).thenReturn(dice);
            return spy;
        });
    }

    public String board(String id) {
        return dealsService.get(id).getGame().getBoardAsString().toString();
    }
}
