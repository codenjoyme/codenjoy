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
    $('#' + partOfId + '-error').val(data.status + ' ' + (data.responseText || data.statusText));
}

var result = function(partOfId, data) {
    $('#' + partOfId + '-result').val(JSON.stringify(data));
    $('#' + partOfId + '-error').val('');
}

var server = function(name) {
    return $('#' + name + '-server').val().replace('THIS_SERVER', window.location.host)
}

var _ajax = function(name, ajaxObject) {
    if (!ajaxObject.success) {
        ajaxObject.success = function(data) {
            result(name, data);
        };
    }

    if (!ajaxObject.error) {
        ajaxObject.error = function(data) {
            error(name, data);
        };
    }

    ajaxObject.dataType = 'json';
    ajaxObject.async = false;

    $('#' + name + '-request').val(
        "[" + ajaxObject.type + "] " + ajaxObject.url +
        ((!!ajaxObject.data) ? (" > " + ajaxObject.data) : "")
    );

    $.ajax(ajaxObject);
}

var registerUser = function(email, firstName,
                            lastName, password,
                            city, skills, comment)
{
    _ajax('register', {
        type: 'POST',
        url: server('balancer') + '/register',
        contentType: 'application/json; charset=utf-8',
        data: '{"email": "' + email + '", ' +
            '"firstName" : "' + firstName + '", ' +
            '"lastName" : "' + lastName + '", ' +
            '"password" : "' + password + '", ' +
            '"city" : "' + city + '", ' +
            '"skills" : "' + skills + '", ' +
            '"comment" : "' + comment + '"}'
    });
};

var loginUser = function(email, password) {
    _ajax('login', {
        type: 'POST',
        url: server('balancer') + '/login',
        contentType: 'application/json; charset=utf-8',
        data: '{"email": "' + email + '", ' +
            '"password" : "' + password + '"}'
    });
};

var getScores = function(day) {
    _ajax('scores', {
        type: 'GET',
        url: server('balancer') + '/score/day/' + day
    });
};

var removeUser = function(email, adminPassword) {
    _ajax('remove', {
        type: 'GET',
        url: server('balancer') + '/remove/' + email + '/' + adminPassword
    });
};

var getUsersOnGameServer = function() {
    _ajax('users-game', {
        type: 'GET',
        url: server('game') + '/game/snakebattle/players'
    });
};

var getUsersOnBalancerServer = function(adminPassword) {
    _ajax('users-balancer', {
        type: 'GET',
        url: server('balancer') + '/players/' + adminPassword
    });
};

var getSettings = function(adminPassword) {
    _ajax('settings', {
        type: 'GET',
        url: server('balancer') + '/settings/' + adminPassword
    });
};

var setSettings = function(settings, adminPassword) {
    _ajax('settings', {
        type: 'POST',
        url: server('balancer') + '/settings/' + adminPassword,
        contentType: 'application/json; charset=utf-8',
        data: settings
    });
};

var getDebug = function(adminPassword) {
    _ajax('debug', {
        type: 'GET',
        url: server('balancer') + '/debug/get/' + adminPassword,
        success: function(data) {
            $('#debug-result').attr("checked", data);
        }
    });
};

var setDebug = function(enabled, adminPassword) {
    _ajax('debug', {
        type: 'GET',
        url: server('balancer') + '/debug/set/' + enabled + '/' + adminPassword,
        success: function(data) {
            $('#debug-result').attr("checked", data);
        }
    });
};

var getContest = function(adminPassword) {
    _ajax('contest', {
        type: 'GET',
        url: server('balancer') + '/contest/enable/get/' + adminPassword,
        success: function(data) {
            $('#contest-result').attr("checked", data);
        }
    });
};

var setContest = function(enabled, adminPassword) {
    _ajax('contest', {
        type: 'GET',
        url: server('balancer') + '/contest/enable/set/' + enabled + '/' + adminPassword,
        success: function(data) {
            $('#contest-result-data').val(JSON.stringify(data));
            getContest($.md5($('#admin-password').val()));
        }
    });
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

    $('#contest-result').change(function() {
        setContest(
            $('#contest-result').is(':checked'),
            $.md5($('#admin-password').val())
        );
    });
    getContest($.md5($('#admin-password').val()));
});
