<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
    <meta charset="utf-8">
    <title>Game boards</title>
    <link href="${ctx}/resources/css/bootstrap.css" rel="stylesheet">
</head>
<body>
<script src="${ctx}/resources/js/google-analytics.js"></script>

<link href="${ctx}/resources/css/dojo.css" rel="stylesheet">
<script src="${ctx}/resources/js/jquery-1.7.2.js"></script>
<script src="${ctx}/resources/js/jcanvas.min.js"></script>
<script src="${ctx}/resources/js/jquery.simplemodal-1.4.4.js"></script>

<script src="${ctx}/resources/js/board.js"></script>
<script src="${ctx}/resources/js/donate.js"></script>
<script src="${ctx}/resources/js/joystick.js"></script>
<script src="${ctx}/resources/js/leaderstable.js"></script>
<script src="${ctx}/resources/js/chat.js"></script>
<script src="${ctx}/resources/js/hotkeys.js"></script>
<script>
    $(document).ready(function () {
        var players = new Object();
        <c:forEach items="${players}" var="player">
        players["${player.name}"] = "${player.name}";
        </c:forEach>
        initBoard(players, ${allPlayersScreen}, ${singleBoardGame}, ${boardSize}, '${gameName}', '${ctx}/');
        initDonate('${ctx}/');
        initJoystick('${playerName}', ${registered}, '${code}', '${ctx}/');
        initLeadersTable('${ctx}/', '${playerName}', '${code}');
        initChat('${playerName}', ${registered}, '${code}', '${ctx}/', '${gameName}');
        initHotkeys('${gameName}', '${ctx}/');
    });
</script>

</body>
    <span class="score-info width-calculator" id="width_calculator_container"></span>
    <div id="showdata"></div>
    <div>
        <div id="glasses">
            <div id="donate" style="display:none;">
                <input type="button" id="want-donate" value="Помочь проекту..."/>
            </div>
            <c:forEach items="${players}" var="player">
                <c:set var="player_name_id" value="${fn:replace(fn:replace(player.name, '.', '_'), '@', '_')}"/>
                <c:set var="player_name" value="${fn:substring(player.name, 0, fn:indexOf(player.name, '@'))}"/>

                <div id="div_${player_name_id}" style="float: left">
                    <table>
                        <tr>
                            <td>
                                <span id="player_name" class="label label-info big">${player_name}</span> :
                                <span class="label label-info big" id="score_${player_name_id}"></span>
                                <%@include file="joystick.jsp"%>
                            </td>
                        </tr>
                        <c:if test="${!allPlayersScreen}">
                            <tr>
                                <td>
                                    <span class="label small">Level</span> :
                                    <span class="label small" id="level_${player_name_id}"></span>
                                </td>
                            </tr>
                        </c:if>
                        <tr>
                            <td>
                                <canvas id="${player_name_id}" width="${boardSize*30}" height="${boardSize*30}" style="border:1px solid">
                                    <!-- each pixel is 24x24-->
                                    Your browser does not support the canvas element.
                                </canvas>

                                <span class="score-info" id="score_info_${player_name_id}">+200</span>
                            </td>
                        </tr>
                    </table>
                </div>
            </c:forEach>

            <div id="systemCanvas" style="display: none">
                <canvas id="_system" width="168" height="24"> <!-- 7 figures x 24px-->
                    Your browser does not support the canvas element.
                </canvas>

                <c:forEach items="${sprites}" var="element">
                        <img src="${ctx}/resources/sprite/${gameName}/${element}.png" id="${elements.key}_${element}">
                </c:forEach>

                <script>
                    var plots = {};
                    <c:forEach items="${sprites}" varStatus="status" var="element">
                        plots['${sprites_alphabet[status.index]}'] = '${elements.key}_${element}';
                    </c:forEach>
                </script>
            </div>
        </div>

        <%@include file="chat.jsp"%>
        <%@include file="leaderstable.jsp"%>
    </div>
</div>
</html>