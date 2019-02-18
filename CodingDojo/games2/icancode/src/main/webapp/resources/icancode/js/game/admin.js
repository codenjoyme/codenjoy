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

function initAdmin(contextPath) {
    var libs = 'js';

    // ----------------------- init ace editors -------------------

    var defaultEditor = initEditor(libs, 'default');
    var winEditor = initEditor(libs, 'win');
    var refactoredEditor = initEditor(libs, 'refactored');
    var helpEditor = initEditor(libs, 'help');
    var mapEditor = initEditor(libs, 'map');

    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        defaultEditor.resize(true);
        winEditor.resize(true);
        refactoredEditor.resize(true);
        helpEditor.resize(true);
        mapEditor.resize(true);
    })

    // ----------------------- init scrollbar ----------------------
//    $('.tab-pane').mCustomScrollbar({
//        theme:'dark-2',
//        axis: 'yx',
//        autoDraggerLength: true
//    });

    // ----------------------- init progressbar -------------------
    var progressBar = initProgressbar('progress-bar');
    progressBar.select = function(level) {
        progressBar.selected = level;
        this.all('level-done');
        this.active(level);
    }
    progressBar.click(function(event) {
        var element = $(event.target);

        if (element.hasClass('level-not-active')) {
            return;
        }

        var level = element.attr('level');
        updateLevelsData();
        progressBar.select(level - 1);
        loadLevelsData();
    });

    // ------------------------ communicate with server -----------------------
    // ------------------------ levels settings -----------------------
    var levelsInfo = [];
    var levels = new AdminSettings(contextPath, 'levels');

    var loadLevels = function() {
        levels.load(function(data) {
            loadLevelsData(data);
        });
    }

    var saveLevels = function() {
        levels.save(levelsInfo,
            function() {
                loadLevels();
            }, function(errMsg) {
                console.log(errMsg);
            });
    }

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

    // ------------------------ collected levels data ----------------------
    var updateLevelsData = function() {
        var updated = {
            level : progressBar.selected + 1,
            init : defaultEditor.getValue(),
            win : winEditor.getValue(),
            refactored : refactoredEditor.getValue(),
            help : helpEditor.getValue(),
            map : mapEditor.getValue()
        };

        levelsInfo[progressBar.selected] = updated;
    }

    var loadLevelsData = function(data) {
        if (!!data) {
            levelsInfo = data;
        }
        var info = levelsInfo[progressBar.selected] || {init:'', win:'', refactored:'', help:'', map:''};

        defaultEditor.setValue(info.init);
        winEditor.setValue(info.win);
        refactoredEditor.setValue(info.refactored);
        helpEditor.setValue(info.help);
        mapEditor.setValue(info.map);
    }

    var levelsSaveButton = $('#levels-save-button');
    levelsSaveButton.click(function() {
        updateLevelsData();
        saveLevels();
    });

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

    // --------------------- starting -------------------------
    progressBar.select(0);
    loadLevels();
    loadGeneral();
};
