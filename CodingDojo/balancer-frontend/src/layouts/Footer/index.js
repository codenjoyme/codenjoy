// vendor
import React, { PureComponent } from 'react';
import { NavLink } from 'react-router-dom';
import classnames from 'classnames';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { FacebookShareButton } from 'react-share';

// proj
import { book } from '../../routes';
import Styles from './styles.module.css';

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
                <ul className={ classnames(Styles.navigation) }>
                    <li>
                        <a href={ 'mailto:' + orgEmail }>
                            Зв'язатися з нами
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
                                Поділитися
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
                            Долучайтесь до чату в Slack
                            <FontAwesomeIcon
                                className={ Styles.navigationIcon }
                                icon={ [ 'fab', 'slack' ] }
                                style={ { color: '#ffffff' } }
                            />
                        </a>
                    </li>
                    <li>
                        <a href={ joinTelegramUrl } rel='noopener noreferrer' target='_blank'>
                            або в Telegram
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
