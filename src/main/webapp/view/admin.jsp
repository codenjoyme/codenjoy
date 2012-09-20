<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Admin page</title>
</head>
<table>
    <tr>
        <td>
            <c:choose>
                <c:when test="${paused}">
                    The dojo was suspended. <a href="/admin31415?resume">Resume game</a>.
                </c:when>
                <c:otherwise>
                    The dojo is active. <a href="/admin31415?pause">Pause game</a>.
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</table>