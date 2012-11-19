<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Tetris coding dojo</title>
    <link href="/resources/css/bootstrap.css" rel="stylesheet">
    <link href="/resources/css/tetris.css" rel="stylesheet">
    <script src="/resources/js/jquery-1.7.2.js"></script>
    <script src="/resources/js/jquery.validate.js"></script>
    <script src="/resources/js/registration.js"></script>
    <script src="/resources/js/validation.js"></script>
</head>
<body>
    <div class="page-header">
        <h1>Registration</h1>
    </div>
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