package com.codenjoy.dojo.icancode.model.items;

import com.codenjoy.dojo.games.icancode.Element;
import com.codenjoy.dojo.icancode.model.BaseItem;
import com.codenjoy.dojo.icancode.model.Cell;

public abstract class RenewableItem extends BaseItem {

    private Cell dock;

    public RenewableItem(Element elements) {
        super(elements);
        dock = null;
    }

    @Override
    public void setCell(Cell cell) {
        if (dock == null && cell != null) {
            dock = cell;
        }
        super.setCell(cell);
    }

    public void reset() {
        dock.add(this);
    }

}
