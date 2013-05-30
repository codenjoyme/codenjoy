<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Test page</title>
</head>
<body>
<table>
    <tr>
        <td height="100%" width="50%">
            <iframe height="600px" frameborder="1" src="${pageContext.request.contextPath}/board/testUser"></iframe>
        </td>
        <td height="100%">
            <iframe src="${pageContext.request.contextPath}/view/testform.jsp"></iframe>
        </td>
    </tr>
</table>


</body>
</html>