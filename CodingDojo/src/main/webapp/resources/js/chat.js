var chatLog = null;

function initChat(playerName, contextPath) {
    var chat = $("#chat");
    var container = $("#chat-container");
    var chatMessage = $("#chat-message");
    var sendButton = $("#chat-send");

    function chatStyle() {
        var width = container.width();
        var margin = 20;
        $("#glasses").width($("#glasses").width() - width - margin);

        container.width(width).css({ position: "absolute",
            marginLeft: 0, marginTop: margin,
            top: 0, left: $("#glasses").width()});
    }

    function send(message) {
        $.ajax({ url:encodeURI(contextPath + "chat?playerName=" + playerName + "&message=" + message),
            data:{},
            dataType:"json",
            cache:false,
           timeout:30000});
    };

    function sendToChat() {
        send(chatMessage.val());
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
        return decodeURIComponent(temp);
    }

    function updateChat() {
        if (chatLog != null) {
            var log = unescapeUnicode(chatLog);
            chat.html(log.split('\\n').join('</br>').split('script').join('div'));
            chatLog = null;
        }
        setTimeout(updateChat, 1000);
    }

    updateChat();

    $(window).resize(chatStyle);
    chatStyle();
}