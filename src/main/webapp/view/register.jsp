<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form:form commandName="player" action="register" method="POST">
    <table>
        <tr>
            <td>Player name:<form:errors path="name"/></td>
        </tr>
        <tr>
            <td><form:input path="name"/></td>
        </tr>
        <tr>
            <td>URL:<form:errors path="callbackUrl"/></td>
        </tr>
        <tr>
            <td><form:input path="callbackUrl"/></td>
        </tr>
        <tr>
            <td colspan="3">
                <input type="submit" value="Register"/>
            </td>
        </tr>
    </table>
</form:form>