package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.settings.Parameter;

import java.util.List;

/**
 * User: sanja
 * Date: 21.04.13
 * Time: 0:17
 */
public class EatSpaceWalls extends WallsDecorator implements Walls { // TODO протестить класс

    private IBoard board;
    private Parameter<Integer> count;
    private Dice dice;

    public EatSpaceWalls(Walls walls, IBoard board, Parameter<Integer> count, Dice dice) {
        super(walls);
        this.board = board;
        this.count = count;
        this.dice = dice;
    }

    private int freeSpaces() {
        return  (board.size()*board.size() - 1) // TODO -1 это один бомбер, а если их несколько?
                - walls.subList(Wall.class).size();
    }

    @Override
    public void tick() {
        super.tick();    // TODO протестить эту строчку

        regenerate();
    }

    private void regenerate() {
        if (count.getValue() < 0) {
            count.update(0);
        }

        List<DestroyWall> destroyWalls = walls.subList(DestroyWall.class);
        int needToCreate = this.count.getValue() - destroyWalls.size();
        if (needToCreate > freeSpaces()) {  // TODO и это потестить
            count.update(count.getValue() - (needToCreate - freeSpaces()) - 50); // 50 это место под бомберов
        }

        int count = destroyWalls.size();
        if (count > this.count.getValue()) { // TODO и удаление лишних
            for (int i = 0; i < (count - this.count.getValue()); i++) {
                DestroyWall meatChopper = destroyWalls.remove(0);
                walls.destroy(meatChopper.getX(), meatChopper.getY());
            }
            return;
        }

        int c = 0;
        int maxc = 10000;
        while (count < this.count.getValue() && c < maxc) {  // TODO и это
            int x = dice.next(board.size());
            int y = dice.next(board.size());

            if (!board.isBarrier(x, y)) {
                walls.add(new DestroyWall(x, y));
                count++;
            }

            c++;
        }

        if (c == maxc) {
            throw new  RuntimeException("Dead loop at MeatChoppers.generate!");
        }
    }
}
