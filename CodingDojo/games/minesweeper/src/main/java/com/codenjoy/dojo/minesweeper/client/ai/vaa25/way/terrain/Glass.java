package com.codenjoy.dojo.minesweeper.client.ai.vaa25.way.terrain;

/**
 * @author Alexander Vlasov
 */
public class Glass extends Terrain {
    public Glass() {
        setWalkable(false);
        setTransparent(true);
        setPunched(true);
    }
}
