<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<div id="leaderboard" style="display:none;">
    <table id="table-logs" class="table table-striped">
        <thead>
            <th width="5%">
                <c:choose>
                    <c:when test="${code != null}">
                        <a href="${ctx}/board?code=${code}">#</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${ctx}/board?gameName=${gameName}">#</a>
                    </c:otherwise>
                </c:choose>
            </th>
            <th width="55%">Player</th>
            <th width="25%" class="center">Score</th>
            <!-- th width="25%" class="center">Max</th -->
            <!-- th width="15%" class="center">Level</th -->
        </thead>
        <tbody id="table-logs-body">
        </tbody>
    </table>
</div>
