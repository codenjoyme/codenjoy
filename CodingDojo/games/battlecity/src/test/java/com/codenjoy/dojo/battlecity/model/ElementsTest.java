package com.codenjoy.dojo.battlecity.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ElementsTest {

    @Test
    public void getConstructions() {
        assertEquals("[╬, ╩, ╦, ╠, ╣, ╨, ╥, ╞, ╡, │, ─, ┌, ┐, └, ┘,  ]",
                Elements.getConstructions().toString());
    }

}