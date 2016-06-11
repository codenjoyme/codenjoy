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
        $(page).prependTo($("#board_page"));
        $("#main_board").empty();
        $("#glasses").prependTo($("#main_board"));

        $(document).ready(function () {
            if (!!onPageLoad) {
                onPageLoad();
            }
        });
    })
}