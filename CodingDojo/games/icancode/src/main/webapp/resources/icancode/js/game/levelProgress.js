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
function initLevelProgress(game, socket, onUpdate, onChangeLevel) {

    if (game.debug) {
        game.debugger();
    }

    var currentLevel = -1;
    var currentLevelIsMultiple = false;

    var progressBar = initProgressbar('progress-bar');
    progressBar.setProgress = function(current, lastPassed) {
        for (var i = 0; i < progressBar.levelsCount; ++i) {
            this.notActive(i);
        }
        for (var i = 0; i <= lastPassed; ++i) {
            this.done(i);
        }
        this.process(lastPassed + 1);
        this.active(current);
    }
    progressBar.click(function (event) {
        if (!game.code) {
            return;
        }

        var element = $(event.target);

        if (element.hasClass('level-not-active')) {
            return;
        }

        var level = element.attr('level');
        if (currentLevel == level) {
            return;
        }

        changeLevel(level);
    });

    var changeLevel = function(level) {
        var url = '/rest/player/' + game.playerName + '/' + game.code + '/level/' + level;
        loadData(url, function(status) {
             // do nothing
        });
    }

    var scrollProgress = function () {
        $(".trainings").mCustomScrollbar("scrollTo", ".level-current");
    }

    $('body').bind("board-updated", function (events, data) {
        if (game.playerName == '' || !data[game.playerName]) {
            return;
        }

        $('body').trigger("tick");

        var board = data[game.playerName].board;

        var level = board.levelProgress.current;
        var countLevels = board.levelProgress.total;
        var lastPassed = board.levelProgress.lastPassed;
        var multiple = (level >= countLevels);

        onUpdate(level, multiple, lastPassed);

        var firstRun = !progressBar.levelProgress;
        if (!firstRun &&
            progressBar.levelProgress.current == level &&
            progressBar.levelProgress.total == countLevels &&
            progressBar.levelProgress.lastPassed == lastPassed)
        {
            return;
        }
        progressBar.levelProgress = board.levelProgress;
        currentLevel = level;
        currentLevelIsMultiple = multiple;

        if (firstRun || progressBar.levelProgress.total != countLevels) {
            progressBar.countLevels(countLevels);
        }
        progressBar.setProgress(currentLevel, lastPassed);

        scrollProgress();

        onChangeLevel(level);
    });

    return {
        getCurrentLevel: function () {
            return currentLevel + 1;
        },
        isCurrentLevelMultiple: function () {
            return currentLevelIsMultiple;
        },
        selectLevel: function (level) {
            $(progressBar[level - 1]).click();
        }
    }
}