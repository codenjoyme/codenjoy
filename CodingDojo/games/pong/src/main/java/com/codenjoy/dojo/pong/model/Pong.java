package com.codenjoy.dojo.pong.model;

import com.codenjoy.dojo.pong.services.Events;
import com.codenjoy.dojo.services.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Pong implements Tickable, Field {

    private List<Player> players = new LinkedList<>();
    private int maxPlayers = 2;
    private Point leftStartingPosition;
    private Point rightStartingPosition;
    private final int size;
    private Dice dice;
    private Ball ball;
    private List<Panel> panels;
    private List<Wall> walls;

    public Pong(Level level, Dice dice) {
        this.dice = dice;
        size = level.getSize();
        leftStartingPosition = new PointImpl(1, size()/2);
        rightStartingPosition = new PointImpl(size - 2, size/2);
        ball = level.getBall();
        ball.init(this);
        walls = level.getWalls();
        panels = level.getPanels();

    }

    private List<Panel> getHeroesPanels() {
        List<Panel> result = new LinkedList<>();
        List<Hero> heroes = getHeroes();
        for (Hero hero : heroes) {
            result.addAll(hero.getPanel());
        }
        return result;
    }

    /**
     * @see Tickable#tick()
     */
    @Override
    public void tick() {
        ball.tick();
        List<Hero> heroes = getHeroes();
        for (Hero hero : heroes) {
            hero.tick();
        }
        for (Player player : players) {
            if (playerPassedBall(player)) {
                List<Player> oppositePlayers = getOppositePlayers(player);
                if (!oppositePlayers.isEmpty()) {
                    for (Player oppositePlayer: oppositePlayers) {
                        oppositePlayer.event(Events.WIN);
                    }
                }
                setBallToCentreWithRandomDirection();
                break;
            } else if (ballIsOutOfBounds()) {
                setBallToCentreWithRandomDirection();
                break;
            }
        }
    }

    private List<Player> getOppositePlayers(Player player) {
        List<Player> oppositePlayers = new LinkedList<>(players);
        oppositePlayers.remove(player);
        return oppositePlayers;
    }

    private boolean ballIsOutOfBounds() {
        return ball.getX() < leftStartingPosition.getX() || ball.getX() > rightStartingPosition.getX();
    }

    private boolean playerPassedBall(Player player) {
        return player.getHero().getX() == leftStartingPosition.getX() && ball.getX() < leftStartingPosition.getX() ||
                player.getHero().getX() == rightStartingPosition.getX() && ball.getX() > rightStartingPosition.getX();
    }

    public int size() {
        return size;
    }

    public void newGame(Player player) {
        if (!players.contains(player) && players.size() < maxPlayers) {
            players.add(player);
            Point heroPosition = getNewHeroPosition();
            player.newHero(this, heroPosition);
        }

        panels = getHeroesPanels();
        setBallToCentreWithRandomDirection();

    }

    private void setBallToCentreWithRandomDirection() {
        ball = new Ball(getBoardCenter());
        ball.init(this);
        ball.setDirection(getRandomBallDirection());
    }

    private Point getNewHeroPosition() {

        if (players.size() > 0 && players.get(0).getHero() != null) {
            if (players.get(0).getHero().equals(rightStartingPosition)) {
                return leftStartingPosition;
            } else {
                return rightStartingPosition;
            }
        }
        return rightStartingPosition;
    }

    private PointImpl getBoardCenter() {
        return new PointImpl(size/2, size/2);
    }

    private BallDirection getRandomBallDirection() {
        return new BallDirection(Direction.valueOf(dice.next(2)), Direction.valueOf(2 + dice.next(1)));
    }

    public void remove(Player player) {
        players.remove(player);
    }

    public BoardReader reader() {
        return new BoardReader() {
            private int size = Pong.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                List<Point> result = new LinkedList<Point>();
                result.addAll(getHeroesPanels());
                result.add(ball);
                result.addAll(walls);
                return result;
            }
        };
    }

    public void setBallDirection(BallDirection direction) {
        ball.setDirection(direction);
    }


    public boolean isGameOver() {
        boolean gameOver = false;
        for(Player player : players) {
            if (player.getScore() == 10) {
                gameOver = true;
            }
         }
        return false;
    }

    public List<Hero> getHeroes() {
        List<Hero> heroes = new LinkedList<Hero>();
        for(Player player : players) {
            heroes.add(player.getHero());
        }
        return heroes;
    }

    @Override
    public boolean isBarrier(int x, int y) {
        Point point = new PointImpl(x, y);
        return walls.contains(point) || panels.contains(point);
    }

    @Override
    public Barrier getBarrier(int x, int y) {
        Point point = new PointImpl(x, y);
        List<Barrier> barriers = new ArrayList<>(walls.size()+panels.size());
        barriers.addAll(walls);
        barriers.addAll(panels);
        if (barriers.indexOf(point) != -1) {
            return barriers.get(barriers.indexOf(point));
        } else {
            throw new IllegalArgumentException("here is no barriers on this point :(");
        }
    }

}
















