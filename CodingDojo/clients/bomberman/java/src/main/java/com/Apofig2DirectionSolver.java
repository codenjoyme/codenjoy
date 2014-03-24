package com;

import com.utils.Board;
import com.utils.Point;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class Apofig2DirectionSolver implements DirectionSolver {

    public static HistoryPoint memory = new HistoryPoint(null);

    private final DirectionSolver solver;

    static class HistoryPoint implements Iterable<HistoryPoint> {
        private List<Element> near = new LinkedList<Element>();
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

        private List<Element> getNearBomberman(Board board) {
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
            List<Element> near = getNearBomberman(board);

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

            List<HistoryPoint> goodPoint = new LinkedList<HistoryPoint>();
            for (HistoryPoint point : equalsPoint) {
                if (point.noKillWay()) {
                    goodPoint.add(point);
                }
            }

            if (goodPoint.size() == 0) {
                return "";
            }

            Collections.sort(goodPoint,  new Comparator<HistoryPoint>() {
                @Override
                public int compare(HistoryPoint o1, HistoryPoint o2) {
                    return Integer.valueOf(o1.dieTime()).compareTo(o2.dieTime());
                }
            });

            return goodPoint.get(goodPoint.size() - 1).command;
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
            return next != null && !next.near.contains(Element.DEAD_BOMBERMAN);
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
            String result = "";

            int count = 0;
            for (Element el : near) {
                result += el.getChar();
                count ++;
                if (count % 3 == 0) {
                    result += "\n";
                }
            }

            result += command;

            return result;
        }

        public boolean itsMe(List<Element> elements) {
            return near.equals(elements);
        }

        public boolean isDie() {
            return near.contains(Element.DEAD_BOMBERMAN);
        }
    }

    public Apofig2DirectionSolver(DirectionSolver solver) {
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

        System.out.println("Add to memory " + learnCounter);
        HistoryPoint point = memory.getLast().prev;
        System.out.println(point.toString());

        if (learnCounter > 100) {
            if (!byMemory.equals("")) {
                byAI = byMemory;
                System.out.println("Get from memory " + byMemory);
            }
        }

        System.out.println("-----------------------------------------------------------------------------");

        oldDirection = byAI;

        return byAI;
    }

}