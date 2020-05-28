// vendor
import React from 'react';
import { Link } from 'react-router-dom';

// proj
import { book } from '../../routes';
import PS4 from '../../styles/images/prizes/PS4.png';
import PSClassic from '../../styles/images/prizes/PSclassic.png';
import PSDualshock from '../../styles/images/prizes/PSdualshock.png';
import Rules from '../../styles/images/icons/rules.svg';
import StepWinHeader from '../../styles/images/layout/presents.png';
import stepBattleHeader from '../../styles/images/layout/battle.png';
import stepCreateHeader from '../../styles/images/layout/create.png';

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
                <img className={ Styles.prizeImage } src={ PS4 } alt='PS4' />
                <img
                    className={ Styles.prizeImage }
                    src={ PSDualshock }
                    alt='PSDualshock'
                />
                <img
                    className={ Styles.prizeImage }
                    src={ PSClassic }
                    alt='PSClassic'
                />
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
                            Завантаж проект гри
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
                            <li>місце - ігрова консоль PlayStation 4 Pro 1TB</li>
                            <li>місце - ігрова консоль Sega Mega Drive Mini</li>
                            <li>місце - настільна Hobby World Fallout</li>
                        </ol>
                    </div>
                </div>
            </div>
            <div className='title'>Правила гри</div>
            <p>
                Завантажуйте проект гри. Створіть логіку для переміщення свого Бота.
                Отримуйте найбільшу кількість балів, щоб увійти до числа
                Фіналістів.
            </p>
            <p>
                Конкурс триватиме з { dayTimeStart } години { startDate }&nbsp;
                до { dayTimeEnd } години { registerEndDate } (за виключенням вихідних).
                Кожного дня визначаються { finalistsCount } Фіналістів.
            </p>
            <p>
                Беріть участь у Фіналі { endDate }. Займайте одне з трьох призових
                місць і отримайте драйвовий подарунок!
            </p>
            <p>
                Детальні правила і проект гри для створення Бота шукайте
                &nbsp;
                <Link to={ book.rules }>
                    за посиланням <img src={ Icon } alt='Правила Конкурсу' />
                </Link>
            </p>
        </div>
    </div>
);

export default HomeContainer;
