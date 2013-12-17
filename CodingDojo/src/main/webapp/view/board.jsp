<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Game boards</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet">
</head>
<body>
<link href="${pageContext.request.contextPath}/resources/css/dojo.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.7.2.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jcanvas.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/js/board.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/joystick.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/leaderstable.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/chat.js"></script>
<script>
    $(document).ready(function () {
        var players = new Object();
        <c:forEach items="${players}" var="player">
        players["${player.name}"] = "${player.name}";
        </c:forEach>
        initBoard(players, ${allPlayersScreen}, ${boardSize}, '${gameType}', '${pageContext.request.contextPath}/');
        initJoystick('${playerName}', ${registered}, '${code}', '${pageContext.request.contextPath}/');
        initLeadersTable('${pageContext.request.contextPath}/', '${playerName}', '${code}');
        initChat('${playerName}', ${registered}, '${code}', '${pageContext.request.contextPath}/');
    });
</script>

</body>
    <span class="score-info width-calculator" id="width_calculator_container"></span>
    <div id="showdata"></div>
    <div>
        <div id="glasses">
            <c:forEach items="${players}" var="player">
                <div id="div_${player.name}" style="float: left">
                    <table>
                        <tr>
                            <td>
                                <span id="player_name" class="label label-info big">${player.name}</span> :
                                <span class="label label-info big" id="score_${player.name}"></span>
                                <%@include file="joystick.jsp"%>
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
                                <canvas id="${player.name}" width="${boardSize*30}" height="${boardSize*30}" style="border:1px solid">
                                    <!-- each pixel is 24x24-->
                                    Your browser does not support the canvas element.
                                </canvas>

                                <span class="score-info" id="score_info_${player.name}">+200</span>
                            </td>
                        </tr>
                    </table>
                </div>
            </c:forEach>

            <div id="systemCanvas" style="display: none">
                <canvas id="_system" width="168" height="24"> <!-- 7 figures x 24px-->
                    Your browser does not support the canvas element.
                </canvas>

                <c:forEach items="${sprites}" var="elements">
                    <c:forEach items="${elements.value}" var="element">
                        <img src="${pageContext.request.contextPath}/resources/sprite/${elements.key}/${element}.png" id="${elements.key}_${element}">
                    </c:forEach>
                </c:forEach>

                <script>
                    var plots = {};
                    <c:forEach items="${sprites}" var="elements">
                        plots.${elements.key} = {};
                        <c:forEach items="${elements.value}" varStatus="status" var="element">
                            plots.${elements.key}['${sprites_alphabet[status.index]}'] = '${elements.key}_${element}';
                        </c:forEach>
                    </c:forEach>
                </script>
            </div>
        </div>

        <%@include file="chat.jsp"%>
        <%@include file="leaderstable.jsp"%>
    </div>
</div>
</html>