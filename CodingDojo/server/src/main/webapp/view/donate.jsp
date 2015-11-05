<%@ page language="java" contentType="text/html; charset=UTF-8" %>
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