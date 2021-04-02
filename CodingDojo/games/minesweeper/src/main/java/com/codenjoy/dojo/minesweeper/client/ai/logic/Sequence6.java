package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.util.ArrayList;

public class Sequence6 {

    public static Integer[] getSequensed(int setted, int whole) {
        checkArguments(setted, whole);
        ArrayList arrayList = new ArrayList();
        int length = (int) Math.pow(2.0D, whole);

        for (int i = 0; i < length; ++i) {
            if (Integer.bitCount(i) == setted) {
                arrayList.add(i);
            }
        }

        Integer[] result = new Integer[arrayList.size()];
        return (Integer[]) arrayList.toArray(result);
    }

    private static void checkArguments(int setted, int whole) {
        if (setted > whole) {
            throw new IllegalArgumentException("Количество установленных бит превышает общее количество бит ( " + setted + " > " + whole + " )");
        } else if (whole > 30) {
            throw new IllegalArgumentException("Разрядность числа превышает 30 бит");
        }
    }


}
