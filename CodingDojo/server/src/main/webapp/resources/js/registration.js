function initRegistration(waitApprove, contextPath) {
    var disable = function(status) {
        $("#submit").prop("disabled", status);
        $("#name").prop("disabled", status);
        $("#password").prop("disabled", status);
        $("#gameName").prop("disabled", status)
    }

    $(document).ready(function() {
        validatePlayerRegistration("#player");
        if ($("#name").val() != "") {
            $("#submit").val("Login");
            $("#title").text("Login");
        }
        if (waitApprove) {
            disable(true);
            $.ajax({ url:contextPath + 'register?approved=' + $("#name").val(),
                cache:false,
                complete:function() {
                    disable(false);
                    $("#submit").click();
                },
                timeout:1000000
            });
        } else {
            if ($("#name").val() == "") {
                $("#name").focus();
            } else {
                $("#password").focus();
            }
        }
    });
}