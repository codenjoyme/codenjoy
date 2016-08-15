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
        progressBar.select(level - 1);
        loadData();
    });

    // ------------------------ communicate with server -----------------------
    var levelsInfo = [];
    var url = '/' + contextPath + '/settings/icancode/levels';
    var load = function() {
        $.get(url, null,
            function (data) {
                loadData(data);
            }, 'json');
    }

    var save = function() {
        $.ajax({
            type: "POST",
            url: url,
            data: JSON.stringify(levelsInfo),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                load();
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    // ------------------------ collection data ----------------------
    var updateData = function() {
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

    var loadData = function(data) {
        if (!!data) {
            levelsInfo = data;
        }
        var info = levelsInfo[progressBar.selected];
        if (!!info) {
            info = {
                init:'',
                win:'',
                refactored:'',
                help:'',
                map:''
            }
        }

        defaultEditor.setValue(info.init);
        winEditor.setValue(info.win);
        refactoredEditor.setValue(info.refactored);
        helpEditor.setValue(info.help);
        mapEditor.setValue(info.map);
    }

    var saveButton = $('#save-button');
    saveButton.click(function() {
        updateData();
        save();
    });

    // --------------------- starting -------------------------
    progressBar.select(0);
    load();

};
