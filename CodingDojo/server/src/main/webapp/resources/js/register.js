/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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
    var disable = function(status) {
        $("#submit").prop("disabled", status);
        $("#name").prop("disabled", status);
        $("#readable-name").prop("disabled", status);
        $("#password").prop("disabled", status);
        $("#game select").prop("disabled", status)
        $("#room select").prop("disabled", status)
        $("#gameMode select").prop("disabled", status)
    }

    var KEYS = {
        game: {
            name: "game",
            room: "room",
            mode: "gameMode"
        },
        userData: {
            email: "registration-email",
            readableName: "registration-readableName",
            data: "registration-data",
            data1: "registration-data1",
            data2: "registration-data2",
            data3: "registration-data3",
            data4: "registration-data4"
        }
    };

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

    function configureFormFromAdminSettings(onFinish) {
        var general = new AdminSettings(contextPath, 'general', 'registration');

        general.load(
            function(data) {
                onLoad(data);
            }, function(error) {
                onLoad(null);            
            });
        
        var onLoad = function(data) {
            if ($.isEmptyObject(data)) {
                data = defaultRegistrationSettings();
            }

            var roomsCount = $('#room select > option').length;
            display('#room', roomsCount > 1);

            var gamesCount = $('#game select > option').length;
            display('#game', gamesCount > 1);

            // will display in fillFormFromLocalStorage
            // display('#gameMode', data.showGameModes);
            display('#readableName', data.showNames);
            display('#data1', data.showData1);
            display('#data2', data.showData2);
            display('#data3', data.showData3);
            display('#data4', data.showData4);

            fillFormFromLocalStorage(data);

            if (!!onFinish) {
                onFinish();
            }
        } 
    }

    function loadRegistrationPage() {
        var checkEls = {};

        var validateEmail = function (email) {
            var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,6})?$/;
            return emailReg.test(email);
        };

        checkEls['email'] = function (value) {
            return value == '' || !validateEmail(value);
        };

        var notEmpty = function (value) {
            return value == '' || value.length == 0;
        };

        checkEls['password'] = notEmpty;

        if ($('#passwordConfirmation').length) {
            checkEls['passwordConfirmation'] = notEmpty;
        }

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

        configurable('readableName');
        configurable('data1');
        configurable('data2');
        configurable('data3');
        configurable('data4');

        var validateElements = function () {
            for (var index in checkEls) {
                if (!checkEls.hasOwnProperty(index)) {
                    continue;
                }

                var element = $('#' + index);
                var value = element.find('input').val();
                if (!element.is(':hidden') && checkEls[index](value)) {
                    element.addClass('not-valid');
                    element.removeClass('valid');
                } else {
                    element.addClass('valid');
                    element.removeClass('not-valid');
                }
            }
        };

        var validation = function (id) {
            var element = $('#' + id);

            element.keyup(validateElements);
            element.focus(validateElements);
            element.blur(validateElements);
            element.mousedown(validateElements);
            element.change(validateElements);

            validateElements();
        };

        $('#email').checkAndTriggerAutoFillEvent();
        $('#readableName').checkAndTriggerAutoFillEvent();
        $('#password').checkAndTriggerAutoFillEvent();

        for (var index in checkEls) {
            if (!checkEls.hasOwnProperty(index)) {
                continue;
            }

            validation(index);
        }

        var fixMd5 = function(element) {
            var from = element + ' input';
            var to = element + '-md5 input';
            if ($(from).length) {
                $(to).val($.md5($(from).val()));
            }
        }

        var submitForm = function () {
            if ($('form .not-valid').length == 0) {
                $('#data input').val(
                    $('#data1 input').val() + "|" +
                    $('#data2 input').val() + "|" +
                    $('#data3 input').val() + "|" +
                    $('#data4 input').val()
                );

                saveDataToLocalStorage();
                fixMd5('#password');
                fixMd5('#passwordConfirmation');
                $('#form').submit();
            }
        };

        $('#submit-button').click(submitForm);
        $('#email, #password, #game, #room, #gameMode, #skills, #readableName, #data1, #data2, #data3, #data4').keypress(function (e) {
            var code = (e.keyCode ? e.keyCode : e.which);
            if (code == 13) {
                submitForm();
                e.preventDefault();
            }
        });
    }

    function loadInput(key, selector) {
        var value = localStorage.getItem(key);
        if (!!value && value !== 'undefined' && !$(selector).attr('hidden')) {
            $(selector).find('input').val(value);
        }
    }

    function setupSelect(key, selector, onSelect) {
        var value = localStorage.getItem(key);

        var params = new URLSearchParams(window.location.search);
        if (params.has(key)) {
            var data = params.get(key);
            if (!!data) {
                value = data;
            }
        }

        var select = $(selector).find('select');
        var hasOption = !!value && select.find('option[value="' + value + '"]').length != 0;
        if (hasOption) {
            select.val(value);
        }

        select.off();
        select.change(function() {
            onSelect(select.val());
        });
        return select;
    }

    function loadSelectValue(key, selector, def, loadDefault) {
        var value = localStorage.getItem(key);
        var select = $(selector).find('select');
        if (!!value
            && !loadDefault
            && value != 'undefined'
            && !select.attr('hidden')
            && !$(selector).attr('hidden'))
        {
            select.val(value);
        } else {
            if (!!def) {
                select.val(def);
            }
        }
    }

    function fillGameRooms(selector, game) {
        var select = $(selector).find('select');
        select.children().each(function() {
            var option = $(this);
            if (option.attr('game') == game) {
                option.show();
            } else {
                option.hide();
            }
        });
        return select;
    }

    function fillGameModes(selector, game, gameModes) {
        var select = $(selector).find('select');
        select.children().remove();

        var current = null;
        for (var index in gameModes) {
            var modes = gameModes[index];
            if (game == index && !!modes) {
                current = modes;
                break;
            }
        }

        for (var index in current) {
            var name = current[index].name;
            var title = current[index].title;
            select.append('<option value="' + name + '">' + title + '</option>');
        }

        return select;
    }

    function fillFormFromLocalStorage(data) {
        var onLoadGame = function(game) {
            var select = fillGameRooms('#room', game);

            var isVisible = (select.find('option:not([style*="display: none"])').length > 1);
            display('#room', isVisible);

            var defaultRoom = select.find('[game*="' + game + '"]:first').val();
            var ignoreStored = localStorage.getItem(KEYS.game.name) != game;
            loadSelectValue(KEYS.game.room, '#room', defaultRoom, ignoreStored);
            onLoadRoom(game, defaultRoom);
        }

        var onLoadRoom = function(game, room) {
            var select = fillGameModes('#gameMode', game, data.gameModes);

            var isVisible = (select.find('option').length > 0 && !!data.showGameModes);
            display('#gameMode', isVisible);

            var defaultGameMode = (!!data.defaultGameMode) ? data.defaultGameMode : select.find(':first-child').val();
            var ignoreStored = localStorage.getItem(KEYS.game.room) != room;
            loadSelectValue(KEYS.game.mode, '#gameMode', defaultGameMode, ignoreStored);
        }

        var roomSelect = setupSelect(KEYS.game.room, '#room', onLoadRoom);
        var gameSelect = setupSelect(KEYS.game.name, '#game', onLoadGame);
        onLoadGame(gameSelect.val());

        loadInput(KEYS.userData.email, '#email');
        loadInput(KEYS.userData.readableName, '#readableName');
        loadInput(KEYS.userData.data1, '#data1');
        loadInput(KEYS.userData.data2, '#data2');
        loadInput(KEYS.userData.data3, '#data3');
        loadInput(KEYS.userData.data4, '#data4');
    }

    function saveDataToLocalStorage() {
        localStorage.setItem(KEYS.game.name, $('#game').find('option:selected').text());
        localStorage.setItem(KEYS.game.room, $('#room').find('option:selected').text());
        localStorage.setItem(KEYS.game.mode, $('#gameMode').find('option:selected').val());
        localStorage.setItem(KEYS.userData.email, $('#email input').val());
        localStorage.setItem(KEYS.userData.readableName, $('#readableName input').val());
        localStorage.setItem(KEYS.userData.data1, $('#data1 input').val());
        localStorage.setItem(KEYS.userData.data2, $('#data2 input').val());
        localStorage.setItem(KEYS.userData.data3, $('#data3 input').val());
        localStorage.setItem(KEYS.userData.data4, $('#data4 input').val());
    }

    $(document).ready(function() {
        validatePlayerRegistration("#player");
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
            configureFormFromAdminSettings(loadRegistrationPage);
        }
    });
}
