<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Codenjoy</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet">
</head>
<body>
    <div class="page-header">
        <h1>Hi ${(user!=null)?user:ip}, please:</h1>
    </div>
    <ol>
        <li><a href="${pageContext.request.contextPath}/help">Read help</a></li>
        <li><a href="${pageContext.request.contextPath}/register">Register</a></li>
        <li><a href="${pageContext.request.contextPath}/board">Check leader board</a></li>
    </ol>
<body>
</html>