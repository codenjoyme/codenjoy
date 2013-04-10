<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Javascript Websocket client</title>
    <script src="/resources/js/jquery-1.7.2.js"></script>
    <link href="/resources/css/bootstrap.css" rel="stylesheet">
    <link href="/resources/css/bootstrap.css" rel="stylesheet">
    <link href="/resources/css/tetris.css" rel="stylesheet">
</head>
<body>
<script>

    var socket = new WebSocket("ws://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/ws?user=${requestScope["user"]}");
    socket.onopen = function(){
        $("#response").innerHTML = "Socket has been opened!";
    }

    socket.onmessage = function(msg){
        $("#response").append(msg.data+"</p>");
        socket.send("left=4, right=0, rotate=0, drop");
    }

</script>

<div id="wrapper">

    <div id="container">

        <h1>WebSockets Client</h1>

        <div id="response">

        </div>

    </div><!-- #container -->

</div>


</body>
</html>
