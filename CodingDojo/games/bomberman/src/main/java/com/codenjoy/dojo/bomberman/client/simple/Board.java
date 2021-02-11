package com.codenjoy.dojo.bomberman.client.simple;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.bomberman.model.Elements;
import com.codenjoy.dojo.services.Point;

public class Board extends com.codenjoy.dojo.bomberman.client.Board {

    @Override
    public String toString() {
        return boardAsString();
    }
    
    // TODO refactor me
    public boolean isNearMe(Pattern pattern) {
        Point meAtMap = getBomberman();
        Board part = (Board)new Board(){
            @Override
            public Elements valueOf(char ch) {
                try {
                    return super.valueOf(ch);
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
        }.forString(pattern.pattern());

        Point meAtPart = part.getBomberman();
        Point corner = meAtMap.relative(meAtPart);

        for (int dx = 0; dx < part.size; dx++) {
            for (int dy = 0; dy < part.size; dy++) {
                Elements real = this.getAt(corner.getX() + dx, corner.getY() + dy);
                Character mask = part.field(dx, dy).get(0);

                if (mask == ANY_CHAR){
                    continue;
                }

                if (mask == real.ch()) {
                    continue;
                }

                if (pattern.synonyms().match(mask, real.ch())) {
                    continue;
                }

                return false;
            }
        }
        return true;
    }
}
