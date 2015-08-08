package com.codenjoy.dojo.football.model;


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