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
<script>
    var players = new Object();
    <c:forEach items="${players}" var="player">
    players["${player.name}"] = "${player.name}";
    </c:forEach>
    initBoard(players, ${allPlayersScreen});
</script>
<style type="text/css"><!--
    .score-info {
        color:red;
        font-size:50px;
        display:none;
    }

    .width-calculator{
        position: absolute;
        visibility: hidden;
        height: auto;
        width: auto;
        white-space:nowrap;
    }
--></style>
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
                                    <span class="label small">Lines removed</span> :
                                    <span class="label small" id="lines_removed_${player.name}"></span>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <span class="label small">For next level</span> :
                                    <span class="label small" id="next_level_${player.name}"></span>
                                </td>
                            </tr>
                        </c:if>
                        <tr>
                            <td>
                                <canvas id="${player.name}" width="240" height="480" style="border:1px solid">
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
                <img src="/resources/blue.png" id="blue">
                <img src="/resources/cyan.png" id="cyan">
                <img src="/resources/green.png" id="green">
                <img src="/resources/orange.png" id="orange">
                <img src="/resources/purple.png" id="purple">
                <img src="/resources/red.png" id="red">
                <img src="/resources/yellow.png" id="yellow">
            </div>
        </div>
        <div id="leaderboard">
            <script>
                $(document).ready(function () {
                    function leaderboardStyle() {
                        var width = 450;
                        var margin = 20;
                        $("#glasses").width($(window).width() - width - margin).css({ marginLeft: margin, marginTop: margin });

                        $("#leaderboard").width(width).css({ position: "absolute",
                                        marginLeft: 0, marginTop: margin,
                                        top: 0, left: $("#glasses").width()});
                    }

                    $(window).resize(leaderboardStyle);
                    leaderboardStyle();
                });
            </script>
            <%@include file="leaderstable.jsp"%>
        </div>
    </div>
</body>
</html>