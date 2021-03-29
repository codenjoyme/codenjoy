package com.codenjoy.dojo.services;

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


import com.codenjoy.dojo.services.hero.HeroData;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.*;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class GameData {

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Hero {}

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Scores {}

    @Getter private int boardSize;
    @Getter private GuiPlotColorDecoder decoder;
    @Scores private Map<String, Object> scores;
    @Getter @Hero private List<String> group;
    @Hero private Map<String, HeroData> coordinates;
    @Hero private Map<String, String> readableNames;

    public String getScores() {
        return toJson(Scores.class);
    }

    public String getHeroesData() {
        return toJson(Hero.class);
    }

    public String toJson(Class<? extends Annotation> annotation) {
        return new GsonBuilder()
                .addSerializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }

                    @Override
                    public boolean shouldSkipField(FieldAttributes field) {
                        return field.getAnnotation(annotation) == null;
                    }
                })
                .create()
                .toJson(this);
    }
}
