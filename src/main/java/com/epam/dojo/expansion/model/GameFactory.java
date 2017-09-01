package com.epam.dojo.expansion.model;

/**
 * Created by Oleksandr_Baglai on 2017-09-01.
 */
public interface GameFactory {
    Expansion get(boolean isMultiple);
}
