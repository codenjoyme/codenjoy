package com.codenjoy.dojo.battlecity.model;

public class Construction extends Point {

    private Elements ch;
    private OnDestroy onDestroy;

    public Construction(int x, int y) {
        super(x, y);
        ch = Elements.CONSTRUCTION;
    }

    public void destroyFrom(Direction direction) {
        if (direction.equals(Direction.UP)) {
            switch (ch) {
                case CONSTRUCTION : ch = Elements.CONSTRUCTION_DESTROYED_DOWN; break;
                case CONSTRUCTION_DESTROYED_DOWN : ch = Elements.CONSTRUCTION_DESTROYED_DOWN_TWICE; break;
                case CONSTRUCTION_DESTROYED_DOWN_TWICE : onDestroy(); break;
            }
        } else if (direction.equals(Direction.RIGHT)) {
            switch (ch) {
                case CONSTRUCTION : ch = Elements.CONSTRUCTION_DESTROYED_LEFT; break;
                case CONSTRUCTION_DESTROYED_LEFT : ch = Elements.CONSTRUCTION_DESTROYED_LEFT_TWICE; break;
                case CONSTRUCTION_DESTROYED_LEFT_TWICE : onDestroy(); break;
            }
        }
    }

    private void onDestroy() {
        if (onDestroy != null) {
            onDestroy.destroy();
        }
    }

    public Elements getChar() {
        return ch;
    }

    public void setOnDestroy(OnDestroy onDestroy) {
        this.onDestroy = onDestroy;
    }
}
