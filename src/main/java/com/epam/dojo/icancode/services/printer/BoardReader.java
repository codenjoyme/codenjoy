package com.epam.dojo.icancode.services.printer;
import com.codenjoy.dojo.services.Point;

import java.util.function.BiFunction;

/**
 * Created by Oleksandr_Baglai on 2017-12-12.
 */
public interface BoardReader<T> {

    int size();

    BiFunction<Integer, Integer, T> elements();

    Point viewCenter(Object player);

    Object[] itemsInSameCell(T item);
}
