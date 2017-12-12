package com.epam.dojo.icancode.services.printer;

import com.codenjoy.dojo.services.Point;

/**
 * Created by Oleksandr_Baglai on 2017-12-12.
 */
public interface BoardReader {

    int size();

    Iterable<? extends Point> elements();
}
