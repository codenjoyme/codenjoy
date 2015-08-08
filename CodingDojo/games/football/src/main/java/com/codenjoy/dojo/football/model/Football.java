package com.codenjoy.dojo.football.model;

import com.codenjoy.dojo.football.model.elements.Ball;
import com.codenjoy.dojo.football.model.elements.Goal;
import com.codenjoy.dojo.football.model.elements.Hero;
import com.codenjoy.dojo.football.model.elements.Wall;
import com.codenjoy.dojo.football.services.Events;
import com.codenjoy.dojo.services.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * О! Это самое сердце игры - борда, на которой все происходит.
 * Если какой-то из жителей борды вдруг захочет узнать что-то у нее, то лучше ему дать интефейс {@see Field}
 * Борда реализует интерфейс {@see Tickable} чтобы быть уведомленной о каждом тике игры. Обрати внимание на {Sample#tick()}
 */
public class Football implements Tickable, Field {

    private List<Wall> walls;
    private List<Goal> topGoals;
    private List<Goal> bottomGoals;
    private List<Player> players;
    
    private final int size;
    private Dice dice;

    private List<Ball> balls;
	//private int numOfHeroes;

    public Football(Level level, Dice dice) {
        this.dice = dice;
        walls = level.getWalls();
        size = level.getSize();
        players = new LinkedList<Player>();
        balls = level.getBalls();
        topGoals = level.getTopGoals();
        bottomGoals = level.getBottomGoals();
        
        for (Ball ball : balls) {
        	ball.init(this);
        }
    }

    /**
     * @see Tickable#tick()
     */
    @Override
    public void tick() {
    	
    	for (Ball ball : balls) {
        	
        	for (Goal goal : topGoals) {
                if(goal.itsMe(ball.getX(), ball.getY())) {
                	for (Player player : players) {
                        player.event(Events.TOP_GOAL);   
                    }
                	return;
                } 
            }
        	
        	for (Goal goal : bottomGoals) {
                if(goal.itsMe(ball.getX(), ball.getY())) {
                	for (Player player : players) {
                        player.event(Events.BOTTOM_GOAL);   
                    }
                	return;
                } 
            }
        	
        }
    	
        for (Player player : players) {
        	Hero hero = player.getHero();
        	hero.tick();
        	
        	/*for (Hero hero : player.getHeroes()) {
        		hero.tick();
        	}*/
        }

        for (Ball ball : balls) {
        	ball.tick();
        	for (Player player : players) {
                Hero hero = player.getHero();
                if(hero.itsMe(ball.getX(), ball.getY())) {
                	hero.setBall(ball);
                } else {
                	hero.setBall(null);
                } /*
        		for (Hero hero : player.getHeroes()) {
                	if(hero.itsMe(ball.getX(), ball.getY())) {
                		hero.setBall(ball);
                	} else {
                		hero.setBall(null);
                	}
        		}*/
            }
        	
        	for (Goal goal : topGoals) {
                if(goal.itsMe(ball.getX(), ball.getY())) {
                	goal.setBall(ball);
                } 
            }
        	
        	for (Goal goal : bottomGoals) {
                if(goal.itsMe(ball.getX(), ball.getY())) {
                	goal.setBall(ball);
                } 
            }
        	
        }
        
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isBarrier(int x, int y) {
        Point pt = PointImpl.pt(x, y);
        return x > size - 1 || x < 0 || y < 0 || y > size - 1 || walls.contains(pt) || getHeroes().contains(pt);
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
            return PointImpl.pt(0, 0);
        }

        return PointImpl.pt(rndX, rndY);
    }

	@Override
	public Point getFreeRandomOnMyHalf(Player player) {
        int rndX = 0;
        int rndY = 0;
        int c = 0;
        do {
            rndX = dice.next(size);
            rndY = dice.next(size);
        } while (!isFreeAndOnMyHalf(rndX, rndY, player) && c++ < 100);

        if (c >= 100) {
            return PointImpl.pt(0, 0);
        }
        if(rndX == 0 && rndY == 0) {
        	return getFreeRandom();
        }
        return PointImpl.pt(rndX, rndY);
	}
	
    private boolean isFreeAndOnMyHalf(int x, int y, Player player) {
		
    	Point pt = PointImpl.pt(x, y);
    	
    	boolean yOnMyHalf = false;
    	if(player.getMyGoal() == Elements.TOP_GOAL) {
    		yOnMyHalf = y > (size / 2);
    	} else {
    		yOnMyHalf = y < (size / 2);
        }
    	return yOnMyHalf &&
    			!walls.contains(pt) &&
                !getHeroes().contains(pt);
	}

	@Override
    public boolean isFree(int x, int y) {
        Point pt = PointImpl.pt(x, y);

        return !walls.contains(pt) &&
                !getHeroes().contains(pt);
    }



   public List<Hero> getHeroes() {
        List<Hero> result = new ArrayList<Hero>(players.size());
        for (Player player : players) {
            result.add(player.getHero());
        	/*for (Hero hero : player.getHeroes()) {
        		result.add(hero);
        	}*/
        }
        return result;
    }

    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
            if(players.size() % 2 == 1) {
            	player.setTeam("Team 1");
            	player.setMyGoal(Elements.BOTTOM_GOAL);
            } else {
            	player.setTeam("Team 2");
            	player.setMyGoal(Elements.TOP_GOAL);
            }
        }
        player.newHero(this);
        /*player.clearHeroes();
        for (int i=0; i< numOfHeroes; i++) {
        	player.newHero(this);
        }*/
        
        for (Ball ball : balls) {
        	
        	ball.setImpulse(0);
        	for (Goal goal : topGoals) {
                if(goal.itsMe(ball.getX(), ball.getY())) {
                	goal.setBall(null);
                	ball.move(size/2, size/2);
                } 
            }
        	for (Goal goal : bottomGoals) {
                if(goal.itsMe(ball.getX(), ball.getY())) {
                	goal.setBall(null);
                	ball.move(size/2, size/2);
                } 
            }
        	for (Goal goal : bottomGoals) {
                if(goal.itsMe(ball.getX(), ball.getY())) {
                	goal.setBall(null);
                	ball.move(size/2, size/2);
                } 
            }
        	
        }

    }

    public void remove(Player player) {
        players.remove(player);
    }

    public List<Wall> getWalls() {
        return walls;
    }


    public BoardReader reader() {
        return new BoardReader() {
            private int size = Football.this.size;
			
            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                List<Point> result = new LinkedList<Point>();
                result.addAll(Football.this.getWalls());
                result.addAll(Football.this.getHeroes());
                result.addAll(Football.this.topGoals);
                result.addAll(Football.this.bottomGoals);
                result.addAll(Football.this.balls);
                return result;
            }
        };
    }

    @Override
	public boolean isBall(int x, int y) {
		Point pt = PointImpl.pt(x, y);
        return balls.contains(pt);
	}
    
	@Override
	public Ball getBall(int x, int y) {
		
		Point pt = PointImpl.pt(x, y);
        for (Ball ball: balls) {
        	if (ball.itsMe(pt)) {
        		return ball;
        	}
        }
        return null;
	}

	public List<Ball> getBalls() {
		return balls;
	}

	@Override
	public boolean isHero(int x, int y) {
		Point pt = PointImpl.pt(x, y);
        return getHeroes().contains(pt);
	}

	@Override
	public boolean isWall(int x, int y) {
		Point pt = PointImpl.pt(x, y);
        return x > size - 1 || x < 0 || y < 0 || y > size - 1 || getWalls().contains(pt);
	}

	public int getPlayersCount() {
		return players.size();
	}

	/*public void setNumOfHeroes(Integer numOfHeroes) {
		this.numOfHeroes = numOfHeroes;
	}*/

}
