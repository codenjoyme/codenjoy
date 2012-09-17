<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Help</title>
    <link href="/resources/css/bootstrap.css" rel="stylesheet">
</head>
<body>
<div class="page-header">
    <h1>Help</h1>
</div>
<h3>Environment setup and registration</h3>
<ol>
    <li>Download server templates from this location <a href="/resources/user/tetris-servers.zip">tetris-servers.zip</a></li>
    <li>Setup project according to instruction in README.txt for your developing language</li>
    <li>Open registration page <a href="http://<%=request.getServerName()+":"+request.getServerPort()+"/register"%>">http://<%=request.getServerName()+":"+request.getServerPort()+"/register"%></a> </li>
    <li>Enter your name and full URL address of your server. Please check, <a href="/resources/how_to_get_ip.png">how to get your IP</a></br>
    For example:</br>
    <img src="/resources/register.png"></li>
</ol>

<h3>Game controls</h3>
<h4>Game server request parameters</h4>
<ol>
    <li><strong>figure</strong> - type of figure.</li>
    <li><strong>x</strong> - x coordinate of the figure center. Direction of X axis is from left to right side of the glass. </li>
    <li><strong>y</strong> - y coordinate of the figure center. Direction of Y axis is from bottom to top of the glass. </li>
    <li><strong>glass</strong> - string representing current state of the glass.
        Size of the string is always equals to 200 (10 columns x 20 rows). Occupied cells are marked by * (asterix).
    </li>
</ol>

<h4>You server response parameters</h4>
<ol>
    <li><strong>left=xxx</strong> - move current figure to the left by xxx amount of steps.</li>
    <li><strong>right=yyy</strong> - move current figure to the right by yyy amount of steps.</li>
    <li><strong>rotate=zzz</strong> - rotate current figure by zzz * 90 degrees clockwise. See Figures section for details. </li>
    <li><strong>drop</strong> - drop current figure.</li>
</ol>

<h3>Figures</h3>
<table class="table">
    <thead>
    <tr>
        <th>I</th>
        <th>O</th>
        <th>L</th>
        <th>J</th>
        <th>S</th>
        <th>Z</th>
        <th>T</th>
    </tr>
    </thead>
    <tr>
        <td><img src="/resources/I.png"/></td>
        <td><img src="/resources/O.png"/></td>
        <td><img src="/resources/L.png"/></td>
        <td><img src="/resources/J.png"/></td>
        <td><img src="/resources/S.png"/></td>
        <td><img src="/resources/Z.png"/></td>
        <td><img src="/resources/T.png"/></td>
    </tr>
</table>
</body>
</html>