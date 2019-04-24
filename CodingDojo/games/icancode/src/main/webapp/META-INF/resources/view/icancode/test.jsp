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
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
    <meta charset="UTF-8">
</head>
<div id="test"></div>

<script src="${ctx}/resources/icancode/js/jquery/jquery-3.1.0.min.js"></script>
<script src="${ctx}/resources/icancode/js/game/direction.js"></script>
<script src="${ctx}/resources/icancode/js/game/elements.js"></script>
<script src="${ctx}/resources/icancode/js/game/board.js"></script>
<script src="${ctx}/resources/icancode/js/game/robot.js"></script>
<script src="${ctx}/resources/icancode/js/game/point.js"></script>
<script src="${ctx}/resources/icancode/js/game/test/robotTest.js"></script>
<script>
    $(document).ready(function () {
        runTest();
    });
</script>