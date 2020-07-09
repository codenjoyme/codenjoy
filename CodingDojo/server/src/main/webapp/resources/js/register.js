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
        $("#gameName select").prop("disabled", status)
        $("#gameType select").prop("disabled", status)
    }

    var KEYS = {
        game: {
            name: "gameName",
            type: "gameType"
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

            var gamesCount = $('#gameName select > option').length;
            display('#gameName', gamesCount > 1);

            display('#gameType', data.showGames);
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
            var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
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
            if ($(element).length) {
                $(element).val($.md5($(element).val()));
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
                fixMd5('#password input');
                fixMd5('#passwordConfirmation input');
                $('#form').submit();
            }
        };

        $('#submit-button').click(submitForm);
        $('#email, #password, #gameName, #gameType, #skills, #readableName, #data1, #data2, #data3, #data4').keypress(function (e) {
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

    function loadGameNameSelect(key, selector, onSelect) {
        var value = localStorage.getItem(key);
        var select = $(selector).find('select');
        if (!!value) {
            select.val(value);
        }

        select.off();
        select.change(function() {
            onSelect(select.val());
        });
        onSelect(select.val());
    }

    function loadGameTypeSelect(key, selector, def) {
        var value = localStorage.getItem(key);
        var select = $(selector).find('select');
        if (!!value && !$(selector).attr('hidden')) {
            select.val(value);
        } else {
            if (!!def) {
                select.val(def);
            }
        }
    }

    function fillGameTypes(selector, gameName, gameTypes) {
        var select = $(selector).find('select');
        select.children().remove();

        var current = null;
        for (var index in gameTypes) {
            var types = gameTypes[index];
            if (gameName == index && !!types) {
                current = types;
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
        loadGameNameSelect(KEYS.game.name, '#gameName', function(gameName) {
            var select = fillGameTypes('#gameType', gameName, data.gameTypes);

            var isVisible = (select.find('option').length > 0 && !!data.showGames);
            display('#gameType', isVisible);

            loadGameTypeSelect(KEYS.game.type, '#gameType', data.defaultGame);
        });
        loadInput(KEYS.userData.email, '#email');
        loadInput(KEYS.userData.readableName, '#readableName');
        loadInput(KEYS.userData.data1, '#data1');
        loadInput(KEYS.userData.data2, '#data2');
        loadInput(KEYS.userData.data3, '#data3');
        loadInput(KEYS.userData.data4, '#data4');
    }

    function saveDataToLocalStorage() {
        localStorage.setItem(KEYS.game.type, $('#gameType').find('option:selected').val());
        localStorage.setItem(KEYS.game.name, $('#gameName').find('option:selected').text());
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
