function initConsole() {
    // ----------------------- init console -------------------
    var console = $('#ide-console');
    console.empty();

    var print = function(message) {
        console.append('> ' + message + '<br>')
        console.animate({scrollTop: console.prop('scrollHeight')});
    }

    var error = function(message) {
        print('Error: ' + message);
    }

    var clean = function() {
        console.empty();
    }

    return {
        print : print,
        error : error,
        clean : clean
    };
};