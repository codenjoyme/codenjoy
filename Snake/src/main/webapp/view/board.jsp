<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Glass board</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet">
</head>
<body>
<script src="/resources/js/jquery-1.7.2.js"></script>
<script src="/resources/js/jcanvas.min.js"></script>
<script src="/resources/js/board.js"></script>
<script src="/resources/js/leaderstable.js"></script>
<script>
    $(document).ready(function () {
        var players = new Object();
        <c:forEach items="${players}" var="player">
        players["${player.name}"] = "${player.name}";
        </c:forEach>
        initBoard(players, ${allPlayersScreen}, ${boardSize});
    });
</script>
</body>

<div id="showdata"></div>
<div class="container-fluid">
    <div class="row-fluid">
        <div class="span10">
            <c:forEach items="${players}" var="player">
                <div id="div_${player.name}" style="float: left">
                    <table>
                        <tr>
                            <td>
                                <span class="label label-info big">${player.name}</span> :
                                <span class="label label-info big" id="score_${player.name}"></span>
                            </td>
                        </tr>
                        <c:if test="${!allPlayersScreen}">
                            <tr>
                                <td>
                                    <span class="label small">Level</span> :
                                    <span class="label small" id="level_${player.name}"></span>
                                </td>
                            </tr>
                        </c:if>
                        <tr>
                            <td>
                                <canvas id="${player.name}" width="${boardSize*24}" height="${boardSize*24}" style="border:1px solid">
                                    <!-- each pixel is 24x24-->
                                    Your browser does not support the canvas element.
                                </canvas>
                            </td>
                        </tr>
                    </table>
                </div>
            </c:forEach>

            <div id="systemCanvas" style="display: none">
                <canvas id="_system" width="168" height="24"> <!-- 7 figures x 24px-->
                    Your browser does not support the canvas element.
                </canvas>
                <img src="/resources/head.png" id="head">
                <img src="/resources/apple.png" id="apple">
                <img src="/resources/stone.png" id="stone">
                <img src="/resources/tail.png" id="tail">
                <img src="/resources/empty.png" id="empty">
                <img src="/resources/wall.png" id="wall">
                <img src="/resources/body.png" id="body">
            </div>
        </div>
        <div class="span2">
            <%@include file="leaderstable.jsp"%>
        </div>
    </div>
</div>
</html>