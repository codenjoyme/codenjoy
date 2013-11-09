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
        initJoystick('${playerName}', ${registered}, '${pageContext.request.contextPath}/');
        initLeadersTable('${pageContext.request.contextPath}/');
        initChat('${playerName}', ${registered}, '${pageContext.request.contextPath}/');
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
                <img src="${pageContext.request.contextPath}/resources/sprite/snake/bad_apple.png" id="bad_apple">
                <img src="${pageContext.request.contextPath}/resources/sprite/snake/break.png" id="break">
                <img src="${pageContext.request.contextPath}/resources/sprite/snake/good_apple.png" id="good_apple">
                <img src="${pageContext.request.contextPath}/resources/sprite/snake/head_down.png" id="head_down">
                <img src="${pageContext.request.contextPath}/resources/sprite/snake/head_left.png" id="head_left">
                <img src="${pageContext.request.contextPath}/resources/sprite/snake/head_right.png" id="head_right">
                <img src="${pageContext.request.contextPath}/resources/sprite/snake/head_up.png" id="head_up">
                <img src="${pageContext.request.contextPath}/resources/sprite/snake/space.png" id="space">
                <img src="${pageContext.request.contextPath}/resources/sprite/snake/tail_end_down.png" id="tail_end_down">
                <img src="${pageContext.request.contextPath}/resources/sprite/snake/tail_end_left.png" id="tail_end_left">
                <img src="${pageContext.request.contextPath}/resources/sprite/snake/tail_end_right.png" id="tail_end_right">
                <img src="${pageContext.request.contextPath}/resources/sprite/snake/tail_end_up.png" id="tail_end_up">
                <img src="${pageContext.request.contextPath}/resources/sprite/snake/tail_horizontal.png" id="tail_horizontal">
                <img src="${pageContext.request.contextPath}/resources/sprite/snake/tail_left_down.png" id="tail_left_down">
                <img src="${pageContext.request.contextPath}/resources/sprite/snake/tail_left_up.png" id="tail_left_up">
                <img src="${pageContext.request.contextPath}/resources/sprite/snake/tail_right_down.png" id="tail_right_down">
                <img src="${pageContext.request.contextPath}/resources/sprite/snake/tail_right_up.png" id="tail_right_up">
                <img src="${pageContext.request.contextPath}/resources/sprite/snake/tail_vertical.png" id="tail_vertical">

                <img src="${pageContext.request.contextPath}/resources/sprite/bomberman/bomberman.png" id="bomberman">
                <img src="${pageContext.request.contextPath}/resources/sprite/bomberman/dead_bomberman.png" id="dead_bomberman">
                <img src="${pageContext.request.contextPath}/resources/sprite/bomberman/meat_chopper.png" id="meat_chopper">
                <img src="${pageContext.request.contextPath}/resources/sprite/bomberman/dead_meat_chopper.png" id="dead_meat_chopper">
                <img src="${pageContext.request.contextPath}/resources/sprite/bomberman/bomb_bomberman.png" id="bomb_bomberman">
                <img src="${pageContext.request.contextPath}/resources/sprite/bomberman/destroy_wall.png" id="destroy_wall">
                <img src="${pageContext.request.contextPath}/resources/sprite/bomberman/destroyed_wall.png" id="destroyed_wall">
                <img src="${pageContext.request.contextPath}/resources/sprite/bomberman/empty.png" id="empty">
                <img src="${pageContext.request.contextPath}/resources/sprite/bomberman/wall.png" id="wall">
                <img src="${pageContext.request.contextPath}/resources/sprite/bomberman/bomb.png" id="bomb">
                <img src="${pageContext.request.contextPath}/resources/sprite/bomberman/bomb_one.png" id="bomb_one">
                <img src="${pageContext.request.contextPath}/resources/sprite/bomberman/bomb_two.png" id="bomb_two">
                <img src="${pageContext.request.contextPath}/resources/sprite/bomberman/bomb_three.png" id="bomb_three">
                <img src="${pageContext.request.contextPath}/resources/sprite/bomberman/bomb_four.png" id="bomb_four">
                <img src="${pageContext.request.contextPath}/resources/sprite/bomberman/bomb_five.png" id="bomb_five">
                <img src="${pageContext.request.contextPath}/resources/sprite/bomberman/boom.png" id="boom">
                <img src="${pageContext.request.contextPath}/resources/sprite/bomberman/other_bomberman.png" id="other_bomberman">
                <img src="${pageContext.request.contextPath}/resources/sprite/bomberman/other_dead_bomberman.png" id="other_dead_bomberman">
                <img src="${pageContext.request.contextPath}/resources/sprite/bomberman/other_bomb_bomberman.png" id="other_bomb_bomberman">

                <img src="${pageContext.request.contextPath}/resources/sprite/minesweeper/bang.png" id="bang">
                <img src="${pageContext.request.contextPath}/resources/sprite/minesweeper/here_is_bomb.png" id="here_is_bomb">
                <img src="${pageContext.request.contextPath}/resources/sprite/minesweeper/detector.png" id="detector">
                <img src="${pageContext.request.contextPath}/resources/sprite/minesweeper/flag.png" id="flag">
                <img src="${pageContext.request.contextPath}/resources/sprite/minesweeper/hidden.png" id="hidden">
                <img src="${pageContext.request.contextPath}/resources/sprite/minesweeper/border.png" id="border">
                <img src="${pageContext.request.contextPath}/resources/sprite/minesweeper/no_mine.png" id="no_mine">
                <img src="${pageContext.request.contextPath}/resources/sprite/minesweeper/one_mine.png" id="one_mine">
                <img src="${pageContext.request.contextPath}/resources/sprite/minesweeper/two_mines.png" id="two_mines">
                <img src="${pageContext.request.contextPath}/resources/sprite/minesweeper/three_mines.png" id="three_mines">
                <img src="${pageContext.request.contextPath}/resources/sprite/minesweeper/four_mines.png" id="four_mines">
                <img src="${pageContext.request.contextPath}/resources/sprite/minesweeper/five_mines.png" id="five_mines">
                <img src="${pageContext.request.contextPath}/resources/sprite/minesweeper/six_mines.png" id="six_mines">
                <img src="${pageContext.request.contextPath}/resources/sprite/minesweeper/eight_mines.png" id="eight_mines">
                <img src="${pageContext.request.contextPath}/resources/sprite/minesweeper/seven_mines.png" id="seven_mines">
                <img src="${pageContext.request.contextPath}/resources/sprite/minesweeper/destroyed_bomb.png" id="destroyed_bomb">

                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/battle_ground.png" id="battle_ground">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/battle_wall.png" id="battle_wall">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/dead_tank.png" id="dead_tank">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/construction.png" id="construction">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/construction_destroyed_down.png" id="construction_destroyed_down">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/construction_destroyed_up.png" id="construction_destroyed_up">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/construction_destroyed_left.png" id="construction_destroyed_left">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/construction_destroyed_right.png" id="construction_destroyed_right">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/construction_destroyed_down_twice.png" id="construction_destroyed_down_twice">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/construction_destroyed_up_twice.png" id="construction_destroyed_up_twice">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/construction_destroyed_left_twice.png" id="construction_destroyed_left_twice">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/construction_destroyed_right_twice.png" id="construction_destroyed_right_twice">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/construction_destroyed_left_right.png" id="construction_destroyed_left_right">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/construction_destroyed_up_down.png" id="construction_destroyed_up_down">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/construction_destroyed_up_left.png" id="construction_destroyed_up_left">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/construction_destroyed_right_up.png" id="construction_destroyed_right_up">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/construction_destroyed_down_left.png" id="construction_destroyed_down_left">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/construction_destroyed_down_right.png" id="construction_destroyed_down_right">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/bullet.png" id="bullet">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/tank_up.png" id="tank_up">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/tank_right.png" id="tank_right">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/tank_down.png" id="tank_down">
                <img src="${pageContext.request.contextPath}/resources/sprite/battlecity/tank_left.png" id="tank_left">
            </div>
        </div>

        <%@include file="chat.jsp"%>
        <%@include file="leaderstable.jsp"%>
    </div>
</div>
</html>