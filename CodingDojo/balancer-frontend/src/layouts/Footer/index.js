// vendor
import React, { PureComponent } from 'react';
import { NavLink } from 'react-router-dom';
import classnames from 'classnames';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { FacebookShareButton } from 'react-share';

// proj
import { book } from '../../routes';
import Styles from './styles.module.css';
import JStifyLogo from '../../styles/images/logos/JStifyLogoCropped.png';
import DojorenaLogo from '../../styles/images/logos/DojorenaLogo.png';

const orgEmail = process.env.REACT_APP_EVENT_ORG_EMAIL;
const joinSlackUrl = process.env.REACT_APP_JOIN_CHAT_LINK;
const joinTelegramUrl = process.env.REACT_APP_JOIN_TELEGRAM_LINK;

export class Footer extends PureComponent {
    render() {
        return (
            <div className={ Styles.footer }>
                <ul className={ Styles.navigation }>
                    <li>
                        <NavLink to={ book.privacyRules }>
                            Правила і положення
                        </NavLink>
                    </li>
                    <li>
                        <NavLink to={ book.privacyPolicy }>
                            Політика конфіденційності
                        </NavLink>
                    </li>
                </ul>
                <ul className={ Styles.navigation }>
                    <li>
                        <a href='https://www.youtube.com/channel/UCY82a2l4__P1F2QvLPbwnmA' target="_blank">
                            <img src={ JStifyLogo } alt='JStify' height={ 50 }/>
                        </a>
                    </li>
                    <li>
                        <a href='https://dojorena.io/' target="_blank">
                            <img src={ DojorenaLogo } alt='Dojorena' width={ 40 }/>
                        </a>
                    </li>
                </ul>
                <ul className={ classnames(Styles.navigation) }>
                    <li>
                        <a href={ 'mailto:' + orgEmail }>
                            <span>Зв'язатися з нами</span>
                            <FontAwesomeIcon
                                className={ Styles.navigationIcon }
                                icon={ [ 'far', 'envelope' ] }
                                style={ { color: '#fff' } }
                            />
                        </a>
                    </li>
                    <li>
                        <div className={ Styles.share }>
                            <FacebookShareButton
                                className={ Styles.facebookShare }
                                url={ process.env.REACT_APP_EVENT_LINK }
                            >
                                <span>Поділитися</span>
                                <FontAwesomeIcon
                                    className={ Styles.navigationIcon }
                                    icon={ [ 'fas', 'share-alt' ] }
                                    style={ { color: '#fff' } }
                                />
                            </FacebookShareButton>
                        </div>
                    </li>
                    <li>
                        <a href={ joinSlackUrl } rel='noopener noreferrer' target='_blank'>
                            <span>Долучайтесь до чату в Slack</span>
                            <FontAwesomeIcon
                                className={ Styles.navigationIcon }
                                icon={ [ 'fab', 'slack' ] }
                                style={ { color: '#ffffff' } }
                            />
                        </a>
                    </li>
                    <li>
                        <a href={ joinTelegramUrl } rel='noopener noreferrer' target='_blank'>
                            <span>або в Telegram</span>
                            <FontAwesomeIcon
                                className={ Styles.navigationIcon }
                                icon={ [ 'fab', 'telegram-plane' ] }
                                style={ { color: '#fff'  } }
                            />
                        </a>
                    </li>
                </ul>
            </div>
        );
    }
}
