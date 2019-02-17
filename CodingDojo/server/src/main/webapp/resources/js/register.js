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
    game.contextPath = getSettings('contextPath');
    game.waitApprove = getSettings('waitApprove');

    initRegistration(game.waitApprove, game.contextPath);

    initHotkeys();
}

function initRegistration(waitApprove, contextPath) {
    var disable = function(status) {
        $("#submit").prop("disabled", status);
        $("#name").prop("disabled", status);
        $("#readable-name").prop("disabled", status);
        $("#password").prop("disabled", status);
        $("#gameName").prop("disabled", status)
    }

    $(document).ready(function() {
        validatePlayerRegistration("#player");
        if ($("#name").val() != "") {
            $("#submit").val("Login");
            $("#title").text("Login");
        }
        if (waitApprove) {
            disable(true);
            $.ajax({ url:contextPath + '/register?approved=' + $("#name").val(),
                cache:false,
                complete:function(data) {
                    window.location.replace(data.responseText);
                },
                timeout:1000000
            });
        } else {
            if ($("#name").val() == "") {
                $("#name").focus();
            } else {
                $("#password").focus();
            }
        }
        $("#player").submit(function() {
            $("#password").val($.md5($("#password").val()));
        });
    });
}
