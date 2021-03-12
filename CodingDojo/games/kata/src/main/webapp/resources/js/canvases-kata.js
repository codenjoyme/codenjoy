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

var setup = setup || {};

var onlyBoard = window.location.href.includes("only=true");

var getQuestionCoordinate = function(x, y) {
    return {x:(onlyBoard ? x : 7), y:y + 1};
}

var getQuestionFormatted = function(value) {
    if (!!value.question) {
        var equals = (value.valid)?'==':'!=';
        var message = 'f(' + value.question + ') '
            + equals + ' ' + value.answer;
        return message;
    } else {
        var message = 'f(' + value + ') = ?';
        return message;
    }
}

function unescapeUnicode(unicode) {
    var r = /\\u([\d\w]{4})/gi;
    var temp = unicode.replace(r, function (match, grp) {
        return String.fromCharCode(parseInt(grp, 16));
    });
    return decodeURIComponent(temp).split("\\\"").join("\"");
}

setup.drawBoard = function(drawer) {
    drawer.clear();
    var centerX = (drawer.canvas.width() / drawer.canvas.plotSize())/2;

    var data = drawer.playerData.board;
    if (typeof setDescription != 'undefined') {
        setDescription(unescapeUnicode(data.description));
    }

    var isWaitNext = (data.questions.length == 0);
    if (isWaitNext) {
        drawer.drawText('Algorithm done! Wait next...',
            getQuestionCoordinate(centerX, 0), '#099');
        return;
    }

    var index = -1;
    var isNewLevel = (data.questions.length < data.history.length);
    if (!isNewLevel) {
        for (var key in data.history) {
            var value = data.history[key];
            if (value.question == data.nextQuestion) continue;

            drawer.drawText(getQuestionFormatted(value),
                getQuestionCoordinate(centerX, ++index),
                (value.valid)?'#090':'#900');
        }
    }

    drawer.drawText(getQuestionFormatted(data.nextQuestion),
        getQuestionCoordinate(centerX, ++index), '#099');
}