function initLayout(gameName, pageName, contextPath, scriptSources, onPageLoad) {

    var appendUrl = function(string, search, substring) {
        $.each(search, function(index, found) {
            string = string.split(found).join(found + substring);
        });
        return string;
    }

    var loadLayout = function(onLoad) {
        var resource = "resources/" + gameName + "/";
        $.ajax({ url:contextPath + resource + pageName,
            success:function (data) {
                var found = ['<link href="', '<img src="', '<script src="'];
                data = appendUrl(data, found, contextPath + resource);
                if (!!onLoad) {
                    onLoad(data);
                }
            },
            cache:false,
            timeout:30000
        });
    }

    loadLayout(function(page) {
        var bodyWasVisible = $(document.body).is(":visible");
        $(document.body).hide();

        $(page).prependTo($("#board_page"));

        $("#main_board").empty();
        $("#glasses").prependTo($("#main_board"));

        $("#main_leaderboard").empty();
        $("#leaderboard").prependTo($("#main_leaderboard"));

        // because http://stackoverflow.com/questions/5085228/does-jquery-append-behave-asynchronously
        setTimeout(function() {
            if (bodyWasVisible) {
                $(document.body).show();
            }
            if (!!onPageLoad) {
                onPageLoad();
            }
            $.each(scriptSources, function(index, script) {
                $("head").append('<script type="text/javascript" src="' +
                        game.contextPath + 'resources/' + gameName + '/' + script +
                        '"></script>');
            });
        }, 300);
    })
}