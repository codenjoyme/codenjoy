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
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.JSONObject;

@AllArgsConstructor
@Getter
public class LevelProgress {

    // так как уровни в настройках хрантся начиная с номера 1,
    // а индексы у нормальных программистов с 0
    // я пробовал нормально - путаница получается, лучше так, поверь
    // эта константа тут, чтобы связать все места в коде (java + js),
    // где считаем с 1 коммит, отвечающий за это так же
    // можно посмортреть в git e26ec4f6
    public static final int levelsStartsFrom1 = 1;

    protected int total;
    protected int current;
    protected int passed;

    public LevelProgress() {
        this.current = levelsStartsFrom1;
        this.passed = levelsStartsFrom1 - 1;
        this.total = 1;
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

    public void change(int level, int passed) {
        this.current = level;
        this.passed = passed;
    }

    public void change(int level) {
        this.current = level;
    }

    public static JSONObject goNext(JSONObject json) {
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
        // for LevelsType: 1..count-1 (training), count (contest)
        return current >= levelsStartsFrom1
                && passed >= levelsStartsFrom1 - 1
                && total > 0
                && current <= total
                && current <= passed + 1
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
