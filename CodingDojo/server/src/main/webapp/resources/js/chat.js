var chatLog = null;

function initChat(playerName, registered, code, contextPath, gameName) {

    var chatInfo = $("#chat-info");
    var gameInfo = $("#game-info");
    var chat = $("#chat");
    var container = $("#chat-container");
    var chatMessage = $("#chat-message");
    var sendButton = $("#chat-send");
    var leaderboard = $("#leaderboard");

    gameInfo.html('<h3><a href="' + contextPath + 'resources/help/' + gameName + '.html" target="_blank">How to play ' + gameName + '</a></h3>');

    if (!registered) {
        if (!!code) {
            chatInfo.html('<h3>This is not your user. Please <a href="' + contextPath + 'register?name=' + playerName + '&gameName=' + gameName + '>login</a></h3>');
        } else {
            chatInfo.html('<h3>Please <a href="' + contextPath + 'register?name=' + playerName + '&gameName=' + gameName + '">register</a></h3>');
        }
        chatMessage.hide();
        sendButton.hide();
    }

    function chatStyle() {
        var minGlassesWidth = 830;
        var width = container.width();
        var margin = 20;
        var newGlassesWidth = $(window).width() - width - 2*margin - $("#leaderboard").width();

        container.css({ position: "absolute",
            marginLeft: 0, marginTop: margin});

        if (newGlassesWidth > minGlassesWidth) {
            container.show();
            leaderboard.show();

            $("#glasses").width(newGlassesWidth);

            container.css({ height: $(window).height() - 150, top: 0, left: $("#glasses").width()});
        } else if ($(window).width() - width - 3*margin < $("canvas").width()) {
            container.hide();
            leaderboard.hide();
        } else {
            container.show();
            leaderboard.show();

            container.css({ height: $(window).height() - 150 - $("#leaderboard").height(),
                top: $("#leaderboard").height(), left: $("#glasses").width()});
        }
        chat.height(container.height());
    }

    function send(message) {
        $.ajax({ url:encodeURI(contextPath + "chat?playerName=" + playerName + "&code=" + code + "&message=" + message),
            data:{},
            dataType:"json",
            cache:false,
           timeout:30000});
    };

    function removeEOL(string) {
        return string.split(/\r\n|\r|\n/g).join(" ");
    }

    function sendToChat() {
        var value = chatMessage.val();
        if (value == '') return;
        send(removeEOL(value));
        chatMessage.val('') ;
    }

    chatMessage.keypress(function(e) {
        var code = (e.keyCode ? e.keyCode : e.which);
        if(code == 13) {
            sendToChat();
            e.preventDefault();
        }
    });

    sendButton.click(sendToChat);

    function unescapeUnicode(unicode) {
        var r = /\\u([\d\w]{4})/gi;
        var temp = unicode.replace(r, function (match, grp) {
            return String.fromCharCode(parseInt(grp, 16)); } );
        return decodeURIComponent(temp).split("\\\"").join("\"");
    }

    function boldName(string) {
        return '<b><span style="color:blueviolet;">' + string.replace("] ", "]</span> ").replace(": ", "</b>: ").replace("[]","");
    }

    function removeScript(string) {
        return string.split('script').join('div');
    }

    function splitWord(word) {
        var max = 65;
        if (word.length < max) return word;

        var result = "";
        for (var index = 0; index < word.length; index++) {
            result += word[index];
            if (index % max == (max - 1)) result += " ";
        }
        return result;
    }

    function cutLong(string) {
        var words = string.split(' ');
        var result = '';
        $.each(words, function(index, value) {
            result += splitWord(value) + " ";
        });
        return result;
    }

    function colorInfo(string) {
        return '<span style="color:silver;">' + string + '</span></br>';
    }

    function uncodeLog(value) {
        var log = unescapeUnicode(value);
        var lines = log.split('\\n');
        var result = "";
        $.each(lines, function(index, value) {
            if (value.indexOf(':') == -1) {
                result += colorInfo(value);
            } else {
                result += boldName(cutLong(strip_tags(removeScript(value), "<a><b><i><span>"))) + "</br>";
            }
        });
        return result;
    }

    function strip_tags (input, allowed) {
        // http://kevin.vanzonneveld.net
        // +   original by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
        // +   improved by: Luke Godfrey
        // +      input by: Pul
        // +   bugfixed by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
        // +   bugfixed by: Onno Marsman
        // +      input by: Alex
        // +   bugfixed by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
        // +      input by: Marc Palau
        // +   improved by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
        // +      input by: Brett Zamir (http://brett-zamir.me)
        // +   bugfixed by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
        // +   bugfixed by: Eric Nagel
        // +      input by: Bobby Drake
        // +   bugfixed by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
        // +   bugfixed by: Tomasz Wesolowski
        // +      input by: Evertjan Garretsen
        // +    revised by: Rafał Kukawski (http://blog.kukawski.pl/)
        // *     example 1: strip_tags('<p>Kevin</p> <br /><b>van</b> <i>Zonneveld</i>', '<i><b>');
        // *     returns 1: 'Kevin <b>van</b> <i>Zonneveld</i>'
        // *     example 2: strip_tags('<p>Kevin <img src="someimage.png" onmouseover="someFunction()">van <i>Zonneveld</i></p>', '<p>');
        // *     returns 2: '<p>Kevin van Zonneveld</p>'
        // *     example 3: strip_tags("<a href='http://kevin.vanzonneveld.net'>Kevin van Zonneveld</a>", "<a>");
        // *     returns 3: '<a href='http://kevin.vanzonneveld.net'>Kevin van Zonneveld</a>'
        // *     example 4: strip_tags('1 < 5 5 > 1');
        // *     returns 4: '1 < 5 5 > 1'
        // *     example 5: strip_tags('1 <br/> 1');
        // *     returns 5: '1  1'
        // *     example 6: strip_tags('1 <br/> 1', '<br>');
        // *     returns 6: '1  1'
        // *     example 7: strip_tags('1 <br/> 1', '<br><br/>');
        // *     returns 7: '1 <br/> 1'
        allowed = (((allowed || "") + "").toLowerCase().match(/<[a-z][a-z0-9]*>/g) || []).join(''); // making sure the allowed arg is a string containing only tags in lowercase (<a><b><c>)
        var tags = /<\/?([a-z][a-z0-9]*)\b[^>]*>/gi,
            commentsAndPhpTags = /<!--[\s\S]*?-->|<\?(?:php)?[\s\S]*?\?>/gi;
        return input.replace(commentsAndPhpTags, '').replace(tags, function ($0, $1) {
            return allowed.indexOf('<' + $1.toLowerCase() + '>') > -1 ? $0 : '';
        });
    }

    function updateChat() {
        if (chatLog != null) {
            chat.html(uncodeLog(chatLog));

            chatLog = null;
        }
        setTimeout(updateChat, 1000);
    }

    updateChat();

    $(window).resize(chatStyle);
    $("#leaderboard").bind('resize', function() {
        chatStyle();
    });
    chatStyle();
}