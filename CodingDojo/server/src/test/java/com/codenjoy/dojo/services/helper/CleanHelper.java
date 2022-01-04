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

import com.codenjoy.dojo.services.FieldService;
import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.SaveService;
import com.codenjoy.dojo.services.chat.PlayersCache;
import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.semifinal.SemifinalService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
@AllArgsConstructor
public class CleanHelper {

    private Chat chat;
    private PlayerService players;
    private Registration registration;
    private GameService games;
    private FieldService fields;
    private PlayersCache playersCache;
    private SaveService saves;
    private SemifinalService semifinal;
    private TimeHelper time;

    public void removeAll() {
        // order is important
        players.removeAll();
        registration.removeAll();
        semifinal.clean();
        games.removeAll(); // тут чистятся rooms и связанные с ними сеттинги
        fields.removeAll();
        playersCache.removeAll();
        saves.removeAllSaves();
        chat.removeAll();
        time.removeAll();
    }
}
