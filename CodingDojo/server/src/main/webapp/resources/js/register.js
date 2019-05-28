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

    var KEYS = {
        gameName: "gameName",
        userData: {
            email: "registration-email",
            readableName: "registration-readableName",
            data: "registration-data",
            data1: "registration-data1",
            data2: "registration-data2",
            data3: "registration-data3"
        }
    };

    function configureFormFromAdminSettings() {
        var general = new AdminSettings(contextPath, 'general', 'registration');

        general.load(function(data) {
            if ($.isEmptyObject(data)) {
                data = {
                    showGames: true,
                    showNames: false,
                    showCities: false,
                    showTechSkills: false,
                    showUniversity: false,
                    defaultGame: null
                };
            }

            var gamesCount = $('#gameName > option').length;
            if (gamesCount > 1) {
                $('#gameName').show();
            } else {
                $('#gameName').hide();
            }
            if (data.showGames) {
                $('#game').show();
            } else {
                $('#game').hide();
            }
            if (data.showNames) {
                $('#readableName').show();
            } else {
                $('#readableName').hide();
            }
            if (data.showCities) {
                $('#data1').show();
            } else {
                $('#data1').hide();
            }
            if (data.showTechSkills) {
                $('#data2').show();
            } else {
                $('#data2').hide();
            }
            if (data.showUniversity) {
                $('#data3').show();
            } else {
                $('#data3').hide();
            }
            if (!data.defaultGame) {
                data.defaultGame = $("#game select option:first").val();
            }
            $('#game select').val(data.defaultGame);
        });
    }

    function loadRegistrationPage() {
        configureFormFromAdminSettings();

        fillFormFromLocalStorage();

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

        var submitForm = function () {
            if ($('form .not-valid').length == 0) {
                $('#data input').val(
                    $('#data1 input').val() + "|" +
                    $('#data2 input').val() + "|" +
                    $('#data3 input').val()
                );

                saveDataToLocalStorage();
                $("#form").submit();
            }
        };

        $('#submit-button').click(submitForm);
        $('#email, #password, #game, #skills, #readableName, #data1, #data2, #data3').keypress(function (e) {
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

    function fillFormFromLocalStorage() {
        var gameName = localStorage.getItem(KEYS.gameName);
        if (!!gameName && !$('#game').attr('hidden')) {
            $('#game select').val(gameName);
        } else {
            var def = $('#game select option[default]').attr('value');
            if (!!def) {
                $('#game select').val(def);
            }
        }

        loadInput(KEYS.userData.email, '#email');
        loadInput(KEYS.userData.readableName, '#readableName');
        loadInput(KEYS.userData.data1, '#data1');
        loadInput(KEYS.userData.data2, '#data2');
        loadInput(KEYS.userData.data3, '#data3');
    }

    function saveDataToLocalStorage() {
        localStorage.setItem(KEYS.gameName, $('#game').find('option:selected').text());
        localStorage.setItem(KEYS.userData.email, $('#email input').val());
        localStorage.setItem(KEYS.userData.readableName, $('#readableName input').val());
        localStorage.setItem(KEYS.userData.data1, $('#data1 input').val());
        localStorage.setItem(KEYS.userData.data2, $('#data2 input').val());
        localStorage.setItem(KEYS.userData.data3, $('#data3 input').val());
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
            loadRegistrationPage();
        }
    });
}
