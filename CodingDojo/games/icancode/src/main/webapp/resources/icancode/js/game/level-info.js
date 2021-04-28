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
            count = get('[Game] Levels count').value;
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

    var save = function(level, data) {
        var prefix = 'Level' + level + ' ';
        saveParameter(prefix + 'map', encode(data.map));
        saveParameter(prefix + 'help', encode(data.help));
        saveParameter(prefix + 'default code', encode(data.defaultCode));
        saveParameter(prefix + 'win code', encode(data.winCode));
        saveParameter(prefix + 'refactoring code', encode(data.refactoringCode));
        saveParameter(prefix + 'befunge commands', encode(data.befungeCommands));
//        saveParameter(prefix + 'autocomplete', JSON.stringify(data.autocomplete)); // TODO разобраться с этим
    }

    var getLevel = function(level) {
        if (level > count) {
            return {
                map:'',
                help:'<pre>// under construction</pre>',
                defaultCode:'function program(robot) {\n'  +
                '    // TODO write your code here\n' +
                '}',
                winCode:'function program(robot) {\n'  +
                '    robot.nextLevel();\n' +
                '}',
                refactoringCode:'function program(robot) {\n'  +
                '    robot.nextLevel();\n' +
                '}',
                autocomplete:'{}',
                befungeCommands:''
            };
        }

        var prefix = 'Level' + level + ' ';
        return {
            map :             decode(get(prefix + 'map').value),
            help :            decode(get(prefix + 'help').value),
            defaultCode :     decode(get(prefix + 'default code').value),
            winCode :         decode(get(prefix + 'win code').value),
            refactoringCode : decode(get(prefix + 'refactoring code').value),
            befungeCommands : decode(get(prefix + 'befunge commands').value),
//            autocomplete :    JSON.parse(get(prefix + 'autocomplete').value) // TODO разобраться с этим
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