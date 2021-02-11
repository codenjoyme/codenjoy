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

function initProgressbar(container) {
    var progressBar = $('#' + container + ' li.training');
    progressBar.all = function(aClass) {
        for (var level = levelsStartsFrom1; level <= progressBar.length; level++) {
            progressBar.change(level, aClass);
        }
    }
    progressBar.clean = function(level) {
        progressBar.remove(level, "level-done");
        progressBar.remove(level, "level-current");
        progressBar.remove(level, "level-not-active");
        progressBar.remove(level, "level-during");
    }
    progressBar.notActive = function(level) {
        progressBar.change(level, "level-not-active");
    }
    progressBar.active = function(level) {
        progressBar.change(level, "level-current");
    }
    progressBar.process = function(level) {
        progressBar.change(level, "level-during");
    }
    progressBar.hide = function(level) {
        progressBar.set(level, "hidden");
    }
    progressBar.show = function(level) {
        progressBar.remove(level, "hidden");
    }
    progressBar.countLevels = function(count) {
        progressBar.levelsCount = count;
        for (var level = levelsStartsFrom1; level <= progressBar.length; level++) {
            progressBar.name(level, "Level " + level);
            if (level == count) {
                progressBar.name(level, "Contest");
            }
            if (level <= count) {
                progressBar.show(level);
            } else {
                progressBar.hide(level);
            }
        }
    }
    progressBar.element = function(level) {
        var index = level - levelsStartsFrom1;
        return $(progressBar[index]);
    }
    progressBar.done = function(level) {
        progressBar.change(level, "level-done");
    }
    progressBar.change = function(level, aClass) {
        progressBar.clean(level);
        progressBar.set(level, aClass);
    }
    progressBar.set = function(level, aClass) {
        progressBar.element(level).addClass(aClass);
    }
    progressBar.remove = function(level, aClass) {
        progressBar.element(level).removeClass(aClass);
    }
    progressBar.name = function(level, name) {
        progressBar.element(level).html(name);
    }

    progressBar.each(function(index) {
        var level = index + levelsStartsFrom1;
        progressBar.notActive(level);
        progressBar.hide(level);
    });

    $(".trainings").mCustomScrollbar({
        scrollButtons:{ enable: true },
        theme:"dark-2",
        axis: "x"
    });

    var initScrolling = function() {
        var width = 0;
        var currentWidth = 0;
        $(".training").each(function() {
            width += $(this).outerWidth();
        });
        $(".training.level-done").each(function() {
            currentWidth += $(this).outerWidth();
        });
        currentWidth += $(".training.level-current").outerWidth();

        if (currentWidth > width) {
            $(".trainings").animate({left: "50%"}, 1000, function(){
            });
        }

        $(".trainings-button.left").click(function(){
            $(".trainings").animate({right: "-=200"}, 1000, function(){
            });
        });

        $(".trainings-button.right").click(function(){
            if ($(".trainings").attr("style")) {
                if (parseFloat($(".trainings").attr("style").substring(7)) >= 0) {
                    return;
                }
            }
            $(".trainings").animate({right: "+=200"}, 1000, function(){
            });
        });
    }
    initScrolling();

    return progressBar;
};
