/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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

/*
 * This script is used for registration page
 * */
(function () {
    var LOCAL_STORAGE_KEYS = {
        gameName: "gameName",
        userData: {
            key: "expansionGamer",
            email: "email",
            id: "id", //first name, last name
            techSkills: "techSkills"
        }
    };

    $(document).ready(loadRegistrationPage);

    function loadRegistrationPage() {
        fillFormFromLocalStorage();
        var data = localStorage.getItem('registration-data');
        if (!!data) {
            $('#data input').val(data);
        }
        var data1 = localStorage.getItem('registration-data1');
        if (!!data1) {
            $('#data1 input').val(data1);
        }
        var data2 = localStorage.getItem('registration-data2');
        if (!!data2) {
            $('#data2 input').val(data2);
        }
        var data3 = localStorage.getItem('registration-data3');
        if (!!data3) {
            $('#data3 input').val(data3);
        }

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

        var configurable = function (name) {
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
                if (checkEls[index](value)) {
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

                $("#password input").val($.md5($("#password input").val()));
                saveDataToLocalStorage();
                $("#form").submit();
            }
        };

        $('#submit-button').click(submitForm);
        $('#email, #password, #skills').keypress(function (e) {
            var code = (e.keyCode ? e.keyCode : e.which);
            if (code == 13) {
                submitForm();
                e.preventDefault();
            }
        });
    }

    function fillFormFromLocalStorage() {
        var gameName = localStorage.getItem(LOCAL_STORAGE_KEYS.gameName);
        var player = localStorage.getItem(LOCAL_STORAGE_KEYS.userData.key);
        if (!!gameName && !$("#game").attr('hidden')) {
            $("#game").find("select").val(gameName);
        }
        if (!!player) {
            player = JSON.parse(player);
            $('#email').find('input').val(player[LOCAL_STORAGE_KEYS.userData.email]);
        }
    }

    function saveDataToLocalStorage() {
        localStorage.setItem(LOCAL_STORAGE_KEYS.gameName, $("#game").find("option:selected").text());
        var saveData = {};
        saveData[LOCAL_STORAGE_KEYS.userData.email] = $('#email').find('input').val();
        saveData[LOCAL_STORAGE_KEYS.userData.techSkills] = $('#skills').find('input').val();
        localStorage.setItem(LOCAL_STORAGE_KEYS.userData.key, JSON.stringify(saveData));
    }
})();