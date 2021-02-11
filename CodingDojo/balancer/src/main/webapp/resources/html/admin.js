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

(function( $ ){
    $.fn.getValue = function() {
        if (this.hasClass('jsonpanel')) {
            return this[0].val();
        } else {
            return this.val();
        }
    };

    $.fn.setValue = function(value) {
        if (this.hasClass('jsonpanel')) {
            this.empty();
            this.jsonpanel({data: JSON.parse(value)});
            this[0].val = function() {
                return value;
            }
        } else {
            this.val(value);
        }
    };
})( jQuery );

var error = function(partOfId, data) {
    $('#' + partOfId + '-result').val('');
    $('#' + partOfId + '-error').val(data.status + ' ' + (data.responseText || data.statusText));
}

var result = function(partOfId, data) {
    var element = $('#' + partOfId + '-result');
    var text = JSON.stringify(data);
    element.setValue(text);
    $('#' + partOfId + '-error').val('');
}

var settings = function() {
    return JSON.parse($('#admin-settings-result').getValue());
}

var gameSettings = function() {
    return JSON.parse($('#admin-game-settings-result').getValue());
}

var server = function(name) {
    return $('#' + name + '-server').val();
}

var _ajax = function(name, ajaxObject) {
    var tryAfter = function(data) {
        if (!!ajaxObject.after) {
            ajaxObject.after(data);
        }
    }

    if (!ajaxObject.success) {
        ajaxObject.success = function(data) {
            result(name, data);
            tryAfter(data);
        };
    } else {
        var old = ajaxObject.success;
        ajaxObject.success = function(data) {
            old(data);
            tryAfter(data);
        }
    }

    if (!ajaxObject.error) {
        ajaxObject.error = function(data) {
            error(name, data);
            tryAfter(data);
        };
    } else {
        var old = ajaxObject.error;
        ajaxObject.error = function(data) {
            old(data);
            tryAfter(data);
        }
    }

    if (!ajaxObject.dataType) {
        ajaxObject.dataType = 'json';
    }
    ajaxObject.async = false;

    $('#' + name + '-request').val(
        '[' + ajaxObject.type + '] ' + ajaxObject.url +
        ((!!ajaxObject.data) ? (' > \n' + ajaxObject.data) : '')
    );

    $.ajax(ajaxObject);
}

function generate(characters, length) {
   var charactersLength = characters.length;
   var result = '';
   for (var i = 0; i < length; i++) {
      result += characters.charAt(Math.floor(Math.random() * charactersLength));
   }
   return result;
}

function randomStr(length, array) {
    var result = '';
    for (var i = length; i > 0; i--) {
        result += array[Math.floor(Math.random() * array.length)];
    }
    return result;
}

var autoIncrementPrefix = function() {
    $('#preffix').val(randomStr(5, 'abcdefghijklmnopqrstuvwxyz'));
}

var autoIncrementPhone = function() {
    var old = $('#phone').val();
    var index = parseInt(old.match(/\d+/g)[0]);
    var aNew = old.replace('' + index, '' + (index + 1));
    changePhone(aNew);
}

var changePhone = function(phone) {
    $('#phone').val(phone);
    $('#phone').trigger('change');
}

var autoIncrement = function() {
    if ($('#auto-increment').is(':checked')) {
        autoIncrementPrefix();
        autoIncrementPhone();
    }
}

var registerUser = function(email, phone, firstName,
                            lastName, password, code,
                            city, skills, comment, whatToDo)
{
    _ajax('register', {
        type: 'POST',
        url: server('balancer') + '/' + whatToDo,
        contentType: 'application/json; charset=utf-8',
        data: '{"email": "' + email + '", ' +
            '"phone" : "' + phone + '", ' +
            '"firstName" : "' + firstName + '", ' +
            '"lastName" : "' + lastName + '", ' +
            '"password" : "' + password + '", ' +
            '"code" : "' + code + '", ' +
            '"city" : "' + city + '", ' +
            '"skills" : "' + skills + '", ' +
            '"comment" : "' + comment + '"}',
        after: function(data){
            updateCode(data.code);
            updateId(data.id);
        }
    });
};

var updateCode = function(code) {
    $('#join-code').val(code);
    $('#code').val(code);
}

var updateId = function(id) {
    $('#player-id').val(id);
    $('#remove-id').val(id);
}

var loginUser = function(email, password) {
    _ajax('login', {
        type: 'POST',
        url: server('balancer') + '/login',
        contentType: 'application/json; charset=utf-8',
        data: '{"email": "' + email + '", ' +
            '"password" : "' + password + '"}',
        after: function(data){
            updateCode(data.code);
            updateId(data.id);
        }
    });
};

var getTicket = function(ticket) {
    _ajax('ticket', {
        type: 'GET',
        url: server('balancer') + '/logs/error?ticket=' + ticket
    });
};

var getInfo = function(ticket) {
    _ajax('ticket', {
        type: 'GET',
        url: server('balancer') + '/logs/info'
    });
};

var getConfirmCode = function(email) {
    _ajax('get-confirm', {
        type: 'GET',
        url: server('balancer') + '/confirm/' + email + '/code',
        after: function(data){
            if (data.statusText != 'OK') {
                $('#confirm-code').val(data.code);
                $('#confirm-type').val(data.type);
            }
        }
    });
};

var confirmUserRegistration = function(phone, code) {
    _ajax('confirm', {
        type: 'POST',
        url: server('balancer') + '/register/confirm',
        contentType: 'application/json; charset=utf-8',
        data: '{"phone": "' + phone + '", ' +
            '"code" : "' + code + '"}'
    });
};

var confirmChangePassword = function(phone, code) {
    _ajax('confirm', {
        type: 'POST',
        url: server('balancer') + '/register/validate-reset',
        contentType: 'application/json; charset=utf-8',
        data: '{"phone": "' + phone + '", ' +
            '"code" : "' + code + '"}'
    });
};

var resendPassword = function(email, phone) {
    _ajax('resend', {
        type: 'POST',
        url: server('balancer') + '/register/reset',
        contentType: 'application/json; charset=utf-8',
        data: '{"phone": "' + phone + '", "email": "' + email + '"}'
    });
};

var resendConfirmation = function(phone) {
    _ajax('resend', {
        type: 'POST',
        url: server('balancer') + '/register/resend',
        contentType: 'application/json; charset=utf-8',
        data: '{"phone": "' + phone + '"}'
    });
};

var joinExitStatusUser = function(email, code, whatToDo) {
    _ajax('join', {
        type: 'GET',
        url: server('balancer') + '/player/' + email + '/' + whatToDo + '/' + code,
        after: function(data){
            if (!!data.code) {
                updateCode(data.code);
                updateId(data.id);
            }
        }
    });
};

var getScores = function(day) {
    _ajax('scores', {
        type: 'GET',
        url: server('balancer') + '/score/day/' + day
    });
};

var getFinalists = function() {
    _ajax('finalists', {
        type: 'GET',
        url: server('balancer') + '/score/finalists'
    });
};

var markWinners = function() {
    _ajax('winners', {
        type: 'GET',
        url: server('balancer') + '/score/winners'
    });
};

var disqualify = function(emails) {
    var players = emails.split(',')
                        .map(function(s) {
                            return '"' + s + '"';
                        });
    _ajax('disqualify', {
        type: 'POST',
        url: server('balancer') + '/score/disqualify',
        contentType: 'application/json; charset=utf-8',
        data: '{"players": [' + players + ']}'
    });
};


var getDisqualified = function() {
    _ajax('disqualified', {
        type: 'GET',
        url: server('balancer') + '/score/disqualified'
    });
};

var removeUser = function(email, whereToRemove) {
    _ajax('remove', {
        type: 'GET',
        url: server('balancer') + '/remove/' + email + '/on/' + whereToRemove
    });
};

var auth = function() {
    var login = settings().adminLogin;
    var password = encodePassword(settings().adminPassword);
    var auth = btoa(login + ":" + password);
    return auth;
}

var getUsersOnGameServer = function() {
    _ajax('users-game', {
        type: 'GET',
        url: server('game') + '/game/' + settings().game.type + '/players',
        headers: {
            "Authorization": "Basic " + auth()
        },
    });
};

var getUsersOnBalancerServer = function() {
    _ajax('users-balancer', {
        type: 'GET',
        url: server('balancer') + '/players'
    });
};

var getSettings = function(gameType, onSuccess) {
    _ajax(gameType || 'settings', {
        type: 'GET',
        url: server('balancer') + '/settings',
        after: onSuccess
    });
};

var getVersions = function(name) {
    _ajax('balancer-server-version', {
        type: 'GET',
        url: server('balancer') + '/version'
    });

    _ajax('game-server-version', {
        type: 'GET',
        url: server('game') + '/admin/version',
        headers: {
            "Authorization": "Basic " + auth()
        },
    });
};

var setSettings = function(settings) {
    _ajax('settings', {
        type: 'POST',
        url: server('balancer') + '/settings',
        contentType: 'application/json; charset=utf-8',
        data: settings
    });
};

var getGameSettings = function(onSuccess) {
    _ajax('game-settings', {
        type: 'GET',
        url: server('balancer') + '/game/settings/get',
        after: onSuccess
    });
};

var setGameSettings = function(settings) {
    _ajax('game-settings', {
        type: 'POST',
        url: server('balancer') + '/game/settings/set',
        contentType: 'application/json; charset=utf-8',
        data: settings
    });
};

var clearCache = function(block, whatToClean) {
    _ajax(block, {
        type: 'GET',
        url: server('balancer') + '/cache/clear/' + whatToClean
    });
};

var getDebug = function() {
    _ajax('debug', {
        type: 'GET',
        url: server('balancer') + '/debug/get',
        success: function(data) {
            $('#debug-result').attr('checked', data);
        }
    });
};

var setDebug = function(enabled) {
    _ajax('debug', {
        type: 'GET',
        url: server('balancer') + '/debug/set/' + enabled,
        success: function(data) {
            $('#debug-result').attr('checked', data);
            $('#debug-error').val('');
        }
    });
};

var getContest = function() {
    _ajax('contest', {
        type: 'GET',
        url: server('balancer') + '/contest/enable/get',
        success: function(data) {
            $('#contest-result').attr('checked', data);
        }
    });
};

var setContest = function(enabled) {
    _ajax('contest', {
        type: 'GET',
        url: server('balancer') + '/contest/enable/set/' + enabled,
        success: function(data) {
            $('#contest-result-data').val(JSON.stringify(data));
            $('#contest-error').val('');

            getContest();
        },
        error: function(data) {
            $('#contest-result-data').val('');
            $('#contest-error').val(JSON.stringify(data));
        }
    });
};

var enableSlide = function(element) {
    $('.block-header').click(function() {
        $(this).next().slideToggle('fast');
    });

    var visible = false;
    $('#collapse-all').click(function() {
        if (visible) {
            $('.block-header').next().hide();
        } else {
            $('.block-header').next().show();
        }
        visible = !visible;
        $('#collapse-all').text(visible ? '(collapse all)' : '(expand all)');
    });

    $('#collapse-all').click();
}

var encodePassword = function(raw) {
    return $.md5(raw);
}

$(document).ready(function() {
    $('#scores-day').val(new Date().toISOString().split('T')[0]);

    $('#balancer-server').val(
        window.location.protocol + '//'
        + window.location.host
        + '/codenjoy-balancer/rest'
    );

    getSettings('admin-settings', function(data) {
        $('#game-server').val(
            data.game.schema + '://'
            + data.game.servers[0].replace('localhost', '127.0.0.1')
            + '/codenjoy-contest/rest'
        );

        getVersions();
    });

    var registerOrUpdate = function(action) {
        $('#' + action).click(function() {
            autoIncrement();
            var preffix = $('#preffix').val();

            registerUser(
                preffix + $('#email').val(),
                $('#phone').val(),
                preffix + $('#first-name').val(),
                preffix + $('#last-name').val(),
                encodePassword(preffix + $('#password').val()),
                $('#code').val(),
                preffix + $('#city').val(),
                preffix + $('#skills').val(),
                preffix + $('#comment').val(),
                action
            );
        });
    }

    registerOrUpdate('register');
    registerOrUpdate('update');

    var sync = function(ids) {
        for (var index in ids) {
            var id = ids[index];

            syncChange(id, ids);
        }
    }

    var syncChange = function(id, ids) {
        $(id).change(function() {
            for (var index2 in ids) {
                var id2 = ids[index2];
                if (id == id2) continue;

                $(id2).val($(id).val());
            }
        });
    }

    sync(['#phone', '#confirm-phone', '#resend-phone']);
    sync(['#email', '#get-confirm-email', '#login-email', '#join-email', '#resend-email']);
    sync(['#password', '#login-password']);
    sync(['#code', '#join-code']);
    sync(['#player-id', '#remove-id']);

    $('#login').click(function() {
        var preffix = $('#preffix').val();
        loginUser(
            preffix + $('#login-email').val(),
            encodePassword(preffix + $('#login-password').val())
        );
    });

    $('#get-confirm').click(function() {
        var preffix = $('#preffix').val();
        getConfirmCode(
            preffix + $('#get-confirm-email').val()
        );
    });

    $('#get-ticket').click(function() {
        getTicket(
            $('#ticket').val()
        );
    });

    $('#get-info').click(function() {
        getInfo(
            $('#ticket').val()
        );
    });

    $('#confirm-registration').click(function() {
        var preffix = $('#preffix').val();
        confirmUserRegistration(
            $('#confirm-phone').val(),
            $('#confirm-code').val()
        );
    });

    $('#confirm-change-password').click(function() {
        var preffix = $('#preffix').val();
        confirmChangePassword(
            $('#confirm-phone').val(),
            $('#confirm-code').val()
        );
    });

    $('#resend-confirmation').click(function() {
        resendConfirmation(
            $('#resend-phone').val()
        );
    });

    $('#resend-password').click(function() {
        var preffix = $('#preffix').val();
        resendPassword(
            preffix + $('#resend-email').val(),
            $('#resend-phone').val()
        );
    });

    $('#join').click(function() {
        var preffix = $('#preffix').val();
        joinExitStatusUser(
            preffix + $('#join-email').val(),
            $('#join-code').val(),
            'join'
        );
    });

    $('#exit').click(function() {
        var preffix = $('#preffix').val();
        joinExitStatusUser(
            preffix + $('#join-email').val(),
            $('#join-code').val(),
            'exit'
        );
    });

    $('#join-status').click(function() {
        var preffix = $('#preffix').val();
        joinExitStatusUser(
            preffix + $('#join-email').val(),
            $('#join-code').val(),
            'active'
        );
    });

    $('#scores').click(function() {
        getScores(
            $('#scores-day').val()
        );
    });

    $('#disqualify').click(function() {
        disqualify(
            $('#disqualify-emails').val()
        );
    });

    $('#disqualified').click(function() {
        getDisqualified();
    });

    $('#finalists').click(function() {
        getFinalists(
            $('#finalists-day').val()
        );
    });

    $('#winners').click(function() {
        markWinners();
    });

    $('#remove').click(function() {
        var preffix = $('#preffix').val();
        removeUser(
            $('#remove-id').val(),
            $('#where-to-remove').val()
        );
    });

    $('#users-game').click(function() {
        getUsersOnGameServer();
    });

    $('#users-balancer').click(function() {
        getUsersOnBalancerServer();
    });

    $('#get-settings').click(function() {
        getSettings();
    });

    $('#set-settings').click(function() {
        setSettings(
            $('#settings-result').getValue()
        );
    });

    $('#get-game-settings').click(function() {
        getGameSettings(function(data) {
            $('#game-settings-post-request').val(JSON.stringify(data[0]));
        });
    });

    $('#set-game-settings').click(function() {
        setGameSettings(
            $('#game-settings-post-request').val()
        );
    });

    $('#debug-result').change(function() {
        setDebug(
            $('#debug-result').is(':checked')
        );
    });

    var loadCheckboxes = function() {
        getDebug();
        getContest();
    };

    loadCheckboxes();

    $('#contest-result').change(function() {
        setContest(
            $('#contest-result').is(':checked')
        );
    });

    $('#clear-cache').click(function() {
        clearCache('cache', $('#cache-mask').val());
    });

    $('#clear-scores').click(function() {
        clearCache('scores', 2); // clean only currentScores
    });

    $('#clear-disqualified').click(function() {
        clearCache('disqualified', 4); // clean only disqualified
    });

    $('#clear-finalists').click(function() {
        clearCache('finalists', 8); // clean only finalists cache
    });

    enableSlide();

    var phone = '+380' + generate('0123456789', 9);
    changePhone(phone);

    autoIncrementPrefix();

    var onTextareaClick = function(event) {
        if (!event.ctrlKey) {
            return;
        }
        var data = $(this).getValue();
        var id = $(this).attr('id');
        $(this).replaceWith('<div id="' + id + '"></div>');
        var element = $('#' + id);
        element.addClass('jsonpanel');
        element.setValue(data);
        element.click(onJsonpanelCick);
    }

    var onJsonpanelCick = function(event) {
        if (!event.ctrlKey) {
            return;
        }
        var data = $(this).getValue();
        var id = $(this).attr('id');
        var rows = $(this).height() / 9;
        rows = (rows < 4) ? 4 : rows;
        $(this).replaceWith('<textarea rows="' + rows + '" id="' + id + '"></textarea>');
        var element = $('#' + id);
        element.setValue(data);
        element.click(onTextareaClick);
    }

    $('.jsonpanel').click(onJsonpanelCick);
});
