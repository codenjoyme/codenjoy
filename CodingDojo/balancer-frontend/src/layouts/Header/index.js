// vendor
import React, { PureComponent } from 'react';
import { NavLink } from 'react-router-dom';
import avaDefault from './icon_ava_default.svg';
import classnames from 'classnames';

// proj
import { book } from '../../routes';
import Game from '../../games';
import EpamLogo from '../../styles/images/logos/EPAM_LOGO_White.png';
import EpamAnywhereLogo from '../../styles/images/logos/Epam_Anywhere_Logo.svg';

// own
import Styles from './styles.module.css';

class HeaderComponent extends PureComponent {
    render() {
        const { email, logout } = this.props;

        return (
            <header>
                <div className={ Styles.container }>
                    <ul>
                        <NavLink className={ Styles.logoContainer } to={ book.home }>
                            <img className={ Styles.logo } src={ Game.logo } alt='' />
                        </NavLink>

                        <a className={ classnames(
                            Styles.logoContainer,
                            Styles.epam
                        ) } href='https://epa.ms/1fYT56' target='_blank'>
                            <img src={ EpamLogo } alt='Epam' />
                        </a>
                        <a className={ classnames(
                            Styles.logoContainer,
                            Styles.epam,
                            Styles.anywhere
                        ) } href='https://anywhere.epam.com/' target='_blank'>
                            <img src={ EpamAnywhereLogo } alt='Epam Anywhere' />
                        </a>
                    </ul>
                    <ul>
                        <li>
                            <NavLink className={ Styles.navMenu } activeClassName={ Styles.activeMenu } to={ book.home }>
                                Головна
                            </NavLink>
                        </li>
                        <li>
                            <NavLink className={ Styles.navMenu } activeClassName={ Styles.activeMenu } to={ book.board }>
                                Трансляція
                            </NavLink>
                        </li>
                        <li>
                            <NavLink className={ Styles.navMenu } activeClassName={ Styles.activeMenu } to={ book.rules }>
                                Правила гри
                            </NavLink>
                        </li>

                        { email && (
                            <li>
                                <div className={ classnames(Styles.navMenu, Styles.logout) } onClick={ () => logout() }>
                                    <img className={ Styles.avatar } src={ avaDefault } alt='Вийти' />
                                    Вийти
                                </div>
                            </li>
                        ) }
                        { !email && (
                            <li>
                                <NavLink
                                    className={ Styles.navMenu }
                                    activeClassName={ Styles.activeMenu }
                                    to={ book.register }
                                >
                                    Реєстрація
                                </NavLink>
                            </li>
                        ) }

                        { !email && (
                            <li>
                                <NavLink className={ Styles.navMenu } activeClassName={ Styles.activeMenu } to={ book.login }>
                                    <img className={ Styles.avatar } src={ avaDefault } alt='' />
                                    Авторизація
                                </NavLink>
                            </li>
                        ) }
                    </ul>
                </div>
            </header>
        );
    }
}

export const Header = HeaderComponent;
