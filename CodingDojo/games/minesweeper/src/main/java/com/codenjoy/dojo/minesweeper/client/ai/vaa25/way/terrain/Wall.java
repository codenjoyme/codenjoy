package com.codenjoy.dojo.minesweeper.client.ai.vaa25.way.terrain;

/**
 * @author Alexander Vlasov
 */
public class Wall extends Terrain {
    public Wall() {
        setWalkable(false);
        setTransparent(false);
        setPunched(false);
    }
}
