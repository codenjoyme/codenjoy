// vendor
import React, { PureComponent } from 'react';
import { NavLink } from 'react-router-dom';
import avaDefault from './icon_ava_default.svg';
import classnames from 'classnames';

import logo_icancode from './icancode/game-logo.png';
import logo_bomberman from './bomberman/game-logo.png';

// proj
import { book } from '../../routes';

// own
import Styles from './styles.module.css';

const logo = function(){
    switch (process.env.REACT_APP_GAME) {
        case 'icancode' : return logo_icancode;
        case 'bomberman' : return logo_bomberman;
    }
}();


class HeaderComponent extends PureComponent {
    render() {
        const { email, logout } = this.props;

        return (
            <header>
                <div className={ Styles.container }>
                    <NavLink className={ Styles.logoContainer } to={ book.home }>
                        <img className={ Styles.logo } src={ logo } alt='' />
                    </NavLink>

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
