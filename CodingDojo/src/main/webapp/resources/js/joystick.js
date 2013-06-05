var currentCommand = null;

function initJoystick(allPlayersScreen, playerName, contextPath) {
    if (allPlayersScreen) {
        $("#joystick").hide();
        return;
    }

    $("#player_name").click(function(){
        if ($("#joystick").is(":visible")) {
            $("#joystick").hide();
        } else {
            $("#joystick").show();
        }
    });

    function constructUrl() {
        return contextPath + "joystick"
    }

    function ok() {
    }

    function sendCommand(command) {
        $.ajax({ url:constructUrl(),
                data:'command=' + command + '&playerName=' + playerName,
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
            if (currentCommand != null) {
                if (currentCommand != "act") {
                    return;
                }
                 currentCommand += "," + command;
            }
            currentCommand = command;
            sendCommand(currentCommand);
        });
        $("body").keypress(function(event) {
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