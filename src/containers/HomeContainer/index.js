// vendor
import React from 'react';
import { Link } from 'react-router-dom';

// proj
import { book } from '../../routes';
import PS4 from '../../styles/images/prizes/PS4.png';
import PSClassic from '../../styles/images/prizes/PSclassic.png';
import PSDualshock from '../../styles/images/prizes/PSdualshock.png';

// own
import Styles from './styles.module.css';

const HomeContainer = () => (
    <div className={ Styles.container }>
        <div className={ Styles.homeTitle }>
            <div className={ Styles.mainText }>Створи розумного бота</div>
            <div className={ Styles.subText }>на Java або JavaScript</div>
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
            <button className={ Styles.acceptButton }>Виклик прийнято</button>
        </div>
        <div className={ Styles.content }>
            <div className={ Styles.title }>Як взяти участь</div>
            <div className={ Styles.guideContainer }>
                <div className={ Styles.guideStep }>
                    <div className={ Styles.stepHeader } />
                    <div className={ Styles.stepTitle }>СТВОРИ</div>
                    <div className={ Styles.stepDescription }>
                        Створи свого Бота на Java або JavaScript
                    </div>
                </div>
                <div className={ Styles.guideStep }>
                    <div className={ Styles.stepHeader } />
                    <div className={ Styles.stepTitle }>ЗМАГАЙСЯ</div>
                    <div className={ Styles.stepDescription }>
                        Змагайся з іншими учасниками
                    </div>
                </div>
                <div className={ Styles.guideStep }>
                    <div className={ Styles.stepHeader } />
                    <div className={ Styles.stepTitle }>ВИГРАВАЙ</div>
                    <div className={ Styles.stepDescription }>
                        <div>Вигравай!</div>
                        <div>
                            Посідай одне з трьох призових місць і отримай:
                        </div>
                        <br />
                        <div>1 місце - PlayStation 4 Slim 1tb</div>
                        <div>2 місце - PlayStation Classic</div>
                        <div>3 місце - Джойстик DualShock</div>
                    </div>
                </div>
            </div>
            <div className={ Styles.title }>Правила гри</div>
            <p>
                Завантаж проект гри, створи логіку для переміщення свого Бота,
                отримуй найбільшу кілкість балів, щоб увійти до числа Фіналістів
                з 21 січня до 6 лютого включно (кожного дня визначаються 10
                Фіналістів). Бери участь у Фіналі 7 лютого, займай одне з трьох
                призових місць і отримай драйвовий подарунок!
            </p>
            <p>
                Детальні правила і проект гри для створення Бота знайдеш{ ' ' }
                <Link to={ book.rules }>тут</Link>
            </p>
        </div>
    </div>
);

export default HomeContainer;
