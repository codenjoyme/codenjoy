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

<html>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Codenjoy admin page</title>

    <link href="${ctx}/resources/css/all.min.css" rel="stylesheet">
    <link href="${ctx}/resources/css/custom.css" rel="stylesheet">

    <script src="${ctx}/resources/js/all.min.js"></script>
</head>
<body>
    <div id="settings" page="admin" contextPath="${ctx}" gameName="${gameName}"></div>

    <%@include file="forkMe.jsp"%>
    <div class="page-header">
        <h1>Admin page</h1>
    </div>

    <table class="admin-table" id="selectGame">
        <tr>
            <td style="width:300px;">
                <b>Please select your game</b>
            </td>
        </tr>
        <tr>
            <td style="width:300px;">
                <c:forEach items="${games}" var="game" varStatus="status">
                    <c:if test="${game == gameName}">
                        <b>
                    </c:if>
                        <a href="${ctx}/admin?gameName=${game}&select">${game}${gamesCount[status.index]}</a>&nbsp;&nbsp;&nbsp;
                    <c:if test="${game == gameName}">
                        </b>
                    </c:if>
                </c:forEach>
            </td>
        </tr>
        <tr>
            <td style="width:500px;">
                <b>Game version is</b> ${gameVersion}
            </td>
        </tr>
    </table>

    <table class="admin-table" id="pauseGame">
        <tr>
            <td>
                <c:choose>
                    <c:when test="${paused}">
                        <b>The codenjoy was suspended</b></br> <a href="${ctx}/admin?resume&gameName=${gameName}">Resume game</a>.
                    </c:when>
                    <c:otherwise>
                        <b>The codenjoy is active</b></br> <a href="${ctx}/admin?pause&gameName=${gameName}">Pause game</a>.
                    </c:otherwise>
                </c:choose>
            </td>
            <form:form commandName="adminSettings" action="admin" method="POST">
                <tr>
                    <td><input type="text" name="timerPeriod" value="${timerPeriod}"/></td>
                </tr>
                <tr>
                    <td>
                        <input type="hidden" name="gameName" value="${gameName}"/>
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
                        <b>The recording is active</b></br> <a href="${ctx}/admin?stopRecording&gameName=${gameName}">Stop recording</a>.
                    </c:when>
                    <c:otherwise>
                        <b>The recording was suspended</b></br> <a href="${ctx}/admin?recording&gameName=${gameName}">Start recording</a>.
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>

    <table class="admin-table" id="debug">
        <tr>
            <td>
                <c:choose>
                    <c:when test="${debug}">
                        <b>The debug in progress</b></br> <a href="${ctx}/admin?stopDebug&gameName=${gameName}">Stop debug</a>.
                    </c:when>
                    <c:otherwise>
                        <b>The debug was suspended</b></br> <a href="${ctx}/admin?startDebug&gameName=${gameName}">Start debug</a>.
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>

    <table class="admin-table" id="auto-save">
        <tr>
            <td>
                <c:choose>
                    <c:when test="${autoSave}">
                        <b>The auto save in progress</b></br> <a href="${ctx}/admin?stopAutoSave&gameName=${gameName}">Stop auto save</a>.
                    </c:when>
                    <c:otherwise>
                        <b>The auto save was suspended</b></br> <a href="${ctx}/admin?startAutoSave&gameName=${gameName}">Start auto save</a>.
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
                        <b>The registration is active</b></br> <a href="${ctx}/admin?close&gameName=${gameName}">Close registration</a>.
                    </c:when>
                    <c:otherwise>
                        <b>The registration was closed</b></br> <a href="${ctx}/admin?open&gameName=${gameName}">Open registration</a>.
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>

    <table class="admin-table" id="cleanGame">
        <tr>
            <td>
                <a href="${ctx}/admin?cleanAll&gameName=${gameName}">Clean all scores</a>.
            </td>
        </tr>
    </table>

    <form:form commandName="adminSettings" action="admin" method="POST">
        <table class="admin-table" id="createNewUsers">
            <tr>
                <td>NameMask</td>
                <td>Count</td>
            <tr>
                <td><input type="text" name="generateNameMask" value="${generateNameMask}"/></td>
                <td><input type="text" name="generateCount" value="${generateCount}"/></td>
            </tr>
            <tr>
                <td>
                    <input type="hidden" name="gameName" value="${gameName}"/>
                    <input type="submit" value="Create"/>
                </td>
            </tr>
        </table>
    </form:form>

    <c:if test="${parameters.size() != 0}">
        <form:form commandName="adminSettings" action="admin" method="POST">
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
                            <c:when test="${element.type == 'editbox'}">
                                <td><form:input path="parameters[${status.index}]"/></td>
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
                        <input type="hidden" name="gameName" value="${gameName}"/>
                        <input type="submit" value="Save"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </c:if>

    <c:if test="${players != null || savedGames != null}">
        <form:form commandName="adminSettings" action="admin" method="POST">
            <table class="admin-table" id="savePlayersGame">
                <tr colspan="4">
                    <td><b>Registered Players</b></td>
                </tr>
                <tr>
                    <td class="header">Player name</td>
                    <td class="header">IP</td>
                    <td class="header">Game name</td>
                    <td>
                        <a href="${ctx}/admin?saveAll&gameName=${gameName}">SaveAll</a>&nbsp;&nbsp;
                    </td>
                    <td>
                        <a href="${ctx}/admin?loadAll&gameName=${gameName}">LoadAll</a>&nbsp;&nbsp;
                    </td>
                    <td>
                        <a href="${ctx}/admin?removeSaveAll&gameName=${gameName}">RemoveSaveAll</a>&nbsp;&nbsp;
                    </td>
                    <td>
                        Registration&nbsp;&nbsp;
                    </td>
                    <td>
                        <a href="${ctx}/admin?gameOverAll&gameName=${gameName}">GameOverAll</a>&nbsp;&nbsp;
                    </td>
                    <td>
                        <a href="${ctx}/board/game/${gameName}">ViewPlayerGame</a>&nbsp;&nbsp;
                    </td>
                    <td>
                        <a href="${ctx}/admin?reloadAllAI&gameName=${gameName}">LoadAllAI</a>&nbsp;&nbsp;
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
                                <td><form:input path="players[${status.index}].name"/></td>
                                <td><form:input path="players[${status.index}].callbackUrl"/></td>
                                <td><a href="${ctx}/board/game/${player.gameName}">${player.gameName}</a></td>
                                <td><a href="${ctx}/admin?save=${player.name}&gameName=${gameName}">Save</a></td>
                                <c:choose>
                                    <c:when test="${player.saved}">
                                        <td><a href="${ctx}/admin?load=${player.name}&gameName=${gameName}">Load</a></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>Load</td>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${player.saved}">
                                        <td><a href="${ctx}/admin?removeSave=${player.name}&gameName=${gameName}">RemoveSave</a></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>RemoveSave</td>
                                    </c:otherwise>
                                </c:choose>
                                <td><a href="${ctx}/admin?removeRegistration=${player.name}&gameName=${gameName}">RemoveReg</a></td>
                                <td><a href="${ctx}/admin?gameOver=${player.name}&gameName=${gameName}">GameOver</a></td>
                                <td><a href="${ctx}/board/player/${player.name}?code=${player.code}">ViewGame</a></td>
                                <c:choose>
                                    <c:when test="${player.aiPlayer}">
                                        <td>Loaded</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td><a href="${ctx}/admin?reloadAI=${player.name}&gameName=${gameName}">LoadAI</a></td>
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
                                <td><input class="uneditable-input" value="${player.name}"/></td>
                                <td><input class="uneditable-input" value="${player.callbackUrl}"/></td>
                                <td><a href="${ctx}/board/game/${player.gameName}">${player.gameName}</a></td>
                                <td>Save</td>
                                <c:choose>
                                    <c:when test="${player.saved}">
                                        <td><a href="${ctx}/admin?load=${player.name}&gameName=${gameName}">Load</a></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>Load</td>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${player.saved}">
                                        <td><a href="${ctx}/admin?removeSave=${player.name}&gameName=${gameName}">RemoveSave</a></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>RemoveSave</td>
                                    </c:otherwise>
                                </c:choose>
                                <td>GameOver</td>
                                <td></td>
                                <td></td>
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
                        <input type="hidden" name="gameName" value="${gameName}"/>
                        <input type="submit" value="Save"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </c:if>

    </br>
    Go to <a href="${ctx}/">main page</a>.
</body>
</html>
