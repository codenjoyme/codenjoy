<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<c:forEach var="cookies" items="${cookie}">

    <strong><c:out value=
                           "${cookies.key}"/></strong>: Object=<c:out value="${cookies.value}"/>, value=<c:out value="${cookies.value.value}"/><br />

</c:forEach>

</body>
</html>