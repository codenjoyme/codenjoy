package com.codenjoy.dojo.football.model.elements;

import com.codenjoy.dojo.football.model.Elements;
import com.codenjoy.dojo.football.model.Player;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

/**
 * Артефакт Ворота на поле
 */
public class Goal extends PointImpl implements State<Elements, Player> {

	private Elements type;
	private Ball ball;
	
    public Goal(int x, int y, Elements type) {
    	super(x, y);
    	this.type = type;
    }

    public Goal(Point point, Elements type) {
        this(point.getX(), point.getY(), type);
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
        
    	if (player.getMyGoal() == type) {
        	if (isWithBall()) {
                return Elements.HITED_MY_GOAL;
        	} else {
        	    return Elements.MY_GOAL;
            }
        } else if (player.getMyGoal() != type) {
        	if (isWithBall()) {
                return Elements.HITED_GOAL;
        	} else {
        	    return Elements.ENEMY_GOAL;
            }
        } else {
        	if (isWithBall()) {
        		return Elements.HITED_GOAL;
        	} else {
                return type;
        	}
        }
    }

	public void setBall(Ball ball) {
		this.ball = ball;
	}
    
}
