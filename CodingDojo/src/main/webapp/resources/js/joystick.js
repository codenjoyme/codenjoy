var currentCommand = null;

function initJoystick(playerName, registered, code, contextPath) {
    var container = "#div_" + playerName;
    var joystick = $(container + " #joystick");
    var actParams = $("#act_params");

    if (!registered) {
        joystick.hide();
        return;
    }

    function visible() {
        return joystick.is(":visible");
    }

    $(container + " #player_name").click(function(){
        if (visible()) {
            joystick.hide();
        } else {
            joystick.show();
        }
    });

    function constructUrl() {
        return contextPath + "joystick";
    }

    function ok() {
    }

    function sendCommand(command) {
        if (!visible()) return;
        $.ajax({ url:constructUrl(),
                data:'command=' + command + '&playerName=' + playerName + "&code=" + code,
                dataType:"json",
                cache:false,
                complete:ok,
                timeout:30000
            });
    }

    function parseCommand(event) {
        var keyCode = event.keyCode;
        if (keyCode == 0) {
            keyCode = event.charCode;
        }
        switch (keyCode) {
            case 38 : return "up";
            case 37 : return "left";
            case 39 : return "right";
            case 40 : return "down";
            case 32 : return "act";
        }
    }

    function registerCommand(command) {
        $("#" + command).click(function() {
            var params = (actParams.val() == '')?"":("(" + actParams.val() + ")");
            var result = command + params;
            if (currentCommand != null) {
                if (currentCommand != "act") {
                    return;
                }
                currentCommand += "," + result;
            }
            currentCommand = result;
            sendCommand(currentCommand);
        });
        $("body").keydown(function(event) {
            if (currentCommand != null) {
                return;
            }
            currentCommand = parseCommand(event);
            sendCommand(currentCommand);
        });
        $("body").keyup(function(event) {
            currentCommand = null;
        });
    }

    registerCommand("up");
    registerCommand("down");
    registerCommand("left");
    registerCommand("right");
    registerCommand("act");
}