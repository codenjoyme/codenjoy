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

const eventName = process.env.REACT_APP_EVENT_NAME;
const BOARD_EXAMPLE =
`board=☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼     &        #  #   ☼☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼
☼ ☼ ☼☼    # # #      ####  ☼☼ ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼☼☺            #
  #☼☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼#☼ ☼ ☼#☼☼# ### + ҉        &  #☼☼#☼ ☼ ☼ ☼҉☼♥☼ ☼ ☼ ☼
☼ ☼☼     ♣H҉҉H     #  #  ☼☼#☼ ☼#☼ ☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼☼ # #    ҉#    # ♥
  ☼☼ ☼ ☼ ☼#☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼☼                     ☼☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼♣
☼ ☼☼        &            ☼☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼☼  # #  #
# ☼☼&☼#☼ ☼ ☼ ☼ ☼#☼ ☼#☼ ☼ ☼☼     #           # & ☼☼ ☼#☼ ☼#☼ ☼ ☼ ☼#☼ ☼
☼ ☼☼##  #      #  #    # ☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼`;

const BOARD_EXAMPLE_2 =
`☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼
☼     &        #  #   ☼
☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼
☼    # # #      ####  ☼
☼ ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼
☼☺            #      #☼
☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼#☼ ☼ ☼#☼
☼# ### + ҉        &  #☼
☼#☼ ☼ ☼ ☼҉☼♥☼ ☼ ☼ ☼ ☼ ☼
☼     ♣H҉҉H     #  #  ☼
☼#☼ ☼#☼ ☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼
☼ # #    ҉#    # ♥    ☼
☼ ☼ ☼ ☼#☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼
☼                     ☼
☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼♣☼ ☼
☼        &            ☼
☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼
☼  # #  #           # ☼
☼&☼#☼ ☼ ☼ ☼ ☼#☼ ☼#☼ ☼ ☼
☼     #           # & ☼
☼ ☼#☼ ☼#☼ ☼ ☼ ☼#☼ ☼ ☼ ☼
☼##  #      #  #    # ☼
☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼`;

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
                    <h2 className='title'>У чому суть гри?</h2>
                    <p>
                        Потрібно написати свого бота для героя (Мага Моллі),
                        що обіграє інших ботів за сумою балів.
                        Всі грають на одному полі. Моллі може
                        пересуватися по відкритим клітинкам в усі чотири
                        сторони.
                    </p>
                    <p>
                        Моллі може також зварити зілля. Зілля вибухне через 5
                        тіків (секунд). Вибуховою хвилею зілля можна зачепити
                        мешканців поля. Всі, хто був зачеплений - зникає.
                        Підірватися можна і на своєму, і на чужому зіллі.
                    </p>
                    <p>
                        На своєму шляху Моллі може зустріти Привида. Ця зустріч
                        є смертельною.
                    </p>
                    <p>
                        Моллі перешкождатимуть в пересуванні незруйновані стіни та
                        зачинені скриньки.
                    </p>
                    <p>
                        Кожен зруйнований об'єкт на полі (Моллі, Привид
                        та скриньки) після руйнування відновлюється в
                        іншому місці. Якщо постраждала Моллі, їй
                        зараховуються штрафні бали{ this._gets('yourHeroDeathPenalty') }.
                    </p>
                    <p>
                        Моллі, від зілля якої сталися руйнування на мапі
                        отримає бонусні бали: за зруйновану стінку{ this._gets('killWallScore') },
                        за Привида{ this._gets('killGhostScore') },
                        за іншу Моллі{ this._gets('killOtherHeroScore') }. Бали сумуються.
                    </p>

                    <div className='subTitle' id='client'>
                        Завантажте Клієнт гри для створення бота
                    </div>
                    <p>
                        { clientLink }
                        { !loggedIn && '(посилання стануть доступні після входу на сайт)' }
                    </p>
                    <p>
                        Пам'ятайте: у процесі написання бота вам необхідно
                        піклуватись про логіку переміщень вашого бота -
                        допоміжні речі вже зроблені за вас. Але ви можете
                        вдосконалювати логіку Клієнта на власний розсуд.
                    </p>
                    <p>
                        Зареєструйтеся за допомогою форми реєстрації Нового
                        Гравця. Запам'ятайте вказані дані (адресу електронної пошти
                        і пароль) - вони знадобляться вам у майбутньому для
                        авторизації на сайті.
                    </p>
                    <p>
                        Далі необхідно приєднатися з коду Клієнта до сервера.
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
                                його ви зможете отримати тут після реєстрації/логіна.
                            </p>
                        </>
                    ) : (
                        <div className={ highligte }>
                            <Link to={ book.login }>
                                Потрібно увійти в систему для отримання посилання
                            </Link>
                        </div>
                    ) }
                    <div style={{ marginLeft:'50px' }}>
                        <p>
                            <b>[Опціонально]</b> Якщо є бажання підключитись до гри, коли
                            сервер недоступний (вихідні, свята або не робочий час) -
                            можно <a className='content' style={{ display:'initial' }}
                            href='https://drive.google.com/uc?export=download&id=174aZrssLxql1_bGsKyAIENUXUv4Qjw9K'>завантажити сервер</a> і
                            запустити його командою (попередньо на ваш комп'ютер потрібно встановити java додаток).
                        </p>
                        <div className={ highligte } style={{whiteSpace:"pre"}} >
                            { "# windows\n" +
                              "java -jar -Dhost=127.0.0.1 -Dport=8080 -Dtimeout=1000\n" +
                              "          -Dlog=\"output.txt\" -DlogTime=true -DshowPlayers=\"2,3\"\n" +
                              "          -Drandom=\"random-soul-string\" -DwaitFor=2\n" +
                              "          -Dsettings=\"{'boardSize':11,'potionPower':7}\"\n" +
                              "\n" +
                              "# linux\n" +
                              "java -jar --host=127.0.0.1 --port=8080 --timeout=1000\n" +
                              "          --log=\"output.txt\" --logTime=true --showPlayers=\"2,3\"\n" +
                              "          --random=\"random-soul-string\" --waitFor=2\n" +
                              "          --settings=\"{'boardSize':11,'potionPower':7}\"" }
                        </div>
                        <p>
                            Як бачиш - є можливicть змінювати хост/порт, кількість мілісекунд на один тік (timeout),
                            налаштування<b><a href='#settings'>*</a></b> гри (у вигляді json), налаштування
                            логування (log, logTime, showPlayers), конфiгурацiю псевдо-генератора випадкових
                            чисел (random) та кiлькостi участникiв (waitFor), що мають пiдключитися за для старту сервера.
                            Після цього можно використати лінк для підключення
                            <br/>
                            <a className='content' style={{ display:'initial' }} href={ localhostConnectionUrl }>{ localhostConnectionUrl }</a>
                            <br/>
                            Також є можливість підключатись декількома клiєнтами - всi гравцi зберуться на одному полі.
                        </p>
                        <p>
                            <b style={{ color:'#ffffff' }}>Увага!</b> Локальний сервер буде вдосконалюватись - слідкуйте за поновленнями на цій сторінці.
                            Наразі наявна версія <a className='content' style={{ display:'initial' }}>'Жоржина' (v3)</a>.
                        </p>
                    </div>
                    <p>
                        Після підключення клієнт буде регулярно (кожну секунду)
                        отримувати рядок символів із закодованим станом поля.
                        Формат такий.
                        <br />
                    </p>
                    <p className={ highligte }>
                        { '^board=(.*)$' }
                    </p>
                    <p>
                        За допомогою цього regexp можна отримати рядок дошки.
                    </p>

                    <div className='subTitle' id='board'>
                        Приклад рядка від сервера
                    </div>
                    <div className={ highligte }>
                        <pre className={ boardExample }>{ BOARD_EXAMPLE }</pre>
                    </div>
                    <p>
                        Довжина рядка дорівнює площі поля. Якщо вставити символ
                        розриву рядків кожні sqrt(length(string)) символів, то
                        вийде читабельним зображення поля.
                    </p>
                    <div className={ highligte }>
                        <pre className={ boardExample }>{ BOARD_EXAMPLE_2 }</pre>
                    </div>
                    <p>
                        Перший символ рядка відповідає осередку розташованої в
                        лівому верхньому кутку і має координату [0,22]. У цьому
                        прикладі – позиція Моллі (символ '☺') - [1, 17].
                        Лівий нижній кут має координату [0, 0].
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
                        Гра покрокова, кожну секунду сервер відправляє вашому
                        клієнту (боту) стан оновленого поля на поточний момент і
                        чекає на відповідь команди героя. За наступну секунду
                        гравець повинен встигнути дати команду герою. Якщо не
                        встиг - герой стоїть на місці.
                    </p>
                    <p>
                        Команд декілька: UP, DOWN, LEFT, RIGHT - призводять до
                        руху героя в заданому напрямку на 1 клітинку; ACT -
                        залишити зiлля на місці героя. Команди руху можна
                        комбінувати з командою ACT, розділяючи їх через кому.
                        Порядок (LEFT, ACT) або (ACT, LEFT) - має значення, або
                        рухаємося вліво і там варимо зiлля, або варимо зiлля, а
                        потім біжимо вліво. Якщо гравець буде використовувати
                        тільки одну команду ACT, то зiлля буде зварено під
                        героєм без його переміщення на полі.
                    </p>
                    <p>
                        Ваше завдання - написати вебсокет клієнта, який підключиться
                        до сервера. Потім змусити героя слухатися команди. Головна
                        мета - вести осмислену гру і перемогти, набравши
                        найбільшу кількість балів в поточному Iгровому дні.
                    </p>

                    <div className='subTitle' id='match'>
                        Раунди/матчі
                    </div>
                    <ul>
                        <li>
                            Матч складається з декількох{ this._gets('roundsPerMatch') } Раундів.
                        </li>
                        <li>
                            Кожний Матч починається в новій Кімнаті після того, як в ній
                            збереться достатня кількість{ this._gets('playersPerRoom') } Учасників.
                        </li>
                        <li>
                            Кожний Матч починається в новій Кімнаті після того, як в ній
                            збереться достатня кількість{ this._gets('teamsPerRoom') } Команд.
                        </li>
                        <li>
                            Моллі очікуватиме початку наступного Раунду на полі в новому,
                            завчасно відомому для всіх Участників, місці у неактивному стані.
                        </li>
                        <li>
                            Кожний Матч починається зі зворотного відліку
                            в певну кількість{ this._gets('timeBeforeStartRound') } тіків (секунд),
                            після чого всі герої стають активними - починають виконувати команди Участників.
                        </li>
                        <li>
                            Кожний Раунд Матчу проходить з певним складом Участників.
                        </li>
                        <li>
                            Учасник, який закінчив останній Раунд Матчу, одразу попадає
                            до нової кімнати, де після того, як збереться достатня кількість
                            Участників почнеться новий Матч у новому складі Участників.
                        </li>
                        <li>
                            Раунд триває певну{ this._gets('timePerRound') } кількість
                            тіків (секунд) і закінчується перемогою Моллі:
                            <ul>
                                <li>
                                    Якщо до кінця Раунду залишились живими більше ніж 1 Моллі -
                                    перемагає той, який зробив найбільше руйнувань (при цьому
                                    підраховується загальна кількість балів отриманих від початку раунду);
                                </li>
                                <li>
                                    Якщо ж на полі живим лишився тільки один Моллі -
                                    вона і перемагає.
                                </li>
                            </ul>
                        </li>
                        <li>
                            Переможець Раунду отримує бонусні бали{ this._gets('winRoundScore') } додатково до тих балів,
                            які отримані під час Раунду за руйнування об'єктів на полі
                            (Молліи, Привиди та Скриньки).
                            <ul>
                                <li>
                                    Але не раніше, ніж мине певна
                                    кількість{ this._gets('minTicksForWin') } тіків (секунд) Раунду.
                                </li>
                            </ul>
                        </li>
                        <li>
                            Моллі, яка примусово покинула Раунд / Матч отримує
                            штрафні бали{ this._gets('yourHeroDeathPenalty') }.
                        </li>
                    </ul>

                    <div className='subTitle' id='exceptions'>
                        Особливі випадки
                    </div>
                    <ul>
                        <li>
                            Поле має форму квадрату
                            з певною довжиною{ this._gets('boardSize') }.
                        </li>
                        <li>
                            Зiлля:
                            <ul>
                                <li>
                                    В Моллі по замовчуванню є
                                    певна кількість{ this._gets('potionsCount') } зiлль.
                                </li>
                                <li>
                                    Зілля залишене на полі підірветься через 5 тіків (секунд).
                                </li>
                                <li>
                                    Зілля розривається в усі боки на певну
                                    кількість клітинок{ this._gets('potionPower') } допоки
                                    не зустріне перешкоду - будь-який елемент.
                                </li>
                                <li>
                                    Моллі, яка підірвалася на своєму або чужому зiллi гине і
                                    отримує штрафні бали{ this._gets('yourHeroDeathPenalty') }.
                                </li>
                                <li>
                                    Моллі, зілля якої підірвала іншого Моллі отрумує
                                    бонусні бали{ this._gets('killOtherHeroScore') }.
                                </li>
                            </ul>
                        </li>
                        <li>
                            Привиди:
                            <ul>
                                <li>
                                    На полі звичайно знаходиться певна кількість{ this._gets('ghostsCount') } Привидів.
                                </li>
                                <li>
                                    Моллі, зілля якої підірвало Привида отрумує
                                    бонусні бали{ this._gets('killGhostScore') }.
                                </li>
                                <li>
                                    Щойно підірваний Привид одразу ж з'являється на полі в новому місці.
                                </li>
                                <li>
                                    Моллі, яка зустрілася із Привидом гине і отримує
                                    штрафні бали{ this._gets('yourHeroDeathPenalty') }.
                                </li>
                            </ul>
                        </li>
                        <li>
                            Скриньки:
                            <ul>
                                <li>
                                    На полі звичайно знаходиться певна
                                    кількість{ this._gets('treasureBoxesCount') } скриньок.
                                </li>
                                <li>
                                    Моллі, зілля якого відкрила скриньку
                                    отрумує бонусні бали{ this._gets('killWallScore') }.
                                </li>
                                <li>
                                    Щойно підірвана стіна одразу ж з'являється на полі в новому місці.
                                </li>
                                <li>
                                    Під час руйнування стінки може з'явитись Перк.
                                </li>
                                <li>
                                    Перк, піднятий Моллі модифікує поведінку деяких ігрових
                                    аспектів (див. розділ 'Модифікатори (Перки)').
                                </li>
                            </ul>
                        </li>
                        <li>
                            На полі також присутні стіни, що не руйнуються -
                            немає ніякої можливості їх зруйнувати.
                        </li>
                    </ul>

                    <div className='subTitle' id='perks'>
                        Модифікатори (Перки)
                    </div>
                    <ul>
                        <li>
                            Перки випадають на місці знищенної стіни з
                            певною{ this._gets('perksDropRatio') } верогідністю у %.
                        </li>
                        <li>
                            Дія перку зникає через деякий час:
                            <ul>
                                <li>
                                    POTION_<wbr/>BLAST_<wbr/>RADIUS_<wbr/>INCREASE певна кількість{ this._gets('perksPotionBlastRadiusIncreaseEffectTimeout') } тіків (секунд).
                                    Але якщо було взято декілька перків підряд - загальний час роботи сумується.
                                </li>
                                <li>
                                    POTION_COUNT_INCREASE певна кількість{ this._gets('perksPotionCountEffectTimeout') } тіків (секунд).
                                </li>
                                <li>
                                    POTION_IMMUNE певна кількість{ this._gets('perksPotionImmuneEffectTimeout') } тіків (секунд).
                                </li>
                                <li>
                                    POTION_REMOTE_CONTROL не закінчується по таймауту.
                                </li>
                            </ul>
                        </li>
                        <li>
                            Якщо перк ніхто не підібрав, він зникає з поля
                            після через деякий{ this._gets('perksPickTimeout') } час.
                        </li>
                        <li>
                            Якщо ж участник підібрав перк, він отримує бонусні
                            бали{ this._gets('catchPerkScore') }.
                        </li>
                        <li>
                            Перк POTION_<wbr/>BLAST_<wbr/>RADIUS_<wbr/>INCREASE:
                            <ul>
                                <li>
                                    Збільшує радіус вибуху зілля на певну
                                    кількість{ this._gets('perksPotionBlastRadiusIncrease') } клітинок
                                    у всі сторони.
                                </li>
                                <li>
                                    Діє лише для нового зілля.
                                </li>
                                <li>
                                    Дія перку зникає через
                                    деякий{ this._gets('perksPotionBlastRadiusIncreaseEffectTimeout') } час.
                                </li>
                                <li>
                                    При отриманні декількох перків цього типу одночасно, радіус
                                    вибуху зілля збільшується пропорційно до кількості отриманних перків,
                                    а загальний час їх дії сумується.
                                </li>
                            </ul>
                        </li>
                        <li>
                            Перк POTION_COUNT_INCREASE:
                            <ul>
                                <li>
                                    Тимчасово збільшує кількість доступного герою зілля на певну
                                    кількість{ this._gets('perksPotionCountIncrease') } додатково
                                    до наявного за замовчуванням зілля{ this._gets('potionsCount') }.
                                </li>
                                <li>
                                    Моллі може готувати не більше ніж одне зілля за тік (секунду).
                                </li>
                                <li>
                                    Дія перку зникає через
                                    деякий{ this._gets('perksPotionCountEffectTimeout') } час.
                                </li>
                                <li>
                                    Перки не сумуються. При отриманні декількох перків цього типу одночасно, кількість
                                    зілля повертається до значення{ this._gets('perksPotionCountIncrease') }&nbsp
                                    додатково до наявних за замовчуванням{ this._gets('potionsCount') }. Таймер
                                    при цьому встановлюється в початкове положення{ this._gets('perksPotionCountEffectTimeout') }.
                                </li>
                            </ul>
                        </li>
                        <li>
                            Перк POTION_IMMUNE:
                            <ul>
                                <li>
                                    Дає імунітет до вибухів зілля (навіть чужих).
                                </li>
                                <li>
                                    Дія перку зникає через
                                    деякий{ this._gets('perksPotionImmuneEffectTimeout') } час.
                                </li>
                                <li>
                                    Перки не сумуються. При отриманні декількох перків цього типу одночасно,
                                    таймер дії встановлюється в початкове положення.
                                </li>
                            </ul>
                        </li>
                        <li>
                            Перк POTION_REMOTE_CONTROL:
                            <ul>
                                <li>
                                    Дає можливість дистанційного керування вибухом зілля за допомогую детонатора.
                                </li>
                                <li>
                                    Зілля встановлюється на полі перщою командою ACT, а вибухає
                                    при повторній дії ACT команди. При цьому використовується як зілля
                                    так детонатор (окремо).
                                </li>
                                <li>
                                    В наявності є певна
                                    кількість{ this._gets('perksNumberOfPotionRemoteControl') } детонаторів.
                                </li>
                                <li>
                                    Дія перку не закінчується по таймауту.
                                </li>
                                <li>
                                    Перки не сумуються. При отриманні декількох перків цього типу одночасно,
                                    загальна кількість детонаторів поновлюється до зазначенного вище.
                                </li>
                            </ul>
                        </li>
                        <li>
                            Перки не впливають одне на одного, тільки доповнюють.
                        </li>
                        <li>
                            Кожен перк має свої власні: таймер та/або лічильник
                            кількостей (залежить від типу).
                        </li>
                    </ul>

                    <div className='subTitle' id='starting'>
                        Підказки
                    </div>
                    <p>
                        Якщо Ви не знаєте, що написати, спробуйте реалізувати
                        наступні варіанти алгоритмів:
                    </p>
                    <ul>
                        <li>
                            Переміщення у випадкову сторону, якщо відповідна
                            клітина вільна.
                        </li>
                        <li>
                            Рух на вільну клітину в бік найближчої стінки,
                            що можно зруйнувати.
                        </li>
                        <li>
                            Приготувати зілля рядом із скринькою, що можно відчинити вибухом.
                        </li>
                        <li>
                            Ухилення від зілля, якщо підраховано що її взривна
                            хвиля може зачепити ващу Моллі.
                        </li>
                        <li>
                            Ухилення від Привидів, що зустрілися на шляху.
                        </li>
                        <li>
                            Намагання підірвати Привида або іншого Моллі зіллям.
                        </li>
                        <li>
                            Збір Перків і реалізація хитрішої стратегії, що безумовно
                            призведе до перемоги.
                        </li>
                    </ul>

                    <div className='subTitle' id='winners'>
                        Як визначатимуться переможці?
                    </div>
                    <p>
                        Детальніше про це можно прочитати
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
                        кількості Раундів в Матчі; сили ефекту, таймаутів, вірогідності
                        випадання Перків та інших змінних треба уточнити у організаторів
                        на початку Ігрового Дня в Slack чаті або тут, на цій сторінці, після авторизації,
                        або&nbsp;
                        <a href={ settingsLink } rel='noopener noreferrer' target='_blank'>
                            за посиланням<img src={ Icon } alt='Долучитися до чату'/>
                        </a>.
                    </p>
                    <p>
                        Будьте уважні - ці значення відрізнятимуться для різних
                        Ігрових днів Конкурсу.
                    </p>
                    <p>
                        Для спілкування між Участниками та Організатором
                        створено Канал у додатку Slack, приєднатися до якого
                        можна
                        &nbsp;
                        <a href={ joinSlackUrl } rel='noopener noreferrer' target='_blank'>
                            за посиланням<img src={ Icon } alt='Долучитися до чату'/>
                        </a>
                    </p>
                    <p>
                        Із детальним описом Правил і Положень гри можна
                        ознайомитися
                        &nbsp;
                        <a href={ privacyRulesUrl }>
                            за посиланням<img src={ Icon } alt='Правила Конкурсу'/>
                        </a>
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
