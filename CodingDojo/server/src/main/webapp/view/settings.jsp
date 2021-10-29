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
<br><br>
    <div>
        <c:choose>
            <c:when test="${subscribed}">
                <input type="checkbox" id="email" name="email" checked onclick="subOrUnsub()"/>
                <label style="width:80%" for="email">
                    <font size="3">Receive email notifications.</font>
                </label>
                <form id="form" object = "${feedbackModel}" action="${ctx}/board/feedback" method="POST" hidden>
                    <div id="data" for="form">
                        <form:input type="text" path="data"/>
                    </div>
                    <label field="${fromPlayerId}" value = "${playerId}" hidden></label>
                    <label field="${forGame}" value = "${game}" hidden></label>
                    <label for="feedback"><font size="3">  How can we better the notifications?</font></label>
                    <textarea id="feedback" for="form" style="resize: none;width:100%;" field="${feedbackText}" not-empty></textarea>
                    <button class="btn-submit" id="submit-button" for="form" type="submit" name="action" value="unsubscribe">Submit</button>
                </form>
            </c:when>
            <c:otherwise>
                <input type="checkbox" id="email" name="email" onclick="subOrUnsub()"/>
                <label style="width:80%" for="email">
                    <font size="3">Receive email notifications.</font>
                </label>
                <form id="form" modelAttribute="feedback" action="${ctx}/board/feedback" method="POST" hidden>
                    <button class="btn-submit" id="submit-button" for="form" type="submit" name="action" value="subscribe">Submit</button>
                </form>
            </c:otherwise>
        </c:choose>
</div>