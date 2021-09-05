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

<div id="field-chat" class="chat" style="display:none;" zoom-on-wheel>
    <script template type="text/x-jquery-tmpl">
        <div id="chat-field-message-{%= id %}" message="{%= id %}" player="{%= player %}" class="chat-message">
            <span class="message-author">{%= author %}</span>
            <span class="message-time" title="{%= dateTime %}">{%= time %}</span>
            <span class="delete-field-message"> x </span>
            <div class="message-text">{{html text}}</div>
        </div>
    </script>
    <div id="field-chat-container" class="messages-container">
    </div>
    <div class="message-area">
        <textarea id="new-field-message" placeholder="Enter - submit, Shift+Enter - new line"></textarea>
    </div>
    <div class="post-message-button">
        <input type="button" id="post-field-message" value="Send">
    </div>
</div>