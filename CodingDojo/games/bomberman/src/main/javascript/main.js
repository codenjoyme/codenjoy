 var checkVisibility = function(checkbox, component) {
    $(checkbox).change(function() {
        if ($(this).is(":checked")) {
            $(component).css('visibility', 'visible');
        } else {
            $(component).css('visibility', 'hidden');
        }
    });
}

var connectDisconnect = function() {
    if ($('#client-connect').prop('checked')) {
         ws = connect();
    } else {
         if (!!ws) {
            ws.close();
            ws = null;
         }
    }
}

var ws = null;

$(document).ready(function() {
    checkVisibility('#show-graphic', '#board-canvas');
    checkVisibility('#show-text', '#board');

    $('#client-connect').change(connectDisconnect);
    $('#client-connect').prop('checked', true);
    connectDisconnect();
});

