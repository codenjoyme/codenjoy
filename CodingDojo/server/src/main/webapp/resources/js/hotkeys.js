var adminKey = false;

function initHotkeys(gameName, contextPath) {
    var gameNameParam = ((gameName == '')?'':'gameName=' + gameName);
    $('body').keydown(function(ev) {
        if (ev.ctrlKey && ev.altKey && ev.keyCode == 65) {
            adminKey = true;
        } else if (adminKey && ev.keyCode == 68) {
            window.open(contextPath + 'admin31415' + (gameNameParam == ''?'':'?select&') + gameNameParam);
        } else if (adminKey && ev.keyCode == 82) {
            window.open(contextPath + 'register' + (gameNameParam == ''?'':'?') + gameNameParam);
        } else if (adminKey && ev.keyCode == 83) {
            window.open(contextPath);
        } else {
            adminKey = false;
        }
    });
}