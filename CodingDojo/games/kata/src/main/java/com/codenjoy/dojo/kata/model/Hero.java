package com.codenjoy.dojo.kata.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.joystick.MessageJoystick;
import org.json.JSONArray;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Это реализация героя. Обрати внимание, что он имплементит {@see Joystick}, а значит может быть управляем фреймворком
 * Так же он имплементит {@see Tickable}, что значит - есть возможность его оповещать о каждом тике игры.
 */
public class Hero extends MessageJoystick implements Tickable {

    private Field field;
    private boolean alive;
    private String answers;
    
    private boolean skipLevel;
    private boolean nextLevel;

    public Hero() {
        alive = true;
        clearFlags();
    }

    public void init(Field field) {
        this.field = field;
    }

    @Override
    public void message(String answers) {
        this.answers = answers;
    }

    @Override
    public void tick() {
        if (!alive) return;
    }

    public boolean isAlive() {
        return alive;
    }

    public List<String> popAnswers() {
        String answers = this.answers;
        this.answers = null;

        if (answers == null) {
            return Arrays.asList();
        }

        JSONArray array = new JSONArray(answers);
        List<String> result = new LinkedList<>();
        for (Object object : array) {
            result.add(object.toString());
        }
        return result;
    }

    @Override
    public void act(int... p) {
        if (p.length != 1) {
            return;
        }
        
        int status = p[0];
        if (status == 0) {
            skipLevel = true; 
        }
        if (status == 1) {
            nextLevel = true;
        }
    }

    public boolean wantsSkipLevel() {
        return skipLevel;
    }

    public boolean wantsNextLevel() {
        return nextLevel;
    }

    public void clearFlags() {
        skipLevel = false;
        nextLevel = false;        
    }
}
