package com.codenjoy.dojo.tetris.model;

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


import com.codenjoy.dojo.services.CharElements;

/**
 * User: serhiy.zelenin
 * Date: 5/9/12
 * Time: 6:56 PM
 */
public enum PlotColor implements CharElements  {
    BLUE {
        @Override
        public char ch() {
            return 'I';
        }
    }, CYAN {
        @Override
        public char ch() {
            return 'J';
        }
    }, ORANGE {
        @Override
        public char ch() {
            return 'L';
        }
    }, YELLOW {
        @Override
        public char ch() {
            return 'O';
        }
    }, GREEN {
        @Override
        public char ch() {
            return 'S';
        }
    }, PURPLE {
        @Override
        public char ch() {
            return 'T';
        }
    }, RED {
        @Override
        public char ch() {
            return 'Z';
        }
    }, NONE{
        @Override
        public char ch() {
            return ' ';
        }
    };
//    public char ch(){
//        return '#';
//    }

    public String getName() {
        return this.name().toLowerCase();
    }


    @Override
    public String toString() {
        return String.valueOf(ch());
    }
}
