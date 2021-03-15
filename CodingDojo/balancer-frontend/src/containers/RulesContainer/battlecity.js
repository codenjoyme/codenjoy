// vendor
import React, {  Component  } from 'react';
import {  connect  } from 'react-redux';
// import classnames from 'classnames';
import {  Link  } from 'react-router-dom';
import {  CopyToClipboard  } from 'react-copy-to-clipboard';

// proj
import {  GameElements  } from '../../components';
import {  getGameConnectionString, getJavaClient  } from '../../utils';
import { requestSettingsStart } from '../../redux/settings';
import {  book  } from '../../routes';
import Icon from '../../styles/images/icons/rules.svg';
import Game from '../../games';

// own
import Styles from './styles.module.css';

const endDate = process.env.REACT_APP_EVENT_END_DATE;
const dayTimeStart = process.env.REACT_APP_EVENT_START_TIME;
const dayTimeEnd = process.env.REACT_APP_EVENT_FINAL_TIME;
const eventName = process.env.REACT_APP_EVENT_NAME;
const finalTimeStart = process.env.REACT_APP_EVENT_FINAL_DAY_START_TIME;

const BOARD_EXAMPLE =
`Board:
☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼
☼ ¿    ¿    ¿        ¿    ¿    ¿ ☼
☼                                ☼
☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼
☼ #╬╬╬# ╬╬╬ #╬╬╬##╬╬╬# ╬╬╬ #╬╬╬# ☼
☼ #╬╬╬# ╬╬╬ #╬╬╬##╬╬╬# ╬╬╬ #╬╬╬# ☼
☼ #╬╬╬# ╬╬╬ #╬╬╬##╬╬╬# ╬╬╬ #╬╬╬# ☼
☼ #╬╬╬# ╬╬╬ #╬╬╬☼☼╬╬╬# ╬╬╬ #╬╬╬# ☼
☼ #╬╬╬# ╬╬╬ #╬╬╬☼☼╬╬╬# ╬╬╬ #╬╬╬# ☼
☼ #╬╬╬# ╬╬╬ #╬╬╬  ╬╬╬# ╬╬╬ #╬╬╬# ☼
☼ #╬╬╬# ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬ #╬╬╬# ☼
☼ #╬╬╬# ╬╬╬            ╬╬╬ #╬╬╬# ☼
☼  ╬╬╬  ╬╬╬   ~    ~   ╬╬╬  ╬╬╬  ☼
☼  ~~~       ╬╬╬  ╬╬╬       ~~~  ☼
☼  ~~        ╬╬╬  ╬╬╬        ~~  ☼
☼     ╬╬╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬╬╬     ☼
☼☼☼   ╬╬╬╬╬            ╬╬╬╬╬   ☼☼☼
☼ ~~          %%%%%%          ~~ ☼
☼           ~╬╬╬%%╬╬╬~           ☼
☼  ╬╬╬  ╬╬╬ ~╬╬╬%%╬╬╬~ ╬╬╬  ╬╬╬  ☼
☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼
☼  ╬╬╬~ ╬╬╬  ╬╬╬╬╬╬╬╬  ╬╬╬ ~╬╬╬  ☼
☼  ╬╬╬  ╬╬╬  ╬╬╬╬╬╬╬╬  ╬╬╬  ╬╬╬  ☼
☼ %╬╬╬  ╬╬╬  ╬╬╬%%╬╬╬  ╬╬╬  ╬╬╬% ☼
☼ %╬╬╬  ╬╬╬~ ╬╬╬%%╬╬╬ ~╬╬╬  ╬╬╬% ☼
☼ %╬╬╬  ╬╬╬~ ╬╬╬%%╬╬╬ ~╬╬╬  ╬╬╬% ☼
☼ %╬╬╬ ~╬╬╬  ╬╬╬%%╬╬╬  ╬╬╬~ ╬╬╬% ☼
☼ %╬╬╬  %%%            %%%  ╬╬╬% ☼
☼  ╬╬╬  %%%    ~~~~    %%%  ╬╬╬  ☼
☼  ╬╬╬  %%%  ╬╬╬╬╬╬╬╬  %%%  ╬╬╬  ☼
☼  ╬╬╬       ╬╬╬╬╬╬╬╬       ╬╬╬  ☼
☼            ╬╬    ╬╬            ☼
☼  %%%%%%    ╬╬    ╬╬    %%%%%%  ☼
☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼`;

const {  boardExample, mask, highligte, highligteNotes  } = Styles;

class RulesContainer extends Component {
    componentDidMount() {
        this.props.requestSettingsStart();
    }

    _gets(name) {
        const {  server, code, id, settings  } = this.props;
        const loggedIn = [ server, code, id ].every(Boolean);

        return (loggedIn && !!settings)
            ? ( <b>&nbsp;(<a href="#settings">{ settings[0][name] }*</a>)</b> )
            : ( <b><a href='#settings'>*</a></b> );
    }

    render() {
        const {  server, code, id, settings  } = this.props;
        const loggedIn = [ server, code, id ].every(Boolean);
        const localhostConnectionUrl = getGameConnectionString('127.0.0.1:8080', '12345678901234567890', 'anyidyouwant');
        const connectionUrl = loggedIn
            ? getGameConnectionString(server, code, id)
            : localhostConnectionUrl;
        const privacyRulesUrl = process.env.REACT_APP_EVENT_LINK + '/privacyRules';
        const settingsLink = process.env.REACT_APP_API_SERVER + '/codenjoy-balancer/rest/game/settings/get';
        const privacyRulesDetailsUrl = privacyRulesUrl + '#details3';
        const joinSlackUrl = process.env.REACT_APP_JOIN_CHAT_LINK;
        const clientLink = loggedIn
            ? (
                <a href={ getJavaClient(server) }>Завантажити клієнт</a>
            )
            : ''

        return (
            <div className='container'>
                <div className={ mask }
                     style={{ backgroundImage: `url("${Game.dark}")` }}>
                    { eventName } - як грати?
                </div>
                <div className='content'>
                    <h2 className='title'>Регламент проведення фіналу:</h2>
                    <p>
                        Пропонуємо приєднатись <span className='command'>{ endDate }</span>
                         <span> о </span>
                          <span className='command'>{ dayTimeStart }</span> годині на сайт гри для того,
                        щоб заздалегідь вирішити всі можливі нюанси зі связком та підключенням до ігрового серверу або будь-які інші.
                    </p>
                    <p>
                        І вже о <span className='command'>{ finalTimeStart }</span> розпочнеться заліковий час,
                        який буде тривати до <span className='command'>{ dayTimeEnd }</span>!
                    </p>
                    <p>
                        Також зверніть увагу, що у Фіналі час від часу ми будемо повертати гравців на перший рівень,
                        щоб прибрати вплив ботів, які не грають.
                    </p>

                    <h2 className='title'>У чому суть гри?</h2>
                     <div className='subTitle' id='commands'>
                                            Параметри гри:
                     </div>

                    <p>
                        Потрібно написати свого бота для танку, який здатен обіграти інших
                        ботів за сумою балів. Всі грають на одному полі. Танк може
                        пересуватися по відкритим клітинкам у чотири доступні сторони.
                        В танка є можливість зробити постріл. Снаряд рухається в двічи швидше ніж гравець.
                        Снаряд вміє руйнувати стіни та вбивати інщі танки.
                    </p>
                    <p> За вбивство вбивство інших танків вам нараховуються очки. За загибель
                        вашого танку можуть нараховуватися штрафні бали. Перемогає гравець,
                        який набрав більше за всіх очків. Вбитий танк відразу з'являється
                        на полі у випадковому місці.
                    </p>
                    <p> Крім звичайних ворожих танків є танки з призами. Щоб знищити
                        такий танк треба потрапити кілька разів. Після вбивства призового танка
                        з нього випадає приз, який потрібно підібрати. Якщо цього не зробити,
                        то через деякий час він зникне. За вбивство призового танка гравцеві також
                        нараховуються очки. Варто бути уважним, приз випадково можна знищити
                        снарядом - якщо це трапиться, він так само зникне з поля.
                    </p>
                    <p> Є кілька видів призів. Кожен з яких на час дає танку
                        певну перевагу. Підібраний під час гри приз PRIZE_IMMORTALITY
                        робить танк гравця невразливим до ворожих снарядів.
                        А приз PRIZE_WALKING_ON_WATER дасть можливість ходити по воді.
                        Приз PRIZE_BREAKING_WALLS дозволить пробивати стіни.
                        Приз діє деякий час. Кожен парний тик гри приз 'мерехтить' символом PRIZE.
                    </p>
                    <p>
                        Герой отримує очки за:
                        <ul>
                            <li> знищення AI-ботів: { this._gets('killOtherAiTankScore') };</li>
                            <li> знищення іншого героя: { this._gets('killOtherHeroTankScore') };</li>
                        </ul>
                    <p>
                        Герой втрачає очки за:
                        <ul>
                            <li> пенальті за загибель: { this._gets('killYourTankPenalty') }.</li>
                        </ul>
                    </p>

                    <div className='subTitle' id='client'>
                        Завантажити клієнт гри для створення бота можна за цим посиланням:  { clientLink } { !loggedIn && '(посилання стануть доступні після авторизації на сайті)' }
                    </div>
                    <p>
                        Пам'ятайте, що у процесі написання бота вам необхідно
                        піклуватись про логіку переміщень.
                        Допоміжні елементи інфраструктури вже імплементовані для вашої зручності.
                        Право на подальше вдосконалення клієнтської частини залишається за вами.
                    </p>
                    <p>
                        Зареєструйтеся за допомогою форми реєстрації нового
                        гравця. Запам'ятайте вказані дані (адресу електронної пошти
                        та пароль) - вони знадобляться вам у майбутньому для
                        авторизації на сайті.
                    </p>
                    <p>
                        Далі необхідно приєднатися з коду клієнта до сервера.
                    </p>

                    <div className='subTitle' id='client-url'>
                        Адреса для підключення до гри на сервері
                    </div>

                    { loggedIn ? (
                        <>
                            <div className={ highligte }>
                                { connectionUrl }
                                <CopyToClipboard text={ connectionUrl }>
                                    <img
                                        className={ Styles.copyConnection }
                                        src={ Icon }
                                        alt='Скопіювати адрес'
                                    />
                                </CopyToClipboard>
                            </div>
                            <p>
                                Тут 'user' - id гравця, a 'code' - ваш security token,
                                його ви зможете отримати тут після авторизації.
                            </p>
                        </>
                    ) : (
                        <div className={ highligte }>
                            <Link to={ book.login }>
                                Потрібно увійти в систему для отримання посилання
                            </Link>
                        </div>
                    ) }

                    <p>
                        Після підключення, клієнт щосекунди отримуватиме
                        рядок символів із інформацією про поточний стан гри.
                        <br />
                    </p>
                    <div className='subTitle' id='board'>
                        Приклад форматованого рядка від сервера
                    </div>
                    <div className={ highligte }>
                        <pre className={ boardExample }>{ BOARD_EXAMPLE }</pre>
                    </div>
                    <p> Лівий нижній кут поля має координату [0, 0]
                    </p>
                     <div className='subTitle' id='elements'>
                        Розшифрування символів
                    </div>
                    <p className="game-field-img-container">
                        <img className="responsive-img" src={ Game.boardSample } alt='Ігрове поле'/>
                    </p>
                    <GameElements
                        settings={ settings }
                    />
                    <div className='subTitle' id='commands'>
                        Керування ботом
                    </div>
                    <p>
                        Гра покрокова. Кожну секунду сервер відправляє вашому
                        клієнту (боту) стан оновленого поля на поточний момент і
                        чекає на відповідь команди героя. За наступну секунду
                        гравець повинен встигнути дати команду герою. В іншому випадку,
                        герой стоятиме нерухомо на тому ж місці.
                    </p>
                    <p>
                        Перелік доступних команд:
                    </p>
                        <ul>
                            <li><span className='command'>Direction[UP, DOWN, LEFT, RIGHT]</span> - призводять до руху танку в заданому напрямку на 1 клітинку.</li>
                            <li><span className='command'>ACT</span> - танк робить постріл стоячи на місці. </li>
                            <li><span className='command'>ACT,Direction</span> - танк робить постріл, а потім рухається у вказанному напрямку. </li>
                            <li><span className='command'>Direction,ACT</span> - танк танк рухається та робить постріл у вказанному напрямку</li>
                        </ul>
                    <p>
                        Ваше завдання - підключитися з клієнтської частини до ігрового сервера через веб-сокет з'єдання.
                        Спостерігайте за ігровим процесом, щоб описана вами поведінка бота приносила бажані очки.
                        Гравці з найбільшою кількістю очків, набраних у ігровому дні,
                        відправляються в фінал для боротьби за головний приз. Чи готові ви боротися за перемогу?!
                    </p>

//                    <p>//todo ПОРЕШАТЬ С ВЫХОДОМ
//                        Гра має одну особливість. Перед тим як почати змагатися с іншими героями,
//                        вам потрібно пройти тренувальні раунди. Для початку це будуть дуже прості рівні,
//                        де ваша ціль - дійти до <span className='command'>EXIT('E')</span>.
//                    </p>

                    <div className='subTitle' id='perks'>
                        Певні особливості гри
                    </div>
                    <ul>
                        <li>
                            <span className='command'>ICE</span> - крижане поле.
                            При заїзді на це поле ваш танк перестає слухатися команд и певну кількість кроків
                            виконує попередню передану команду. <br />
                            <ul class="sub_ul">
                                <li>Кількість тіків, які танк ковзає на кризі: { this._gets('slipperiness') }.</li>
                            </ul>
                        </li>
                        <li>
                            <span className='command'>TREE</span> - Дерева/кущі.
                            Приховують танки, які при цьому можуть продовжувати рух та стріляти.
                            Постріли також не видно поза кущами. <br />
                            Через кущі можна побачити лише тільки призи.
                        </li>
                        <li>
                            <span className='command'>RIVER</span> - Річка/вода.
                            Танк не може по ній їздити але може стріляти поверх неї.
                            <br />
                        </li>
                    </ul>

                    <div className='subTitle' id='perks'>
                        Модифікатори (Перки)
                    </div>
                    <ul>
                        <li>
                            <span className='command'>PRIZE_IMMORTALITY</span> - дає гравцю тимчасову
                            невразливість до ворожих пострілів. <br />
                            <ul class="sub_ul">
                                <li>
                                    Дія перку зникає через { this._gets('prizeWorking') } тіків (секунд).
                                </li>
                            </ul>
                        </li>
                        <li>
                            <span className='command'>PRIZE_BREAKING_WALLS</span> - дає право гравцю руйнувати стіни
                             з одного пострілу(навіть неруйнівні, окрім межи поля). <br />
                            <ul class="sub_ul">
                                <li>
                                    Дія перку зникає через { this._gets('prizeWorking') } тіків (секунд).
                                </li>
                            </ul>
                        </li>
                      <li>
                            <span className='command'>PRIZE_WALKING_ON_WATER</span> - дає право гравцю рухатися по воді. <br />
                            Якшо дія перку закінчилася а танк ще у воді - накладається штраф.
                            У цьому разі ви зможете рухатися та стріляти лише один за на кілька
                            тіків, доки танк не потрапить на вільну клітину.
                            <ul class="sub_ul">
                                <li>
                                    Дія перку зникає через { this._gets('prizeWorking') } тіків (секунд).
                                </li>
                                <li>
                                    Кількість штрафних тіків { this._gets('prizeWorking') } тіків (секунд). //todo Решить, что с реализацией штрафа на воде.
                                </li>

                            </ul>
                        </li>
                        <li>
                            Призи-перки з'являтимуться на місці вбитого призового AI-танку, який позначаеться символом
                            <span className='command'>AI_TANK_PRIZE</span> с певною періодичністю.
                        </li>
                        <li>
                            Якщо перк ніхто не підібрав, він зникає з поля
                            після { this._gets('prizeOnField') } тіків(секунд).
                        </li>
                    </ul>

                      <div className='subTitle' id='winners'>
                        Як визначатимуться переможці?
                    </div>
                    <p>
                        Детальніше про це можна прочитати
                        &nbsp;
                        <a href={ privacyRulesDetailsUrl }>
                            за посиланням<img src={ Icon } alt='Правила Конкурсу'/>
                        </a>
                    </p>

                    <div className='subTitle' id='additional'>
                        Додаткова інформація
                    </div>
                    <p>
                        <b>(<a name='settings'>*</a>)</b> - Точні значення: балів за руйнування на полі та штрафних балів;
                        кількості раундів в матчі; сили ефекту, таймаутів, ймовірності
                        випадання перків та інших змінних треба уточнити у організаторів
                        на початку ігрового дня у slack чаті або на цій сторінці після успішної авторизації:
                    </p>

                    <div className={ highligte } style={{whiteSpace:"pre"}} >
                    <p id="settings">
                        Параметри гри:
                    </p>
                        <ul>
                            <li>Штрафні бали коли гине ваш танк: { this._gets('killYourTankPenalty') }.</li>
                            <li>Очки, які ви заробляєтете, знищуючи інших ботів: { this._gets('killOtherHeroTankScore') }.</li>
                            <li>Очки, за знищення AI-ботів: { this._gets('killOtherAiTankScore') }.</li>
                            <li>Кількість пострілів, які потрібно зробити по призовому танку: { this._gets('killHitsAiPrize') }.</li>
                            <li>Час існування приза на полі: { this._gets('prizeOnField') }.</li>
                            <li>Час впливу приза на танк: { this._gets('prizeWorking') }.</li>
                            <li>Затримка між пострілами для AI-танків: { this._gets('aiTicksPerShoot') }.</li>
                            <li>Затримка між пострілами звичайних танків: { this._gets('tankTicksPerShoot') }.</li>
                            <li>Кількість тіків, які танк ковзає на кризі: { this._gets('slipperiness') }.</li>
                        </ul>
                    </div>
                    <p>
                        Будьте уважні - ці значення відрізнятимуться для різних
                        ігрових днів конкурсу.
                    </p>
                    <p>
                        Для спілкування між учасниками та організаторами
                        створено канал у додатку slack, приєднатися до якого
                        можна
                        &nbsp;
                        <a href={ joinSlackUrl } rel='noopener noreferrer' target='_blank'>
                            за посиланням<img src={ Icon } alt='Долучитися до чату'/>
                        </a>
                    </p>
                    <p>
                        Із детальним описом правил та положень гри можна
                        ознайомитися
                        &nbsp;
                        <a href={ privacyRulesUrl }>
                            за посиланням<img src={ Icon } alt='Правила Конкурсу'/>
                        </a>
                    </p>
                    </p>
                </div>
            </div>
        );
    }
}

const mapStateToProps = state => ({
    id:       state.auth.id,
    server:   state.auth.server,
    code:     state.auth.code,
    settings: state.settings.settings,
});
const mapDispatchToProps = { requestSettingsStart };

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(RulesContainer);
