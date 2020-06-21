 var checkVisibility = function(checkbox, component) {
    $(checkbox).change(function() {
        if ($(this).is(":checked")) {
            $(component).css('visibility', 'visible');
        } else {
            $(component).css('visibility', 'hidden');
        }
    });
}

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

var joystickSolver = function(board){
     return {
         get : function() {
             return Direction.LEFT;
         }
     };
};

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

var oldSolver = null;

var joystickEnableDisable = function() {
    if ($('#client-connect').prop('checked')) {
        _disconnect();
    }

    if ($('#joystick').prop('checked')) {
        oldSolver = DirectionSolver;
        DirectionSolver = joystickSolver;

        _connect();
    } else {
        DirectionSolver = oldSolver;
        oldSolver = null;
    }
}

var ws = null;

$(document).ready(function() {
    checkVisibility('#show-graphic', '#board-canvas');
    checkVisibility('#show-text', '#board');

    $('#client-connect').change(function() {
        apply(this.checked);
    });
    _connect();

    $('#joystick').prop('checked', false);
    $('#joystick').change(joystickEnableDisable);
});

