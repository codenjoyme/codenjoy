package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.settings.Parameter;

import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 10/10/12
 * Time: 8:29 PM
 */
public class OriginalWalls extends WallsImpl implements Iterable<Wall> {
    private int currentSize;
    private Parameter<Integer> size;

    public OriginalWalls(Parameter<Integer> size) {
        super();
        currentSize = size.getValue();
        this.size = size;

        regenerate();
    }

    private void regenerate() {
        for (int x = 0; x < size.getValue(); x++) {
            add(x, 0);
            add(x, size.getValue() - 1);
        }

        final int D = 1;
        for (int y = D; y < size.getValue() - D; y++) {
            add(0, y);
            add(size.getValue() - 1, y);
        }

        for (int x = 2; x <= size.getValue() - 2; x++) {
            for (int y = 2; y <= size.getValue() - 2; y++) {
                if (y % 2 != 0 || x % 2 != 0) {
                    continue;
                }
                add(x, y);
            }
        }
    }

    @Override
    public void tick() {
        super.tick(); // TODO протестить эту строчку

        if (currentSize != size.getValue()) {
            currentSize = size.getValue();
            List<Wall> walls = subList(Wall.class);
            for (Wall wall : walls.toArray(new Wall[0])) {
                destroy(wall.getX(), wall.getY());
            }

            regenerate();
        }
    }

}
