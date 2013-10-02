package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.settings.Parameter;

import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 8:17 PM
 */
public class MeatChoppers extends WallsDecorator implements Walls {

    private Parameter<Integer> count;
    private IBoard board;
    private Dice dice;

    public MeatChoppers(Walls walls, IBoard board, Parameter<Integer> count, Dice dice) {
        super(walls);
        this.board = board;
        this.dice = dice;
        this.count = count;
    }

    private int freeSpaces() {
        return  (board.size()*board.size() - 1) // TODO -1 это один бомбер, а если их несколько?
                - walls.subList(Wall.class).size();
    }

    public void regenerate() {                 // TODO потестить динамический рефреш монстров по тику    // TODO тут жестокое дублирование с EatSpaceWalls
        if (count.getValue() < 0) {  // TODO потестить
            count.update(0);
        }

        List<MeatChopper> meatChoppers = walls.subList(MeatChopper.class);
        int needToCreate = this.count.getValue() - meatChoppers.size();
        if (needToCreate > freeSpaces()) {  // TODO и это потестить
            count.update(count.getValue() - (needToCreate - freeSpaces()) - 50); // 50 это место под бомберов
        }

        int count = meatChoppers.size();
        if (count > this.count.getValue()) { // TODO и удаление лишних
            for (int i = 0; i < (count - this.count.getValue()); i++) {
                MeatChopper meatChopper = meatChoppers.remove(0);
                walls.destroy(meatChopper.getX(), meatChopper.getY());
            }
            return;
        }

        int c = 0; int maxc = 10000;
        while (count < this.count.getValue() && c < maxc) {  // TODO и это
            int x = dice.next(board.size());
            int y = dice.next(board.size());

            if (!board.isBarrier(x, y)) {
                walls.add(new MeatChopper(x, y));
                count++;
            }

            c++;
        }

        if (c == maxc) {
            throw new  RuntimeException("Dead loop at MeatChoppers.regenerate!");
        }
    }

    @Override
    public void tick() {
        super.tick(); // TODO протестить эту строчку + сделать через Template Method
        regenerate();

        List<MeatChopper> meatChoppers = walls.subList(MeatChopper.class);
        for (MeatChopper meatChopper : meatChoppers) {
            Direction direction = meatChopper.getDirection();
            if (direction != null && dice.next(5) > 0) {
                int x = direction.changeX(meatChopper.getX());
                int y = direction.changeY(meatChopper.getY());
                if (!walls.itsMe(x, y)) {
                    meatChopper.move(x, y);
                    continue;
                } else {
                    // do nothing
                }
            }
            meatChopper.setDirection(tryToMove(meatChopper));
        }
    }

    private Direction tryToMove(PointImpl pt) {
        int count = 0;
        int x = pt.getX();
        int y = pt.getY();
        Direction direction = null;
        do {
            int n = 4;
            int move = dice.next(n);
            direction = Direction.valueOf(move);

            x = direction.changeX(pt.getX());
            y = direction.changeY(pt.getY());

        } while ((walls.itsMe(x, y) || isOutOfBorder(x, y)) && count++ < 10);

        if (count < 10) {
            pt.move(x, y);
            return direction;
        }
        return null;
    }

    private boolean isOutOfBorder(int x, int y) {
        return x >= board.size() || y >= board.size() || x < 0 || y < 0;
    }

}
