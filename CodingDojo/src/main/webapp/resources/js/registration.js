$(document).ready(function() {
    validatePlayerRegistration("#player");
    if ($("#name").val() == "") {
        $("#name").focus();
    } else {
        $("#submit").val("Login");
        $("#title").text("Login");
        $("#password").focus();
    }
});