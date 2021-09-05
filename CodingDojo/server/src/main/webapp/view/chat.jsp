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

<div class="chat" style="display:none;" zoom-on-wheel>
    <script template type="text/x-jquery-tmpl">
        <div message="{%= id %}" player="{%= player %}" class="chat-message, id-chat-message-{%= id %}">
            <span class="message-author">{%= author %}</span>
            <span class="message-time" title="{%= dateTime %}">{%= time %}</span>
            <span class="delete-message"> x </span>
            <div class="message-text">{{html text}}</div>
        </div>
    </script>
    <div class="messages-container id-chat-container">
    </div>
    <div class="message-area">
        <textarea class="id-new-message" placeholder="Enter - submit, Shift+Enter - new line"></textarea>
    </div>
    <div class="post-message-button">
        <input type="button" class="id-post-message" value="Send">
    </div>
</div>