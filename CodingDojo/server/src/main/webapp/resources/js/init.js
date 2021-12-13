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
var setup = setup || {};

setup.enableDonate = false;
setup.enableJoystick = false;
setup.enableAlways = false;
setup.enablePlayerInfo = true;
setup.enablePlayerInfoLevel = true;
setup.enableLeadersTable = true;
setup.enableForkMe = true;
setup.enableInfo = true;
setup.enableHotkeys = true;
setup.enableAdvertisement = false;
setup.showBody = true;
setup.sprites = null;
setup.heroInfo = null;
/**
 * На канве будут прорисовываться только измененные
 * (между двумя тиками) спрайты, что сильно улучшит
 * производительность отрисовки борды на канве.
 * Особенно это заметно когда рисуется большое количество
 * (больше 30 штук) больших по размеру (от 50x50) борд.
 */
setup.isDrawOnlyChanges = true;
/**
 * true - если спрайты будут рисоваться по типам
 * в порядке, указанном в Elements. Иначе спрайты
 * будут прорисовываться по координатам, независимо
 * от их типа (слева направо, сверху вниз)
 */
setup.isDrawByOrder = false;
/**
 * true если background и fog мы размножаем по канве,
 * false если растягиваем.
 */
setup.isFillOrStrechBackground = true;
/**
 * Печатать ли пришедшую борду с каждым тиком в консоль аль нет
 */
setup.isPrintBoardToConsole = false;
setup.canvasCursor = 'auto';
setup.loadBoardData = true;
setup.drawCanvases = true;
setup.enableChat = true;
setup.setupSprites = function() {
    // override this method if you want to customize sprites before draw
}

setup.debug = false;
setup['debugger'] = function() {
    debugger;
}

var getSettings = function(name) {
    var value = $('#settings').attr(name);

    if (typeof(value) === 'undefined') {
        return null
    }

    if (value === '') {
        return null;
    }

    if (value === 'true' || value === 'false'){
        return (value === 'true');
    }

    return value;
}

var getTickDateTime = function(time, readable) {
    var date = new Date(parseInt(time));
    return date.getFullYear() + '-' +
            (date.getMonth() + 1).padLeft() + '-' +
            date.getDate().padLeft() +
           (!!readable ? ' ' : 'T') +
           getTickTime(time, readable);
}

var getTickTime = function(time, readable) {
    var date = new Date(parseInt(time));
    return date.getHours().padLeft() + ':' +
           date.getMinutes().padLeft() +
           (!!readable ? '' : (':' + date.getSeconds().padLeft() +
                               '.' + date.getMilliseconds()));
}

var setupCsrf = function() {
    if (!!(setup.csrfToken = getSettings('csrfToken'))) {
        setup.csrfParameterName = getSettings('csrfParameter');
        setup.csrfHeaderName = getSettings('csrfHeader');
        setup.csrfAjaxHeader = {};
        setup.csrfAjaxHeader[setup.csrfHeaderName] = setup.csrfToken;
    } else {
        setup.csrfToken = null;
        setup.csrfParameterName = null;
        setup.csrfHeaderName = null;
        setup.csrfAjaxHeader = null;
    }
}

var pages = pages || {};

$(document).ready(function() {
    setupCsrf();

    var page = getSettings('page');
    pages[page]();
});