<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title></title>
    <script src="/resources/jquery-1.7.2.js"></script>
</head>
<body>
<script type="text/javascript">
    $(document).keydown(function (e) {
        if (e.keyCode == 37) {
            $.ajax({url:"/test/?test=true&move=left"});
            return false;
        }
        if (e.keyCode == 38) {
            $.ajax({url:"/test/?test=true&move=down"});
            return false;
        }
        if (e.keyCode == 39) {
            $.ajax({url:"/test/?test=true&move=right"});
            return false;
        }
        if (e.keyCode == 40) {
            $.ajax({url:"/test/?test=true&move=up"});
            return false;
        }
    });
</script>

<form action="/test">
    Joystick: <input type="text" name="left"/> <br/>
</form>
</body>
</html>