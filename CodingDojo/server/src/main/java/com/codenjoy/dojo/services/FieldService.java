package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.services.multiplayer.GameField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
// TODO test me
public class FieldService {

    private AtomicInteger id;
    private Map<GameField, Integer> fields;

    @Autowired
    private Chat chat;

    @PostConstruct
    public void init() {
        removeAll();
        id.set(chat.getLastFieldId());
    }

    public void register(GameField field) {
        fields.put(field, id.incrementAndGet());
    }

    public int id(GameField field) {
        return fields.get(field);
    }

    public void removeAll() {
        id = new AtomicInteger(0);
        fields = new ConcurrentHashMap<>();
    }

    public void remove(GameField field) {
        fields.remove(field);
    }
}
