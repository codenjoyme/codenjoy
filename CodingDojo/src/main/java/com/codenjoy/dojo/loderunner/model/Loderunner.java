package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.*;

import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:56
 */
public class Loderunner implements Tickable, Game, Field {

    private final List<Point> borders;
    private final List<Brick> bricks;
    private List<Point> gold;
    private List<Point> ladder;
    private final Hero hero;
    private final int size;
    private final Printer printer;
    private Level level;
    private Dice dice;

    public Loderunner(Level level, Dice dice) {
        this.level = level;
        this.dice = dice;
        borders = level.getBorders();
        bricks = level.getBricks();
        gold = level.getGold();
        ladder = level.getLadder();
        hero = level.getHero().iterator().next();
        hero.init(this);
        size = level.getSize();
        printer = new Printer(this);
    }

    @Override
    public void tick() {
        hero.tick();
        if (gold.contains(hero)) {
            gold.remove(hero);

            Point pos = getFreeRandom();
            gold.add(pt(pos.getX(), pos.getY()));
        }
        for (Brick brick : bricks) {
            brick.tick();
        }
        hero.checkAlive();
    }

    public int getSize() {
        return size;
    }

    @Override
    public Joystick getJoystick() {
        return hero; 
    }

    @Override
    public int getMaxScore() {
        return 0;  
    }

    @Override
    public int getCurrentScore() {
        return 0;  
    }

    @Override
    public boolean isGameOver() {
        return false;  
    }

    @Override
    public void newGame() {
        
    }

    @Override
    public String getBoardAsString() {
        return printer.toString();
    }

    @Override
    public void destroy() {
        
    }

    @Override
    public void clearScore() {
        
    }

    public List<Point> getBorders() {
        return borders;
    }

    public Hero getHero() {
        return hero;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    @Override
    public boolean isBarrier(int x, int y) {
        Point pt = pt(x, y);
        return x >= size - 1 || x <= 0 || y <= 0 || y >= size - 1 || isFullBrick(pt);   // TODO тут еще могут быть кроме кирпичей стены неразрушаемые
    }

    @Override
    public boolean tryToDrill(int x, int y) {
        Point pt = pt(x, y);
        if (!isFullBrick(pt)) {
            return false;
        }

        if (ladder.contains(pt(x, y + 1))) {
            return false;
        }

        Brick brick = getBrick(pt);
        brick.drill();

        return true;
    }

    @Override
    public boolean isPit(int x, int y) {
        Point pt = pt(x, y - 1);

        if (!isFullBrick(pt) && !ladder.contains(pt) && !borders.contains(pt)) {
            return true;
        }

        // TODO проверить что под низом не Wall и на верху нет трубы

        return false;
    }

    private boolean isFullBrick(Point pt) {
        Brick brick = getBrick(pt);
        return brick != null && brick.state() == Elements.BRICK;
    }

    @Override
    public Point getFreeRandom() {
        int rndX = 0;
        int rndY = 0;
        int c = 0;
        do {
            rndX = dice.next(size);
            rndY = dice.next(size);
        } while (!isFree(rndX, rndY) && c++ < 100);

        if (c >= 100) {
            return pt(0, 0);  // этого никогда не должно случиться, но никогда не говори никогда. чтобы заметить поставил координаты 0, 0
        }

        return pt(rndX, rndY);
    }

    @Override
    public boolean isLadder(int x, int y) {
        return ladder.contains(pt(x, y));
    }

    private boolean isFree(int x, int y) {
        Point pt = pt(x, y);

        return !gold.contains(pt) && !borders.contains(pt) && !bricks.contains(pt) && !hero.itsMe(pt); // TODO потестить кажддое условие
    }

    private Brick getBrick(Point pt) {
        int index = bricks.indexOf(pt);
        if (index == -1) return null;
        return bricks.get(index);
    }

    public List<Point> getGold() {
        return gold;
    }

    public List<Point> getLadder() {
        return ladder;
    }
}
