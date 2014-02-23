package com.apofig;

/**
 * User: sanja
 * Date: 09.10.13
 * Time: 14:08
 */
public class Edge {
    public Color color1;
    public Color color2;
    public Face face1;
    public Face face2;

    public Edge(Face face1, Face face2, Color color1, Color color2) {
        this.face1 = face1;
        this.face2 = face2;
        this.color1 = color1;
        this.color2 = color2;
    }

    @Override
    public String toString() {
        return String.format("[%s-%s:%s%s]", face1.name(), face2.name(), color1.value(), color2.value());
    }
}
