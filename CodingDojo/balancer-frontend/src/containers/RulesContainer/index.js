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
import BoardSample from '../../styles/images/game/icancode/field_sample.png';


// own
import Styles from './styles.module.css';

const BOARD_EXAMPLE =
` ##############
 #S...O.....˅.#
 #˃.....$O....#
 #....####....#
 #....#  #....#
 #.O###  ###.O#
 #.$#      #..#
 #..#      #$.#
 #O.###  ###O.#
 #....#  #....#
 #....####....#
 #....O$.....˂#
 #.˄.....O...E#
 ##############`;

//const BOARD_EXAMPLE_2 =
//`☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼
//☼     &        #  #   ☼
//☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼
//☼    # # #      ####  ☼
//☼ ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼
//☼☺            #      #☼
//☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼#☼ ☼ ☼#☼
//☼# ### + ҉        &  #☼
//☼#☼ ☼ ☼ ☼҉☼♥☼ ☼ ☼ ☼ ☼ ☼
//☼     ♣H҉҉H     #  #  ☼
//☼#☼ ☼#☼ ☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼
//☼ # #    ҉#    # ♥    ☼
//☼ ☼ ☼ ☼#☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼
//☼                     ☼
//☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼♣☼ ☼
//☼        &            ☼
//☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼
//☼  # #  #           # ☼
//☼&☼#☼ ☼ ☼ ☼ ☼#☼ ☼#☼ ☼ ☼
//☼     #           # & ☼
//☼ ☼#☼ ☼#☼ ☼ ☼ ☼#☼ ☼ ☼ ☼
//☼##  #      #  #    # ☼
//☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼`;

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
                <div className={ mask }>Bot Challenge - як грати?</div>
                <div className='content'>
                    <h2 className='title'>У чому суть гри?</h2>
                     <div className='subTitle' id='commands'>
                                            Параметри гри:
                     </div>

                    <p>
                        Потрібно написати свого Бота для героя, який обіграє інших
                        Ботів за сумою балів. Всі грають на одному полі. Герой може
                        пересуватися по відкритим клітинкам в усі чотири
                        сторони.
                    </p>
                    <p>
                        Герой може бігати, стрибати, стріляти лазером.
                        На шляху Героя може зустрітися провалля, в яке Герой може впасти.
                        На полі також присутні пересувні елементи: ящики. Їх можна рухати, будувати для себе схованки та пастки для інших Героїв.
                    </p>
                    <p>
                        На ігровому полі можно зустріти Зомбі що знищує всіх на своєму шляху.
                        Також поцілити нашого Героя намагаются Лазерні машини. Але вони стаціонарні та дуже прогнозовані. Маємо надію що вони не стануть для Вас проблемою.
                    </p>
                    <p>
                        Герой отримує очки за:
                        <ul>
                            <li> знищення зомбі: { this._gets('killZombieScore') };</li>
                            <li>за знищення іншого героя: { this._gets('killHeroScore') };</li>
                            <li>за кожний мішечок золота, доставленний до фінальної точки: { this._gets('goldScore') };</li>
                            <li>за проходження рівня: { this._gets('winScore') };</li>
                        </ul>
                    <p>
                        Герой втрачає очки за:
                        <ul>
                            <li>пенальті за загибель: { this._gets('loosePenalty') }.</li>
                        </ul>
                    </p>

                    <div className='subTitle' id='client'>
                        Завантажте Клієнт гри для створення Бота можна по цьому посиланню:  { clientLink } { !loggedIn && '(посилання стануть доступні після входу на сайт)' }
                    </div>

                    <p>
                        Пам'ятайте: у процесі написання Бота вам необхідно
                        піклуватись про логіку переміщень вашого Бота -
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

                     <div className='subTitle' id='elements'>
                        Розшифрування символів
                    </div>
                    <p className="game-field-img-container">
                        <img className="responsive-img" src={ BoardSample } alt='Ігрове поле'/>
                    </p>
                    <GameElements
                        settings={ settings }
                    />

                    <div className='subTitle' id='commands'>
                        Керування ботом
                    </div>
                    <p>
                        Гра покрокова, кожну секунду сервер відправляє вашому
                        клієнту (Боту) стан оновленого поля на поточний момент і
                        чекає на відповідь команди героя. За наступну секунду
                        гравець повинен встигнути дати команду герою. Якщо не
                        встиг - герой стоїть на місці.
                    </p>
                    <p>
                        Команд декілька:
                    </p>
                        <ul>
                            <li><span className='command'>Direction[UP, DOWN, LEFT, RIGHT]</span> - призводять до руху героя в заданому напрямку на 1 клітинку;</li>
                            <li><span className='command'>ACT(1)</span> - без вказанного напрямку команда змушує героя стрибати на місці;</li>
                            <li><span className='command'>ACT(1),Direction</span> - стрибок у заданному напрямку. Стрибок займає 2 тіка і призводить до руху героя на 2 клітини в заданному напрямку.
                            Можна стрибати через дірки, ящики, зомбі, лазери, інших героїв; </li>
                            <li><span className='command'>ACT(2),Direction</span> - змушує героя тягнути ящик у вказаному напрямку.</li>
                            <li><span className='command'>ACT(3),Direction</span> - герой стріляє лазером у вказанному напрямку. Між пострілами існує затримка в { this._gets('gunRecharge') } тіків;</li>
                        </ul>
                    <p>
                        Ваше завдання - написати вебсокет клієнта, який підключиться
                        до сервера. Потім змусити героя слухатися команди. Головна
                        мета - вести осмислену гру і перемогти, набравши
                        найбільшу кількість балів в поточному Iгровому дні.
                    </p>

                    <p>
                        Гра маэ одну особливість. Перед тим як почати змагатися с іншими героями,
                        вам потрібно пройти тренувальні раунди. Для початку це будуть дуже прості рівні,
                        де завданням буде мета дойти до  <span className='command'>EXIT('E')</span> по прямій.
                        Надалі рівні стануть складніщі. Вам необхідно буде навчитися збирати золото, обходити дірки,
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
                    </ul>

                    <div className='subTitle' id='perks'>
                        Модифікатори (Перки)
                    </div>
                    <ul>
                        <li>
                            <span className='command'>UNSTOPPABLE_LASER</span> - Дає можливість герою запустити лазер,
                            який не зникатимє при взаємодії з коробками, зомбі та іншими героями.<br />
                            <ul class="sub_ul">
                                <li>
                                    Дія перку зникає через { this._gets('perkAvailability') } тіків (секунд).
                                </li>
                            </ul>
                        </li>
                        <li>
                            <span className='command'>DEATH_RAY</span> - дозволяє герою вистрілити промінем смерті  в заданному напрямку.
                            Промінь розповсюджується одразу на певну кількість клітинок и зникає через 1 тік.
                            <ul class="sub_ul">
                                <li>
                                    Дія перку зникає через { this._gets('perkAvailability') } тіків (секунд).
                                </li>
                                <li>
                                    Довжина проміня { this._gets('deathRayRange') } клітинок.
                                </li>
                            </ul>
                        </li>

                        <li>
                            Перки випадають на місці вбитого зомбі з
                            певною верогідністю { this._gets('perkDropRatio') }%.
                        </li>
                        <li>
                            Якщо перк ніхто не підібрав, він зникає з поля
                            після через деякий час { this._gets('perkActivity') } тіків(секунд).
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
                        на початку Ігрового Дня в Slack чаті або тут, на цій сторінці, після авторизації:
                    </p>

                    <div className={ highligte } style={{whiteSpace:"pre"}} >
                    <p id="settings">
                        Параметри гри:
                    </p>
                        <ul>
                            <li>Розмір кімнати: { this._gets('roomSize') }.</li>
                            <li>Винагорода за перемогу в раунді: { this._gets('winScore') }.</li>
                            <li>Винагорода за доставку 1 мішка золота до виходу: { this._gets('goldScore') }.</li>
                            <li>Винагорода за перемогу над іншим героєм: { this._gets('killHeroScore') }.</li>
                            <li>Винагорода за знищення зомбі: { this._gets('killZombieScore') }.</li>
                            <li>Очки за поразку: { this._gets('loosePenalty') }.</li>
                            <li>Частота випадіння перків : { this._gets('perkDropRatio') }.</li>
                            <li>Скільки часу перк не зникатимє з карті: { this._gets('perkAvailability') }.</li>
                            <li>Термін дії перку: { this._gets('perkActivity') }.</li>
                            <li>Область дії луча смерті: { this._gets('deathRayRange') }.</li>
                            <li>Затримка між пострілами: { this._gets('gunRecharge') }.</li>
                        </ul>
                    </div>
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
