package com.codenjoy.dojo.sudoku.model.level;

import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.sudoku.model.level.levels.Level1;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LevelImplTest {

    private final int SIZE = 9;
    private Level1 level;

    @Before
    public void setup() {
        level = new Level1();
    }

    @Test
    public void testAll() {
        assertEquals(
                "8 3*6 2*1 5*7 9*4*" +
                "2 1 9*7 4*6 8*5 3 " +
                "4 7*5 3 9*8 6 2*1*" +
                "6 9 7*5*2*4 1 3 8*" +
                "1*8*4*9 7 3 5*6 2 " +
                "3 5*2 6 8 1*9 4 7*" +
                "9*4 1 8*5 2 3 7*6*" +
                "5*6*8 4*3 7 2*1 9 " +
                "7 2*3 1 6 9*4 8 5*",
                level.all());
    }

    @Test
    public void testMap() {
        assertEquals(
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼836☼215☼794☼\n" +
                "☼219☼746☼853☼\n" +
                "☼475☼398☼621☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼697☼524☼138☼\n" +
                "☼184☼973☼562☼\n" +
                "☼352☼681☼947☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼941☼852☼376☼\n" +
                "☼568☼437☼219☼\n" +
                "☼723☼169☼485☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n",
                TestUtils.injectN(level.map()));
    }

    @Test
    public void testMask() {
        assertEquals(
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼ * ☼* *☼ **☼\n" +
                "☼  *☼ * ☼*  ☼\n" +
                "☼ * ☼ * ☼ **☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  *☼** ☼  *☼\n" +
                "☼***☼   ☼*  ☼\n" +
                "☼ * ☼  *☼  *☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼*  ☼*  ☼ **☼\n" +
                "☼** ☼*  ☼*  ☼\n" +
                "☼ * ☼  *☼  *☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n",
                TestUtils.injectN(level.mask()));
    }

    @Test
    public void testSize() {
        assertEquals(13, level.size());
    }

    @Test
    public void testWalls() {
        assertEquals(
                "[[0,12], [1,12], [2,12], [3,12], [4,12], [5,12], [6,12], [7,12], [8,12], [9,12], [10,12], [11,12], [12,12], " +
                "[0,11], [4,11], [8,11], [12,11], " +
                "[0,10], [4,10], [8,10], [12,10], " +
                "[0,9], [4,9], [8,9], [12,9], " +
                "[0,8], [1,8], [2,8], [3,8], [4,8], [5,8], [6,8], [7,8], [8,8], [9,8], [10,8], [11,8], [12,8], " +
                "[0,7], [4,7], [8,7], [12,7], " +
                "[0,6], [4,6], [8,6], [12,6], " +
                "[0,5], [4,5], [8,5], [12,5], " +
                "[0,4], [1,4], [2,4], [3,4], [4,4], [5,4], [6,4], [7,4], [8,4], [9,4], [10,4], [11,4], [12,4], " +
                "[0,3], [4,3], [8,3], [12,3], " +
                "[0,2], [4,2], [8,2], [12,2], " +
                "[0,1], [4,1], [8,1], [12,1], " +
                "[0,0], [1,0], [2,0], [3,0], [4,0], [5,0], [6,0], [7,0], [8,0], [9,0], [10,0], [11,0], [12,0]]",
                level.walls().toString());
    }

    @Test
    public void testCells() {
        assertEquals(
                "[pt[1,11]=+8, pt[2,11]=-3, pt[3,11]=+6, " +
                "pt[5,11]=-2, pt[6,11]=+1, pt[7,11]=-5, " +
                "pt[9,11]=+7, pt[10,11]=-9, pt[11,11]=-4, " +
                "pt[1,10]=+2, pt[2,10]=+1, pt[3,10]=-9, " +
                "pt[5,10]=+7, pt[6,10]=-4, pt[7,10]=+6, " +
                "pt[9,10]=-8, pt[10,10]=+5, pt[11,10]=+3, " +
                "pt[1,9]=+4, pt[2,9]=-7, pt[3,9]=+5, " +
                "pt[5,9]=+3, pt[6,9]=-9, pt[7,9]=+8, " +
                "pt[9,9]=+6, pt[10,9]=-2, pt[11,9]=-1, " +
                "pt[1,7]=+6, pt[2,7]=+9, pt[3,7]=-7, " +
                "pt[5,7]=-5, pt[6,7]=-2, pt[7,7]=+4, " +
                "pt[9,7]=+1, pt[10,7]=+3, pt[11,7]=-8, " +
                "pt[1,6]=-1, pt[2,6]=-8, pt[3,6]=-4, " +
                "pt[5,6]=+9, pt[6,6]=+7, pt[7,6]=+3, " +
                "pt[9,6]=-5, pt[10,6]=+6, pt[11,6]=+2, " +
                "pt[1,5]=+3, pt[2,5]=-5, pt[3,5]=+2, " +
                "pt[5,5]=+6, pt[6,5]=+8, pt[7,5]=-1, " +
                "pt[9,5]=+9, pt[10,5]=+4, pt[11,5]=-7, " +
                "pt[1,3]=-9, pt[2,3]=+4, pt[3,3]=+1, " +
                "pt[5,3]=-8, pt[6,3]=+5, pt[7,3]=+2, " +
                "pt[9,3]=+3, pt[10,3]=-7, pt[11,3]=-6, " +
                "pt[1,2]=-5, pt[2,2]=-6, pt[3,2]=+8, " +
                "pt[5,2]=-4, pt[6,2]=+3, pt[7,2]=+7, " +
                "pt[9,2]=-2, pt[10,2]=+1, pt[11,2]=+9, " +
                "pt[1,1]=+7, pt[2,1]=-2, pt[3,1]=+3, " +
                "pt[5,1]=+1, pt[6,1]=+6, pt[7,1]=-9, " +
                "pt[9,1]=+4, pt[10,1]=+8, pt[11,1]=-5]",
                level.cells().toString());
    }

    @Test
    public void testSumsIsOk() {
        Levels.all().forEach(
                level -> assertSum(level.map()));
    }

    private void assertSum(String board) {
        board = board.replaceAll("☼", "");
        int[][] arr = new int[SIZE][SIZE];

        LengthToXY xy = new LengthToXY(SIZE);
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                arr[x][y] = Integer.parseInt("" + board.charAt(xy.getLength(x, y)));
            }
        }

        for (int i = 0; i < SIZE; i++) {
            assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9]", getY(i, arr).toString());
            assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9]", getX(i, arr).toString());
        }

        for (int tx = 0; tx <= 2; tx++) {
            for (int ty = 0; ty <= 2; ty++) {
                assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9]", getC(tx, ty, arr).toString());
            }
        }
    }

    public List<Integer> getY(int y, int[][] arr) {
        List<Integer> result = new LinkedList<>();

        for (int x = 0; x < SIZE; x++) {
            result.add(arr[x][y]);
        }

        Collections.sort(result);
        return result;
    }

    public List<Integer> getX(int x, int[][] arr) {
        List<Integer> result = new LinkedList<>();

        for (int y = 0; y < SIZE; y++) {
            result.add(arr[x][y]);
        }

        Collections.sort(result);
        return result;
    }

    public List<Integer> getC(int tx, int ty, int[][] arr) {
        List<Integer> result = new LinkedList<>();

        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int x = 3*tx + dx + 1;
                int y = 3*ty + dy + 1;

                result.add(arr[x][y]);
            }
        }

        Collections.sort(result);
        return result;
    }

}
