/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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
/**
 * Created by Mikhail_Udalyi on 09.08.2016.
 */

var autocompleteMaps = {};
var autocomplete = {
    getCompletions: function(editor, session, pos, prefix, callback) {
        var line = editor.session.getLine(pos.row);
        var isFind = false;

        for(var index in autocompleteMaps) {
            var startFindIndex = pos.column - index.length;

            var part = line.substring(startFindIndex, pos.column);
            if (startFindIndex >= 0 && part == index) {
                isFind = true;
                break;
            }
        }

        if (!isFind) {
            return;
        }

        callback(null, autocompleteMaps[index].map(function(word) {
            return {
                caption: word,
                value: word,
                meta: "game",
                score: 1000
            };
        }));

    }
}

debugger;

var initAutocomplete = function(level, levelInfo) {
    autocompleteMaps = {};

    for (var levelIndex = 0; levelIndex <= level; levelIndex++) {
        if (!levelInfo.getInfo) {
            continue;
        }

        var data = levelInfo.getInfo(levelIndex + 1).autocomplete;

        if (!data) {
            continue;
        }

        for(var template in data) {
            if (!data.hasOwnProperty(template)) {
                continue;
            }

            var values = data[template].values.slice(0);
            if (autocompleteMaps.hasOwnProperty(template)) {
                autocompleteMaps[template] = autocompleteMaps[template].concat(values);
            } else {
                autocompleteMaps[template] = values;
            }

            for(var index = 0; index < data[template].synonyms.length; index++) {
                var synonym = data[template].synonyms[index];
                autocompleteMaps[synonym] = autocompleteMaps[template];
            }
        }
    }
};