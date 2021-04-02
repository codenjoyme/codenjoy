
package com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2;

import java.math.BigInteger;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
public class DeepIsland {
    private final int deepMines;
    private final BigInteger deepCombs;

    public BigInteger getDeepCombs() {
        return this.deepCombs;
    }

    public DeepIsland(int deepMines, BigInteger deepCombs) {
        this.deepMines = deepMines;
        this.deepCombs = deepCombs;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.deepMines).append("->").append(this.deepCombs);
        return sb.toString();
    }
}
