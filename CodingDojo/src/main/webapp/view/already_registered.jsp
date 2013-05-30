<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Registration</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet">
</head>
<body>
    <div class="page-header">
        <h1>Registration</h1>
    </div>
    Hi ${user}, you have already registered with ${url}, please go to <a href="${pageContext.request.contextPath}/">main page</a>.
</body>
</html>