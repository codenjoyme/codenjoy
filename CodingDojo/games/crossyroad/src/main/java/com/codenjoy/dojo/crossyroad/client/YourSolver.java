package com.codenjoy.dojo.crossyroad.client;

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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;

import java.util.LinkedList;
import java.util.List;

/**
 * User: your name
 * Это твой алгоритм AI для игры. Реализуй его на свое усмотрение.
 * Обрати внимание на {@see YourSolverTest} - там приготовлен тестовый
 * фреймворк для тебя.
 */
public class YourSolver implements Solver<Board> {

    private Dice dice;
    private Board board;
    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;
        Direction dir = Direction.STOP;// дефолтное напраление
        List<Point> barriers = board.getBarriers();// получение всех координат препятствий

        //реализовать: выбор направления(left, up, right)
        Point myPos = board.getMe();
        double myX = myPos.getX();//10
        double myY = myPos.getY();//18

        System.out.println(myX+" "+myY);//для проверок, потом удалить

        List<Point> possibleDirection = new LinkedList<>();//← ↑ →
        boolean check[] = {true, true, true};
        int i=0;
        for (Direction direction : Direction.onlyDirections()){
            if(  !(direction.equals(Direction.DOWN))  ) {
                Point heroAfterToMove = board.getMe();//мое положение
                heroAfterToMove.change(direction);//мое положение после хода(типа)
                possibleDirection.add(heroAfterToMove);//добавление этой позиции в лист
                //флаги(наличие слева/свеху/справа чего-нибудь)(сравнение этой позиции с координатами барьеров)
                for(Point point : barriers){
                    if(point.equals(heroAfterToMove)){
                        check[i] = false;// что логично, если возможная точка совпала с точкой барьера, туда ходить нельзя, печалька
                    }
                }
                i++;
            }
        }
        //на выходе: список с координатами для хода и массив флагов, говорящих, можно ли туда ходить


        // алгоритм выбора направления (лево, вверх, вправо, стоп)
        // реализация пока самая простая
        if(check[1])                    // если впереди свободно, то вперед
            return Direction.UP.toString();
        if( !(check[1]) && check[2])    // если впереди занято, а с права свободно, то ждем
            return Direction.STOP.toString();
        // в любом другом случае(при направлении платформ в одну сторону), суицыд

        //реализовать: проверку на встречу игрока со стеной(не здесь, а в Crossyroad.tick() , там есть заготовки)
        return dir.toString();
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // paste here board page url from browser after registration
                "http://localhost:8080/codenjoy-contest/board/player/0?code=000000000000",
                new YourSolver(new RandomDice()),
                new Board());
    }
}





























