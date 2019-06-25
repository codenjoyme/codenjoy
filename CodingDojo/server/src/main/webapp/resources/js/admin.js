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
        showCities: false,
        showTechSkills: false,
        showUniversity: false,
        defaultGame: null,
        gameTypes: {
            icancode: ['Training', 'Contest', 'eKids', 'Befunge']
        }
    };
}

pages.admin = function() {
    var contextPath = game.contextPath = getSettings('contextPath');

    // ------------------------ registration settings -----------------------
    var settings = new AdminSettings(contextPath, 'general', 'registration');

    var loadRegSettings = function() {
        settings.load(function(data) {
            setRegSettings(data);
        });
    }

    var saveRegSettings = function() {
        settings.save(getRegSettings(),
            function() {
                loadRegSettings();
            }, function(errMsg) {
                console.log(errMsg);
            });
    }

    var getRegSettings = function() {
        var result = defaultRegistrationSettings();

        result.showGames = $('#show-games').prop('checked');
        result.showNames = $('#show-names').prop('checked');
        result.showTechSkills = $('#show-tech').prop('checked');
        result.showUniversity = $('#show-university').prop('checked');
        result.defaultGame = $('#default-game').find('option:selected').text();

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
        $('#show-tech').prop('checked', data.showTechSkills);
        $('#show-university').prop('checked', data.showUniversity);

        var select = $('#default-game');
        select.children().remove();

        var allTypes = defaultRegistrationSettings().gameTypes;
        for (var gameName in allTypes) {
            var gameTypes = allTypes[gameName];
            for (var index in gameTypes) {
                var gameType = gameTypes[index];
                select.append('<option value="' + gameType + '">' + gameType + '</option>');
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

        var buttons = $('[id^=save-user]');
        buttons.each(function(index, obj) {
            var button = $(obj);
            var index = button.attr('index');
            var preffix = '#players' + index + '\\.';

            button.click(function() {
                ajax.save({
                        readableName : $(preffix + 'readableName').val(),
                        name : $(preffix + 'name').val(),
                        score : $(preffix + 'score').val(),
                        callbackUrl : $(preffix + 'callbackUrl').val(),
                        data : $(preffix + 'data').val()
                    },
                    function() {
                        // do nothing
                    },
                    function(e) {
                        alert('error: ' + e);
                    });
            });
        });
    }

    // ------------------------ init ----------------------
    validatePlayerRegistration("#adminSettings");
    initHotkeys();
    loadRegSettings();
    setupSaveUserDetails();
}
