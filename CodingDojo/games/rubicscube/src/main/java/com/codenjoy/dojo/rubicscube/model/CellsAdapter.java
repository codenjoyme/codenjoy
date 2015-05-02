package com.codenjoy.dojo.rubicscube.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.LinkedList;
import java.util.List;

public class CellsAdapter {
    private Cube cube;

    public CellsAdapter(Cube cube) {
        this.cube = cube;
    }

    public List<Cell> getCells() {
        List<Cell> result = new LinkedList<Cell>();

        fillAt(result, Face.LEFT,  0, 6);
        fillAt(result, Face.FRONT, 3, 6);
        fillAt(result, Face.RIGHT, 6, 6);
        fillAt(result, Face.BACK,  9, 6);
        fillAt(result, Face.UP,    3, 9);
        fillAt(result, Face.DOWN,  3, 3);

        return result;
    }

    private void fillAt(List<Cell> result, Face face, int dx, int dy) {
        FaceValue faceValue = cube.face(face);
        for (int x = 0; x < 3; x++) {
            Line row = faceValue.getRow(x);
            for (int y = 0; y < 3; y++) {
                Point pt = PointImpl.pt(x + dx, y + dy);
                result.add(new Cell(pt, row.get(2 - y)));
            }
        }
    }
}
