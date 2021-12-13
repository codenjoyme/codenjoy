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

pages = pages || {};

function defaultRegistrationSettings() {
    return {
        showNames: true,
        showData1: true,
        showData2: true,
        showData3: true,
        showData4: true,
        showGameModes: true,
        defaultGameMode: null,
        gameModes: {
            icancode: [   // TODO вынести это как-то в настройки игры icancode
                {'name' : 'JavaScript', 'title':'JavaScript (browser version)'},
                {'name' : 'Contest',    'title':'Java | Kotlin | .Net | JavaScript'},
                {'name' : 'Befunge',    'title':'Befunge (esoteric language)'},
                {'name' : 'eKids',      'title':'For Kids'}
            ]
        }
    };
}

pages.admin = function() {
    var contextPath = setup.contextPath = getSettings('contextPath');
    setup.game = getSettings('game');
    setup.room = getSettings('room');

    // ------------------------ registration settings -----------------------
    var settings = new AdminSettings(contextPath, 'general', 'registration');

    var loadRegSettings = function() {
        settings.load(
            function(data) {
                setRegSettings(data);
            }, function(error) {
                setRegSettings(null);
            });
    }

    var saveRegSettings = function() {
        settings.save(getRegSettings(),
            function() {
                loadRegSettings();
            }, function(error) {
                // do nothing
            });
    }

    var getRegSettings = function() {
        var result = defaultRegistrationSettings();

        result.showGameModes = $('#show-game-modes').prop('checked');
        result.showNames = $('#show-names').prop('checked');
        result.showData1 = $('#show-data1').prop('checked');
        result.showData2 = $('#show-data2').prop('checked');
        result.showData3 = $('#show-data3').prop('checked');
        result.showData4 = $('#show-data4').prop('checked');
        result.defaultGameMode = $('#default-game-mode').find('option:selected').val();

        return result;
    }

    var setRegSettings = function(data) {
        if ($.isEmptyObject(data)) {
            data = defaultRegistrationSettings();
        }
        if (!data.defaultGameMode) {
            data.defaultGameMode = $('#default-game-mode option:first').val();
        }

        $('#show-game-modes').prop('checked', data.showGameModes);
        $('#show-names').prop('checked', data.showNames);
        $('#show-data1').prop('checked', data.showData1);
        $('#show-data2').prop('checked', data.showData2);
        $('#show-data3').prop('checked', data.showData3);
        $('#show-data4').prop('checked', data.showData4);

        var select = $('#default-game-mode');
        select.children().remove();

        var allModes = defaultRegistrationSettings().gameModes;
        for (var game in allModes) {
            var gameModes = allModes[game];
            for (var index in gameModes) {
                var name = gameModes[index].name;
                var title = gameModes[index].title;
                select.append('<option value="' + name + '">' + title + '</option>');
            }
        }

        select.val(data.defaultGameMode);
    }

    $('#registration-save-button').click(function() {
        saveRegSettings();
    });

    // ------------------------ save user details ----------------------

    var setupSaveUserDetails = function() {
        var ajax = new AdminAjax(contextPath, 'admin/player');
        var PLAYER_ID = 'id';

        var elements = $('[id$=\\.' + PLAYER_ID + ']');
        elements.each(function(index, obj) {
            var element = $(obj);
            var index = element.attr('index');
            var playerId = element.val();
            var prefix = '#players' + index + '\\.';

            var setup = function(field) {
                var input = $(prefix + field);
                input.on('input', function() {
                    if (!!input.data('button')) return;
                    var test = $('<button type="button">Save</button>').click(function () {
                        var data = {};
                        data[PLAYER_ID] = playerId;
                        data['game'] = setup.game;
                        data[field] = input.val();
                        ajax.save(data,
                            function() {
                                input.data('button', null);
                                test.remove();
                            },
                            function(e) {
                                alert('error: ' + e);
                            });
                    });
                    input.after(test);
                    input.data('button', test);
                });
            };

            // setup(PLAYER_ID); // readonly
            // setup('code');    // readonly
            setup('readableName');
            setup('email');
            setup('room');
            setup('teamId');
            setup('score');
            setup('callbackUrl');
            setup('data');
        });
    }

    var setupSpanHref = function() {
        $('span.a').click(function() {
            var url = $(this).attr('href');
            $.get(url);
        });
    }

    var setupLevelsMapArea = function(elements, init) {
        var resize = function(){
            this.style.height = 'auto';
            this.style.height = this.scrollHeight + 'px';
        }

        elements.each(function(){
            if (!init) return;
            resize.call(this);
        }).on('input', resize);
    }

    var getLargestLevelKey = function() {
        var result = [];
        $('#levels input[with="key"]').each(function(){
            var value = $(this).val();
            if (!!value) {
                result.push(value.replace('[Level] Map[', '').replace(']', ''));
            }
        });
        if (result.length == 0) {
            return "0";
        }

        result.sort();
        return result.pop();
    }

    var setupLevelsNewMapButton = function() {
        $('#addNewLevelMap').click(function(){
            var last = $('#levels tr[index]').last();
            var lastIndex = parseInt(last.attr('index'));
            var lastKey = getLargestLevelKey().split(',');
            if (lastKey.length == 1) {
                var levelNumber = parseInt(lastKey[0]) + 1;
                var newKey = levelNumber;
            } else if (lastKey.length == 2) {
                var levelNumber = parseInt(lastKey[0]);
                var mapNumber = parseInt(lastKey[1]) + 1;
                var newKey = levelNumber + ',' + mapNumber;
            } else {
                throw 'Parsing error';
            }
            var newIndex = lastIndex + 1;
            var newMapSettings = $('#levels script')
                .tmpl([{
                    index : newIndex,
                    key : newKey
                }]);
            // TODO почему-то это не работает после вставки элемента
            //      scrollHeight = высоте контрола, а не тому что в нем
            //      реально содержится
            setupLevelsMapArea(newMapSettings);
            newMapSettings.insertBefore('#levels .levelsButtons');
        });
    }

    // ------------------------ init ----------------------
    validatePlayerRegistration('#adminSettings');
    initHotkeys();
    loadRegSettings();
    setupSaveUserDetails();
    setupSpanHref();
    setupLevelsMapArea($('textarea.map'), false);
    setupLevelsMapArea($('textarea.loggers'), true);
    setupLevelsMapArea($('textarea.version'), true);
    setupLevelsNewMapButton();
}
