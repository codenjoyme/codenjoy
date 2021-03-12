package com.codenjoy.dojo.icancode.model.items;

import com.codenjoy.dojo.icancode.model.BaseItem;
import com.codenjoy.dojo.icancode.model.Cell;
import com.codenjoy.dojo.icancode.model.Elements;

public abstract class RenewableItem extends BaseItem {

    private Cell dock;

    public RenewableItem(Elements elements) {
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
