package com.codenjoy.dojo.rubicscube.model;

/**
 * User: sanja
 * Date: 09.10.13
 * Time: 14:08
 */
public class Edge {
    public Elements color1;
    public Elements color2;
    public Face face1;
    public Face face2;

    public Edge(Face face1, Face face2, Elements color1, Elements color2) {
        this.face1 = face1;
        this.face2 = face2;
        this.color1 = color1;
        this.color2 = color2;
    }

    @Override
    public String toString() {
        return String.format("[%s-%s:%s%s]",
                face1.name(),
                face2.name(),
                color1.value(),
                color2.value());
    }
}
