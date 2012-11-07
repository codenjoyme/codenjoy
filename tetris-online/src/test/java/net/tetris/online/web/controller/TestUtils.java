package net.tetris.online.web.controller;

import java.util.List;

/**
 * User: serhiy.zelenin
 * Date: 11/7/12
 * Time: 3:00 PM
 */
public class TestUtils {
    static Object getCell(List<List> dataList, int row, int column) {
        return row(dataList, row).get(column);
    }

    private static List row(List<List> dataList, int index) {
        return dataList.get(index);
    }
}
