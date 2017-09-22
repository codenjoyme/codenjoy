package com.epam.dojo.expansion.model.replay;

import com.codenjoy.dojo.services.Point;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Oleksandr_Baglai on 2017-09-22.
 */
public interface LoggerReader {
    JSONObject getCurrentAction(int tick);

    List<JSONObject> getOtherCurrentActions(int tick);

    Point getBasePosition(int tick);

    JSONObject getBoard(int tick);

    int size();
}
