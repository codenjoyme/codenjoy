<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Codenjoy</title>
    <link href="${ctx}/resources/css/bootstrap.css" rel="stylesheet">
    <script src="${ctx}/resources/js/jquery-1.7.2.js"></script>
    <script src="${ctx}/resources/js/hotkeys.js"></script>
    <script>
        $(document).ready(function () {
            initHotkeys('${gameName}', '${ctx}/');
        });
    </script>
</head>
<body>
    <div class="page-header">
        <h1>WTF! Something wrong...</h1>
    </div>
        <div>${message}</div></br>
        <a href="${ctx}">Go to main page</a>
    </div>
<body>
</html>