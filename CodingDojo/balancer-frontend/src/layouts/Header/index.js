// vendor
import React, { PureComponent } from 'react';
import { NavLink } from 'react-router-dom';
import avaDefault from './icon_ava_default.svg';
import classnames from 'classnames';

// proj
import { book } from '../../routes';
import Game from '../../games';
import Company1Logo from '../../styles/images/logos/Company1Logo.png';
import Company2Logo from '../../styles/images/logos/Company2Logo.png';

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
                            Styles.logo,
                            Styles.logo1
                        ) } href='https://company1.com' target='_blank'>
                            <img src={ Company1Logo } alt='Company1' />
                        </a>
                        <a className={ classnames(
                            Styles.logoContainer,
                            Styles.logo,
                            Styles.logo2
                        ) } href='https://company2.com/' target='_blank'>
                            <img src={ Company2Logo } alt='Company2' />
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
