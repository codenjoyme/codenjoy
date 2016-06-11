function initLayout(gameName, contextPath, onPageLoad) {

    var appendUrl = function(string, search, substring) {
        $.each(search, function(index, found) {
            string = string.split(found).join(found + substring);
        });
        return string;
    }

    var loadLayout = function(onLoad) {
        var resource = "resources/" + gameName + "/";
        $.ajax({ url:contextPath + resource + "layout.html",
            success:function (data) {
                var found = ['<link href="', '<img src="'];
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

        // because http://stackoverflow.com/questions/5085228/does-jquery-append-behave-asynchronously
        setTimeout(function() {
            if (bodyWasVisible) {
                $(document.body).show();
            }
            if (!!onPageLoad) {
                onPageLoad();
            }
        }, 300);
    })
}