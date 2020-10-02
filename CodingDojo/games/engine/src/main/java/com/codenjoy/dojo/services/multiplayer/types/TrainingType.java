package com.codenjoy.dojo.services.multiplayer.types;

import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.multiplayer.Single;
import org.json.JSONObject;

public class TrainingType extends MultiplayerType {

    public TrainingType(Integer levels) {
        super(1, levels, !DISPOSABLE);
    }

    @Override
    public int getRoomSize(LevelProgress progress) {
        if (progress == null) {
            return super.roomSize;
        }

        if (progress.getCurrent() < progress.getTotal()) {
            return SINGLE.getRoomSize();
        } else {
            return MULTIPLE.getRoomSize();
        }
    }

    @Override
    public Object postProcessBoard(Object board, Single single) {
        LevelProgress progress = single.getProgress();

        if (board instanceof JSONObject) {
            JSONObject json = (JSONObject) board;
            progress.saveTo(json);
            return json;
        }

        JSONObject json = new JSONObject();
        progress.saveTo(json);
        json.put("board", board);
        return json;
    }

    @Override
    public JSONObject postProcessSave(JSONObject save, Single single) {
        LevelProgress progress = single.getProgress();

        JSONObject result = new JSONObject();
        result.put("field", save);
        progress.saveTo(result);
        return result;
    }

    @Override
    public LevelProgress progress() {
        return new LevelProgress(){{
            this.current = 0;
            this.passed = -1;
            this.total = getLevelsCount();
        }};
    }

    @Override
    public int loadProgress(Game game, JSONObject save) {
        if (!save.has("levelProgress")) {
            return super.loadProgress(game, save);
        }

        LevelProgress progress = new LevelProgress(save);
        int roomSize = getRoomSize(progress);
        game.setProgress(progress);
        return roomSize;
    }
}