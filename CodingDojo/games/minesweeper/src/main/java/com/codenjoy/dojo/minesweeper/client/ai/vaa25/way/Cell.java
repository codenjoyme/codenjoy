package com.codenjoy.dojo.minesweeper.client.ai.vaa25.way;


import com.codenjoy.dojo.minesweeper.client.ai.vaa25.way.terrain.*;

/**
 * @author Alexander Vlasov
 */
public class Cell {
    private Coord coord;
    private Terrain terrain;
    private Person person;

    public Cell(Coord coord) {
        this.coord = coord;
    }

    public Coord getCoord() {
        return coord;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        if (person != null) return "o";
        else if (terrain.getClass().equals(Road.class)) return ".";
        else if (terrain.getClass().equals(Wall.class)) return "W";
        else if (terrain.getClass().equals(Bush.class)) return "B";
        else if (terrain.getClass().equals(Glass.class)) return "G";
        return coord.toString();
    }

    public String print() {
        if (person != null) return "o";
        else if (terrain.getClass().equals(Road.class)) return ".";
        else if (terrain.getClass().equals(Wall.class)) return "W";
        else if (terrain.getClass().equals(Bush.class)) return "B";
        else if (terrain.getClass().equals(Glass.class)) return "G";
        return null;
    }
}
