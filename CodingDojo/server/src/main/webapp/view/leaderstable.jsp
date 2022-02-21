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
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<div class="leader-board" style="flex:0 0 33%;">
    <div class="leaderboard-board">
        <div class="leader-board-label">
            <h3> Leaderboard </h3>
        </div>
        <div class="div-table overflow">
                <table id="table-logs" class = "inner-table leaderboard-table">
                    <th style = "width:5%">
                        <c:choose>
                            <c:when test="${code != null}">
                                <a href="${ctx}/board/game/${game}?code=${code}">#</a>
                            </c:when>
                            <c:otherwise>
                                <a href="${ctx}/board/game/${game}">#</a>
                            </c:otherwise>
                        </c:choose>
                    </th>
                    <th style = "width:70%">
                        <div class="score-header">
                            Player
                            <sec:authorize access="isAuthenticated()">
                                <c:if test="${playerScoreCleanupEnabled}">
                                    <span class="pow">
                                        <a href="#" onclick="cleanPlayerScores()">clear score</a>
                                    </span>
                                </c:if>
                            </sec:authorize>
                        </div>
                    </th>
                    <th style = "width:25%" class="center">Score</th>
                    <c:forEach items="${leaderboard}" var="currPlayer">
                        <tr>
                            <td>${currPlayer.key}. </td>
                            <td><a href="${ctx}/board/player/email?code=${code}"> ${currPlayer.value.key}</a></td>
                            <td class="center"> ${currPlayer.value.value}</td>
                        </tr>
                    </c:forEach>
                </table>
        </div>
    </div>
</div>