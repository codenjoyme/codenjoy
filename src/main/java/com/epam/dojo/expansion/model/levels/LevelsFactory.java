package com.epam.dojo.expansion.model.levels;

import com.epam.dojo.expansion.model.interfaces.ILevel;

import java.util.List;

/**
 * Created by Oleksandr_Baglai on 2017-09-01.
 */
public interface LevelsFactory {
    List<ILevel> get();
}
