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
function initButtons(onCommitClick, onResetClick, onHelpClick) {
    var resetButton = $('#ide-reset');
    var commitButton = $('#ide-commit');
    var helpButton = $("#ide-help");

    var enable = function(button, enable) {
        button.prop('disabled', !enable);
    }

    var enableAll = function() {
        enable(resetButton, true);
        commitButton.text('Commit');
        enable(commitButton, true);
    }

    var disableAll = function() {
        enable(resetButton, false);
        enable(commitButton, false);
    }

    resetButton.click(onResetClick);
    commitButton.click(onCommitClick);
    helpButton.click(onHelpClick);

    return {
        disableAll : disableAll,
        enableAll : enableAll,
        error : function() {
            commitButton.text('Error');
            enable(commitButton, false);
        },
        enableReset : function() {
            enable(resetButton, true);
        },
        disableHelp : function() {
            enable(helpButton, false);
        }
    }
}