package com.codenjoy.dojo.football.model.elements;

import com.codenjoy.dojo.football.model.Actions;
import com.codenjoy.dojo.football.model.Elements;
import com.codenjoy.dojo.football.model.Field;
import com.codenjoy.dojo.football.model.Player;
import com.codenjoy.dojo.services.*;

/**
 * Это реализация героя. Обрати внимание, что он имплементит {@see Joystick}, а значит может быть управляем фреймворком
 * Так же он имплементит {@see Tickable}, что значит - есть возможность его оповещать о каждом тике игры.
 */
public class Hero extends PointImpl implements Joystick, Tickable, State<Elements, Player> {

    private Field field;
    private Ball ball;
    private Direction direction;
	private String team;

    public Hero(Point xy) {
    	super(xy);
        direction = null;
    }

	public void init(Field field) {
        this.field = field;
        this.ball = field.getBall(x, y);
	}

    @Override
    public void down() {

        direction = Direction.DOWN;
        
    }

    @Override
    public void up() {

        direction = Direction.UP;
    }

    @Override
    public void left() {

        direction = Direction.LEFT;
    }

    @Override
    public void right() {

        direction = Direction.RIGHT;
    }

    @Override
    public void act(int... p) {
       
    	int action = 0;
    	int param1 = 0;
    	//int param2 = 0;
    	if(p.length > 0) {
    		action = p[0];
    	}
    	if(p.length > 1) {
    		param1 = p[1];
    	}
    	/*if(p.length > 1) {
    		param2 = p[2];
    	}*/
    	//for(int action : p) {
    		
    		if(action == Actions.HIT_RIGHT.getValue()) {
    			if (ball != null) {
    				//ball.setImpulse(COUNTER);
    				if(param1 == 0) param1 = 1;
    				ball.right(param1);
    			}
    		} else if(action == Actions.HIT_UP.getValue()) {
    			if (ball != null) {
    				//ball.setImpulse(COUNTER);
    				if(param1 == 0) param1 = 1;
    				ball.up(param1);
    			}
    		} else if(action == Actions.HIT_LEFT.getValue()) {
    			if (ball != null) {
    				//ball.setImpulse(COUNTER);
    				if(param1 == 0) param1 = 1;
    				ball.left(param1);
    			}
    		} else if(action == Actions.HIT_DOWN.getValue()) {
    			if (ball != null) {
    				//ball.setImpulse(COUNTER);
    				if(param1 == 0) param1 = 1;
    				ball.down(param1);
    			}
    		} else if(action == Actions.STOP_BALL.getValue()) {
    			if (ball != null) {
    				ball.stop();
    			}
    		}
    		
    	//}
        
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void tick() {

        if (direction != null) {
            int newX = direction.changeX(x);
            int newY = direction.changeY(y);

            if (!field.isBarrier(newX, newY)) {
                move(newX, newY);
            }
            
        }
        direction = null;
    }

    public boolean isWithBall() {
        if (ball == null) {
        	return false;
        } else {
        	return true;
        }
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
    	Hero playersHero = player.getHero();
		if (playersHero == this){
	    	if (isWithBall()) {
	            return Elements.HERO_W_BALL;
	        } else {
	        	return Elements.HERO;
	        }
    	} else if (playersHero.getTeam() == team){
			if (!isWithBall()) {
	            return Elements.TEAM_MEMBER;
	        } else {
	        	return Elements.TEAM_MEMBER_W_BALL;
	        }
    	} else {//if (playersHero.getTeam() != team){
			if (!isWithBall()) {
	            return Elements.ENEMY;
	        } else {
	        	return Elements.ENEMY_W_BALL;
	        }
    	}
    }

	public void setBall(Ball ball) {
		this.ball = ball;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

}
