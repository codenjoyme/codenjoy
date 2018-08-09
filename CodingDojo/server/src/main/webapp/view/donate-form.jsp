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
<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<form id="payment" name="payment" method="post" action="https://sci.interkassa.com/" enctype="utf-8" target="_blank">
    <input type="hidden" name="ik_co_id" value="${donateCode}" />
    <input type="hidden" name="ik_pm_no" value="ID_4233" />
    <h4>Как поможешь?</h4>
    <input type="text" id="donate-count" name="ik_am" value="100" />
    <select name="ik_cur">
        <option>UAH</option>
        <option>EUR</option>
        <option>USD</option>
    </select>
    <input type="hidden" name="ik_desc" value="Codenjoy Donate" />
    <!--input type="hidden" name="ik_exp" value="${today}" /--><br>
    <input type="submit" id="ok-donate" value="Помочь">
    <input type="button" id="close-donate" value="Не сейчас">
</form>
