package com.epam.dojo.icancode.services.printer;
import java.util.function.BiFunction;

/**
 * Created by Oleksandr_Baglai on 2017-12-12.
 */
public interface BoardReader<T> {

    int size();

    BiFunction<Integer, Integer, T> elements();

    Object[] getItemsInSameCell(T item);
}
