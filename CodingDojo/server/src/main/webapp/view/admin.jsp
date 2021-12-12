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

    <form:form modelAttribute="data" action="admin#gameRoomStatus" method="POST">
        <input type="hidden" name="game" value="${data.game}"/>
        <input type="hidden" name="room" value="${data.room}"/>

        <table class="admin-table" id="gameRoomStatus">
            <tr>
                <td>
                    <b>Room: </b><span id="room">${data.room}</span>
                </td>
            </tr>
            <tr>
                <td>
                    <input type="submit" name="action"
                           value="${data.actions.deleteRoom}"/>
                </td>
                <td class="info">
                    You cannot delete the default game room. When you delete a room, <br>
                    all players and their saves will also be removed from it.
                </td>
            </tr>
            <tr>
                <td>
                    <input type="text" name="newRoom" value=""/><br>
                    <input type="submit" name="action"
                           value="${data.actions.createRoom}"/>
                </td>
                <td class="info">
                    We will create a new empty room and show its settings.
                </td>
            </tr>
            <tr>
                <td>
                    <b>Game: </b><span id="game">${data.game}</span>
                </td>
            </tr>
            <tr>
                <td>
                    <b>Game version:</b>
                </td>
                <td style="width:500px;">
                    <textarea class="version small" cols="95">${data.gameVersion}</textarea>
                </td>
            </tr>
        </table>
    </form:form>

    <table class="admin-table" id="statistic">
        <tr>
            <td>
                <b>Server time:</b> ${data.statistic.tickTime}
            </td>
        </tr>
        <tr>
            <td>
                <b>Deals:</b> ${data.statistic.dealsCount}
            </td>
        </tr>
        <tr>
            <td>
                <b>Screen updates:</b> ${data.statistic.screenUpdatesCount}
            </td>
        </tr>
        <tr>
            <td>
                <b>Request controls:</b> ${data.statistic.requestControlsCount}
            </td>
        </tr>
        <tr>
            <td>
                <b>Tick duration:</b> ${data.statistic.tickDuration} ms
            </td>
        </tr>
    </table>

    <form:form modelAttribute="data" action="admin#activeGames" method="POST">
        <input type="hidden" name="game" value="${data.game}"/>
        <input type="hidden" name="room" value="${data.room}"/>

        <table class="admin-table" id="activeGames">
            <tr>
                <td colspan="3" style="width:300px;">
                    <b>Active games for participants</b>
                </td>
            </tr>
            <c:forEach items="${data.gamesRooms.all}" var="gameRooms" varStatus="status">
                <tr game="${gameRooms.game}">
                    <td class="rightStep">
                        <form:checkbox id="enable-games-${gameRooms.game}" path="activeGames[${status.index}]"/>
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
                    <input type="submit" name="action"
                           value="${data.actions.saveActiveGames}"/>
                </td>
            </tr>
        </table>
    </form:form>

    <form:form modelAttribute="data" action="admin#pauseResumeGame" method="POST">
        <input type="hidden" name="game" value="${data.game}"/>
        <input type="hidden" name="room" value="${data.room}"/>

        <table class="admin-table" id="pauseResumeGame">
            <tr>
                <td>
                    <c:choose>
                        <c:when test="${data.active}">
                            <b><spring:message key="game.active"/></b></br>
                            <input type="submit" name="action"
                                   value="${data.actions.pauseGame}"/>
                        </c:when>
                        <c:otherwise>
                            <b><spring:message key="game.suspended"/></b></br>
                            <input type="submit" name="action"
                                   value="${data.actions.resumeGame}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>
    </form:form>

    <form:form modelAttribute="data" action="admin#setTimerPeriod" method="POST">
        <input type="hidden" name="game" value="${data.game}"/>
        <input type="hidden" name="room" value="${data.room}"/>

        <table class="admin-table" id="setTimerPeriod">
            <tr>
                <td><form:input path="timerPeriod"/></td>
            </tr>
            <tr>
                <td>
                    <input type="submit" name="action"
                           value="${data.actions.setTimerPeriod}"/>
                </td>
            </tr>
        </table>
    </form:form>

    <form:form modelAttribute="data" action="admin#recordGame" method="POST">
        <input type="hidden" name="game" value="${data.game}"/>
        <input type="hidden" name="room" value="${data.room}"/>

        <table class="admin-table" id="recordGame">
            <tr>
                <td>
                    <c:choose>
                        <c:when test="${data.recording}">
                            <b><spring:message key="recording.active"/></b></br>
                            <input type="submit" name="action"
                                   value="${data.actions.stopRecording}"/>
                        </c:when>
                        <c:otherwise>
                            <b><spring:message key="recording.suspended"/></b></br>
                            <input type="submit" name="action"
                                   value="${data.actions.startRecording}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>
    </form:form>

    <form:form modelAttribute="data" action="admin#debug" method="POST">
        <input type="hidden" name="game" value="${data.game}"/>
        <input type="hidden" name="room" value="${data.room}"/>

        <table class="admin-table" id="debug">
            <tr>
                <td>
                    <c:choose>
                        <c:when test="${data.debugLog}">
                            <b><spring:message key="debug.active"/></b></br>
                            <input type="submit" name="action"
                                   value="${data.actions.stopDebug}"/>
                        </c:when>
                        <c:otherwise>
                            <b><spring:message key="debug.suspended"/></b></br>
                            <input type="submit" name="action"
                                   value="${data.actions.startDebug}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr class="info">
                <td>
                    Feel free to choose any package or class you want.</br>
                    Format is 'NAME:LEVEL', where:</br>
                    &nbsp;&nbsp;&nbsp;&nbsp;NAME    = PACKAGE | PACKAGE.CLASS</br>
                    &nbsp;&nbsp;&nbsp;&nbsp;PACKAGE = 'com.codenjoy.dojo.services'</br>
                    &nbsp;&nbsp;&nbsp;&nbsp;CLASS   = 'GameServiceImpl'</br>
                    &nbsp;&nbsp;&nbsp;&nbsp;LEVEL   = 'ALL' | 'TRACE' | 'DEBUG' | 'INFO' | 'WARN' | 'ERROR' | 'OFF</br>
                    You can combine several loggers together, just press Enter and write in new line.</br>
                </td>
            </tr>
            <tr>
                <td>
                    <form:textarea class="loggers small" cols="90" path="loggersLevels"/>
                </td>
            </tr>
            <tr>
                <td>
                    <input type="submit" name="action"
                           value="${data.actions.updateLoggers}"/>
                </td>
            </tr>
        </table>
    </form:form>

    <form:form modelAttribute="data" action="admin#autoSave" method="POST">
        <input type="hidden" name="game" value="${data.game}"/>
        <input type="hidden" name="room" value="${data.room}"/>

        <table class="admin-table" id="autoSave">
            <tr>
                <td>
                    <c:choose>
                        <c:when test="${data.autoSave}">
                            <b><spring:message key="autoSave.active"/></b></br>
                            <input type="submit" name="action"
                                   value="${data.actions.stopAutoSave}"/>
                        </c:when>
                        <c:otherwise>
                            <b><spring:message key="autoSave.suspended"/></b></br>
                            <input type="submit" name="action"
                                   value="${data.actions.startAutoSave}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>
    </form:form>

    <form:form modelAttribute="data" action="admin#serverRegistration" method="POST">
        <input type="hidden" name="game" value="${data.game}"/>
        <input type="hidden" name="room" value="${data.room}"/>

        <table class="admin-table" id="serverRegistration">
            <tr>
                <td>
                    <c:choose>
                        <c:when test="${data.opened}">
                            <b><spring:message key="registration.active"/></b></br>
                            <input type="submit" name="action"
                                   value="${data.actions.closeRegistration}"/>
                        </c:when>
                        <c:otherwise>
                            <b><spring:message key="registration.suspended"/></b></br>
                            <input type="submit" name="action"
                                   value="${data.actions.openRegistration}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>
    </form:form>

    <form:form modelAttribute="data" action="admin#roomRegistration" method="POST">
        <input type="hidden" name="game" value="${data.game}"/>
        <input type="hidden" name="room" value="${data.room}"/>

        <table class="admin-table" id="roomRegistration">
            <tr>
                <td>
                    <c:choose>
                        <c:when test="${data.roomOpened}">
                            <b><spring:message key="registration.room.active"/></b></br>
                            <input type="submit" name="action"
                                   value="${data.actions.closeRoomRegistration}"/>
                        </c:when>
                        <c:otherwise>
                            <b><spring:message key="registration.room.suspended"/></b></br>
                            <input type="submit" name="action"
                                   value="${data.actions.openRoomRegistration}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>
    </form:form>

    <form:form modelAttribute="data" action="admin#reloadRoom" method="POST">
        <input type="hidden" name="game" value="${data.game}"/>
        <input type="hidden" name="room" value="${data.room}"/>

        <table class="admin-table" id="reloadRoom">
            <tr colspan="2">
                <td><b>Clean / Reset</b></td>
            <tr>
            </tr>
                <td>
                    <input type="submit" name="action"
                           value="${data.actions.cleanAllScores}"/>
                </td>
                <td class="info">
                    Clean all players scores. For some games, the field may also be updated.
                </td>
            </tr>
            <tr>
                <td>
                    <input type="submit" name="action"
                           value="${data.actions.reloadAllRooms}"/>
                </td>
                <td class="info">
                    Not working for !disposable rooms.
                </td>
            </tr>
            <tr>
                <td>
                    <input type="submit" name="action"
                           value="${data.actions.reloadAllPlayers}"/>
                </td>
                <td class="info">
                    Reload occurs through saves: saveAll -> removeAll -> loadAll.
                </td>
            </tr>
        </table>
    </form:form>

    <form:form modelAttribute="data" action="admin#loadSaveForAll" method="POST">
        <input type="hidden" name="game" value="${data.game}"/>
        <input type="hidden" name="room" value="${data.room}"/>

        <table class="admin-table" id="loadSaveForAll">
            <tr>
                <td><b>Load save (progress) for all</b></td>
            </tr>
            <tr>
                <td><form:input path="progress"/></td>
            </tr>
            <tr>
                <td>
                    <input type="submit" name="action"
                           value="${data.actions.loadSaveForAll}"/>
                </td>
            </tr>
        </table>
    </form:form>

    <table class="admin-table" id="registrationSettings">
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
                <button id="registration-save-button" class="button save">
                    ${data.actions.saveRegistrationFormSettings}</button>
            </td>
        </tr>
    </table>

    <form:form modelAttribute="data" action="admin#createDummyUsers" method="POST">
        <input type="hidden" name="game" value="${data.game}"/>
        <input type="hidden" name="room" value="${data.room}"/>

        <table class="admin-table" id="createDummyUsers">
            <tr colspan="2">
                <td><b>Create new users</b></td>
            </tr>
            <tr>
                <td>NameMask</td>
                <td>Count</td>
            </tr>
            <tr>
                <td><form:input path="generateNameMask"/></td>
                <td><form:input path="generateCount"/></td>
            </tr>
            <tr>
                <td>
                    <input type="submit" name="action"
                           value="${data.actions.createDummyUsers}"/>
                </td>
            </tr>
        </table>
    </form:form>

    <c:if test="${not empty data.rounds.parameters}">
        <form:form modelAttribute="data" action="admin#rounds" method="POST">
            <input type="hidden" name="game" value="${data.game}"/>
            <input type="hidden" name="room" value="${data.room}"/>

            <table class="admin-table" id="rounds">
                <tr colspan="2">
                    <td><b>Rounds settings</b></td>
                </tr>
                <tr>
                    <td>Enable Rounds</td>
                    <td><form:checkbox path="rounds.roundsEnabled"/></td>
                </tr>
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
                        <input type="submit" name="action"
                               value="${data.actions.updateRoundsSettings}"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </c:if>

    <c:if test="${not empty data.semifinal.parameters}">
        <form:form modelAttribute="data" action="admin#semifinal" method="POST">
            <input type="hidden" name="game" value="${data.game}"/>
            <input type="hidden" name="room" value="${data.room}"/>

            <table class="admin-table" id="semifinal">
                <tr colspan="2">
                    <td><b>Semifinal settings</b></td>
                </tr>
                <tr>
                    <td>Enable semifinal</td>
                    <td><form:checkbox path="semifinal.enabled"/></td>
                </tr>
                <tr>
                    <td>Ticks before recalculation</td>
                    <td><form:input path="semifinal.timeout"/></td>
                </tr>
                <tr>
                    <td>Current tick</td>
                    <td>${data.semifinalTick}</td>
                </tr>
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
                        <input type="submit" name="action"
                               value="${data.actions.updateSemifinalSettings}"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </c:if>

    <c:if test="${not empty data.inactivity.parameters}">
        <form:form modelAttribute="data" action="admin#inactivity" method="POST">
            <input type="hidden" name="game" value="${data.game}"/>
            <input type="hidden" name="room" value="${data.room}"/>

            <table class="admin-table" id="inactivity">
                <tr colspan="2">
                    <td><b>Inactivity settings</b></td>
                </tr>
                <tr>
                    <td>Enable kick inactive players</td>
                    <td><form:checkbox path="inactivity.kickEnabled"/></td>
                </tr>
                <tr>
                    <td>Inactive ticks before kick</td>
                    <td><form:input path="inactivity.inactivityTimeout"/></td>
                </tr>
                <tr>
                    <td>
                        <input type="submit" name="action"
                               value="${data.actions.updateInactivitySettings}"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </c:if>

    <c:if test="${not empty data.other}">
        <form:form modelAttribute="data" action="admin#gameSettings" method="POST">
            <input type="hidden" name="game" value="${data.game}"/>
            <input type="hidden" name="room" value="${data.room}"/>

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
                                <td><form:textarea class="map" path="otherValues[${status.index}]"/></td>
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
                        <input type="submit" name="action"
                               value="${data.actions.updateOtherSettings}"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </c:if>

    <c:if test="${not empty data.levels.parameters}">
        <form:form modelAttribute="data" action="admin#levels" method="POST">
            <input type="hidden" name="game" value="${data.game}"/>
            <input type="hidden" name="room" value="${data.room}"/>

            <table class="admin-table" id="levels">
                <tr colspan="2">
                    <td><b>Game levels settings</b></td>
                </tr>
                <c:forEach items="${data.levels.parameters}" var="element" varStatus="status">
                    <tr index="${status.index}">
                        <td>
                            <input id="levelsKeys${status.index}"
                                   name="levelsKeys[${status.index}]"
                                   type="hidden"
                                   with="newKey"
                                   value="${element.name}"/>
                            <input id="levelsNewKeys${status.index}"
                                   name="levelsNewKeys[${status.index}]"
                                   type="text"
                                   with="key"
                                   value="${element.name}"/>
                        </td>
                        <c:choose>
                            <c:when test="${element.type == 'editbox' && element.multiline}">
                                <td>
                                    <form:textarea
                                        class="map"
                                        with="value"
                                        path="levelsValues[${status.index}]"/>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td>${element.value}</td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:forEach>
                <script template type="text/x-jquery-tmpl">
                    <tr index="{%= index %}">
                        <td>
                            <input id="levelsKeys{%= index %}"
                                   name="levelsKeys[{%= index %}]"
                                   type="hidden"
                                   with="newKey"
                                   value="[Level] Map[{%= key %}]"></input>
                            <input id="levelsNewKeys{%= index %}"
                                   name="levelsNewKeys[{%= index %}]"
                                   type="text"
                                   with="key"
                                   value="[Level] Map[{%= key %}]"></input>
                        </td>
                        <td>
                            <textarea id="levelsValues{%= index %}"
                                      name="levelsValues[{%= index %}]"
                                      with="value"
                                      class="map"></textarea>
                        </td>
                    </tr>
                </script>
                <tr class="levelsButtons">
                    <td>
                        <input type="submit" name="action"
                               value="${data.actions.saveLevelsMaps}"/>
                    </td>
                    <td>
                        <input type="button" id="addNewLevelMap"
                               value="${data.actions.addNewLevelMap}"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </c:if>

    <c:if test="${not empty data.players || savedGames != null}">
        <form:form modelAttribute="data" action="admin#players" method="POST">
            <input type="hidden" name="game" value="${data.game}"/>
            <input type="hidden" name="room" value="${data.room}"/>

            <table class="admin-table" id="players">
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
                        <input type="submit" name="action"
                               value="${data.actions.saveAllPlayers}"/>
                    </td>
                    <td>
                        <input type="submit" name="action"
                               value="${data.actions.loadAllPlayers}"/>
                    </td>
                    <td>
                        <input type="submit" name="action"
                               value="${data.actions.removeAllPlayersSaves}"/>
                    </td>
                    <td>
                        <input type="submit" name="action"
                               value="${data.actions.removeAllPlayersRegistrations}"/>
                    </td>
                    <td>
                        <input type="submit" name="action"
                               value="${data.actions.gameOverAllPlayers}"/>
                    </td>
                    <td>
                        <a href="${ctx}/board/room/${data.room}">ViewGameAll</a>&nbsp;&nbsp;
                    </td>
                    <td class="header">PlayerLogAll</td>
                    <td>
                        <input type="submit" name="action"
                               value="${data.actions.loadAIsForAllPlayers}"/>
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
                                <td><form:input class="input-id"
                                                path="players[${status.index}].id"
                                                readonly="true"
                                                index="${status.index}"/></td>
                            </c:when>
                            <c:otherwise>
                                <td><input type="text"
                                           readonly="true"
                                           class="input-id"
                                           value="${player.id}"/></td>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${player.active}">
                                <td><form:input class="input-code"
                                                path="players[${status.index}].code"
                                                readonly="true"/></td>
                            </c:when>
                            <c:otherwise>
                                <td><input type="text"
                                           readonly="true"
                                           class="input-code"
                                           value="${player.code}"/></td>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${player.active}">
                                <td><form:input class="input-readable"
                                                path="players[${status.index}].readableName"/></td>
                            </c:when>
                            <c:otherwise>
                                <td><input type="text"
                                           readonly="true"
                                           class="input-readable"
                                           value="${player.readableName}"/></td>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${player.active}">
                                <td><form:input class="input-email"
                                                path="players[${status.index}].email"/></td>
                            </c:when>
                            <c:otherwise>
                                <td><input type="text"
                                           readonly="true"
                                           class="input-email"
                                           value="${player.email}"/></td>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${player.active}">
                                <td><form:input class="input-room"
                                                path="players[${status.index}].room"/></td>
                            </c:when>
                            <c:otherwise>
                                <td><input type="text"
                                           readonly="true"
                                           class="input-room"
                                           value="${player.room}"/></td>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${player.active}">
                                <td><form:input class="input-team"
                                                path="players[${status.index}].teamId"/></td>
                            </c:when>
                            <c:otherwise>
                                <td><input type="text"
                                           readonly="true"
                                           class="input-team"
                                           value="${player.teamId}"/></td>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${player.active}">
                                <td><form:input class="input-score"
                                                path="players[${status.index}].score"/></td>
                            </c:when>
                            <c:otherwise>
                                <td><input type="text"
                                           readonly="true"
                                           class="input-score"
                                           value="${player.score}"/></td>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${player.active}">
                                <td><form:input class="input-callback"
                                                path="players[${status.index}].callbackUrl"/></td>
                            </c:when>
                            <c:otherwise>
                                <td><input type="text"
                                           readonly="true"
                                           class="input-callback"
                                           value="${player.callbackUrl}"/></td>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${player.active}">
                                <td>&nbsp;<span class="input-ticks-inactive">${player.ticksInactive}</span>&nbsp;</td>
                            </c:when>
                            <c:otherwise>
                                <td>&nbsp;<span class="input-ticks-inactive"></span>&nbsp;</td>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${player.code != null && player.active}">
                                <td class="joystick">
                                    <span class="a" href="${ctx}/rest/joystick/player/${player.id}/do/up#players">U</span>
                                    <span class="a" href="${ctx}/rest/joystick/player/${player.id}/do/down#players">D</span>
                                    <span class="a" href="${ctx}/rest/joystick/player/${player.id}/do/left#players">L</span>
                                    <span class="a" href="${ctx}/rest/joystick/player/${player.id}/do/right#players">R</span>
                                    <span class="a" href="${ctx}/rest/joystick/player/${player.id}/do/act#players">A</span>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td>UDLRA</td>
                            </c:otherwise>
                        </c:choose>

                        <td><a href="${ctx}/board/room/${data.room}">${data.room}</a></td>

                        <c:choose>
                            <c:when test="${player.active}">
                                <td>
                                    <input type="submit" name="action"
                                           formaction="admin?player=${player.id}#players"
                                           value="${data.actions.savePlayer}"/>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td>${data.actions.savePlayer}</td>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${player.saved}">
                                <td>
                                    <input type="submit" name="action"
                                           formaction="admin?player=${player.id}#players"
                                           value="${data.actions.loadPlayer}"/>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td>${data.actions.loadPlayer}</td>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${player.saved}">
                                <td><a href="${ctx}/admin/player/${player.id}/save/remove?room=${data.room}#players">RemoveSave</a></td>
                            </c:when>
                            <c:otherwise>
                                <td>RemoveSave</td>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${player.code != null}">
                                <td><a href="${ctx}/admin/player/${player.id}/registration/remove?room=${data.room}#players">RemoveReg</a></td>
                            </c:when>
                            <c:otherwise>
                                <td>RemoveReg</td>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${player.active}">
                                <td><a href="${ctx}/admin/player/${player.id}/gameOver?room=${data.room}#players">GameOver</a></td>
                            </c:when>
                            <c:otherwise>
                                <td>GameOver</td>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${player.active}">
                                <td><a href="${ctx}/board/player/${player.id}?code=${player.code}">ViewGame</a></td>
                            </c:when>
                            <c:otherwise>
                                <td>ViewGame</td>
                            </c:otherwise>
                        </c:choose>

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
                            <c:when test="${!player.active}">
                                <td>LoadAI</td>
                            </c:when>
                            <c:otherwise>
                                <td><a href="${ctx}/admin/player/${player.id}/ai/reload?room=${data.room}#players">LoadAI</a></td>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${player.data == null}">
                                <td></td>
                            </c:when>
                            <c:otherwise>
                                <td><form:input class="player-save"
                                                path="players[${status.index}].data"/></td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:forEach>
                <tr>
                    <td>
                        <input type="submit" name="action"
                               value="${data.actions.updateAllPlayers}"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </c:if>

    </br>
    Go to <a href="${ctx}/">main page</a>.
</body>
</html>
