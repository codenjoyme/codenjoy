package com.epam.dojo.icancode.model.interfaces;

import com.codenjoy.dojo.services.State;
import com.epam.dojo.icancode.model.Elements;
import com.epam.dojo.icancode.model.Player;
import com.epam.dojo.icancode.model.enums.FeatureItem;

import java.util.List;

/**
 * Created by Mikhail_Udalyi on 01.07.2016.
 */
public interface IItem extends Fieldable, State<Elements, Player> {
    void action(IItem item);

    ICell getCell();

    List<IItem> getItemsInSameCell();

    void setCell(ICell value);

    boolean hasFeatures(FeatureItem[] features);
}
