package com.codenjoy.dojo.services.algs;

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
import com.codenjoy.dojo.services.Point;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class DeikstraFindWay {

    // карта возможных передвижений, которые не будут менять на этом уровне: стены и прочие препятствия
    private Points basic;

    // карта возможных передвижений дополненная движимыми объектами
    private Points dynamic;

    private int size;
    private Possible checker;
    private boolean possibleIsConstant;

    public DeikstraFindWay() {
        this(false);
    }

    /**
     * @param possibleIsConstant Бывают случаи, когда варианты куда двигаться на карте
     * не меняется от тика к тику, а потому не надо пересчитывать варианты движений всякий раз.
     * Вот в таких случаях мы и ставим тут true.
     */
    public DeikstraFindWay(boolean possibleIsConstant) {
        this.possibleIsConstant = possibleIsConstant;
    }

    public interface Possible {

        default boolean possible(Point from, Direction where) {
            return true;
        }

        default boolean possible(Point point) {
            return true;
        }

        default boolean check(int size, Point from, Direction where) {
            if (from.isOutOf(size)) return false;
            if (!possible(from)) return false;

            Point dest = where.change(from);

            if (dest.isOutOf(size)) return false;
            if (!possible(dest)) return false;

            if (!possible(from, where)) return false;

            return true;
        }
    }

    public List<Direction> getShortestWay(int size, Point from, List<Point> goals, Possible possible) {
        if (possible == null) {
            throw new RuntimeException("Please setup Possible object before run getShortestWay");
        }
        getPossibleWays(size, possible);

        return buildPath(from, goals);
    }

    public List<Direction> buildPath(Point from, List<Point> goals) {
        if (goals.isEmpty()) {
            return Arrays.asList();
        }

        List<List<Direction>> paths = new LinkedList<>();
        Path pathMap = getPath(from, goals);
        for (Point to : goals) {
            List<Direction> path = pathMap.get(to);
            if (path == null || path.isEmpty()) continue;
            paths.add(path);
        }

        int minDistance = Integer.MAX_VALUE;
        int indexMin = 0;
        for (int index = 0; index < paths.size(); index++) {
            List<Direction> path = paths.get(index);
            if (minDistance > path.size()) {
                minDistance = path.size();
                indexMin = index;
            }
        }

        if (paths.isEmpty()) return Arrays.asList();
        List<Direction> shortest = paths.get(indexMin);
        if (shortest.size() == 0) return Arrays.asList();

        return shortest;
    }

    private Path getPath(Point from, List<Point> inputGoals) {
        Set<Point> goals = new HashSet<>(inputGoals);
        Path path = new Path(size);
        Vectors vectors = new Vectors(size, ways());
        vectors.add(inputGoals, from, 0);
        Vector current;
        while (!goals.isEmpty() && (current = vectors.next()) != null) {
            if (vectors.wasHere(current.to())) continue;
            List<Direction> before = path.get(current.from());
            List<Direction> directions = path.get(current.to());
            if (before.size() < directions.size() - 1) {
                // мы нашли более короткий путь,
                // но это никогда не случится )
                directions.clear();
            }
            if (directions.isEmpty()) {
                if (!before.isEmpty()) {
                    directions.addAll(before);
                }
                directions.add(current.where());

                if (!vectors.processed(current.to())) {
                    vectors.add(inputGoals, current.to(), directions.size());
                }
            } else {
                // do nothing
            }
            goals.remove(current.from());
        }

        return path;
    }

    private Points ways() {
        return (dynamic != null) ? dynamic : basic;
    }

    private Points setupWays() {
        Points points = new Points(size);

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Point from = pt(x, y);
                Status status = points.add(from);
                for (Direction direction : Direction.getValues()) {
                    if (!checker.check(size, from, direction)) continue;
                    status.add(direction);
                }
            }
        }
        return points;
    }

    public void updateWays(Possible possible) { // TODO закончить с этим
        dynamic = new Points(size);

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Point pt = pt(x, y);
                if (basic.isAdded(pt)) {
                    Status status = basic.get(pt);
                    boolean[] goes = status.goes();
                    for (int index = 0; index < goes.length; index++) {
                        if (!goes[index]) continue;

                        Direction direction = Direction.valueOf(index);
                        goes[index] = possible.check(size, pt, direction);
                    }
                }
            }
        }
    }

    public Collector<Map.Entry<Point, List<Direction>>, ?, Map<Point, List<Direction>>> toMap() {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    public Points getPossibleWays(int size, Possible possible) {
        this.size = size;
        this.checker = possible;

        if (possibleIsConstant && basic != null) {
            return basic;
        }

        return basic = setupWays();
    }

    public Points getBasic() {
        return basic;
    }

    public Points getDynamic() {
        return dynamic;
    }
}
