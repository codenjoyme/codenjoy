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
var registerUser = function(email, firstName,
                            lastName, password,
                            city, skills, comment)
{
    $.ajax({
        type: 'POST',
        url: $("#balancer-server").val().replace('THIS_SERVER', window.location.host) + '/register',
        dataType: 'json',
        async: false,
        contentType: "application/json; charset=utf-8",
        data: '{"email": "' + email + '", ' +
            '"firstName" : "' + firstName + '", ' +
            '"lastName" : "' + lastName + '", ' +
            '"password" : "' + password + '", ' +
            '"city" : "' + city + '", ' +
            '"skills" : "' + skills + '", ' +
            '"comment" : "' + comment + '"}',
        success: function(data) {
            $("#register-result").val(JSON.stringify(data));
        },
        error : function(data) {
            $("#register-result").val(data.status + " " + data.responseText);
        }
    })
};

var loginUser = function(email, password) {
    $.ajax({
        type: 'POST',
        url: $("#balancer-server").val().replace('THIS_SERVER', window.location.host) + '/login',
        dataType: 'json',
        async: false,
        contentType: "application/json; charset=utf-8",
        data: '{"email": "' + email + '", ' +
            '"password" : "' + password + '"}',
        success: function(data) {
            $("#login-result").val(JSON.stringify(data));
        },
        error : function(data) {
            $("#login-result").val(data.status + " " + data.responseText);
        }
    })
};

var getScores = function(day) {
    $.ajax({
        type: 'GET',
        url: $("#balancer-server").val().replace('THIS_SERVER', window.location.host) + '/score/day/' + day,
        dataType: 'json',
        async: false,
        success: function(data) {
            $("#scores-result").val(JSON.stringify(data));
        },
        error : function(data) {
            $("#scores-result").val(data.status + " " + data.responseText);
        }
    })
};

var removeUser = function(email, password) {
    $.ajax({
        type: 'POST',
        url: $("#balancer-server").val().replace('THIS_SERVER', window.location.host) + '/remove',
        dataType: 'json',
        async: false,
        contentType: "application/json; charset=utf-8",
        data: '{"email": "' + email + '", ' +
            '"password" : "' + password + '"}',
        success: function(data) {
            $("#remove-result").val(JSON.stringify(data));
        },
        error : function(data) {
            $("#remove-result").val(data.status + " " + data.responseText);
        }
    })
};

var getUsersOnGameServer = function() {
    $.ajax({
        type: 'GET',
        url: $("#game-server").val().replace('THIS_SERVER', window.location.host) + '/game/snakebattle/players',
        dataType: 'json',
        async: false,
        success: function(data) {
            $("#users-game-result").val(JSON.stringify(data));
        },
        error : function(data) {
            $("#users-game-result").val(data.status + " " + data.responseText);
        }
    })
};

var getUsersOnBalancerServer = function(email, password) {
    $.ajax({
        type: 'POST',
        url: $("#balancer-server").val().replace('THIS_SERVER', window.location.host) + '/players',
        dataType: 'json',
        async: false,
        contentType: "application/json; charset=utf-8",
        data: '{"email": "' + email + '", ' +
            '"password" : "' + password + '"}',
        success: function(data) {
            $("#users-balancer-result").val(JSON.stringify(data));
        },
        error : function(data) {
            $("#users-balancer-result").val(data.status + " " + data.responseText);
        }
    })
};

$(document).ready(function() {
    $("#register").click(function() {
        var preffix = $("#preffix").val();
        registerUser(
            preffix + $("#email").val(),
            preffix + $("#first-name").val(),
            preffix + $("#last-name").val(),
            $.md5(preffix + $("#password").val()),
            preffix + $("#city").val(),
            preffix + $("#skills").val(),
            preffix + $("#comment").val()
        );
    });

    $("#login").click(function() {
        var preffix = $("#preffix").val();
        loginUser(
            preffix + $("#login-email").val(),
            $.md5(preffix + $("#login-password").val())
        );
    });

    $("#scores").click(function() {
        getScores(
            $("#scores-day").val()
        );
    });

    $("#remove").click(function() {
        var preffix = $("#preffix").val();
        removeUser(
            preffix + $("#revmove-email").val(),
            preffix + $("#revmove-password").val()
        );
    });

    $("#users-game").click(function() {
        getUsersOnGameServer();
    });

    $("#users-balancer").click(function() {
        getUsersOnBalancerServer(
            $("#admin-email").val(),
            $.md5($("#admin-password").val())
        );
    });
});
