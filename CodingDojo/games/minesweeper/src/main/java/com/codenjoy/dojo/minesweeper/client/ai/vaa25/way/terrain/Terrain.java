package com.codenjoy.dojo.minesweeper.client.ai.vaa25.way.terrain;

/**
 * @author Alexander Vlasov
 */
public class Terrain {
    private boolean transparent, walkable, punched;

    public boolean isTransparent() {
        return transparent;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public boolean isPunched() {
        return punched;
    }

    public void setPunched(boolean punched) {
        this.punched = punched;
    }
}
