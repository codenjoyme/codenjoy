package com.codenjoy.dojo.rubicscube.model;

import java.util.LinkedList;
import java.util.List;

/**
 * User: sanja
 * Date: 09.10.13
 * Time: 14:38
 */
public class Edges {

    private List<Edge> edges;

    public Edges() {
        edges = new LinkedList<Edge>();
    }

    public void add(Face face1, Face face2, Element color1, Element color2) {
        edges.add(new Edge(face1, face2, color1, color2));
    }

    public Edge find(String colors) {
        for (Edge edge : edges) {
            if (edge.toString().contains(":" + colors)) {
                return edge;
            }
        }
        throw new IllegalArgumentException("Грань не найдена: " + colors);
    }

    @Override
    public String toString() {
        return edges.toString();
    }
}
