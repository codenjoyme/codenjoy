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
        showGames: true,
        showNames: true,
        showData1: true,
        showData2: true,
        showData3: true,
        showData4: true,
        defaultGame: null,
        gameTypes: {
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
    var contextPath = game.contextPath = getSettings('contextPath');

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

        result.showGames = $('#show-games').prop('checked');
        result.showNames = $('#show-names').prop('checked');
        result.showData1 = $('#show-data1').prop('checked');
        result.showData2 = $('#show-data2').prop('checked');
        result.showData3 = $('#show-data3').prop('checked');
        result.showData4 = $('#show-data4').prop('checked');
        result.defaultGame = $('#default-game').find('option:selected').val();

        return result;
    }

    var setRegSettings = function(data) {
        if ($.isEmptyObject(data)) {
            data = defaultRegistrationSettings();
        }
        if (!data.defaultGame) {
            data.defaultGame = $("#default-game option:first").val();
        }

        $('#show-games').prop('checked', data.showGames);
        $('#show-names').prop('checked', data.showNames);
        $('#show-data1').prop('checked', data.showData1);
        $('#show-data2').prop('checked', data.showData2);
        $('#show-data3').prop('checked', data.showData3);
        $('#show-data4').prop('checked', data.showData4);

        var select = $('#default-game');
        select.children().remove();

        var allTypes = defaultRegistrationSettings().gameTypes;
        for (var gameName in allTypes) {
            var gameTypes = allTypes[gameName];
            for (var index in gameTypes) {
                var name = gameTypes[index].name;
                var title = gameTypes[index].title;
                select.append('<option value="' + name + '">' + title + '</option>');
            }
        }

        select.val(data.defaultGame);
    }

    $('#registration-save-button').click(function() {
        saveRegSettings();
    });

    // ------------------------ save user details ----------------------

    var setupSaveUserDetails = function() {
        var ajax = new AdminAjax(contextPath, 'admin/user/info');
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

            setup('readableName');
            setup(PLAYER_ID);
            setup('roomName');
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

    // ------------------------ init ----------------------
    validatePlayerRegistration("#adminSettings");
    initHotkeys();
    loadRegSettings();
    setupSaveUserDetails();
    setupSpanHref();
}
