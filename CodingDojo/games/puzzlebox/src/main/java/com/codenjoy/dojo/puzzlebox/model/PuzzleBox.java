package com.codenjoy.dojo.puzzlebox.model;

import com.codenjoy.dojo.puzzlebox.services.Events;
import com.codenjoy.dojo.services.*;

import java.util.LinkedList;
import java.util.List;

/**
 * О! Это самое сердце игры - борда, на которой все происходит.
 * Если какой-то из жителей борды вдруг захочет узнать что-то у нее, то лучше ему дать интефейс {@see Field}
 * Борда реализует интерфейс {@see Tickable} чтобы быть уведомленной о каждом тике игры. Обрати внимание на {PuzzleBox#tick()}
 */
public class PuzzleBox implements Tickable, Field {

    private final Level level;
    private List<Player> players;

    public final int size;
    private Dice dice;
    private List<Wall> walls;
    private List<Target> targets;

    private int clicks;

    public PuzzleBox(Level level, Dice dice) {
        this.dice = dice;
        walls = level.getWalls();
        targets = level.getTargets();
        this.level = level;
        size = level.getSize();
        players = new LinkedList<Player>();
    }

    /**
     * @see Tickable#tick()
     */
    @Override
    public void tick() {
        for (Player player : players) {
            if(player.isWin()){
                return;
            }
            for(Box box: player.getBoxes()) {
                box.tick();
            }
        }

    }

    @Override
    public boolean isBarrier(int x, int y) {
        Point pt = PointImpl.pt(x, y);
        return x > size - 1 || x < 0 || y < 0 || y > size - 1 || walls.contains(pt) || getBoxes().contains(pt);
    }

    @Override
    public boolean isTarget(int x, int y) {
        Point pt = PointImpl.pt(x, y);
        boolean isTarget = targets.contains(pt);
        if(isTarget) {
            players.get(0).event(Events.FILL);
        }
        return isTarget;
    }

    public int size() {
        return size;
    }

    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.setBoxes(level.getBoxes());
        player.initBoxes(this);
    }

    public void fillEvent() {
        players.get(0).increaseScore();
    }

    public void remove(Player player) {
        players.remove(player);
    }

    public BoardReader reader() {
        return new BoardReader() {
            private int size = PuzzleBox.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                List<Point> result = new LinkedList<Point>();
                result.addAll(getBoxes());
                result.addAll(walls);
                result.addAll(targets);
                return result;
            }
        };
    }

    public List<Box> getBoxes() {
        List<Box> boxes = new LinkedList<Box>();
        for (Player player : players) {
            boxes.addAll(player.getBoxes());
        }
        return boxes;
    }
}
















