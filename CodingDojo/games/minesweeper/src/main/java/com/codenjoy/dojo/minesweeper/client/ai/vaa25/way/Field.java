package com.codenjoy.dojo.minesweeper.client.ai.vaa25.way;


import com.codenjoy.dojo.minesweeper.client.ai.vaa25.way.terrain.Road;
import com.codenjoy.dojo.minesweeper.client.ai.vaa25.way.terrain.Terrain;
import com.codenjoy.dojo.minesweeper.client.ai.vaa25.way.terrain.Wall;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Alexander Vlasov
 */
public class Field {
    private int width, height;
    private Cell[][] field;

    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        field = new Cell[width][height];
        init();
    }

    public boolean isValid(Coord coord) {
        if (coord.getX() < 0 || coord.getX() >= width || coord.getY() < 0 || coord.getY() >= height) return false;
        else return true;
    }

    public Cell getCell(Coord coord) {
        return field[coord.getX()][coord.getY()];
    }

    private void init() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell cell = new Cell(new Coord(i, j));
                field[i][j] = cell;
                cell.setTerrain(new Road());
            }
        }
    }

    public void fillTerrain(Collection<Coord> coords, Terrain terrain) {
        Iterator<Coord> iterator = coords.iterator();
        while (iterator.hasNext()) {
            getCell(iterator.next()).setTerrain(terrain);
        }
    }

    public void setPerson(Person person) {
        getCell(person.getCoord()).setPerson(person);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Coord getWalkableCoord() {
        do {
            Coord coord = CoordService.getRandomCoord(width, height);
            Cell cell = getCell(coord);
            if (cell.getTerrain().isWalkable()) {
                return coord;
            }
        } while (true);

    }

    public void setRandomWalls(int amount) {
        for (int i = 0; i < amount; i++) {
            Coord coord = CoordService.getRandomCoord(width, height);
            if (getCell(coord).getTerrain() == null) {
                i--;
                continue;
            } else {
                setWall(coord);
            }

        }

    }

    public void setWall(Coord coord) {
        getCell(coord).setTerrain(new Wall());
    }

    public void print() {
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                System.out.print(field[i][j].print() + " ");
            }
            System.out.println();
        }
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
    }


}
