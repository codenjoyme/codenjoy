package com.codenjoy.dojo.quake2d.services.ai;

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
import com.codenjoy.dojo.games.quake2d.Board;
import com.codenjoy.dojo.games.quake2d.Element;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;

import java.util.*;

public class AISolver implements Solver<Board> {

    private DeikstraFindWay way;
    public static final int SHIFT_COMMAND = 100;

    public AISolver(Dice dice) {
        this.way = new DeikstraFindWay();
    }

    public DeikstraFindWay.Possible possible(final Board board) {
        return new DeikstraFindWay.Possible() {
            @Override // TODO test me
            public boolean possible(Point from, Direction where) {
                Point to = where.change(from);
                int nx = to.getX();
                int ny = to.getY();

                if (board.isBarrierAt(nx, ny)) return false;
                if (board.isBombAt(nx, ny)) return false;

                return true;
            }

            @Override
            public boolean possible(Point point) {
                return !board.isBarrierAt(point.getX(), point.getY());
            }
        };
    }

    @Override
    public String get(final Board board) {
        if (board.isGameOver()) return "";
        List<Direction> result = getDirections(board);
        if (result.isEmpty()) return "";

        return moveWithAction(result.get(0));
    }

    private String moveWithAction(Direction direction){
        for (Direction elemDirection : Direction.getValues()){
            if (direction == elemDirection){
                return Direction.ACT(elemDirection.ordinal() + SHIFT_COMMAND);
            }
        }
        return direction.toString();
    }

    public List<Direction> getDirections(Board board) {
        int size = board.size();
        int priority;
//        Element elementOfPriority;
        Point from = board.getMe();

        List<Point> to_OtherHero = board.get(Element.OTHER_HERO);
        List<Point> to_Abilities = board.get(Element.SUPER_WEAPON);
        to_Abilities.addAll(board.get(Element.SUPER_DEFENCE));
        to_Abilities.addAll(board.get(Element.HEALTH_PACKAGE));
        List<Point> to_Walls = board.get(Element.WALL);
        List<Point> to_SuperOtherHero = board.get(Element.SUPER_OTHER_HERO);
        to_OtherHero.addAll(to_SuperOtherHero);
//        List<Point> to_Bullet = board.get(Element.BULLET);

        Map<Element, Integer> variantsWays = getDestinationToObject(board, size, from);
        Point superHero = (to_SuperOtherHero.size() == 0) ? null : to_SuperOtherHero.get(0);//isAnyHeroWithAbility();
        Point unUsedAbility = (to_Abilities.size() == 0) ? null : to_Abilities.get(0);
        DeikstraFindWay.Possible map = possible(board);
        if (superHero == null && unUsedAbility == null){
            return way.getShortestWay(size, from, to_OtherHero, map);
        } else if (unUsedAbility != null){
            return way.getShortestWay(size, from, to_Abilities, map);
        } else {
            return inversionOfDirection(size, from, to_OtherHero, map);
//          way.getPossibleWays()
        }

//        DeikstraFindWay.Possible map = possible(board);
//        return way.getShortestWay(size, from, to_Abilities, map);
    }

    private List<Direction> inversionOfDirection(int pSize, Point pFrom, List<Point> pTo_OtherHero, DeikstraFindWay.Possible pMap) {
        List<Direction> path = way.getShortestWay(pSize, pFrom, pTo_OtherHero, pMap);
        if (path.isEmpty()) {
            return Arrays.asList();
        }
        return Arrays.asList(path.get(0).inverted());
    }

//    private Hero isAnyHeroWithAbility() {
//        for (Hero otherHero : game.getHeroes()){
//            if (otherHero.getAbility() != null){
//                return otherHero;
//            }
//        }
//        return null;
//    }

    private Map<Element, Integer> getDestinationToObject(Board board, int pSize, Point pFrom) {
        Element[] analyze = {Element.OTHER_HERO, Element.SUPER_WEAPON, Element.SUPER_DEFENCE,
                                      Element.HEALTH_PACKAGE, Element.BULLET};
        Map<Element, Integer> variantsWays = new HashMap<>(analyze.length);
        DeikstraFindWay.Possible map = possible(board);
        for (Element elem : analyze){
            int destination = way.getShortestWay(pSize, pFrom, board.get(elem), map).size();
            variantsWays.put(elem, (destination == 0) ? null : destination);
        }
        return variantsWays;
    }
}
