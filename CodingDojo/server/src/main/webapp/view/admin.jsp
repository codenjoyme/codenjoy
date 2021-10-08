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

<%@ page trimDirectiveWhitespaces="true" %>

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
    <div id="settings"
         page="${page}"
         contextPath="${ctx}"
         game="${data.game}"
         room="${data.room}"></div>

    <%@include file="forkMe.jsp"%>

    <div class="page-header">
        <h1>Admin page</h1>
    </div>

    <table class="admin-table" id="gameVersion">
        <tr>
            <td>
                <b>Room:</b> ${data.room}
            </td>
        <tr>
        </tr>
            <td>
                <b>Game:</b> ${data.game}
            </td>
        </tr>
        <tr>
            <td>
                <b>Game version:</b>
            </td>
            <td style="width:500px;">
                <textarea rows="3" cols="45">${data.gameVersion}</textarea>
            </td>
        </tr>
    </table>

    <form:form modelAttribute="data" action="admin#activeGames" method="POST">
        <table class="admin-table" id="activeGames">
            <tr>
                <td colspan="3" style="width:300px;">
                    <b>Active games for participants</b>
                </td>
            </tr>
            <c:forEach items="${data.gamesRooms.all}" var="gameRooms" varStatus="status">
                <tr game="${gameRooms.game}">
                    <td class="rightStep">
                        <form:checkbox id="enable-games-${gameRooms.game}" path="games[${status.index}]"/>
                        <label class="check-label" for="enable-games-${gameRooms.game}"></label>
                        <span>${gameRooms.game}</span>
                    </td>
                    <td class="rightStep">
                        <a id="game-${gameRooms.game}"
                           href="${ctx}/admin?game=${gameRooms.game}">game</a>
                    </td>
                    <td class="rightStep">
                        <c:forEach items="${gameRooms.rooms}" var="roomItem" varStatus="status2">
                            <a class="bold-room-${roomItem == room}"
                               room="${roomItem}"
                               href="${ctx}/admin?room=${roomItem}">
                                    ${roomItem}<span class="pow">${data.playersCount.get(roomItem)}</span></a>&nbsp;
                        </c:forEach>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td>
                    <input type="hidden" name="game" value="${data.game}"/>
                    <input type="hidden" name="room" value="${data.room}"/>
                    <input type="submit" value="Save"/>
                </td>
            </tr>
        </table>
    </form:form>

    <table class="admin-table" id="pauseGame">
        <tr>
            <td>
                <c:choose>
                    <c:when test="${data.active}">
                        <b><spring:message key="game.active"/></b></br>
                        <a href="${ctx}/admin/game/pause?room=${data.room}#pauseGame">Pause game</a>.
                    </c:when>
                    <c:otherwise>
                        <b><spring:message key="game.suspended"/></b></br>
                        <a href="${ctx}/admin/game/resume?room=${data.room}#pauseGame">Resume game</a>.
                    </c:otherwise>
                </c:choose>
            </td>
            <form:form modelAttribute="data" action="admin#pauseGame" method="POST">
                <tr>
                    <td><form:input path="timerPeriod"/></td>
                </tr>
                <tr>
                    <td>
                        <input type="hidden" name="game" value="${data.game}"/>
                        <input type="hidden" name="room" value="${data.room}"/>
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
                    <c:when test="${data.recording}">
                        <b>The recording is active</b></br>
                        <a href="${ctx}/admin/recording/stop?room=${data.room}#recordGame">Stop recording</a>.
                    </c:when>
                    <c:otherwise>
                        <b>The recording was suspended</b></br>
                        <a href="${ctx}/admin/recording/start?room=${data.room}#recordGame">Start recording</a>.
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>

    <table class="admin-table" id="debug">
        <tr>
            <td>
                <c:choose>
                    <c:when test="${data.debugLog}">
                        <b>The debug in progress</b></br>
                        <a href="${ctx}/admin/debug/stop?room=${data.room}#debug">Stop debug</a>.
                    </c:when>
                    <c:otherwise>
                        <b>The debug was suspended</b></br>
                        <a href="${ctx}/admin/debug/start?room=${data.room}#debug">Start debug</a>.
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>

    <table class="admin-table" id="autoSave">
        <tr>
            <td>
                <c:choose>
                    <c:when test="${data.autoSave}">
                        <b>The auto save in progress</b></br>
                        <a href="${ctx}/admin/autoSave/stop?room=${data.room}#autoSave">Stop auto save</a>.
                    </c:when>
                    <c:otherwise>
                        <b>The auto save was suspended</b></br>
                        <a href="${ctx}/admin/autoSave/start?room=${data.room}#autoSave">Start auto save</a>.
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>

    <table class="admin-table" id="closeRegistration">
        <tr>
            <td>
                <c:choose>
                    <c:when test="${data.opened}">
                        <b><spring:message key="registration.active"/></b></br>
                        <a href="${ctx}/admin/registration/stop?room=${data.room}#closeRegistration">Close registration</a>.
                    </c:when>
                    <c:otherwise>
                        <b><spring:message key="registration.closed"/></b></br>
                        <a href="${ctx}/admin/registration/start?room=${data.room}#closeRegistration">Open registration</a>.
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>

    <table class="admin-table" id="closeRoomRegistration">
        <tr>
            <td>
                <c:choose>
                    <c:when test="${data.roomOpened}">
                        <b><spring:message key="registration.room.active"/></b></br>
                        <a href="${ctx}/admin/room/registration/stop?room=${data.room}#closeRoomRegistration">Close room registration</a>.
                    </c:when>
                    <c:otherwise>
                        <b><spring:message key="registration.room.closed"/></b></br>
                        <a href="${ctx}/admin/room/registration/start?room=${data.room}#closeRoomRegistration">Open room registration</a>.
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
                <a href="${ctx}/admin/game/scores/cleanAll?room=${data.room}#cleanGame">Clean all scores</a>. </br>
                <a href="${ctx}/admin/game/board/reloadAll?room=${data.room}#cleanGame">Reload all rooms</a>. Not working for !disposable rooms. </br>
                <a href="${ctx}/admin/player/reloadAll?room=${data.room}#cleanGame">Reload all players</a>. Through saves: saveAll -> removeAll -> loadAll
            </td>
        </tr>
    </table>

    <form:form modelAttribute="data" action="admin#loadSaveForAll" method="POST">
        <table class="admin-table" id="loadSaveForAll">
            <tr>
                <td><b>Load save (progress) for all</b></td>
            </tr>
            <tr>
                <td><form:input path="progress"/></td>
            </tr>
            <tr>
                <td>
                    <input type="hidden" name="game" value="${data.game}"/>
                    <input type="hidden" name="room" value="${data.room}"/>
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
                <input id="show-game-modes" type="checkbox">
                <label class="check-label" for="show-game-modes"></label>
                <span>Show games modes on registration/login</span>
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
                <span class="white">Default game mode</span>
                <select placeholder="Select default game mode" id="default-game-mode">
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

    <form:form modelAttribute="data" action="admin#createNewUsers" method="POST">
        <table class="admin-table" id="createNewUsers">
            <tr colspan="2">
                <td><b>Create new users</b></td>
            </tr>
            <tr>
                <td>NameMask</td>
                <td>Count</td>
                <td>RoomName</td>
            <tr>
                <td><form:input path="generateNameMask"/></td>
                <td><form:input path="generateCount"/></td>
                <td><form:input path="generateRoom"/></td>
            </tr>
            <tr>
                <td>
                    <input type="hidden" name="game" value="${data.game}"/>
                    <input type="hidden" name="room" value="${data.room}"/>
                    <input type="submit" value="Create"/>
                </td>
            </tr>
        </table>
    </form:form>

    <c:if test="${not empty data.rounds.parameters}">
        <form:form modelAttribute="data" action="admin#rounds" method="POST">
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
                    <td>Teams per room</td>
                    <td><form:input path="rounds.teamsPerRoom"/></td>
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
                        <input type="hidden" name="game" value="${data.game}"/>
                        <input type="hidden" name="room" value="${data.room}"/>
                        <input type="submit" value="Save"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </c:if>

    <c:if test="${not empty data.semifinal.parameters}">
        <form:form modelAttribute="data" action="admin#semifinal" method="POST">
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
                    <td>${data.semifinalTick}</td>
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
                    <td>Clear scores</td>
                    <td><form:checkbox path="semifinal.clearScores"/></td>
                </tr>
                <tr>
                    <td>
                        <input type="hidden" name="game" value="${data.game}"/>
                        <input type="hidden" name="room" value="${data.room}"/>
                        <input type="submit" value="Save"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </c:if>

    <c:if test="${not empty data.inactivity.parameters}">
        <form:form modelAttribute="data" action="admin#inactivity" method="POST">
            <table class="admin-table" id="inactivity">
                <tr colspan="2">
                    <td><b>Inactivity settings</b></td>
                </tr>
                <tr>
                    <td>Enable kick inactive players</td>
                    <td><form:checkbox path="inactivity.kickEnabled"/></td>
                <tr>
                <tr>
                    <td>Inactive ticks before kick</td>
                    <td><form:input path="inactivity.inactivityTimeout"/></td>
                </tr>
                <tr>
                    <td>
                        <input type="hidden" name="game" value="${data.game}"/>
                        <input type="hidden" name="room" value="${data.room}"/>
                        <input type="submit" value="Save"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </c:if>

    <c:if test="${not empty data.other}">
        <form:form modelAttribute="data" action="admin#gameSettings" method="POST">
            <table class="admin-table" id="gameSettings">
                <tr colspan="2">
                    <td><b>Game settings</b></td>
                </tr>
                <c:forEach items="${data.other}" var="element" varStatus="status">
                    <tr>
                        <td>${element.name}</td>
                        <c:choose>
                            <c:when test="${element.type == 'selectbox'}">
                                <td><form:select path="otherValues[${status.index}]"
                                        items="${element.options}"/></td>
                            </c:when>
                            <c:when test="${element.type == 'editbox' && !element.multiline}">
                                <td><form:input path="otherValues[${status.index}]"/></td>
                            </c:when>
                            <c:when test="${element.type == 'editbox' && element.multiline}">
                                <td><form:textarea rows="5" cols="50" path="otherValues[${status.index}]"/></td>
                            </c:when>
                            <c:when test="${element.type == 'checkbox'}">
                                <td><form:checkbox path="otherValues[${status.index}]"/></td>
                            </c:when>
                            <c:otherwise>
                                <td>${element.value}</td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:forEach>
                <tr>
                    <td>
                        <input type="hidden" name="game" value="${data.game}"/>
                        <input type="hidden" name="room" value="${data.room}"/>
                        <input type="submit" value="Save"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </c:if>

    <c:if test="${not empty data.levels.parameters}">
        <form:form modelAttribute="data" action="admin#levels" method="POST">
            <table class="admin-table" id="levels">
                <tr colspan="2">
                    <td><b>Game levels settings</b></td>
                </tr>
                <c:forEach items="${data.levels.parameters}" var="element" varStatus="status">
                    <tr>
                        <td>${element.name}</td>
                        <c:choose>
                            <c:when test="${element.type == 'editbox' && element.multiline}">
                                <td><form:textarea rows="5" cols="50" path="levelsValues[${status.index}]"/></td>
                            </c:when>
                            <c:otherwise>
                                <td>${element.value}</td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:forEach>
                <tr>
                    <td>
                        <input type="hidden" name="game" value="${data.game}"/>
                        <input type="hidden" name="room" value="${data.room}"/>
                        <input type="submit" value="Save"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </c:if>

    <c:if test="${not empty data.players || savedGames != null}">
        <form:form modelAttribute="data" action="admin#savePlayersGame" method="POST">
            <table class="admin-table" id="savePlayersGame">
                <tr colspan="4">
                    <td><b>Registered Players</b></td>
                </tr>
                <tr>
                    <td class="header">PlayerId&nbsp;&nbsp;</td>
                    <td class="header">Code&nbsp;&nbsp;</td>
                    <td class="header">PlayerName&nbsp;&nbsp;</td>
                    <td class="header">Email&nbsp;&nbsp;</td>
                    <td class="header">RoomName&nbsp;&nbsp;</td>
                    <td class="header">Team&nbsp;&nbsp;</td>
                    <td class="header">Score&nbsp;&nbsp;</td>
                    <td class="header">IP&nbsp;&nbsp;</td>
                    <td class="header">Inactive&nbsp;&nbsp;</td>
                    <td class="header">Joystick&nbsp;&nbsp;</td>
                    <td class="header">GameName&nbsp;&nbsp;</td>
                    <td>
                        <a href="${ctx}/admin/player/saveAll?room=${data.room}#savePlayersGame">SaveAll</a>&nbsp;&nbsp;
                    </td>
                    <td>
                        <a href="${ctx}/admin/player/loadAll?room=${data.room}#savePlayersGame">LoadAll</a>&nbsp;&nbsp;
                    </td>
                    <td>
                        <a href="${ctx}/admin/player/save/removeAll?room=${data.room}#savePlayersGame">RemoveSaveAll</a>&nbsp;&nbsp;
                    </td>
                    <td>
                        <a href="${ctx}/admin/player/registration/removeAll?room=${data.room}#savePlayersGame">RemoveRegAll</a>&nbsp;&nbsp;
                    </td>
                    <td>
                        <a href="${ctx}/admin/player/gameOverAll?room=${data.room}#savePlayersGame">GameOverAll</a>&nbsp;&nbsp;
                    </td>
                    <td>
                        <a href="${ctx}/board/room/${data.room}">ViewGameAll</a>&nbsp;&nbsp;
                    </td>
                    <td class="header">PlayerLogAll</td>
                    <td>
                        <a href="${ctx}/admin/player/ai/reloadAll?room=${data.room}#savePlayersGame">LoadAIAll</a>&nbsp;&nbsp;
                    </td>
                    <td class="header">Save data&nbsp;&nbsp;</td>
                </tr>
                <c:forEach items="${data.players}" var="player" varStatus="status">
                    <c:choose>
                        <c:when test="${player.hidden}">
                            <tr style="display:none;">
                        </c:when>
                        <c:otherwise>
                            <tr style="" player="${player.email}">
                        </c:otherwise>
                    </c:choose>
                        <c:choose>
                            <c:when test="${player.active}">
                                <td><form:input class="input-id"       path="players[${status.index}].id"   readonly="true" index="${status.index}"/></td>
                                <td><form:input class="input-code"     path="players[${status.index}].code" readonly="true"/></td>
                                <td><form:input class="input-readable" path="players[${status.index}].readableName"/></td>
                                <td><form:input class="input-email"    path="players[${status.index}].email"/></td>
                                <td><form:input class="input-room"     path="players[${status.index}].room"/></td>
                                <td><form:input class="input-team"     path="players[${status.index}].teamId"/></td>
                                <td><form:input class="input-score"    path="players[${status.index}].score"/></td>
                                <td><form:input class="input-callback" path="players[${status.index}].callbackUrl"/></td>
                                <td>&nbsp;<span class="input-ticks-inactive">${player.ticksInactive}</span>&nbsp;</td>
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
                                <td><a href="${ctx}/board/room/${data.room}">${data.room}</a></td>
                                <td><a href="${ctx}/admin/player/${player.id}/save?room=${data.room}#savePlayersGame">Save</a></td>
                                <c:choose>
                                    <c:when test="${player.saved}">
                                        <td><a href="${ctx}/admin/player/${player.id}/load?room=${data.room}#savePlayersGame">Load</a></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>Load</td>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${player.saved}">
                                        <td><a href="${ctx}/admin/player/${player.id}/save/remove?room=${data.room}#savePlayersGame">RemoveSave</a></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>RemoveSave</td>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${player.code != null}">
                                        <td><a href="${ctx}/admin/player/${player.id}/registration/remove?room=${data.room}#savePlayersGame">RemoveReg</a></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>RemoveReg</td>
                                    </c:otherwise>
                                </c:choose>
                                <td><a href="${ctx}/admin/player/${player.id}/gameOver?room=${data.room}#savePlayersGame">GameOver</a></td>
                                <td><a href="${ctx}/board/player/${player.id}?code=${player.code}">ViewGame</a></td>
                                <c:choose>
                                    <c:when test="${player.code != null}">
                                        <td><a href="${ctx}/board/log/player/${player.id}?code=${player.code}&game=${data.game}&room=${data.room}">PlayerLog</a></td>
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
                                        <td><a href="${ctx}/admin/player/${player.id}/ai/reload?room=${data.room}#savePlayersGame">LoadAI</a></td>
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
                                <td><input type="text" readonly="true" class="input-code"     value="${player.code}"/></td>
                                <td><input type="text" readonly="true" class="input-readable" value="${player.readableName}"/></td>
                                <td><input type="text" readonly="true" class="input-email"    value="${player.email}"/></td>
                                <td><input type="text" readonly="true" class="input-room"     value="${player.room}"/></td>
                                <td><input type="text" readonly="true" class="input-team"     value="${player.teamId}"/></td>
                                <td><input type="text" readonly="true" class="input-score"    value="${player.score}"/></td>
                                <td><input type="text" readonly="true" class="input-callback" value="${player.callbackUrl}"/></td>
                                <td>&nbsp;<span class="input-ticks-inactive"></span>&nbsp;</td>
                                <td>UDLRA</td>
                                <td><a href="${ctx}/board/room/${data.room}">${data.room}</a></td>
                                <td>Save</td>
                                <c:choose>
                                    <c:when test="${player.saved}">
                                        <td><a href="${ctx}/admin/player/${player.id}/load?room=${data.room}#savePlayersGame">Load</a></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>Load</td>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${player.saved}">
                                        <td><a href="${ctx}/admin/player/${player.id}/save/remove?room=${data.room}#savePlayersGame">RemoveSave</a></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>RemoveSave</td>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${player.code != null}">
                                        <td><a href="${ctx}/admin/player/${player.id}/registration/remove?room=${data.room}#savePlayersGame">RemoveReg</a></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>RemoveReg</td>
                                    </c:otherwise>
                                </c:choose>
                                <td>GameOver</td>
                                <td>ViewGame</td>
                                <c:choose>
                                    <c:when test="${player.code != null}">
                                        <td><a href="${ctx}/board/log/player/${player.id}?code=${player.code}&game=${data.game}&room=${data.room}">PlayerLog</a></td>
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
                        <input type="hidden" name="game" value="${data.game}"/>
                        <input type="hidden" name="room" value="${data.room}"/>
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
