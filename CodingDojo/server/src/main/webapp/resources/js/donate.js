function initDonate(contextPath) {
    var donate = $('#donate');
    var want = $('#want-donate');
    var payment = $('#payment');

    donate.css({position: 'absolute', left: 300});
    donate.show();

    want.click( function() {
        $.ajax({ url:contextPath + "donate",
            success:function (data) {
                donate.append('<div id="donate-container"></div>');
                $("#payment").remove();
                var form = $('#donate-container');

                want.hide();
                form.hide();
                form.append(data);

                function closeDonate() {
                    modal.close();
                    want.show();
                }

                var modal = form.modal({onClose: closeDonate});
                $("#close-donate").click(closeDonate);

                $("#ok-donate").click( function () {
                    $("#payment").submit(function() {
                        setTimeout(function(){
                            modal.close();
                            want.hide();
                        }, 3000);
                    });
                });
            },
            cache:false,
            timeout:30000
        });
    });
}