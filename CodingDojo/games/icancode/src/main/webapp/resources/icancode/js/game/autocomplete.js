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
/**
 * Created by Mikhail_Udalyi on 09.08.2016.
 */

var autocompleteMaps = {};

String.prototype.ltrim = function() {
	return this.replace(/^\s+/,"");
}
String.prototype.rtrim = function() {
	return this.replace(/\s+$/,"");
}
String.prototype.isAlphanumeric = function(){
    return /^[0-9a-zA-Z]+$/.test(this);
}

var autocomplete = {
    getCompletions: function(editor, session, pos, prefix, callback) {
        var line = editor.session.getLine(pos.row).substring(0, pos.column).trim();

        var found = [];
        for(var template in autocompleteMaps) {
            var templateTrim = template.trim();

            if (line.indexOf(templateTrim) == -1) {
                continue;
            }

            var between = line.substring(line.indexOf(templateTrim) + templateTrim.length);

            if (!!between && !between.isAlphanumeric()) {
                continue;
            }

            found.push(template);
        }


        if (found.length == 0) {
            return;
        }

        var result = '';
        for (var index in found) {
            var template = found[index];
            if (template.length > result.length) {
                result = template;
            }
        }

        if (!result) {
            return;
        }

        callback(null, autocompleteMaps[result].map(function(word) {
            return {
                caption: word,
                value: word,
                meta: "game",
                score: 1000
            };
        }));

    }
}

var initAutocomplete = function(current, levelInfo) {
    autocompleteMaps = {};

    for (var level = levelsStartsFrom1; level <= current; level++) {
        if (!levelInfo.getLevel) {
            continue;
        }

        var data = levelInfo.getLevel(level).autocomplete;

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