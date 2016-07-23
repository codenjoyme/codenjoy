package com.codenjoy.dojo.tetris.model;

import com.codenjoy.dojo.services.CharElements;

/**
 * User: serhiy.zelenin
 * Date: 5/9/12
 * Time: 6:56 PM
 */
public enum PlotColor implements CharElements  {
    BLUE, CYAN, ORANGE, YELLOW, GREEN, PURPLE, RED;
    public char ch(){
        return '#';
    }

    public String getName() {
        return this.name().toLowerCase();
    }
}
