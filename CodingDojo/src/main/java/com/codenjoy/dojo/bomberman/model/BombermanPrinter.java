package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;

import java.util.List;

import static com.codenjoy.dojo.bomberman.model.Elements.*;
import static com.codenjoy.dojo.services.PointImpl.pt;

public class BombermanPrinter implements GamePrinter {

    private IBoard board;
    private Player player;

    public BombermanPrinter(IBoard board, Player player) {
        this.board = board;
        this.player = player;
    }

    @Override
    public Enum get(int x, int y) {
        Point pt = pt(x, y);

        Bomberman bomberman = player.getBomberman();
        List<Bomberman> bombermans = board.getBombermans();
        List<Bomb> bombs = board.getBombs();
        Walls walls = board.getWalls();

        List<Point> blasts = board.getBlasts();
        if (blasts.contains(pt)) {
            Point blast = blasts.get(blasts.indexOf(pt));

            if (bomberman.itsMe(blast)) {
                return DEAD_BOMBERMAN;
            }

            if (isOtherBomberman(bombermans, bomberman, blast)) {
                return OTHER_DEAD_BOMBERMAN;
            }

            if (isMeatChopper(walls, blast)) {
                return DEAD_MEAT_CHOPPER;
            }

            if (isDestroyWall(walls, blast)) {
                return DESTROYED_WALL;
            }

            if (!bombs.contains(pt)) {
                return BOOM;
            }
        }

        Wall wall = walls.get(pt.getX(), pt.getY());
        if (wall.itsMe(pt)) {
            if (wall instanceof DestroyWall) {
                return DESTROY_WALL;
            }

            if (wall instanceof MeatChopper) {
                if (bomberman.itsMe(wall)) {
                    return DEAD_BOMBERMAN;
                }

                if (isOtherBomberman(bombermans, bomberman, wall)) {
                    return OTHER_DEAD_BOMBERMAN;
                }

                return MEAT_CHOPPER;
            }

            return WALL;
        }

        if (bombs.contains(pt)) {
            Bomb bomb = bombs.get(bombs.indexOf(pt));
            if (bomberman.itsMe(bomb)) {
                return BOMB_BOMBERMAN;
            }

            if (isOtherBomberman(bombermans, bomberman, bomb)) {
                return OTHER_BOMB_BOMBERMAN;
            }

            return bomb.state();
        }

        if (bomberman.itsMe(x, y)) {
            if (bomberman.isAlive()) {
                return BOMBERMAN;
            } else {
                return DEAD_BOMBERMAN;
            }
        }

        if (bombermans.contains(pt)) {
            Bomberman other = bombermans.get(bombermans.indexOf(pt));

            if (other.isAlive()) {
                return OTHER_BOMBERMAN;
            } else {
                return OTHER_DEAD_BOMBERMAN;
            }
        }


        return EMPTY;
    }

    private boolean isDestroyWall(Walls walls, Point blast) {
        if (walls.itsMe(blast.getX(), blast.getY())) {
            Wall wall = walls.get(blast.getX(), blast.getY());
            if (wall instanceof DestroyWall) {
                return true;
            }
        }
        return false;
    }

    private boolean isMeatChopper(Walls walls, Point blast) {
        if (walls.itsMe(blast.getX(), blast.getY())) {
            Wall wall = walls.get(blast.getX(), blast.getY());
            if (wall instanceof MeatChopper) {
                return true;
            }
        }
        return false;
    }

    private boolean isOtherBomberman(List<Bomberman> bombermans, Bomberman bomberman, Object element) {
        if (bombermans.contains(element)) {
            Bomberman other = bombermans.get(bombermans.indexOf(element));
            if (other != bomberman) {
                return true;
            }
        }
        return false;
    }
}
