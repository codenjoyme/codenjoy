<table id="table-logs" class="table table-striped">
    <thead>
    <th width="5%">#</th>
    <th width="40%">Player</th>
    <th width="30%">Score</th>
    <th width="25%">Level</th>
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