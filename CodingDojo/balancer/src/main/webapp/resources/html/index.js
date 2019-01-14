/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

var error = function(partOfId, data) {
    $('#' + partOfId + '-result').val('');
    $('#' + partOfId + '-error').val(data.status + ' ' + data.responseText);
}

var result = function(partOfId, data) {
    $('#' + partOfId + '-result').val(JSON.stringify(data));
    $('#' + partOfId + '-error').val('');
}

var server = function(name) {
    return $('#' + name + '-server').val().replace('THIS_SERVER', window.location.host)
}

var registerUser = function(email, firstName,
                            lastName, password,
                            city, skills, comment)
{
    $.ajax({
        type: 'POST',
        url: server('balancer') + '/register',
        dataType: 'json',
        async: false,
        contentType: 'application/json; charset=utf-8',
        data: '{"email": "' + email + '", ' +
            '"firstName" : "' + firstName + '", ' +
            '"lastName" : "' + lastName + '", ' +
            '"password" : "' + password + '", ' +
            '"city" : "' + city + '", ' +
            '"skills" : "' + skills + '", ' +
            '"comment" : "' + comment + '"}',
        success: function(data) {
            result('register', data);
        },
        error : function(data) {
            error('register', data);
        }
    })
};

var loginUser = function(email, password) {
    $.ajax({
        type: 'POST',
        url: server('balancer') + '/login',
        dataType: 'json',
        async: false,
        contentType: 'application/json; charset=utf-8',
        data: '{"email": "' + email + '", ' +
            '"password" : "' + password + '"}',
        success: function(data) {
            result('login', data);
        },
        error : function(data) {
            error('login', data);
        }
    })
};

var getScores = function(day) {
    $.ajax({
        type: 'GET',
        url: server('balancer') + '/score/day/' + day,
        dataType: 'json',
        async: false,
        success: function(data) {
            result('scores', data);
        },
        error : function(data) {
            error('scores', data);
        }
    })
};

var removeUser = function(email, adminPassword) {
    $.ajax({
        type: 'GET',
        url: server('balancer') + '/remove/' + email + '/' + adminPassword,
        dataType: 'json',
        async: false,
        success: function(data) {
            result('remove', data);
        },
        error : function(data) {
            error('remove', data);
        }
    })
};

var getUsersOnGameServer = function() {
    $.ajax({
        type: 'GET',
        url: server('game') + '/game/snakebattle/players',
        dataType: 'json',
        async: false,
        success: function(data) {
            result('users-game', data);
        },
        error : function(data) {
            error('users-game', data);
        }
    })
};

var getUsersOnBalancerServer = function(adminPassword) {
    $.ajax({
        type: 'GET',
        url: server('balancer') + '/players/' + adminPassword,
        dataType: 'json',
        async: false,
        success: function(data) {
            result('users-balancer', data);
        },
        error : function(data) {
            error('users-balancer', data);
        }
    })
};

var getSettings = function(adminPassword) {
    $.ajax({
        type: 'GET',
        url: server('balancer') + '/settings/' + adminPassword,
        dataType: 'json',
        async: false,
        success: function(data) {
            result('settings', data);
        },
        error : function(data) {
            error('settings', data);
        }
    })
};

var setSettings = function(settings, adminPassword) {
    $.ajax({
        type: 'POST',
        url: server('balancer') + '/settings/' + adminPassword,
        dataType: 'json',
        async: false,
        contentType: 'application/json; charset=utf-8',
        data: settings,
        success: function(data) {
            result('settings', data);
        },
        error : function(data) {
            error('settings', data);
        }
    })
};

var getDebug = function(adminPassword) {
    $.ajax({
        type: 'GET',
        url: server('balancer') + '/debug/get/' + adminPassword,
        dataType: 'json',
        async: false,
        success: function(data) {
            $('#debug-result').attr("checked", data);
        },
        error : function(data) {
            error('debug', data);
        }
    })
};

var setDebug = function(enabled, adminPassword) {
    $.ajax({
        type: 'GET',
        url: server('balancer') + '/debug/set/' + enabled + '/' + adminPassword,
        dataType: 'json',
        async: false,
        success: function(data) {
            $('#debug-result').attr("checked", data);
        },
        error : function(data) {
            error('debug', data);
        }
    })
};

$(document).ready(function() {
    $('#register').click(function() {
        var preffix = $('#preffix').val();
        registerUser(
            preffix + $('#email').val(),
            preffix + $('#first-name').val(),
            preffix + $('#last-name').val(),
            $.md5(preffix + $('#password').val()),
            preffix + $('#city').val(),
            preffix + $('#skills').val(),
            preffix + $('#comment').val()
        );
    });

    $('#login').click(function() {
        var preffix = $('#preffix').val();
        loginUser(
            preffix + $('#login-email').val(),
            $.md5(preffix + $('#login-password').val())
        );
    });

    $('#scores').click(function() {
        getScores(
            $('#scores-day').val()
        );
    });

    $('#remove').click(function() {
        var preffix = $('#preffix').val();
        removeUser(
            preffix + $('#remove-email').val(),
            $.md5($('#admin-password').val())
        );
    });

    $('#users-game').click(function() {
        getUsersOnGameServer();
    });

    $('#users-balancer').click(function() {
        getUsersOnBalancerServer(
            $.md5($('#admin-password').val())
        );
    });

    $('#get-settings').click(function() {
        getSettings(
            $.md5($('#admin-password').val())
        );
    });

    $('#set-settings').click(function() {
        setSettings(
            $('#settings-result').val(),
            $.md5($('#admin-password').val())
        );
    });

    $('#debug-result').change(function() {
        setDebug(
            $('#debug-result').is(':checked'),
            $.md5($('#admin-password').val())
        );
    });
    getDebug($.md5($('#admin-password').val()));
});
