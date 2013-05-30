<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<body>
<h2>Welcome! This is our Bomberman bot application</h2>
Ones it's connected to master server we will start receiving requests with playboards
and responding with bot actions. <br>
Requests received <%= request.getAttribute("totalNumberOfRequests") %><br>
Average response time (ms) <%= request.getAttribute("averageResponseTime") %>
</body>
</html>
