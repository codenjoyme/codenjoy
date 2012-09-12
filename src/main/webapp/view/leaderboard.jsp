<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="refresh" content="15">
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Leader Board</title>
    <link href="/resources/css/bootstrap.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <h3>Leader Board</h3>
    <table id="table-logs" class="table table-striped">
        <thead>
        <th width="5%">#</th>
        <th width="20%">Player</th>
        <th width="60%">Score</th>
        <th width="15%">Level</th>
        </thead>
        <tbody>
        <c:forEach items="${players}" var="record" varStatus="status">
            <tr>
                <td>${status.index + 1}</td>
                <td>
                        ${record.name}
                </td>
                <td>
                        ${record.score}
                </td>
                <td>
                        ${record.currentLevel + 1}
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<img/>

</body>
</html>