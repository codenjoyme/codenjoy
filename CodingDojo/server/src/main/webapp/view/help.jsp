<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Help</title>
    <link href="${ctx}/resources/css/bootstrap.css" rel="stylesheet">
    <script src="${ctx}/resources/js/jquery-1.7.2.js"></script>
    <script>
        $(document).ready(function () {
            $("#games").change(function () {
                var val = $("#games").val();
                if (val == '') return;
                window.location.href = "${ctx}/resources/user/" + val + "-servers.zip";
            });
        });
    </script>
</head>
<body>
<div class="page-header">
    <h1>Help</h1>
</div>
<h3>Environment setup and registration</h3>
<ol>
    <li>Download client templates for your game
        <select id="games">
        <option value="">(select your game)</option>
        <c:forEach items="${gameNames}" var="gameName">
            <option value="${gameName}">${gameName}</option>
        </c:forEach>
        </select>
    <li>Setup project according to instruction in README.txt for your developing language</li>
    <li>Read game instructions: <c:forEach items="${gameNames}" var="gameName"><a href="${ctx}/resources/help/${gameName}.html">${gameName}</a>&nbsp;&nbsp;</c:forEach></li>
    <li>Open <a href="${ctx}/register">registration page</a></li>
    <li>Enter your name/password and codenjoy!</li>
</ol>
</body>
</html>