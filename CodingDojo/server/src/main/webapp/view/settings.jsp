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

<style>
.popup {
    display: inline-block;
}
.popup .popuptext {
    visibility: hidden;
    width: 160px;
    background-color: #b1b1b1;
    text-align: center;
    border-radius: 6px;
    padding: 20px;
    position:relative;
    top:50px;
    right:150px;
}
.popup .show {
    visibility: visible;
    -webkit-animation: fadeIn 1s;
    animation: fadeIn 1s;
}
</style>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<br><br>
    <div>
        <label style="width:80%" for="email">
            <font size="3">Receive email notifications.</font>
        </label>
        <form id="form" action="${ctx}/board/feedback" method="POST">
            <c:forEach items="${subscribed}" var="currentSubscription">
                <table>
                    <tr>
                      <c:choose>
                        <c:when test="${currentSubscription.emailSubscription}">
                            <td>
                                <input type="checkbox"
                                 id="email${currentSubscription.query.id}"
                                  name="email${currentSubscription.query.id}"
                                   value="${currentSubscription.emailSubscription}"
                                    checked
                                     onclick="changeValue('email${currentSubscription.query.id}')"/>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td>
                                <input type="checkbox"
                                 id="email${currentSubscription.query.id}"
                                  name="email${currentSubscription.query.id}"
                                   value="${currentSubscription.emailSubscription}"
                                    onclick="changeValue('email${currentSubscription.query.id}')"/>
                            </td>
                        </c:otherwise>
                      </c:choose>
                      <c:choose>
                        <c:when test="${currentSubscription.slackSubscription}">
                            <td>
                                <input type="checkbox"
                                 id="slackEmail${currentSubscription.query.id}"
                                  name="slackEmail${currentSubscription.query.id}"
                                   value="${currentSubscription.slackSubscription}"
                                    checked onclick="changeValue('slackEmail${currentSubscription.query.id}')"/>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td>
                                <input type="checkbox"
                                 id="slackEmail${currentSubscription.query.id}"
                                  name="slackEmail${currentSubscription.query.id}"
                                   value="${currentSubscription.slackSubscription}"
                                    onclick="changeValue('slackEmail${currentSubscription.query.id}')"/>
                            </td>
                        </c:otherwise>
                      </c:choose>
                      <td><c:out value="${currentSubscription.query.id}."/></td>
                      <td><c:out value="${currentSubscription.query.description}"/></td>
                    </tr>
                </table>
            </c:forEach>
            <textarea id="playerId" name="playerId" for="form" hidden>"${playerId}"</textarea>
            <textarea id="game" name="game" for="form" hidden>"${game}"</textarea>
            <textarea id="code" name="code" for="form" hidden>"${code}"</textarea>

            <a onclick="pop()"><font size="20px">Submit</font></a>
            <div class="popup">
                <span class="popuptext" id="myPopup">
                    Give us your feedback:
                    <textarea id="feedback" name="feedback" for="form" style="resize:none;width:100%;"></textarea>
                    <button class="btn-submit" id="submit-button" for="form" type="submit" name="action" value="unsubscribe" onclick="reset()">Submit</button>
                </span>
            </div>
        </form>
</div>