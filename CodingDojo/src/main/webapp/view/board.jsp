<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Game boards</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet">
</head>
<body>
<link href="/resources/css/dojo.css" rel="stylesheet">
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
        initBoard(players, ${allPlayersScreen}, ${boardSize}, '${gameType}');
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
                <img src="/resources/sprite/snake/bad_apple.png" id="bad_apple">
                <img src="/resources/sprite/snake/break.png" id="break">
                <img src="/resources/sprite/snake/good_apple.png" id="good_apple">
                <img src="/resources/sprite/snake/head_down.png" id="head_down">
                <img src="/resources/sprite/snake/head_left.png" id="head_left">
                <img src="/resources/sprite/snake/head_right.png" id="head_right">
                <img src="/resources/sprite/snake/head_up.png" id="head_up">
                <img src="/resources/sprite/snake/space.png" id="space">
                <img src="/resources/sprite/snake/tail_end_down.png" id="tail_end_down">
                <img src="/resources/sprite/snake/tail_end_left.png" id="tail_end_left">
                <img src="/resources/sprite/snake/tail_end_right.png" id="tail_end_right">
                <img src="/resources/sprite/snake/tail_end_up.png" id="tail_end_up">
                <img src="/resources/sprite/snake/tail_horizontal.png" id="tail_horizontal">
                <img src="/resources/sprite/snake/tail_left_down.png" id="tail_left_down">
                <img src="/resources/sprite/snake/tail_left_up.png" id="tail_left_up">
                <img src="/resources/sprite/snake/tail_right_down.png" id="tail_right_down">
                <img src="/resources/sprite/snake/tail_right_up.png" id="tail_right_up">
                <img src="/resources/sprite/snake/tail_vertical.png" id="tail_vertical">

                <img src="/resources/sprite/bomberman/bomberman.png" id="bomberman">
                <img src="/resources/sprite/bomberman/dead_bomberman.png" id="dead_bomberman">
                <img src="/resources/sprite/bomberman/meat_chopper.png" id="meat_chopper">
                <img src="/resources/sprite/bomberman/dead_meat_chopper.png" id="dead_meat_chopper">
                <img src="/resources/sprite/bomberman/bomb_bomberman.png" id="bomb_bomberman">
                <img src="/resources/sprite/bomberman/destroy_wall.png" id="destroy_wall">
                <img src="/resources/sprite/bomberman/destroyed_wall.png" id="destroyed_wall">
                <img src="/resources/sprite/bomberman/empty.png" id="empty">
                <img src="/resources/sprite/bomberman/wall.png" id="wall">
                <img src="/resources/sprite/bomberman/bomb.png" id="bomb">
                <img src="/resources/sprite/bomberman/bomb_one.png" id="bomb_one">
                <img src="/resources/sprite/bomberman/bomb_two.png" id="bomb_two">
                <img src="/resources/sprite/bomberman/bomb_three.png" id="bomb_three">
                <img src="/resources/sprite/bomberman/bomb_four.png" id="bomb_four">
                <img src="/resources/sprite/bomberman/bomb_five.png" id="bomb_five">
                <img src="/resources/sprite/bomberman/boom.png" id="boom">
            </div>
        </div>
        <div id="leaderboard">
            <%@include file="leaderstable.jsp"%>
        </div>
    </div>
</div>
</html>