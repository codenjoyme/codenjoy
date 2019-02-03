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

<div id="leaderboard" class="board" style="display:none;" zoom-on-wheel>
    <table id="table-logs" class="table table-striped leaderboard-table">
        <thead>
            <th width="5%">
                <c:choose>
                    <c:when test="${code != null}">
                        <a href="${ctx}/board/game/${gameName}?code=${code}">#</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${ctx}/board/game/${gameName}">#</a>
                    </c:otherwise>
                </c:choose>
            </th>
            <th width="55%">Player</th>
            <th width="25%" class="center">Score</th>
        </thead>
        <tbody id="table-logs-body">
            <!--
                <td>1</td>
                <td><a href="${ctx}/board/player/email?code=code">name</a></td>
                <td class="center">score</td>
            -->
        </tbody>
    </table>
    <%@include file="info.jsp"%>
</div>