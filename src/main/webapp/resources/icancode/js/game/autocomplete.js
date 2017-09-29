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

            if (startFindIndex >= 0 && line.substring(startFindIndex, pos.column) == index) {
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

var initAutocomplete = function(level, levelInfo) {
    autocompleteMaps = {};

    for (var levelIndex = 0; levelIndex <= level; levelIndex++) {
        if (!levelInfo.getInfo || !levelInfo.getInfo(levelIndex).hasOwnProperty('autocomplete')) {
            continue;
        }

        var data = levelInfo.getInfo(levelIndex).autocomplete;

        for(var index in data) {
            if (!data.hasOwnProperty(index)) {
                continue;
            }

            if (autocompleteMaps.hasOwnProperty(index)) {
                autocompleteMaps[index] = autocompleteMaps[index].concat(data[index].values);
            } else {
                autocompleteMaps[index] = data[index].values;
            }

            for(var isynonym = 0; isynonym < data[index].synonyms.length; isynonym++) {
                autocompleteMaps[data[index].synonyms[isynonym]] = autocompleteMaps[index];
            }
        }
    }
};