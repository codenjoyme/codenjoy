<!DOCTYPE html>
<!--
  #%L
  iCanCode - it's a dojo-like platform from developers to developers.
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
  -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>ICanCode | Sign Up</title>
    <link href="${ctx}/resources/icancode/css/reset.css" rel="stylesheet"/>
    <link href="${ctx}/resources/css/bootstrap/bootstrap.min.css" rel="stylesheet"/>
    <link href="${ctx}/resources/icancode/fonts/font-awesome-4.6.3/css/font-awesome.min.css" rel="stylesheet"/>
    <link href="${ctx}/resources/icancode/css/registration-style.css" rel="stylesheet"/>
    <script src="${ctx}/resources/js/jquery/jquery-3.1.0.min.js"></script>
    <script src="${ctx}/resources/icancode/js/autofill-event.js"></script>
    <script src="${ctx}/resources/icancode/js/jquery/jquery.md5.js"></script>
    <script src="${ctx}/resources/icancode/js/game/admin-settings.js"></script>
    <script src="${ctx}/resources/icancode/js/scripts/registration.js" type="application/javascript"></script>
  </head>
  <body>
    <div class="header-container">
      <div class="container-fluid">
        <header class="header row">
          <div class="col-xs-6">
            <a class="logo inline" href="#" title="Home"><img src="${ctx}/resources/icancode/img/logo.png"></a>
            <span class="title dojo-title">EPAM DOJO&nbsp;&nbsp;&nbsp;&nbsp;</span>
            <a class="logo inline" href="#" title="Home"><img src="${ctx}/resources/icancode/img/i_can_code_Logo.png"></a>
            <span class="title icancode-title">ICanCode</span>
          </div>
          <nav class="nav col-xs-6 pull-right text-right">
            <ul class="nav-list">
              <li class="title icancode-title inline"><a id="additional-link" target="_blank" href="#"></a></li>
              <li class="title icancode-title inline"><a id="help-link" target="_blank" href="#"></a></li>
              <li class="logo title inline"><img src="${ctx}/resources/icancode/img/profile.png"></li>
            </ul>
          </nav>
        </header>
      </div>
    </div>
    <div class="container-fluid">
      <form:form class="form-registr" id="form" modelAttribute="player" action="register" method="POST">
        <h2 class="form-title">Sign up</h2>
        <div class="inputs">
          <div id="readableName" class="field not-valid" not-empty>
            <form:errors path="readableName" cssClass="error-message" />
            <form:input type="text" placeholder="FirstName LastName" path="readableName"/>
            <span class="icon fa"></span>
          </div>
          <div id="email" class="field not-valid">
            <form:errors path="email" cssClass="error-message" />
            <form:input type="email" placeholder="Email address (valid)" path="email"/>
            <span class="icon fa"></span>
          </div>
          <div id="password" class="field not-valid">
            <form:errors path="password" cssClass="error-message" />
            <form:input type="password" placeholder="Password" path="password"/>
            <span class="icon fa"></span>
          </div>
          <div id="passwordConfirmation" class="field not-valid">
            <form:errors path="passwordConfirmation" cssClass="error-message" />
            <form:input type="password" placeholder="Confirm password" path="passwordConfirmation"/>
            <span class="icon fa"></span>
          </div>
          <!-- add attribute 'hidden' to the div if you want to hide this select box -->
          <div id="game" class="field valid" hidden>
            <select placeholder="Select your game" name="game">
              <option value="iCanCode Contest" >iCanCode Contest</option>
              <option value="iCanCode Training" selected>iCanCode Training</option>
              <option value="eKids">eKids</option>
            </select>
          </div>
          <div id="data" hidden>
              <form:input type="text" path="data"/>
          </div>
          <!-- add attribute 'not-empty' to the div if you want to enable validation -->
          <!-- add attribute 'hidden' to the div if you want to hide this edit box -->
          <div id="data1" class="field not-valid" not-empty>
              <input type="text" placeholder="Tech skills"/>
              <span class="icon fa"></span>
          </div>
          <div id="data2" class="field not-valid" not-empty>
               <!-- also you can change input placeholder="..." -->
              <input type="text" placeholder="Experience (years)"/>
              <span class="icon fa"></span>
          </div>
          <div id="data3" class="field not-valid" not-empty>
            <input type="text" placeholder="Company"/>
            <span class="icon fa"></span>
          </div>
          <form:input type="hidden" path="gameName" value="icancode"/>
          <button class="btn-submit" id="submit-button" type="button">Sign up</button>
          <%--<div id="error-message" class="error-message" hidden></div>--%>
        </div>
      </form:form>
    </div>
    <footer class="footer">
      <div class="container-fluid">
        <nav class="footer-nav">
          <ul class="footer-list">
              <li class="footer-item inline"><a href="https://epa.ms/dojo-habr" target="blank">About DOJO</a></li>
              <li class="footer-item inline"><a href="https://epa.ms/EPMDOJO" target="blank">DOJO KB</a></li>
              <li class="footer-item inline"><a href="https://epa.ms/DOJO-CHAT" target="blank">DOJO CHAT</a></li>
              <li class="footer-item inline"><a href="https://github.com/codenjoyme/codenjoy" target="blank">Codenjoy on GitHub</a></li>
              <li class="footer-item inline"><a href="mailto:Oleksandr_Baglai@Epam.com" target="blank">Ask me anything</a></li>
          </ul>
        </nav>
      </div>
    </footer>
  </body>
</html>
