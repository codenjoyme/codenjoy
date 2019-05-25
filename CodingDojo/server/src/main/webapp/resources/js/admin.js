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

pages.admin = function() {
    var contextPath = game.contextPath = getSettings('contextPath');

    // ------------------------ general settings -----------------------
    var generalInfo = [];
    var general = new AdminSettings(contextPath, 'general');

    var loadGeneral = function() {
        general.load(function(data) {
            loadGeneralData(data);
        });
    }

    var saveGeneral = function() {
        general.save(generalInfo,
            function() {
                loadGeneral();
            }, function(errMsg) {
                console.log(errMsg);
            });
    }

    // ------------------------ collected general data ----------------------
    var updateGeneralData = function() {
        var updated = {
            showGamesOnRegistration : $('#show-games-on-registration').prop('checked'),
            showNamesOnRegistration : $('#show-names-on-registration').prop('checked'),
            showTechSkillsOnRegistration : $('#show-tech-on-registration').prop('checked'),
            showUniversityOnRegistration : $('#show-university-on-registration').prop('checked'),
            defaultGameOnRegistration : $('#default-game-on-registration').find('option:selected').text()
        };

        generalInfo = updated;
    }

    var loadGeneralData = function(data) {
        if (!!data) {
            generalInfo = data;
        }

        $('#show-games-on-registration').prop('checked', generalInfo.showGamesOnRegistration);
        $('#show-names-on-registration').prop('checked', generalInfo.showNamesOnRegistration);
        $('#show-tech-on-registration').prop('checked', generalInfo.showTechSkillsOnRegistration);
        $('#show-university-on-registration').prop('checked', generalInfo.showUniversityOnRegistration);
        $('#default-game-on-registration').val(generalInfo.defaultGameOnRegistration);
    }

    var generalSaveButton = $('#general-save-button');
    generalSaveButton.click(function() {
        updateGeneralData();
        saveGeneral();
    });

    // ------------------------ init ----------------------
    validatePlayerRegistration("#adminSettings");
    initHotkeys();
    loadGeneral();
}
