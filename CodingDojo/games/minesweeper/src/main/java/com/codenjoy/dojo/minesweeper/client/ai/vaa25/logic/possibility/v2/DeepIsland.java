package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.possibility.v2;

import java.math.BigInteger;

/**
 * @author Alexander Vlasov
 */
public class DeepIsland {
    private final int deepMines;

    private final BigInteger deepCombs;

    public BigInteger getDeepCombs() {
        return deepCombs;
    }


    public DeepIsland(int deepMines, BigInteger deepCombs) {

        this.deepMines = deepMines;
        this.deepCombs = deepCombs;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(deepMines).append("->").append(deepCombs);
        return sb.toString();
    }
}
