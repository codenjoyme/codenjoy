/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

/**
 * Отображать ли поле донейта.
 */
setup.enableDonate = false;

/**
 * Включен ли джойстик по-умолчанию.
 */
setup.enableJoystick = false;

// TODO понять что за настройка
setup.enableAlways = false;

/**
 * Отображать ли имя игрока над бордой.
 */
setup.enablePlayerInfo = true;

/**
 * Отображать ли уровень игрока над бордой.
 */
setup.enablePlayerInfoLevel = true;

/**
 * Отображать ли таблицу с очкам участников.
 */
setup.enableLeadersTable = true;

/**
 * Отображать ли поле fork me from github.
 */
setup.enableForkMe = true;

/**
 * Отображать ли ссылку на правила игры.
 */
setup.enableInfo = true;

/**
 * Работают ли хоткеи для перехода между страничками (Ctrl-Alt-A, ...)
 */
setup.enableHotkeys = true;

/**
 * Отображать ли поле с рекламой над лидербордом.
 */
setup.enableAdvertisement = false;

/**
 * Отображать ли борду после загрузки всего контента
 * или предоставить это самой игре, когда она будет готова.
 */
setup.showBody = true;

/**
 * Иногда случается так, что игра рисуется разными спрайтами -
 * тут задается набор этих спрайтов.
 */
setup.sprites = null;

// TODO понять что за настройка
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

/**
 * Стиль курсора мыши над областью борды.
 */
setup.canvasCursor = 'auto';

/**
 * Грузить ли данные с сервера необходимые для прорисовки борды.
 */
setup.loadBoardData = true;

/**
 * Прорисовывать ли борды.
 */
setup.drawCanvases = true;

/**
 * Включать ли чат.
 */
setup.enableChat = true;

setup.setupGame = function() {
    // override this method if you want to do
    // something before board page loading
}

setup.setupSprites = function() {
    // override this method if you want to customize
    // sprites after loading data from server
}

setup.onPageLoad = function(allPlayers) {
    // override this method if you want to customize
    // onBoardAllPageLoad/onBoardPageLoad methods

    if (allPlayers) {
        if (!!setup.onBoardAllPageLoad) {
            setup.onBoardAllPageLoad();
        }
    } else {
        if (!!setup.onBoardPageLoad) {
            setup.onBoardPageLoad();
        }
    }
}

setup.debug = false;
setup['debugger'] = function() {
    debugger;
}

var getSettings = function(name, group) {
    group = (!group) ? '#settings' : group;
    var value = $(group).attr(name);

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
    if (!!(setup.csrfToken = getSettings('token', '#csrf'))) {
        setup.csrfParameterName = getSettings('parameter', '#csrf');
        setup.csrfHeaderName = getSettings('header', '#csrf');
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