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
function validatePlayerRegistration(formId) {
    $.validator.addMethod("alphabet", function(value){
        return /^[a-zA-Z]+[a-zA-Z0-9@._-]*$/.test(value);
    }, "Please use only English letters and numbers");

    var rules = {
        name: {
            required: true,
            minlength: 2,
            maxlength: 150,
            email: true,
            alphabet : true
        },
        callbackUrl: {
            required: true,
            url: true
        }
    };

    for (var elementName in rules) {
        $('[name*="' + elementName + '"]').each(function(index, element) {
            if (element.name == elementName) return;
            rules[element.name] = rules[elementName];
        });
    }

    $(formId).validate({
        rules: rules,
        errorPlacement: function(error, element) {
            error.insertAfter($('input[name*="' + element[0].name + '"]'));
        }
    });
}
