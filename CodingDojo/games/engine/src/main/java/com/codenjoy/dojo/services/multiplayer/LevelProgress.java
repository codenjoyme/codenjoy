package com.codenjoy.dojo.services.multiplayer;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.utils.JsonUtils;
import org.json.JSONObject;

public class LevelProgress {

    protected int total;
    protected int current;
    protected int passed;

    public LevelProgress() {
        this.current = 0;
        this.passed = -1;
        this.total = 1;
    }

    public LevelProgress(int total, int current, int passed) {
        this.total = total;
        this.current = current;
        this.passed = passed;
    }

    public LevelProgress(MultiplayerType type) {
        this();
        if (type.isTraining()) {
            this.current = 0;
            this.passed = -1;
            this.total = type.getLevelsCount();
        }
    }

    public LevelProgress(JSONObject json) {
        try {
            JSONObject progress = json.getJSONObject("levelProgress");
            total = progress.getInt("total");
            current = progress.getInt("current");
            passed = progress.getInt("lastPassed");
        } catch (Exception e) {
            throw jsonFormatError(e, json.toString());
        }

        validate();
    }

    private static RuntimeException jsonFormatError(Exception e, String jsonString) {
        return new IllegalArgumentException(
                "Bad progress json format: " + e.getMessage() +
                ": " + jsonString, e);
    }

    void validate() {
        if (!isValid()) {
            throw new IllegalArgumentException("Progress is invalid: " + this);
        }
    }

    public static LevelProgress parse(String jsonString) {
        JSONObject json;
        try {
            json = new JSONObject(jsonString);
        } catch (Exception e) {
            throw jsonFormatError(e, jsonString);
        }
        return new LevelProgress(json);
    }

    public JSONObject saveTo(JSONObject json) {
        JSONObject progress = new JSONObject();
        progress.put("total", total);
        progress.put("current", current);
        progress.put("lastPassed", passed);

        json.put("levelProgress", progress);
        return json;
    }

    public int getTotal() {
        return total;
    }

    public int getCurrent() {
        return current;
    }

    public int getPassed() {
        return passed;
    }

    public void change(int level, int passed) {
        this.current = level;
        this.passed = passed;
    }

    public void change(int level) {
        this.current = level;
    }

    public static JSONObject winLevel(JSONObject json) {
        LevelProgress progress = new LevelProgress(json);
        JSONObject clone = new JSONObject(json.toString());

        progress.change(progress.current + 1,
                Math.max(progress.passed, progress.current));

        if (progress.isValid()) {
            return progress.saveTo(clone);
        } else {
            return null;
        }
    }

    public boolean isValid() {
        return current < total + 1 // TODO 0..count (single), count+1 (multiple)
                && current - 1 <= passed
                && passed <= total;
    }

    public boolean canChange(int level) {
        return new LevelProgress(total, level, passed).isValid();
    }

    @Override
    public String toString() {
        return JsonUtils.toStringSorted(this).replace('"', '\'');
    }

}
