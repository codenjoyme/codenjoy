package com.codenjoy.dojo.services;

/**
 * Created by Sanja on 26.10.2014.
 */
public interface BoardReader {

    int size();

    Iterable<? extends Point> elements();
}
