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

// так как уровни в настройках хрантся начиная с номера 1,
// а индексы у нормальных программистов с 0
// я пробовал нормально - путаница получается, лучше так, поверь
// эта константа тут, чтобы связать все места в коде (java + js),
// где считаем с 1 коммит, отвечающий за это так же
// можно посмортреть в git e26ec4f6
var levelsStartsFrom1 = 1

function initLevelProgress(game, onChangeLevel) {

    if (game.debug) {
        game.debugger();
    }

    var currentLevel = -1;
    var currentLevelIsContest = false;

    var progressBar = initProgressbar('progress-bar');
    progressBar.setProgress = function(current, lastPassed) {
        for (var i = levelsStartsFrom1; i <= progressBar.levelsCount; ++i) {
            this.notActive(i);
        }
        for (var i = levelsStartsFrom1; i <= lastPassed; ++i) {
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
        var url = '/rest/player/' + game.playerId + '/' + game.code + '/level/' + level;
        loadData(url, function(status) {
             // do nothing
        });
    }

    var scrollProgress = function () {
        $(".trainings").mCustomScrollbar("scrollTo", ".level-current");
    }

    $('body').bind("board-updated", function (events, data) {
        if (game.playerId == '' || !data[game.playerId]) {
            return;
        }

        $('body').trigger("tick");

        var board = data[game.playerId].board;

        var level = board.levelProgress.current;
        var countLevels = board.levelProgress.total;
        var lastPassed = board.levelProgress.lastPassed;
        var contest = (level == countLevels);

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
        currentLevelIsContest = contest;

        if (firstRun || progressBar.levelProgress.total != countLevels) {
            progressBar.countLevels(countLevels);
        }
        progressBar.setProgress(currentLevel, lastPassed);

        scrollProgress();

        fireOnChangeLevel(level, contest, lastPassed);
    });

    var oldLastPassed = -1;
    var oldLevel = -1;
    var fireOnChangeLevel = function(level, multiple, lastPassed) {
        var levelIncreased = oldLevel < level;
        if (levelIncreased) {
            oldLevel = level;
        }
        var lastPassedIncreased = oldLastPassed < lastPassed;
        var win = false;
        if (lastPassedIncreased) {
            var firstWin = (lastPassed == 0 && level == levelsStartsFrom1 && oldLastPassed == -1);
            win = (firstWin || oldLastPassed != -1);
            oldLastPassed = lastPassed;
        }
        if (!!onChangeLevel) {
            onChangeLevel(level, multiple, lastPassed, levelIncreased, win);
        }
    }

    return {
        getCurrentLevel: function() {
            return currentLevel;
        },
        isCurrentLevelMultiple: function() {
            return currentLevelIsContest;
        },
        selectLevel: function(level) {
            progressBar.element(level).click();
        }
    }
}