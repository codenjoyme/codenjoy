package com.codenjoy.dojo.tetris.model;

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
    };
//    public char ch(){
//        return '#';
//    }

    public String getName() {
        return this.name().toLowerCase();
    }
}
