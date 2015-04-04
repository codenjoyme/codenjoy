package com.codenjoy.dojo.minesweeper.client.ai.vaa25.way;

import java.util.Collection;
import java.util.Iterator;

/**
 * ������-����� ������ � WayCoord
 *
 * @author Alexander Vlasov
 */
public class WayCoordService {
    public static WayCoord getFirst(WayCoord wayCoord) {
        while (wayCoord.getPrev() != null) {
            wayCoord = wayCoord.getPrev();
        }
        return wayCoord;
    }

    /**
     * ������� � �������� �������� ���������� ���� ���������� �������� ���� ����
     *
     * @param set      ������ ��������� ��������� �������� ����
     * @param wayCoord ���������� �������� ���� ����
     * @return
     */
    public static WayCoord find(Collection<WayCoord> set, WayCoord wayCoord) {
        Coord coord = wayCoord.getCoord();
        Iterator<WayCoord> iterator = set.iterator();
        while (iterator.hasNext()) {
            WayCoord next = iterator.next();
            if (next.getCoord().equals(coord)) return next;
        }
        return null;
    }
}
