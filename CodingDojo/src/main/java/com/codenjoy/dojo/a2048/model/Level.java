package com.codenjoy.dojo.a2048.model;

import com.codenjoy.dojo.services.Point;

import java.util.List;

public interface Level {

    int getSize();

    List<Number> getNumbers();
}
