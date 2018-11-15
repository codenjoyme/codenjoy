package com.codenjoy.dojo.puzzlebox.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by indigo on 2018-07-04.
 */
public class Hero extends PlayerHero<Field> {

    private int currentIndex;
    private List<Box> boxes;
    int deltaClicks;
    private Player player;

    public Hero() {
        super();

        currentIndex = 0;
        boxes = new LinkedList<>();
    }

    @Override
    public void down() { // TODO надо сохранять тут только куда двигать а действие провожить в тике потому что клиент теперь может вызвать эту команду много раз
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
        Box current = getBox();
        current.isCurrent = false;
        changeCurrentBox();
        current = getBox();
        if(current.isMoving) return;
        current.isCurrent = true;
        deltaClicks++;
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
        if (boxes.size() == 0){
            player.event(Events.WIN);
            this.move(-1, -1);
            return null;
        }
        Box box = boxes.get(currentIndex);
        this.move(box);
        return box;
    }

    public List<Box> getBoxes() {return boxes;}

    public void setBoxes(List<Box> input, Field field) {
        boxes = input;
        for (Box box: boxes){
            box.init(field);
        }
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

    @Override
    public void tick() {
        // do nothing
    }

    public void init(Player player) {
        this.player = player;
    }
}
