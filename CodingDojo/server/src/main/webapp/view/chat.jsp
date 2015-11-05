<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<div id="chat-container">
    <div id="game-info"></div>
    <textarea cols="20" rows="3" id="chat-message" style="width:400px;"></textarea>
    <input type="button" value="send" id="chat-send"/>
    <div id="chat-info"></div>
    <div id="chat" style="overflow-x: hidden; overflow-y: auto;"></div>
</div>