$(document).ready(function() {
    $.validator.addMethod("alphabet", function(value){
        return /^[a-zA-Z]+[a-zA-Z0-9]*$/.test(value);
    }, "Please use only English letters and numbers");

    $("#player").validate({
        rules: {
            name: {
                required: true,
                minlength: 2,
                maxlength: 15,
                alphabet: true
            },
            callbackUrl: {
                required: true,
                url: true
            }
        },
        errorPlacement: function(error, element) {
            if (element.attr("name") == "name") error.insertAfter($("input[name=name]"));
            if (element.attr("name") == "callbackUrl") error.insertAfter($("input[name=callbackUrl]"));
        }
    });
});