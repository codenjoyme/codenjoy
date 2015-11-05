<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="${ctx}/resources/js/jquery-1.7.2.js"></script>
<script>
    $(document).ready(function () {
        window.close();
    });
</script>