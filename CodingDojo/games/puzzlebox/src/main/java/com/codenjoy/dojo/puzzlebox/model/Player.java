package com.codenjoy.dojo.puzzlebox.model;

import com.codenjoy.dojo.puzzlebox.services.Events;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;

import java.util.LinkedList;
import java.util.List;

/**
 * Класс игрока. Тут кроме героя может подсчитываться очки. Тут же ивенты передабтся лиснеру фреймворка.
 */
public class Player implements Joystick {

    private EventListener listener;
    private int maxScore;
    public int score;
    private int currentIndex;

    private int deltaClicks;

    List<Box> boxes;

    public Player(EventListener listener) {
        this.listener = listener;
        clearScore();
        currentIndex = 0;
        boxes = new LinkedList<Box>();
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
            case WIN: win(); break;
            case FILL: increaseScore(); break;
        }

        if (listener != null) {
            listener.event(event);
        }
    }

    private void gameOver() {
        score = 0;
    }

    public void increaseScore() {
        this.score += 21;
        this.score -= deltaClicks;
        deltaClicks = 0;
        maxScore = Math.max(maxScore, this.score);
    }

    private void win() {
        return;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void clearScore() {
        score = 0;
        maxScore = 0;
    }

    public void changeCurrentBox() {
        currentIndex ++;

        if(currentIndex >= boxes.size()) {
            currentIndex = 0;
        }

        while (boxes.get(currentIndex).getDone()){
            currentIndex ++;

            if(currentIndex >= boxes.size()) {
                currentIndex = 0;
            }

        }
    }

    public boolean isWin() {
        for (Box box : boxes) {
            if (!box.getDone()) {
                return false;
            }
        }
        return true;
    }


    public Box getBox() {
        if(boxes.size() == 0){
            event(Events.WIN);
            return null;
        }
        Box box = boxes.get(currentIndex);
        return box;
    }

    public List<Box> getBoxes() {return boxes;}

    @Override
    public void down() {
        Box box = getBox();
        box.down();
        if(box.isMoving) return;
        deltaClicks++;
    }

    @Override
    public void up() {
        Box box = getBox();
        box.up();
        if(box.isMoving) return;
        deltaClicks++;
    }

    @Override
    public void left() {
        Box box = getBox();
        box.left();
        if(box.isMoving) return;
        deltaClicks++;
    }

    @Override
    public void right() {
        Box box = getBox();
        box.right();
        if(box.isMoving) return;
        deltaClicks++;
    }

    @Override
    public void act(int... p) {
        Box curBox = getBox();
        curBox.isCurrent = false;
        changeCurrentBox();
        curBox=getBox();
        if(curBox.isMoving) return;
        curBox.isCurrent = true;
        deltaClicks++;
    }

    public void setBoxes(List<Box> boxes) {

        this.boxes = boxes;
    }

    public void initBoxes(Field field) {
        for (Box box: boxes){
            box.init(field);
        }
    }

    public Joystick getJoystick() {
        return this;
    }
}