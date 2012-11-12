<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Tetris coding dojo</title>
    <link href="/resources/css/bootstrap.css" rel="stylesheet">
    <script src="/resources/jquery-1.7.2.js"></script>
    <script src="/resources/jquery.validate.js"></script>
    <style type="text/css"><!--
        label.error {
            color:red;
        }
    --></style>
</head>
<body>
    <div class="page-header">
        <h1>Registration</h1>
    </div>
    <script>
        $(document).ready(function() {
            $.validator.addMethod("alphabet", function(value){
                return /^[a-zA-Z]+$/.test(value);
            }, "Please use only English letters");

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
    </script>
    <form:form commandName="player" action="register" method="POST">
        <table>
            <tr>
                <td>Player name:<form:errors path="name"/></td>
            </tr>
            <tr>
                <td><form:input path="name"/></td>
            </tr>
            <tr>
                <td>URL:<form:errors path="callbackUrl"/></td>
            </tr>
            <tr>
                <td><form:input path="callbackUrl"/></td>
            </tr>
            <tr>
                <td colspan="3">
                    <input type="submit" value="Register"/>
                </td>
            </tr>
        </table>
    </form:form>
</body>
</html>