package com.codenjoy.dojo.sokoban.model.items;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.multiplayer.GameField;
import org.json.JSONObject;

public abstract class PointEnriched  <F extends GameField> extends PointImpl implements Tickable{
    protected F field;

    public PointEnriched(int x, int y) {
        super(x, y);
    }

    public PointEnriched(Point point) {
        super(point);
    }

    public PointEnriched(JSONObject json) {
        super(json);
    }

    public void init(F field) {
        this.field = field;
    }
}
