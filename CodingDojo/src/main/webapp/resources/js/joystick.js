function initJoystick(allPlayersScreen, playerName, contextPath) {
    if (allPlayersScreen) {
        $("#joystick").hide();
        return;
    }

    $("#joystick").show();

    function constructUrl() {
        return contextPath + "joystick"
    }

    function ok() {
    }

    function sendKey(key) {
        $.ajax({ url:constructUrl(),
                data:'key=' + key + '&playerName=' + playerName,
                dataType:"json",
                cache:false,
                complete:ok,
                timeout:30000
            });
    }

    function registerKey(key) {
        $("#" + key).click(function() {
            sendKey(key);
        });
    }

    registerKey("up");
    registerKey("down");
    registerKey("left");
    registerKey("right");
    registerKey("act");
}