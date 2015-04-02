function validatePlayerRegistration(formId) {
    $.validator.addMethod("alphabet", function(value){
        return /^[a-zA-Z]+[a-zA-Z0-9]*$/.test(value);
    }, "Please use only English letters and numbers");

    var rules = {
        name: {
           required: true,
            minlength: 2,
            maxlength: 150,
            email: true
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