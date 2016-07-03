package com.codenjoy.dojo.football.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.football.model.elements.Hero;
import com.codenjoy.dojo.football.services.Events;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;

/**
 * Класс игрока. Тут кроме героя может подсчитываться очки. Тут же ивенты передабтся лиснеру фреймворка.
 */
public class Player {

    private EventListener listener;
    private int maxScore;
    private int score;
    private boolean goalHited;
    Hero hero;
    //List<Hero> allHeroes;
	private Elements myGoal;
	private String team;

    /**
     * @param listener Это шпийон от фреймоврка. Ты должен все ивенты которые касаются конкретного пользователя сормить ему.
     * @param myGoal 
     */
    public Player(EventListener listener) {
        this.listener = listener;
        clearScore();
        //allHeroes = new ArrayList<Hero>();
    }

    private void increaseScore() {
        score = score + 1;
        maxScore = Math.max(maxScore, score);
        
        if (listener != null) {
            listener.event(Events.WIN);
        }
    }
    
    /*private void decreaseScore() {
        score = score - 1;
        
        if (listener != null) {
            //listener.event(Events.LOOSE);
        }
    }*/

    public int getMaxScore() {
        return maxScore;
    }

    public int getScore() {
        return score;
    }

    /**
     * Борда может файрить ивенты юзера с помощью этого метода
     * @param event тип ивента
     */
    public void event(Events event) {
        switch (event) {
            case TOP_GOAL: 
            	goalHited = true;
            	if (myGoal != Elements.TOP_GOAL) {
            		increaseScore();
            	}
            	break;
            case BOTTOM_GOAL: 
            	goalHited = true;
            	if (myGoal != Elements.BOTTOM_GOAL) {
            		increaseScore();
            	}
            	break;
            case WIN:
            	break;
		default:
			break;
        }
        
        if (listener != null) {
            listener.event(event);
        }
    }

    public void clearScore() {
        score = 0;
        maxScore = 0;
    }

    public Hero getHero() {
        return hero;
    }

    /**
     * Когда создается новая игра для пользователя, кто-то должен создать героя
     * @param field борда
     */
    public void newHero(Field field) {
        Point pt = field.getFreeRandomOnMyHalf(this);
        hero = new Hero(pt);
        hero.setTeam(team);
        hero.init(field);
        //allHeroes.add(hero);
    }

	public boolean isGoalHited() {
		return goalHited;
	}

	public void setGoalHited(boolean goalHited) {
		this.goalHited = goalHited;
	}

	public Elements getMyGoal() {
		return myGoal;
	}

	public void setMyGoal(Elements myGoal) {
		this.myGoal = myGoal;
	}

	public void setTeam(String team) {
		this.team = team;	
	}

	public String getTeam() {
		return team;
	}

	/*public List<Hero> getHeroes() {
		return allHeroes;
	}

	public void clearHeroes() {
		allHeroes.clear();
	}*/

}