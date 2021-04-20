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
const BOARD_EXAMPLE =
`Board:
  01234567890123456789\t01234567890123456789\t   01234567890123456789
0                     \t--------------------\t   --------------------
1 ┐      ╔═════════┐  \t--------------------\t   --------------------
2 │      ║˃.....$..│  \t---------→------→---\t   --------------------
3 │      ║....Z....│ ╔\t--------------------\t   --------------------
4 │      ║.........│ ║\t-----------B-BBBB---\t   --------------------
5 │  ╔═══╝˃...$...O│ ║\t---------→------→---\t   --------------------
6 ╚══╝......O..S...│ ║\t--------♂------☺----\t   --------------------
7 .....┌─╗....$..OO│ ║\t--------------------\t   --------------------
8 ┌────┘ ║.O.......│ ║\t--------------------\t   --------------------
9 │      ║.........╚═╝\t--------------------\t   --------------------
0 ┘ ╔════╝...OO.......\t----------B---------\t   --------------------
1   ║..$..............\t--------B-B----B-B--\t   --------------------
2   ║$..┌─╗...┌─────╗.\t----------B--------B\t   --------------------
3   ║...│ ║...│     ║O\t---------BB---------\t   --------------------
4   └───┘ ║...│     ║.\t--------------------\t   --------------------
5 ┐       ║...│  ╔══╝.\t----------B---------\t   --------------------
6 │  ╔════╝...│  ║....\t----------BB--------\t   --------------------
7 │  ║........╚══╝....\t----B------------BB-\t   --------------------
8 ╚══╝O...O...$....┌──\t----------------♀---\t   --------------------
9 ...........O┌────┘  \t--------------------\t   --------------------
  LAYER1              \tLAYER2              \t   LAYER3`;

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
        const connectionUrl = loggedIn
            ? getGameConnectionString(server, code, id)
            : void 0;
        const localhostConnectionUrl = getGameConnectionString('127.0.0.1:8080', '12345678901234567890', 'anyidyouwant');
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
                    Bot Challenge - як грати?
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
                        І вже о <span className='command'>10:00</span> розпочнеться заліковий час,
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
                        Потрібно написати свого бота для героя, який здатен обіграти інших
                        ботів за сумою балів. Всі грають на одному полі. Герой може
                        пересуватися по відкритим клітинкам у чотири доступні сторони.
                    </p>
                    <p>
                        Герой може бігати, стрибати, стріляти лазером, пересувати предмети.
                        На шляху героя може зустрітися провалля, в яке герой може впасти.
                        На полі також присутні пересувні ящики. Використовуйте їх, щоб будувати для себе схованки та пастки для інших героїв.
                    </p>
                    <p>
                        На ігровому полі можна зустріти зомбі. Їх необхідно остерігатися, інакше вони уб'ють вас!
                        Також поцілити нашого героя намагаются лазерні машини. Але вони стаціонарні та дуже прогнозовані. Маємо надію, що вони не стануть для вас проблемою.
                    </p>
                    <p>
                        Герой отримує очки за:
                        <ul>
                            <li> знищення зомбі: { this._gets('killZombieScore') };</li>
                            <li> знищення іншого героя: { this._gets('killHeroScore') };</li>
                            <li> мішечок золота, доставленний до фінальної точки: { this._gets('goldScore') };</li>
                            <li> успішне проходження рівня: { this._gets('winScore') };</li>
                        </ul>
                    <p>
                        Герой втрачає очки за:
                        <ul>
                            <li>пенальті за загибель: { this._gets('losePenalty') }.</li>
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
                        Атрибут 'layers' містить масив значень різних слоїв поля.
                        Кожен з об'єктів карти представлений на своєму слої.
                        Для прикладу, гравець пересувається на другому слої,
                        в той чай як при польоті він переходить на третій слой.
                        <br />
                    </p>
                    <p className={ highligte }>
                        { '(.*),\\"layers\\":\\[(.*)\\](.*)' }
                    </p>
                    <p>
                        За допомогою цього regexp можна отримати значення поля.
                    </p>

                    <div className='subTitle' id='board'>
                        Приклад форматованого рядка від сервера
                    </div>
                    <div className={ highligte }>
                        <pre className={ boardExample }>{ BOARD_EXAMPLE }</pre>
                    </div>

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
                            <li><span className='command'>Direction[UP, DOWN, LEFT, RIGHT]</span> - призводять до руху героя в заданому напрямку на 1 клітинку.</li>
                            <li><span className='command'>ACT(1)</span> - без вказанного напрямку команда змушує героя стрибнути на місці.</li>
                            <li><span className='command'>ACT(1),Direction</span> - стрибок у заданному напрямку. Стрибок займає 2 тіка і призводить до руху героя на 2 клітини в заданному напрямку.
                            Можна стрибати через провалля, ящики, зомбі, лазери та інших героїв. </li>
                            <li><span className='command'>ACT(2),Direction</span> - змушує героя тягнути ящик у вказаному напрямку.</li>
                            <li><span className='command'>ACT(3),Direction</span> - герой стріляє лазером у вказанному напрямку. Між пострілами існує затримка в { this._gets('gunRecharge') } тіків.</li>
                        </ul>
                    <p>
                        Ваше завдання - підключитися з клієнтської частини до ігрового сервера через веб-сокет з'єдання.
                        Спостерігайте за ігровим процесом, щоб описана вами поведінка бота приносила бажані очки.
                        Гравці з найбільшою кількістю очків, набраних у ігровому дні,
                        відправляються в фінал для боротьби за головний приз. Чи готові ви боротися за перемогу?!
                    </p>

                    <p>
                        Гра має одну особливість. Перед тим як почати змагатися с іншими героями,
                        вам потрібно пройти тренувальні раунди. Для початку це будуть дуже прості рівні,
                        де ваша ціль - дійти до <span className='command'>EXIT('E')</span> по прямій.
                        Надалі рівні ставатимуть складнішими. Вам необхідно буде навчитися збирати золото, обходити провалля,
                        перестрибувати або вбивати зомбі. Все це зможе підготувати вас до фінального раунда, де
                        ви на одному ігровому полі будете змагатися с іншими героями.
                    </p>

                    <div className='subTitle' id='exceptions'>
                        Особливі випадки
                    </div>
                    <ul>
                        <li>
                            Ігрове поле може мати невідомі габарити. Ігровий клієнт отримує тільки видиму для нього частину поля.
                            Ви маєте враховувати, що на видимій частині може не бути золота, виходу та інших героїв.
                            Вам треба навчити вашого бота проводити розвідку закритих частин карти.
                        </li>
                        <li>
                            Герой не може стріляти бесперервно. С часом рушниця може перегрітися та їй потрібен час,
                            щоб охолонути.
                            <ul class="sub_ul">
                                <li>Рушниця перегрівається після кількості пострілів вряд(шт): { this._gets('gunShotQueue') }.</li>
                                <li>Час потрібній на охолодження рушниці(тік): { this._gets('gunRestTime') }.</li>
                                <li>Якщо між пострилами робити перерву більше ніж довжина перезарядки { this._gets('gunRecharge') }
                                    рушниця буде поступово охолоджуватися
                                </li>
                            </ul>
                        </li>

                    </ul>

                    <div className='subTitle' id='perks'>
                        Модифікатори (Перки)
                    </div>
                    <ul>
                        <li>
                            <span className='command'>UNSTOPPABLE_LASER</span> - дозволяє герою вистрілити лазером,
                            який не зникатиме при взаємодії з коробками, зомбі та іншими героями.<br />
                            <ul class="sub_ul">
                                <li>
                                    Дія перку зникає через { this._gets('perkAvailability') } тіків (секунд).
                                </li>
                            </ul>
                        </li>
                        <li>
                            <span className='command'>DEATH_RAY</span> - дозволяє герою вистрілити променем смерті  в заданному напрямку.
                            Промінь розповсюджується одразу на певну кількість клітинок та зникає на наступний тік.
                            <ul class="sub_ul">
                                <li>
                                    Дія перку зникає через { this._gets('perkAvailability') } тіків (секунд).
                                </li>
                                <li>
                                    Довжина променя { this._gets('deathRayRange') } клітинок.
                                </li>
                            </ul>
                        </li>
                        <li>
                            <span className='command'>UNLIMITED_FIRE_PERK</span> - дозволяє герою виконувати постріл на кожний новий тік.
                            <br /> Під цим перком рушніця не перегрівається.
                            <ul class="sub_ul">
                                <li>
                                    Дія перку зникає через { this._gets('perkAvailability') } тіків (секунд).
                                </li>
                            </ul>
                        </li>

                        <li>
                            Перки з'являтимуться на місці вбитого зомбі з
                            певною ймовірністю { this._gets('perkDropRatio') }%.
                        </li>
                        <li>
                            Якщо перк ніхто не підібрав, він зникає з поля
                            після { this._gets('perkActivity') } тіків(секунд).
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
                            <li>Розмір кімнати: { this._gets('roomSize') }.</li>
                            <li>Винагорода за перемогу в раунді: { this._gets('winScore') }.</li>
                            <li>Винагорода за доставку 1 мішка золота до виходу: { this._gets('goldScore') }.</li>
                            <li>Винагорода за вбивство іншого героя: { this._gets('killHeroScore') }.</li>
                            <li>Винагорода за знищення зомбі: { this._gets('killZombieScore') }.</li>
                            <li>Очки за поразку: { this._gets('losePenalty') }.</li>
                            <li>Частота випадання перків : { this._gets('perkDropRatio') }.</li>
                            <li>Термін доступності перків на карті: { this._gets('perkAvailability') }.</li>
                            <li>Термін дії перків: { this._gets('perkActivity') }.</li>
                            <li>Область дії луча смерті: { this._gets('deathRayRange') }.</li>
                            <li>Затримка між пострілами(перезарядка)(тік): { this._gets('gunRecharge') }.</li>
                            <li>Рушниця перегрівається після певної кількості пострілів поспіль(шт): { this._gets('gunShotQueue') }.</li>
                            <li>Час потрібній на охолодження рушниці(тік): { this._gets('gunRestTime') }.</li>
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
