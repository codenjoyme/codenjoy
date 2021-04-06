<%--
  #%L
  Codenjoy - it's a dojo-like platform from developers to developers.
  %%
  Copyright (C) 2018 Codenjoy
  %%
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as
  published by the Free Software Foundation, either version 3 of the
  License, or (at your option) any later version.
  
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public
  License along with this program.  If not, see
  <http://www.gnu.org/licenses/gpl-3.0.html>.
  #L%
  --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="page" scope="request" value="admin"/>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Codenjoy admin page</title>

    <link href="${ctx}/resources/css/all.min.css" rel="stylesheet">
    <link href="${ctx}/resources/css/custom.css" rel="stylesheet">

    <jsp:include page="common-inclusion.jsp" />

</head>
<body>
    <div id="settings" page="${page}" contextPath="${ctx}" game="${game}" room="${room}"></div>

    <%@include file="forkMe.jsp"%>

    <div class="page-header">
        <h1>Admin page</h1>
    </div>

    <table class="admin-table" id="gameVersion">
        <tr>
            <td>
                <b>Room:</b> ${room}
            </td>
        <tr>
        </tr>
            <td>
                <b>Game:</b> ${game}
            </td>
        </tr>
        <tr>
            <td>
                <b>Game version:</b>
            </td>
            <td style="width:500px;">
                <textarea rows="3" cols="45">${gameVersion}</textarea>
            </td>
        </tr>
    </table>

    <form:form modelAttribute="adminSettings" action="admin#activeGames" method="POST">
        <table class="admin-table" id="activeGames">
            <tr>
                <td colspan="3" style="width:300px;">
                    <b>Active games for participants</b>
                </td>
            </tr>
            <c:forEach items="${gameRooms}" var="gameItem" varStatus="status">
                <tr>
                    <td class="rightStep">
                        <form:checkbox id="enable-games-${gameItem.game}" path="games[${status.index}]"/>
                        <label class="check-label" for="enable-games-${gameItem.game}"></label>
                        <span>${gameItem.game}</span>
                    </td>
                    <td class="rightStep">
                        <a id="game-${gameItem.game}" href="${ctx}/admin?game=${gameItem.game}">game</a>
                    </td>
                    <td class="rightStep">
                        <c:forEach items="${gameItem.rooms}" var="roomItem" varStatus="status2">
                            <c:if test="${roomItem == room}">
                                <b style="font-size:18px">
                            </c:if>
                            <a id="game-${gameItem.game}-room-${status2.index}" href="${ctx}/admin?room=${roomItem}">${roomItem}<span class="pow">${playersCount.get(roomItem)}</span></a>&nbsp;
                            <c:if test="${roomItem == room}">
                                </b>
                            </c:if>
                        </c:forEach>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td>
                    <input type="hidden" name="game" value="${game}"/>
                    <input type="hidden" name="room" value="${room}"/>
                    <input type="submit" value="Save"/>
                </td>
            </tr>
        </table>
    </form:form>

    <table class="admin-table" id="pauseGame">
        <tr>
            <td>
                <c:choose>
                    <c:when test="${active}">
                        <b><spring:message key="game.active"/></b></br>
                        <a href="${ctx}/admin/game/pause?room=${room}#pauseGame">Pause game</a>.
                    </c:when>
                    <c:otherwise>
                        <b><spring:message key="game.suspended"/></b></br>
                        <a href="${ctx}/admin/game/resume?room=${room}#pauseGame">Resume game</a>.
                    </c:otherwise>
                </c:choose>
            </td>
            <form:form modelAttribute="adminSettings" action="admin#pauseGame" method="POST">
                <tr>
                    <td><input type="text" name="timerPeriod" value="${timerPeriod}"/></td>
                </tr>
                <tr>
                    <td>
                        <input type="hidden" name="game" value="${game}"/>
                        <input type="hidden" name="room" value="${room}"/>
                        <input type="submit" value="Set"/>
                    </td>
                </tr>
            </form:form>
        </tr>
    </table>

    <table class="admin-table" id="recordGame">
        <tr>
            <td>
                <c:choose>
                    <c:when test="${recording}">
                        <b>The recording is active</b></br>
                        <a href="${ctx}/admin/recording/stop?room=${room}#recordGame">Stop recording</a>.
                    </c:when>
                    <c:otherwise>
                        <b>The recording was suspended</b></br>
                        <a href="${ctx}/admin/recording/start?room=${room}#recordGame">Start recording</a>.
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>

    <table class="admin-table" id="debug">
        <tr>
            <td>
                <c:choose>
                    <c:when test="${debugLog}">
                        <b>The debug in progress</b></br>
                        <a href="${ctx}/admin/debug/stop?room=${room}#debug">Stop debug</a>.
                    </c:when>
                    <c:otherwise>
                        <b>The debug was suspended</b></br>
                        <a href="${ctx}/admin/debug/start?room=${room}#debug">Start debug</a>.
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>

    <table class="admin-table" id="autoSave">
        <tr>
            <td>
                <c:choose>
                    <c:when test="${autoSave}">
                        <b>The auto save in progress</b></br>
                        <a href="${ctx}/admin/autoSave/stop?room=${room}#autoSave">Stop auto save</a>.
                    </c:when>
                    <c:otherwise>
                        <b>The auto save was suspended</b></br>
                        <a href="${ctx}/admin/autoSave/start?room=${room}#autoSave">Start auto save</a>.
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>

    <table class="admin-table" id="closeRegistration">
        <tr>
            <td>
                <c:choose>
                    <c:when test="${opened}">
                        <b><spring:message key="registration.active"/></b></br>
                        <a href="${ctx}/admin/registration/stop?room=${room}#closeRegistration">Close registration</a>.
                    </c:when>
                    <c:otherwise>
                        <b><spring:message key="registration.closed"/></b></br>
                        <a href="${ctx}/admin/registration/start?room=${room}#closeRegistration">Open registration</a>.
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>

    <table class="admin-table" id="closeRoomRegistration">
        <tr>
            <td>
                <c:choose>
                    <c:when test="${roomOpened}">
                        <b><spring:message key="registration.room.active"/></b></br>
                        <a href="${ctx}/admin/room/registration/stop?room=${room}#closeRoomRegistration">Close room registration</a>.
                    </c:when>
                    <c:otherwise>
                        <b><spring:message key="registration.room.closed"/></b></br>
                        <a href="${ctx}/admin/room/registration/start?room=${room}#closeRoomRegistration">Open room registration</a>.
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>

    <table class="admin-table" id="cleanGame">
        <tr>
            <tr colspan="2">
                <td><b>Clean / Reset</b></td>
            </tr>
            <td>
                <a href="${ctx}/admin/game/scores/cleanAll?room=${room}#cleanGame">Clean all scores</a>. </br>
                <a href="${ctx}/admin/game/board/reloadAll?room=${room}#cleanGame">Reload all rooms</a>. Not working for !disposable rooms. </br>
                <a href="${ctx}/admin/player/reloadAll?room=${room}#cleanGame">Reload all players</a>. Through saves: saveAll -> removeAll -> loadAll
            </td>
        </tr>
    </table>

    <form:form modelAttribute="adminSettings" action="admin#loadSaveForAll" method="POST">
        <table class="admin-table" id="loadSaveForAll">
            <tr>
                <td><b>Load save (progress) for all</b></td>
            </tr>
            <tr>
                <td><input type="text" class="player-save" name="progress" value="${defaultProgress}"/></td>
            </tr>
            <tr>
                <td>
                    <input type="hidden" name="game" value="${game}"/>
                    <input type="hidden" name="room" value="${room}"/>
                    <input type="submit" value="Apply for all"/>
                </td>
            </tr>
        </table>
    </form:form>

    <table class="admin-table" id="cleanGame">
        <tr>
            <td><b>Registration settings</b></td>
        </tr>
        <tr>
            <td>
                <input id="show-games" type="checkbox">
                <label class="check-label" for="show-games"></label>
                <span>Show GamesList on registration</span>
            </td>
        </tr>
        <tr>
            <td>
                <input id="show-names" type="checkbox">
                <label class="check-label" for="show-names"></label>
                <span>Show First/Last names on registration</span>
            </td>
        </tr>
        <tr>
            <td>
                <input id="show-data1" type="checkbox">
                <label class="check-label" for="show-data1"></label>
                <span>Show Country/City on registration</span>
            </td>
        </tr>
        <tr>
            <td>
                <input id="show-data2" type="checkbox">
                <label class="check-label" for="show-data2"></label>
                <span>Show TechSkills on registration</span>
            </td>
        </tr>
        <tr>
            <td>
                <input id="show-data3" type="checkbox">
                <label class="check-label" for="show-data3"></label>
                <span>Show Company/Position on registration</span>
            </td>
        </tr>
        <tr>
            <td>
                <input id="show-data4" type="checkbox">
                <label class="check-label" for="show-data4"></label>
                <span>Show Experience on registration</span>
            </td>
        </tr>
        <tr>
            <td>
                <span class="white">Default game</span>
                <select placeholder="Select default game" id="default-game">
                    <!--option value="Type1">Type1</option-->
                    <!--option value="Type2">Type2</option-->
                    <!--option value="Type3">Type3</option-->
                </select>
            </td>
        </tr>
        <tr>
            <td>
                <button id="registration-save-button" class="button save">Save settings</button>
            </td>
        </tr>
    </table>

    <form:form modelAttribute="adminSettings" action="admin#createNewUsers" method="POST">
        <table class="admin-table" id="createNewUsers">
            <tr colspan="2">
                <td><b>Create new users</b></td>
            </tr>
            <tr>
                <td>NameMask</td>
                <td>Count</td>
                <td>RoomName</td>
            <tr>
                <td><input type="text" name="generateNameMask" value="${generateNameMask}"/></td>
                <td><input type="text" name="generateCount" value="${generateCount}"/></td>
                <td><input type="text" name="generateRoom" value="${generateRoom}"/></td>
            </tr>
            <tr>
                <td>
                    <input type="hidden" name="game" value="${game}"/>
                    <input type="hidden" name="room" value="${room}"/>
                    <input type="submit" value="Create"/>
                </td>
            </tr>
        </table>
    </form:form>

    <c:if test="${not empty adminSettings.rounds.parameters}">
        <form:form modelAttribute="adminSettings" action="admin#rounds" method="POST">
            <table class="admin-table" id="rounds">
                <tr colspan="2">
                    <td><b>Rounds settings</b></td>
                </tr>
                <tr>
                    <td>Enable Rounds</td>
                    <td><form:checkbox path="rounds.roundsEnabled"/></td>
                <tr>
                <tr>
                    <td>Players per room</td>
                    <td><form:input path="rounds.playersPerRoom"/></td>
                </tr>
                <tr>
                    <td>Time per Round</td>
                    <td><form:input path="rounds.timePerRound"/></td>
                </tr>
                <tr>
                    <td>Time for Winner</td>
                    <td><form:input path="rounds.timeForWinner"/></td>
                </tr>
                <tr>
                    <td>Time before start Round</td>
                    <td><form:input path="rounds.timeBeforeStart"/></td>
                </tr>
                <tr>
                    <td>Rounds per Match</td>
                    <td><form:input path="rounds.roundsPerMatch"/></td>
                </tr>
                <tr>
                    <td>Min ticks for win</td>
                    <td><form:input path="rounds.minTicksForWin"/></td>
                </tr>
                <tr>
                    <td>
                        <input type="hidden" name="game" value="${game}"/>
                        <input type="hidden" name="room" value="${room}"/>
                        <input type="submit" value="Save"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </c:if>

    <c:if test="${not empty adminSettings.semifinal.parameters}">
        <form:form modelAttribute="adminSettings" action="admin#semifinal" method="POST">
            <table class="admin-table" id="semifinal">
                <tr colspan="2">
                    <td><b>Semifinal settings</b></td>
                </tr>
                <tr>
                    <td>Enable semifinal</td>
                    <td><form:checkbox path="semifinal.enabled"/></td>
                <tr>
                <tr>
                    <td>Ticks before recalculation</td>
                    <td><form:input path="semifinal.timeout"/></td>
                </tr>
                <tr>
                    <td>Current tick</td>
                    <td>${semifinalTick}</td>
                <tr>
                <tr>
                    <td>Percentage or quantitative criterion</td>
                    <td><form:checkbox path="semifinal.percentage"/></td>
                </tr>
                <tr>
                    <td>Finalists limit (% or count)</td>
                    <td><form:input path="semifinal.limit"/></td>
                </tr>
                <tr>
                    <td>Reset board after recalculation</td>
                    <td><form:checkbox path="semifinal.resetBoard"/></td>
                </tr>
                <tr>
                    <td>Shuffle board after recalculation</td>
                    <td><form:checkbox path="semifinal.shuffleBoard"/></td>
                </tr>
                <tr>
                    <td>
                        <input type="hidden" name="game" value="${game}"/>
                        <input type="hidden" name="room" value="${room}"/>
                        <input type="submit" value="Save"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </c:if>

    <c:if test="${parameters.size() != 0}">
        <form:form modelAttribute="adminSettings" action="admin#gameSettings" method="POST">
            <table class="admin-table" id="gameSettings">
                <tr colspan="2">
                    <td><b>Game settings</b></td>
                </tr>
                <c:forEach items="${settings}" var="element" varStatus="status">
                    <tr>
                        <td>${element.name}</td>
                        <c:choose>
                            <c:when test="${element.type == 'selectbox'}">
                                <td><form:select path="parameters[${status.index}]"
                                        items="${element.options}"/></td>
                            </c:when>
                            <c:when test="${element.type == 'editbox' && !element.multiline}">
                                <td><form:input path="parameters[${status.index}]"/></td>
                            </c:when>
                            <c:when test="${element.type == 'editbox' && element.multiline}">
                                <td><form:textarea rows="5" cols="50" path="parameters[${status.index}]"/></td>
                            </c:when>
                            <c:when test="${element.type == 'checkbox'}">
                                <td><form:checkbox path="parameters[${status.index}]"/></td>
                            </c:when>
                            <c:otherwise>
                                <td>${parameter.value}</td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:forEach>
                <tr>
                    <td>
                        <input type="hidden" name="game" value="${game}"/>
                        <input type="hidden" name="room" value="${room}"/>
                        <input type="submit" value="Save"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </c:if>

    <c:if test="${players != null || savedGames != null}">
        <form:form modelAttribute="adminSettings" action="admin#savePlayersGame" method="POST">
            <table class="admin-table" id="savePlayersGame">
                <tr colspan="4">
                    <td><b>Registered Players</b></td>
                </tr>
                <tr>
                    <td class="header">PlayerId</td>
                    <td class="header">PlayerName</td>
                    <td class="header">RoomName</td>
                    <td class="header">Score</td>
                    <td class="header">IP</td>
                    <td class="header">Joystick</td>
                    <td class="header">GameName&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                    <td>
                        <a href="${ctx}/admin/player/saveAll?room=${room}#savePlayersGame">SaveAll</a>&nbsp;&nbsp;
                    </td>
                    <td>
                        <a href="${ctx}/admin/player/loadAll?room=${room}#savePlayersGame">LoadAll</a>&nbsp;&nbsp;
                    </td>
                    <td>
                        <a href="${ctx}/admin/player/save/removeAll?room=${room}#savePlayersGame">RemoveSaveAll</a>&nbsp;&nbsp;
                    </td>
                    <td>
                        <a href="${ctx}/admin/player/registration/removeAll?room=${room}#savePlayersGame">RemoveRegAll</a>&nbsp;&nbsp;
                    </td>
                    <td>
                        <a href="${ctx}/admin/player/gameOverAll?room=${room}#savePlayersGame">GameOverAll</a>&nbsp;&nbsp;
                    </td>
                    <td>
                        <a href="${ctx}/board/game/${game}">ViewGameAll</a>&nbsp;&nbsp;
                    </td>
                    <td class="header">PlayerLogAll</td>
                    <td>
                        <a href="${ctx}/admin/player/ai/reloadAll?room=${room}#savePlayersGame">LoadAIAll</a>&nbsp;&nbsp;
                    </td>
                    <td class="header">Save data&nbsp;&nbsp;</td>
                </tr>
                <c:forEach items="${players}" var="player" varStatus="status">
                    <c:choose>
                        <c:when test="${player.hidden}">
                            <tr style="display:none;">
                        </c:when>
                        <c:otherwise>
                            <tr style="">
                        </c:otherwise>
                    </c:choose>
                        <c:choose>
                            <c:when test="${player.active}">
                                <td><form:input class="input-id" readonly="true" index="${status.index}" path="players[${status.index}].id"/></td>
                                <td><form:input class="input-readable" path="players[${status.index}].readableName"/></td>
                                <td><form:input class="input-room" path="players[${status.index}].room"/></td>
                                <td><form:input class="input-score" path="players[${status.index}].score"/></td>
                                <td><form:input class="input-callback" path="players[${status.index}].callbackUrl"/></td>
                                <c:choose>
                                    <c:when test="${player.code != null}">
                                        <td class="joystick">
                                            <span class="a" href="${ctx}/joystick?command=up&player=${player.id}&code=${player.code}#savePlayersGame">U</span>
                                            <span class="a" href="${ctx}/joystick?command=down&player=${player.id}&code=${player.code}#savePlayersGame">D</span>
                                            <span class="a" href="${ctx}/joystick?command=left&player=${player.id}&code=${player.code}#savePlayersGame">L</span>
                                            <span class="a" href="${ctx}/joystick?command=right&player=${player.id}&code=${player.code}#savePlayersGame">R</span>
                                            <span class="a" href="${ctx}/joystick?command=act&player=${player.id}&code=${player.code}#savePlayersGame">A</span>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>UDLRA</td>
                                    </c:otherwise>
                                </c:choose>
                                <td><a href="${ctx}/board/game/${player.game}">${player.game}</a></td>
                                <td><a href="${ctx}/admin/player/${player.id}/save?room=${room}#savePlayersGame">Save</a></td>
                                <c:choose>
                                    <c:when test="${player.saved}">
                                        <td><a href="${ctx}/admin/player/${player.id}/load?room=${room}#savePlayersGame">Load</a></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>Load</td>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${player.saved}">
                                        <td><a href="${ctx}/admin/player/${player.id}/save/remove?room=${room}#savePlayersGame">RemoveSave</a></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>RemoveSave</td>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${player.code != null}">
                                        <td><a href="${ctx}/admin/player/${player.id}/registration/remove?room=${room}#savePlayersGame">RemoveReg</a></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>RemoveReg</td>
                                    </c:otherwise>
                                </c:choose>
                                <td><a href="${ctx}/admin/player/${player.id}/gameOver?room=${room}#savePlayersGame">GameOver</a></td>
                                <td><a href="${ctx}/board/player/${player.id}?code=${player.code}">ViewGame</a></td>
                                <c:choose>
                                    <c:when test="${player.code != null}">
                                        <td><a href="${ctx}/board/log/player/${player.id}?code=${player.code}&game=${game}&room=${room}">PlayerLog</a></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>PlayerLog</td>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${player.aiPlayer}">
                                        <td>Loaded</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td><a href="${ctx}/admin/player/${player.id}/ai/reload?room=${room}#savePlayersGame">LoadAI</a></td>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${player.data == null}">
                                        <td></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td><form:input class="player-save" path="players[${status.index}].data"/></td>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>

                            <c:otherwise>
                                <td><input type="text" readonly="true" class="input-id"       value="${player.id}"/></td>
                                <td><input type="text" readonly="true" class="input-readable" value="${player.readableName}"/></td>
                                <td><input type="text" readonly="true" class="input-room" value="${player.room}"/></td>
                                <td><input type="text" readonly="true" class="input-score"    value="${player.score}"/></td>
                                <td><input type="text" readonly="true" class="input-callback" value="${player.callbackUrl}"/></td>
                                <td>UDLRA</td>
                                <td><a href="${ctx}/board/game/${player.game}">${player.game}</a></td>
                                <td>Save</td>
                                <c:choose>
                                    <c:when test="${player.saved}">
                                        <td><a href="${ctx}/admin/player/${player.id}/load?room=${room}#savePlayersGame">Load</a></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>Load</td>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${player.saved}">
                                        <td><a href="${ctx}/admin/player/${player.id}/save/remove?room=${room}#savePlayersGame">RemoveSave</a></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>RemoveSave</td>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${player.code != null}">
                                        <td><a href="${ctx}/admin/player/${player.id}/registration/remove?room=${room}#savePlayersGame">RemoveReg</a></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>RemoveReg</td>
                                    </c:otherwise>
                                </c:choose>
                                <td>GameOver</td>
                                <td>ViewGame</td>
                                <c:choose>
                                    <c:when test="${player.code != null}">
                                        <td><a href="${ctx}/board/log/player/${player.id}?code=${player.code}&game=${game}&room=${room}">PlayerLog</a></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>PlayerLog</td>
                                    </c:otherwise>
                                </c:choose>
                                <td>LoadAI</td>
                                <c:choose>
                                    <c:when test="${player.data == null}">
                                        <td></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td><form:input class="player-save" path="players[${status.index}].data"/></td>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:forEach>
                <tr>
                    <td>
                        <input type="hidden" name="game" value="${game}"/>
                        <input type="hidden" name="room" value="${room}"/>
                        <input type="submit" value="Save all"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </c:if>

    </br>
    Go to <a href="${ctx}/">main page</a>.
</body>
</html>
