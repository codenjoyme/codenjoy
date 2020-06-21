 var checkVisibility = function(checkbox, component) {
    $(checkbox).change(function() {
        if ($(this).is(":checked")) {
            $(component).css('visibility', 'visible');
        } else {
            $(component).css('visibility', 'hidden');
        }
    });
}

var ws = null;

var apply = function(isConnect) {
    if (isConnect) {
         ws = connect();

         ws.on('open', function() {
            $('#client-connect').prop('checked', true);
         });

         ws.on('close', function() {
            $('#client-connect').prop('checked', false);
         });
    } else {
         if (!!ws) {
            ws.close();
            ws = null;
         }
    }
}

var applyCheck = function(isConnect) {
    $('#client-connect').prop('checked', isConnect);
    apply(isConnect);
}

var _connect = function() {
    applyCheck(true);
}

var _disconnect = function() {
    applyCheck(false);
}

var parseCommand = function(event) {
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
        default : {
            if (keyCode >= 48 && keyCode <= 57) {
                return "act(" + (keyCode - 48) + ")"
            } else {
                return null;
            }
        };
    }
}

var currentCommand = null;

var onKeyDown = function(event) {
    if (!isJoystickEnabled) {
        return;
    }
    var command = parseCommand(event);
    if (!command) {
        return;
    }

    if (!!currentCommand) {
        if (command == 'act' && currentCommand != 'act' ||
            command != 'act' && currentCommand == 'act')
        {
            command = currentCommand + "," + command;
        }
    }
    currentCommand = command;
    if (hasData) {
        sendSockets = true;
        ws.send(currentCommand);
        sendSockets = false;
    }

    event.preventDefault();
};

var isJoystickEnabled = function() {
    return $('#joystick').prop('checked');
}

var joystickEnableDisable = function() {
    if ($('#client-connect').prop('checked')) {
        _disconnect();
    }

    if (isJoystickEnabled()) {
        sendSockets = false;

        _connect();
    } else {
        sendSockets = true;
    }
}

$(document).ready(function() {
    checkVisibility('#show-graphic', '#board-canvas');
    checkVisibility('#show-text', '#board');

    $('#client-connect').change(function() {
        apply(this.checked);
    });
    _connect();

    $('#joystick').prop('checked', false);
    $('#joystick').change(joystickEnableDisable);

    $("body").keydown(onKeyDown);
});

