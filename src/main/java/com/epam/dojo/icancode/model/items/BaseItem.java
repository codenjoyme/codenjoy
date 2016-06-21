package com.epam.dojo.icancode.model.items;

import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.epam.dojo.icancode.model.*;

/**
 * Created by Mikhail_Udalyi on 08.06.2016.
 */
public abstract class BaseItem extends PointImpl implements State<Elements, Player>, Fieldable {

    protected ICell cell;
    protected Field field;

    private ItemLogicType[] types;
    private Elements element;

    public ItemLogicType[] getTypes() {
        return types;
    }

    public BaseItem(ItemLogicType[] types, Elements element) {
        super(0, 0);

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
        move(cell);
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
}
