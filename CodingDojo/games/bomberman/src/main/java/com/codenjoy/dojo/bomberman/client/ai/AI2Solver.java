package com.codenjoy.dojo.bomberman.client.ai;

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


import com.codenjoy.dojo.bomberman.client.Board;
import com.codenjoy.dojo.bomberman.model.Elements;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Point;

import java.util.*;

public class AI2Solver implements Solver<Board> {

    public static HistoryPoint memory = new HistoryPoint(null);

    private final Solver solver;

    static class HistoryPoint implements Iterable<HistoryPoint> {
        private List<Elements> near = new LinkedList<Elements>();
        private String command = null;

        private HistoryPoint prev = null;
        private HistoryPoint next = null;

        public HistoryPoint(HistoryPoint parent) {
            if (parent != null) {
                this.prev = parent;
                parent.next = this;
            }
        }

        public void add(Board board, String command) {
            HistoryPoint last = getLast();
            last.command = command;
            last.near = getNearBomberman(board);
        }

        private List<Elements> getNearBomberman(Board board) {
            Point bomberman = board.getBomberman();
            return board.getNear(bomberman.getX(), bomberman.getY());
        }

        public HistoryPoint getLast() {
            HistoryPoint current = this;

            while (current.command != null) {
                current = current.next;
            }

            new HistoryPoint(current);

            return current;
        }

        public String getFor(String oldDirection, Board board) {
            List<Elements> near = getNearBomberman(board);

            List<HistoryPoint> equalsPoint = new LinkedList<HistoryPoint>();
            for (HistoryPoint point : this) {
                if (point.itsMe(near)) {
                    if (point.prev != null &&
                        point.prev.command != null &&
                        point.prev.command.equals(oldDirection))
                    {
                        equalsPoint.add(point);
                    }
                }
            }

            List<HistoryPoint> goodPoint = new LinkedList<HistoryPoint>(); // TODO это можно убрать
            for (HistoryPoint point : equalsPoint) {
                if (point.noKillWay() && point.dieTime() > 5) {
                    goodPoint.add(point);
                }
            }

            if (goodPoint.size() == 0) {
                return "";
            }

            Map<String, List<HistoryPoint>> byDirection = new HashMap<String, List<HistoryPoint>>();
            for (HistoryPoint point : goodPoint) {
                String command = point.command;
                if (!byDirection.containsKey(command)) {
                    byDirection.put(command, new LinkedList<HistoryPoint>());
                }
                byDirection.get(command).add(point);
            }

            Map<String, Double> directionScores = new HashMap<String, Double>();
            for (Map.Entry<String, List<HistoryPoint>> entry : byDirection.entrySet()) {
                double sum = 0.0;
                for (HistoryPoint point : entry.getValue()) {
                    sum += point.dieTime();
                }
                sum /= entry.getValue().size();
                directionScores.put(entry.getKey(), sum);
            }

            double maxLive = 0.0;
            String selectedCommand = "";
            for (Map.Entry<String, Double> entry : directionScores.entrySet()) {
                if (entry.getValue() > maxLive) {
                    maxLive = entry.getValue();
                    selectedCommand = entry.getKey();
                }
            }

            return selectedCommand;
        }

        private void sortByDieTime(List<HistoryPoint> goodPoint) {
            Collections.sort(goodPoint, new Comparator<HistoryPoint>() {
                @Override
                public int compare(HistoryPoint o1, HistoryPoint o2) {
                    return Integer.compare(o1.dieTime(), o2.dieTime());
                }
            });
        }

        private int dieTime() {
            int result = 0;
            for (HistoryPoint point : this) {
                if (point.isDie()) {
                    break;
                }
                result++;
            }
            return result;
        }

        private boolean noKillWay() {
            return next != null && !next.near.contains(Elements.DEAD_BOMBERMAN);
        }

        @Override
        public Iterator<HistoryPoint> iterator() {
            final HistoryPoint[] current = {this};
            return new Iterator<HistoryPoint>() {
                @Override
                public boolean hasNext() {
                    return current[0].next != null;
                }

                @Override
                public HistoryPoint next() {
                    current[0] = current[0].next;
                    return current[0];
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();

            int count = 0;
            for (Elements el : near) {
                result.append(el.ch());
                count ++;
                if (count % 3 == 0) {
                    result.append("\n");
                }
            }

            result.append(command);

            return result.toString();
        }

        public boolean itsMe(List<Elements> elements) {
            return near.equals(elements);
        }

        public boolean isDie() {
            return near.contains(Elements.DEAD_BOMBERMAN);
        }
    }

    public AI2Solver(Solver solver) {
        this.solver = solver;
    }

    static int learnCounter = 0;
    static String oldDirection = "STOP";

    @Override
    public String get(Board board) {
        learnCounter++;
        String byAI = solver.get(board);

        String byMemory = memory.getFor(oldDirection, board);

        memory.add(board, byAI);

        if (!byMemory.equals("")) {
            byAI = byMemory;
        }

        oldDirection = byAI;

        return byAI;
    }
}
