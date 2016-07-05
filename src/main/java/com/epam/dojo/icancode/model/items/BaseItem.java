package com.epam.dojo.icancode.model.items;

import com.epam.dojo.icancode.model.Elements;
import com.epam.dojo.icancode.model.Player;
import com.epam.dojo.icancode.model.enums.FeatureItem;
import com.epam.dojo.icancode.model.interfaces.ICell;
import com.epam.dojo.icancode.model.interfaces.IItem;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Mikhail_Udalyi on 08.06.2016.
 */
public abstract class BaseItem implements IItem {
    private ICell cell;
    private FeatureItem[] features;
    private Elements element;

    //================================ Constructors ================================

    public BaseItem(Elements element) {
        this.element = element;
        this.features = new FeatureItem[0];
    }

    public BaseItem(Elements element, FeatureItem[] features) {
        this.element = element;
        this.features = features;
    }

    //================================ Implements ================================

    public void action(IItem item) {
        // do nothing
    }

    public ICell getCell() {
        return cell;
    }

    public List<IItem> getItemsInSameCell() {
        if (cell == null) {
            return Arrays.asList();
        }
        List<IItem> items = cell.getItems();
        items.remove(this);
        return items;
    }

    public Elements getState() {
        return element;
    }

    public void setCell(ICell value) {
        cell = value;
    }

    //================================ Overrides ================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseItem baseItem = (BaseItem) o;

        if (cell == null && baseItem.cell != null) {
            return false;
        }
        if (cell != null && baseItem.cell == null) {
            return false;
        }

        if (cell == null && baseItem.cell == null) {
            return element == baseItem.element;
        } else {
            return element == baseItem.element && cell.equals(baseItem.cell);
        }

    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return element;
    }

    @Override
    public String toString() {
        return String.format("'%s'", element);
    }

    public boolean hasFeatures(FeatureItem[] features) {
        for (int i = 0; i < this.features.length; ++i) {
            for (int j = 0; j < features.length; ++j) {
                if (this.features[i] == features[j]) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return element.hashCode();
    }
}
