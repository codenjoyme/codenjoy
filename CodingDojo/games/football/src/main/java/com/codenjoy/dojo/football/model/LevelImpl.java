package com.codenjoy.dojo.football.model;

import com.codenjoy.dojo.football.model.elements.Ball;
import com.codenjoy.dojo.football.model.elements.Goal;
import com.codenjoy.dojo.football.model.elements.Hero;
import com.codenjoy.dojo.football.model.elements.Wall;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;

import java.util.LinkedList;
import java.util.List;

/**
 * Полезный утилитный класс для получения объектов на поле из текстового вида.
 */
public class LevelImpl implements Level {
    private final LengthToXY xy;

    private String map;

    public LevelImpl(String map) {
        this.map = map;
        xy = new LengthToXY(getSize());
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Hero> getHero() {
        List<Hero> result = new LinkedList<Hero>();

        for (Point pt : getPointsOf(Elements.HERO)) {
            result.add(new Hero(pt));
        }
        
        for (Point pt : getPointsOf(Elements.HERO_W_BALL)) {
            result.add(new Hero(pt));
        }
        
        for (Point pt : getPointsOf(Elements.TEAM_MEMBER)) {
            result.add(new Hero(pt));
        }
        
        for (Point pt : getPointsOf(Elements.TEAM_MEMBER_W_BALL)) {
            result.add(new Hero(pt));
        }
        
        for (Point pt : getPointsOf(Elements.ENEMY)) {
            result.add(new Hero(pt));
        }
        
        for (Point pt : getPointsOf(Elements.ENEMY_W_BALL)) {
            result.add(new Hero(pt));
        }

        return result;
    }

    @Override
    public List<Wall> getWalls() {
        List<Wall> result = new LinkedList<Wall>();

        for (Point pt : getPointsOf(Elements.WALL)) {
            result.add(new Wall(pt));
        }

        return result;
    }

    private List<Point> getPointsOf(Elements element) {
        List<Point> result = new LinkedList<Point>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == element.ch) {
                result.add(xy.getXY(index));
            }
        }
        return result;
    }
    
    private List<Point> getPointsOf(Elements... elements) {
        List<Point> result = new LinkedList<Point>();
        for (int index = 0; index < map.length(); index++) {
            for (Elements element : elements) {
            	if (map.charAt(index) == element.ch) {
                    result.add(xy.getXY(index));
                    break;
                }
            }
        }
        return result;
    }

	@Override
	public List<Ball> getBalls() {
		List<Ball> result = new LinkedList<Ball>();

        for (Point pt : getPointsOf(Elements.BALL, 
        							Elements.STOPPED_BALL, 
									Elements.HERO_W_BALL, 
        							Elements.TEAM_MEMBER_W_BALL,
        							Elements.ENEMY_W_BALL, 
        							Elements.HITED_GOAL,
        							Elements.HITED_MY_GOAL
        							)) {
            result.add(new Ball(pt));
        }
        
        return result;
	}

	@Override
	public List<Goal> getTopGoals() {
		List<Goal> result = new LinkedList<Goal>();

        for (Point pt : getPointsOf(Elements.TOP_GOAL)) {
            result.add(new Goal(pt, Elements.TOP_GOAL));
        }
        
        //for (Point pt : getPointsOf(Elements.HITED_GOAL)) {
        //    result.add(new Goal(pt, Elements.HITED_GOAL));
        //}

        return result;
	}

	@Override
	public List<Goal> getBottomGoals() {
		List<Goal> result = new LinkedList<Goal>();

        for (Point pt : getPointsOf(Elements.BOTTOM_GOAL)) {
            result.add(new Goal(pt, Elements.BOTTOM_GOAL));
        }
        
        //for (Point pt : getPointsOf(Elements.HITED_GOAL)) {
        //    result.add(new Goal(pt, Elements.HITED_GOAL));
        //}

        return result;
	}
}
