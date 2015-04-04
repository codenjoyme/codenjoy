package com.codenjoy.dojo.minesweeper.client.ai.vaa25.way;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * ���������� ����������� ���� �� ����� �� ����� � ������� ��������� ���������
 *
 * @author Alexander Vlasov
 */
public class PathFinder {
    private final int WALKABLE = Integer.MAX_VALUE;
    private final int UNWALKABLE = Integer.MAX_VALUE - 1;
    private final int FINISH = 0;
    private final int START = Integer.MAX_VALUE - 2;
    private Field field;
    private List<Coord> result;
    private List<WayCoord> startList, finishList;
    private int[][] r;
    private int width, height;

    /**
     * �������� ������ ��� ����������� ����
     *
     * @param field ����
     */
    public PathFinder(Field field) {
        this.field = field;
        width = field.getWidth();
        height = field.getHeight();
        r = new int[width + 2][height + 2];
        for (int i = 0; i < width + 2; i++) {
            for (int j = 0; j < height + 2; j++) {
                if (i == 0 || j == 0 || i == width + 1 || j == height + 1) r[i][j] = UNWALKABLE;
                else if (field.getCell(new Coord(i - 1, j - 1)).getTerrain().isWalkable()) r[i][j] = WALKABLE;
                else r[i][j] = UNWALKABLE;
            }
        }

    }

    private Coord getMinR(int[][] r, Coord was) {
        int x = was.getX();
        int y = was.getY();
        int min = r[x - 1][y - 1];
        int xmin = x - 1;
        int ymin = y - 1;

        if (r[x + 1][y - 1] < min) {
            min = r[x + 1][y - 1];
            xmin = x + 1;
            ymin = y + 1;
        }
        if (r[x - 1][y + 1] < min) {
            min = r[x - 1][y + 1];
            xmin = x - 1;
            ymin = y + 1;
        }
        if (r[x + 1][y + 1] < min) {
            min = r[x + 1][y + 1];
            xmin = x + 1;
            ymin = y + 1;
        }
        if (r[x + 1][y] < min) {
            min = r[x + 1][y];
            xmin = x + 1;
            ymin = y;
        }
        if (r[x][y - 1] < min) {
            min = r[x][y - 1];
            xmin = x;
            ymin = y + 1;
        }
        if (r[x - 1][y] < min) {
            min = r[x - 1][y];
            xmin = x - 1;
            ymin = y;
        }
        if (r[x][y + 1] < min) {
            min = r[x][y + 1];
            xmin = x;
            ymin = y + 1;
        }
        return new Coord(xmin, ymin);
    }

    /**
     * �������� ��������
     *
     * @param from �������, ��������� �����
     * @param to   �������� �����
     * @return ������ ��������� ���� �� ������� ����� ���� (���������� �� ��������� �� �������� �����)
     */
    public List<Coord> findPath(Coord from, Coord to) {
        result = new ArrayList<Coord>();
        startList = new ArrayList<WayCoord>();
        finishList = new ArrayList<WayCoord>();
        if (from.equals(to)) return null;
        WayCoord finishWayCoord = new WayCoord(to, null);
        WayCoord startWayCoord = new WayCoord(from, null);
        startList.add(startWayCoord);
        finishList.add(finishWayCoord);
        int startListIndex = 0;
        int finishListIndex = 0;
        boolean found = false;
        WayCoord center;
        do {
            WayCoord stWayCoord = startList.get(startListIndex++);

            if ((center = next(finishList, startList, stWayCoord)) != null) {
                found = true;
                break;
            }

            WayCoord finWay = finishList.get(finishListIndex++);
            if ((center = next(startList, finishList, finWay)) != null) {
                found = true;
                break;
            }
        } while (finishList.size() > finishListIndex && startList.size() > startListIndex);
        if (found == false) return null;
        WayCoord centerStart = WayCoordService.find(startList, center);
        fillResult(centerStart);
        Collections.reverse(result);
        WayCoord centerFinish = WayCoordService.find(finishList, center);
        fillResult(centerFinish);
        System.out.println("Center = " + center.getCoord());
        return result;
    }

    /**
     * ��������� List ���������� ���� ������������
     *
     * @param centerStart ���������� �������� ����
     */
    private void fillResult(WayCoord centerStart) {
        do {
            if (!result.contains(centerStart.getCoord())) result.add(centerStart.getCoord());
            centerStart = centerStart.getPrev();
        } while (centerStart != null);
    }

    /**
     * ��������� �������� ���������� ����� �� ������� �������� �� ������ ������
     *
     * @param secondWave ������ �����
     * @param firstWave  ������ �����
     * @param WayCoord1  ����� ������ �����
     * @return
     */
    private WayCoord next(List<WayCoord> secondWave, Collection<WayCoord> firstWave, WayCoord WayCoord1) {
        Coord coord = WayCoord1.getCoord();
        CoordService coordService = new CoordService(coord);
        coordService.setNearest(secondWave.get(0).getCoord());
        while (coordService.hasNext()) {
            Coord next = coordService.next();
            if (field.isValid(next)) {
                Cell cell = field.getCell(next);
                if (cell.getTerrain().isWalkable() && cell.getPerson() == null) {
                    WayCoord wayCoord = new WayCoord(next, WayCoord1);
                    if (!firstWave.contains(wayCoord)) firstWave.add(wayCoord);
                    if (secondWave.contains(wayCoord)) {
                        return wayCoord;
                    }
                }
            }
        }
        return null;
    }
}
