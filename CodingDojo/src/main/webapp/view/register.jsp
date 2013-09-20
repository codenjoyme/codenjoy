<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Coding dojo</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/dojo.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/resources/js/jquery-1.7.2.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery.validate.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/registration.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/validation.js"></script>
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
            <tr style="display:none;">
                <td>URL:<form:errors path="callbackUrl"/></td>
            </tr>
            <tr style="display:none;">
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