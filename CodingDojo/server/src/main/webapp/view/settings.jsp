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
    <div class="querySubscription">
        <form id="form" name="form" action="${ctx}/board/feedback" method="POST">
            <caption style="padding: 10px;"> <font class="white-text" size="3">Settings for notifications.</font> </caption>
            <table class= "add-border">
                <th class="white-text add-border" style="width:15%">Email</th>
                <th class="white-text add-border" style="width:15%">Slack</th>
                <th class="white-text add-border" style="width:70%">Event Description</th>
                <c:forEach items="${subscribed}" var="currentSubscription">
                    <tr class= "add-border">
                      <c:choose>
                        <c:when test="${currentSubscription.emailSubscription}">
                            <td class="white-text">
                                <input type="checkbox"
                                 id="email${currentSubscription.query.id}"
                                  name="email${currentSubscription.query.id}"
                                   value="${currentSubscription.emailSubscription}"
                                    checked
                                     onclick="changeValue('email${currentSubscription.query.id}')"/>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td class="white-text add-border">
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
                            <td class="white-text add-border">
                                <input type="checkbox"
                                 id="slackEmail${currentSubscription.query.id}"
                                  name="slackEmail${currentSubscription.query.id}"
                                   value="${currentSubscription.slackSubscription}"
                                    checked onclick="changeValue('slackEmail${currentSubscription.query.id}')"/>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td class="white-text add-border">
                            <c:choose>
                                <c:when test="${isSlackSubscribed}">
                                    <input type="checkbox"
                                        id="slackEmail${currentSubscription.query.id}"
                                         name="slackEmail${currentSubscription.query.id}"
                                         value="${currentSubscription.slackSubscription}"
                                          onclick="changeValue('slackEmail${currentSubscription.query.id}')"/>
                                </c:when>
                                <c:otherwise>
                                    <input type="checkbox"
                                        id="slackEmail${currentSubscription.query.id}"
                                         name="slackEmail${currentSubscription.query.id}"
                                         value="${currentSubscription.slackSubscription}"
                                          disabled/>
                                </c:otherwise>
                            </c:choose>
                            </td>
                        </c:otherwise>
                      </c:choose>
                      <td class="white-text add-border">
                        <c:out value="${currentSubscription.query.id}."/>
                        <c:out value="${currentSubscription.query.description}"/>
                      </td>
                    </tr>
                </c:forEach>
                <textarea id="playerId" name="playerId" hidden>"${playerId}"</textarea>
                <textarea id="game" name="game" hidden>"${game}"</textarea>
                <textarea id="code" name="code" hidden>"${code}"</textarea>
                <tr class= "add-border">
                    <td colspan="3">
                        <a style="align = left" onclick="pop()"><font size="3">Submit</font></a>
                        <span id="myPopup" hidden></br>
                            <p class="white-text">Give us your feedback: </p>
                            <textarea id="feedback" name="feedback" for="form" style="color: black !important;resize:none;width:100%;" required></textarea>
                            <button class="btn-submit" style="color: black !important;" id="submit-button" for="form" type="submit">Submit</button>
                        </span>
                    </td>
                </tr>
            </table>
        </form>
</div>