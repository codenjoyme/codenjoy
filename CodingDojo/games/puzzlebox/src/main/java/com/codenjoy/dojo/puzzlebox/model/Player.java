package com.codenjoy.dojo.puzzlebox.model;

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


import com.codenjoy.dojo.puzzlebox.services.Events;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.joystick.DirectionActJoystick;

import java.util.LinkedList;
import java.util.List;

/**
 * Класс игрока. Тут кроме героя может подсчитываться очки. Тут же ивенты передабтся лиснеру фреймворка.
 */
public class Player extends DirectionActJoystick implements Joystick {

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