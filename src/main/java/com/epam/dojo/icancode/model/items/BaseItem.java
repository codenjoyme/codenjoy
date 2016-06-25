package com.epam.dojo.icancode.model.items;

import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.epam.dojo.icancode.model.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Mikhail_Udalyi on 08.06.2016.
 */
public abstract class BaseItem implements State<Elements, Player>, Fieldable {

    protected ICell cell;
    protected Field field;

    private ItemLogicType[] types;
    private Elements element;

    public ItemLogicType[] getTypes() {
        return types;
    }

    public BaseItem(ItemLogicType[] types, Elements element) {
        this.types = types;
        this.element = element;
    }

    public boolean is(ItemLogicType type) {
        for (int i = 0; i < types.length; i++) {

            if (types[i] == type) {
                return true;
            }
        }

        return false;
    }

    public void setCell(ICell value) {
        cell = value;
    }

    public ICell getCell() {
        return cell;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return element;
    }

    public void action(BaseItem item) {
        // do nothing
    }

    @Override
    public String toString() {
        return String.format("'%s'", element);
    }

    @Override
    public void init(Field field) {
        this.field = field;
    }

    public List<BaseItem> getItemsInSameCell() {
        if (cell == null) {
            return Arrays.asList();
        }
        List<BaseItem> items = cell.getItems();
        items.remove(this);
        return items;
    }

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
    public int hashCode() {
        return element.hashCode();
    }
}
