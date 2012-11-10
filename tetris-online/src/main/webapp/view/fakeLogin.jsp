<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<form action="${pageContext.request.contextPath}/fakelogin" method="POST" >
    <input type="text" name="userName"/>
    <input type="submit" value="Login"/>
</form>

</body>
</html>