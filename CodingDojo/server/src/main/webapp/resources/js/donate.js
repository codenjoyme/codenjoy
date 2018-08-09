/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
function initDonate(contextPath) {
    var donate = $('#donate');
    var want = $('#want-donate');
    var payment = $('#payment');

    donate.show();

    want.click( function() {
        $.ajax({ url:contextPath + "/donate",
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
