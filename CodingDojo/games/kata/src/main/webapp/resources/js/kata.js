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

game.enableJoystick = false;

var getQuestionCoordinate = function(index) {
    return {x:7, y:index + 1};
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

var description = null;
var setDescription = function(text) {
    description = text;
}

var showDescriptionOnClick = function() {
    if (!game.registered) {
        return;
    }
    var container = "#div_" + game.playerId;
    $(container + " #player_name").click(function(){
        if (!!description) {
            alert(description.replace(/\\n/g, "\n"));
        }
    });
}

game.onBoardPageLoad = function() {
    showDescriptionOnClick();
}

game.onBoardAllPageLoad = function() {
    showDescriptionOnClick();
}

game.drawBoard = function(drawer) {
    drawer.clear();

    var data = drawer.playerData.board;
    setDescription(unescapeUnicode(data.description));

    var isWaitNext = (data.questions.length == 0);
    if (isWaitNext) {
        drawer.drawText('Algorithm done! Wait next...',
                        getQuestionCoordinate(0), '#099');
        return;
    }

    var index = -1;
    var isNewLevel = (data.questions.length < data.history.length);
    if (!isNewLevel) {
        for (var key in data.history) {
            var value = data.history[key];
            if (value.question == data.nextQuestion) continue;

            drawer.drawText(getQuestionFormatted(value),
                    getQuestionCoordinate(++index),
                    (value.valid)?'#090':'#900');
        }
    }

    drawer.drawText(getQuestionFormatted(data.nextQuestion),
                getQuestionCoordinate(++index), '#099');
}
