// vendor
import React from 'react';
import { Link } from 'react-router-dom';

// proj
import { book } from '../../routes';
import First from '../../styles/images/prizes/first.png';
import Second from '../../styles/images/prizes/second.png';
import Third from '../../styles/images/prizes/third.png';
import Icon from '../../styles/images/icons/rules.svg';
import StepWinHeader from '../../styles/images/layout/presents.jpg';
import stepBattleHeader from '../../styles/images/layout/battle.jpg';
import stepCreateHeader from '../../styles/images/layout/create.jpg';

// own
import Styles from './styles.module.css';

const startDate = process.env.REACT_APP_EVENT_START_DATE;
const endDate = process.env.REACT_APP_EVENT_END_DATE;
const dayTimeStart = process.env.REACT_APP_EVENT_START_TIME;
const dayTimeEnd = process.env.REACT_APP_EVENT_FINAL_TIME;
const finalistsCount = process.env.REACT_APP_EVENT_FINALISTS_COUNT;
const registerEndDate = process.env.REACT_APP_EVENT_REGISTER_END_DATE;

const HomeContainer = () => (
    <div className='container'>
        <div className={ Styles.homeTitle }>
            <div className={ Styles.mainText }>Створи розумного бота</div>
            <div className={ Styles.subText }>Змагайся з іншими учасниками</div>
            <div className={ Styles.subText }>Вигравай один із трьох призів</div>
            <div className={ Styles.prizeList }>
                <img className={ Styles.prizeImage } src={ First } alt='PS5'
                     title='Iгрова консоль PlayStation 5 Pro 1TB'/>
                <img className={ Styles.prizeImage } src={ Second } alt='NintendoSwitchLite'
                     title='Ігрова консоль Nintendo Switch Lite (Yellow)'/>
                <img className={ Styles.prizeImage } src={ Third } alt='HobbyWorldFallout'
                     title='Настільна гра Hobby World Fallout'/>
            </div>
            <Link to={ book.rules } className={ Styles.acceptButton }>
                Виклик прийнято
            </Link>
        </div>
        <div className='content'>
            <div className='title'>Як взяти участь</div>
            <div className={ Styles.guideContainer }>
                <div className={ Styles.guideStep }>
                    <div className={ Styles.steps }>
                        <img src={ stepCreateHeader } alt='Створи' />
                    </div>
                    <div className={ Styles.stepTitle }>СТВОРИ</div>
                    <div className={ Styles.stepDescription }>
                        <div>
                            Завантаж проєкт гри
                        </div>
                        <br />
                        <div>
                            Cтвори логіку для переміщення свого Бота
                        </div>
                    </div>
                </div>
                <div className={ Styles.guideStep }>
                    <div className={ Styles.steps }>
                        <img src={ stepBattleHeader } alt='Змагайся' />
                    </div>
                    <div className={ Styles.stepTitle }>ЗМАГАЙСЯ</div>
                    <div className={ Styles.stepDescription }>
                        <div>
                            Змагайся з іншими учасниками
                        </div>
                        <br />
                        <div>
                            Вдосконалюй свого Бота кожен день
                        </div>
                        <br />
                        <div>
                            Більше модифікацій - більше шансів!
                        </div>
                    </div>
                </div>
                <div className={ Styles.guideStep }>
                    <div className={ Styles.steps }>
                        <img src={ StepWinHeader } alt='Вигравай' />
                    </div>
                    <div className={ Styles.stepTitle }>ВИГРАВАЙ</div>
                    <div className={ Styles.stepDescription }>
                        <div>
                            Посідай одне з трьох призових місць і отримай:
                        </div>
                        <br/>
                        <ol>
                            <li>Ігрова консоль PlayStation 5 Pro 1TB</li>
                            <li>Ігрова консоль Nintendo Switch Lite (Yellow)</li>
                            <li>Настільна гра Hobby World Fallout</li>
                        </ol>
                    </div>
                </div>
            </div>
            <div className='title'>Правила гри</div>
            <p>
                Завантажуйте проєкт гри. Створіть логіку для переміщення свого Бота.
                Отримуйте найбільшу кількість балів, щоб увійти до числа
                Фіналістів.
            </p>
            <p>
                Конкурс триватиме кожного дня з { startDate } до { registerEndDate }&nbsp;
                за виключенням вихідних, починаться з { dayTimeStart } години
                i закінчується { dayTimeEnd } годиною.
                Кожного дня визначаються { finalistsCount } Фіналістів.
            </p>
            <p>
                Беріть участь у Фіналі { endDate }. Займайте одне з трьох призових
                місць і отримайте драйвовий подарунок!
            </p>
            <p>
                Детальні правила і проєкт гри для створення Бота шукайте
                &nbsp;
                <Link to={ book.rules }>
                    за посиланням <img src={ Icon } alt='Правила Конкурсу' />
                </Link>
            </p>
        </div>
    </div>
);

export default HomeContainer;
