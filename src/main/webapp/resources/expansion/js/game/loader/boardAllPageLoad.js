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
/**
 * Created by Mikhail_Udalyi on 09.08.2016.
 */

var boardAllPageLoad = function() {

    // https://github.com/uxitten/polyfill/blob/master/string.polyfill.js
    // https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/padStart
    if (!String.prototype.padStart) {
        String.prototype.padStart = function padStart(targetLength,padString) {
            targetLength = targetLength>>0; //floor if number or convert non-number to 0;
            padString = String(padString || ' ');
            if (this.length > targetLength) {
                return String(this);
            }
            else {
                targetLength = targetLength-this.length;
                if (targetLength > padString.length) {
                    padString += padString.repeat(targetLength/padString.length); //append to original to ensure we are longer than needed
                }
                return padString.slice(0,targetLength) + String(this);
            }
        };
    }

    // https://stackoverflow.com/a/17606289
    String.prototype.replaceAll = function(search, replacement) {
        var target = this;
        return target.split(search).join(replacement);
    };

    initLeadersTable(game.contextPath, game.playerName, game.code,
        function() {
        },
        function(count, you, link, name, score, maxLength, level) {
            var star = '';
            if (count == 1) {
                star = 'first';
            } else if (count <= 3) {
                star = 'second';
            }

            var rounds = score.rounds.reverse().join('&nbsp;');
            var score = ("" + score.score).padStart(5, ' ').replaceAll(' ', '&nbsp;');

            return '<tr>' +
                '<td><span class="' + star + ' star">' + count + '<span></td>' +
                '<td>' + you + '<a href="' + link + '">' + name + '</a></td>' +
                '<td class="left">' + score + '&nbsp;' +
                    '<span class="small">(' + rounds + ')</span></td>' +
                '</tr>';
        },
        function(scoreJson) {
            return scoreJson.score;
        });
    $('#table-logs').removeClass('table');
    $('#table-logs').removeClass('table-striped');
    $(document.body).show();
}