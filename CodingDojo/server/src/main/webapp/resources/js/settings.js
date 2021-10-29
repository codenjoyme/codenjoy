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

pages = pages || {};

pages.register = function() {
    setup.contextPath = getSettings('contextPath');
    setup.waitApprove = getSettings('waitApprove');

    initRegistration(setup.waitApprove, setup.contextPath);

    initHotkeys();
}

function initRegistration(waitApprove, contextPath) {
    window.alert("opa zdravei");
    var disable = function(status) {
        $("#submit").prop("disabled", status);
        $("#feedback").prop("disabled", status);
    }

    function display(element, isVisible) {
        element = $(element);
        if (isVisible) {
            element.removeAttr('hidden');
            element.show();
        } else {
            element.attr('hidden', 'hidden');
            element.hide();
        }
    }

    function loadFeedbackPage() {
        var configurable = function (name) {
                if (!$('#' + name).length) {
                    return;
                }
                checkEls[name] = function (value) {
                if ($('#' + name)[0].hasAttribute('hidden')) {
                    return false;
                }
                if ($('#' + name)[0].hasAttribute('not-empty')) {
                    return notEmpty(value);
                }
                return false;
            };
        };

        configurable('feedback');

        var submitForm = function () {
            if ($('form .not-valid').length == 0) {
                $('#data input').val();
                $('#form').submit();
            }
        };

        $('#submit-button').click(submitForm);
        $('#feedback').keypress(function (e) {
            var code = (e.keyCode ? e.keyCode : e.which);
            if (code == 13) {
                submitForm();
                e.preventDefault();
            }
        });
    }
}
function click(){
    window.alert("here");
    $('#submit-button').click(submitForm);
}

function subOrUnsub() {
    var x = document.getElementById('form');
    if (x.hidden) {
        x.hidden = false;
    } else {
        x.hidden = true;
    }
}

