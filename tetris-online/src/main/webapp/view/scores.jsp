<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <link href="/resources/css/bootstrap.css" rel="stylesheet">
</head>
<body>

<table id="table-logs" class="table table-striped">
    <thead>
    <th width="5%">#</th>
    <th width="40%">Player</th>
    <th width="20%">Score</th>
    <th width="25%">Level</th>
    <th width="10%">Date</th>
    </thead>
    <tbody>
    <c:forEach items="${scores}" var="record" varStatus="status">
        <tr>
            <td>${status.index + 1}</td>
            <td>
                ${record.playerName}
            </td>
            <td>
                ${record.score}
            </td>
            <td>
                ${record.level + 1}
            </td>
            <td>
                ${record.timestamp}
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>