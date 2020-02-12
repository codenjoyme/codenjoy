package com.codenjoy.dojo.bomberman.client.simple;

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
