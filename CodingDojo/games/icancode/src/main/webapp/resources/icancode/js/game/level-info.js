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

var initLevelInfo = function(contextPath) {
    var settings = null;
    var count = 0;

    var splitMap = function(value) {
        var size = parseInt(Math.sqrt(value.length));
        return value.replace(new RegExp('(.{' + size + '})', 'g'), '$1\n')
    }

    var joinMap = function(value) {
        return value.replace(new RegExp('\n', 'g'), '');
    }

    var decode = function(value) {
        return value;
    }

    var encode = function(value) {
        return value;
    }

    var get = function(key) {
        for (var index in settings) {
            var option = settings[index];
            if (key == option.name) {
                return option;
            }
        }
    }

    var load = function(onLoad, onError) {
        var ajax = new AdminSettings(contextPath, 'icancode', '_settings_');
        ajax.load(function(data) {
            settings = data.parameters;
            count = get('levels.count').value;
            if (!!onLoad) {
                onLoad();
            }
        }, onError);
    }

    var saveParameter = function(name, value) {
        get(name).value = value;

        // TODO не городить столько запросов, а послать 1
        var ajax = new AdminSettings(contextPath, 'icancode', name);
        ajax.save(value);
    }

    var save = function(number, level) {
        saveParameter('level' + number + '.map', joinMap(level.map));
        saveParameter('level' + number + '.help', encode(level.help));
        saveParameter('level' + number + '.defaultCode', encode(level.defaultCode));
        saveParameter('level' + number + '.winCode', encode(level.winCode));
        saveParameter('level' + number + '.refactoringCode', encode(level.refactoringCode));
//        saveParameter('level' + number + '.autocomplete', JSON.stringify(level.autocomplete)); // TODO разобраться с этим
    }

    var getLevel = function(number) {
        if (number >= count) {
            return {
                'help':'<pre>// under construction</pre>',
                'defaultCode':'function program(robot) {\n'  +
                '    // TODO write your code here\n' +
                '}',
                'winCode':'function program(robot) {\n'  +
                '    robot.nextLevel();\n' +
                '}',
                'refactoringCode':'function program(robot) {\n'  +
                '    robot.nextLevel();\n' +
                '}'
            };
        }

        return {
            map :             splitMap(decode(get('level' + number + '.map').value)),
            help :            decode(get('level' + number + '.help').value),
            defaultCode :     decode(get('level' + number + '.defaultCode').value),
            winCode :         decode(get('level' + number + '.winCode').value),
            refactoringCode : decode(get('level' + number + '.refactoringCode').value),
//            autocomplete :    JSON.parse(get('level' + number + '.autocomplete').value) // TODO разобраться с этим
        };
    }

    return {
        save : save,
        load : load,
        getLevel : getLevel,
        getCount : function() {
            return count;
        }
    }
}