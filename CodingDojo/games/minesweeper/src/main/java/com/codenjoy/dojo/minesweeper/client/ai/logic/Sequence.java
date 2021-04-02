package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.util.ArrayList;
import java.util.List;

public class Sequence {

    public static Integer[] get(int setted, int whole) {
        if (setted > whole) {
            throw new IllegalArgumentException("Количество установленных бит превышает общее количество бит ( " + setted + " > " + whole + " )");
        } else if (whole > 30) {
            throw new IllegalArgumentException("Разрядность числа превышает 30 бит");
        }

        List list = new ArrayList();
        int length = (int) Math.pow(2.0D, whole);

        for (int i = 0; i < length; ++i) {
            if (Integer.bitCount(i) == setted) {
                list.add(i);
            }
        }

        return (Integer[]) list.toArray(new Integer[0]);
    }
}
