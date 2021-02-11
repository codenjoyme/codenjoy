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

function initAdmin(contextPath) {
    var libs = 'js';

    // ----------------------- init ace editors -------------------

    var mapEditor = initEditor(libs, 'map');
    mapEditor.setShowInvisibles(true);
    var helpEditor = initEditor(libs, 'help');
    var defaultEditor = initEditor(libs, 'default');
    var winEditor = initEditor(libs, 'win');
    var refactoredEditor = initEditor(libs, 'refactored');
    var befungeCommandsEditor = initEditor(libs, 'befungeCommands');

    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        mapEditor.resize(true);
        helpEditor.resize(true);
        defaultEditor.resize(true);
        winEditor.resize(true);
        refactoredEditor.resize(true);
        befungeCommandsEditor.resize(true);
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
        progressBar.selected = parseInt(level);
        this.all('level-done');
        this.active(level);
    }
    progressBar.click(function(event) {
        var element = $(event.target);

        if (element.hasClass('level-not-active')) {
            return;
        }

        var level = element.attr('level');
        saveLevel();
        progressBar.loadLevel(level);
    });
    progressBar.loadLevel = function(level) {
        progressBar.select(level);
        loadLevel();
    }

    // ------------------------ levels settings -----------------------
    var levelInfo = initLevelInfo(contextPath);

    var saveLevel = function() {
        var index = progressBar.selected;
        var current = levelInfo.getLevel(index);
        var updated = {
            map :             mapEditor.getValue(),
            help :            helpEditor.getValue(),
            defaultCode :     defaultEditor.getValue(),
            winCode :         winEditor.getValue(),
            refactoringCode : refactoredEditor.getValue(),
            befungeCommands : befungeCommandsEditor.getValue(),
            autocomplete :    current.autocomplete // TODO научиться редактировать
        };
        levelInfo.save(index, updated);
    }

    var setEditorValue = function(editor, value) {
        editor.setValue(value);
        editor.selection.clearSelection();
    }

    var loadLevel = function() {
        var level = levelInfo.getLevel(progressBar.selected);

        setEditorValue(mapEditor, level.map);
        setEditorValue(helpEditor, level.help);
        setEditorValue(defaultEditor, level.defaultCode);
        setEditorValue(winEditor, level.winCode);
        setEditorValue(refactoredEditor, level.refactoringCode);
        setEditorValue(befungeCommandsEditor, level.befungeCommands);
        // autocomplete.setValue(level.autocomplete); // TODO научиться редактировать
    }

    var saveButton = $('#levels-save-button');
    saveButton.click(function() {
        saveLevel();
    });

    // --------------------- starting -------------------------
    levelInfo.load(function() {
        progressBar.countLevels(levelInfo.getCount());
        progressBar.loadLevel(0);
    });
};
