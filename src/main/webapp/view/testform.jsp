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
            $.ajax({url:"/test/?test=true&left=1"});
            return false;
        }
        if (e.keyCode == 38) {
            $.ajax({url:"/test/?test=true&rotate=1"});
            return false;
        }
        if (e.keyCode == 39) {
            $.ajax({url:"/test/?test=true&right=1"});
            return false;
        }
        if (e.keyCode == 40) {
            $.ajax({url:"/test/?test=true&drop=1"});
            return false;
        }
    });
</script>

<form action="/test">
    Left: <input type="text" name="left"/> <br/>
    Right: <input type="text" name="right"/> <br/>
    Rotate: <input type="text" name="rotate"/> <br/>
    Drop: <input type="checkbox" name="drop" value="drop"/> <br/>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>