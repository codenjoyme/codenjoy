package com.codenjoy.dojo.bomberman.model;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 8:17 PM
 */
public class DestroyWalls extends WallsDecorator implements Walls {

    public DestroyWalls(Walls walls) {
        super(new WallsImpl());
        for (Wall wall : walls) {
            this.walls.add(new DestroyWall(wall.getX(), wall.getY()));
        }
    }

}
