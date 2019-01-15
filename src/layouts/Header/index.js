// vendor
import React, { Component } from 'react';
import { NavLink, Link } from 'react-router-dom';
import { CopyToClipboard } from 'react-copy-to-clipboard';

// proj
import { book } from '../../routes';
import { getGameConnectionString } from '../../utils';

// own
import Styles from './styles.module.css';

export class Header extends Component {
    render() {
        const { server, logout, email, code } = this.props;

        return (
            <div style={ Styles.header }>
                <ul className={ Styles.navigation }>
                    <li className={ Styles.link }>
                        <NavLink
                            to={ book.board }
                            activeClassName={ Styles.activeNavLink }
                        >
                            Home
                        </NavLink>
                    </li>
                    <li className={ Styles.link }>
                        <NavLink
                            to={ book.board }
                            activeClassName={ Styles.activeNavLink }
                        >
                            About
                        </NavLink>
                    </li>
                    <li className={ Styles.link }>
                        <NavLink
                            to={ book.board }
                            activeClassName={ Styles.activeNavLink }
                        >
                            Contact
                        </NavLink>
                    </li>
                    <li className={ Styles.link }>
                        { server ? (
                            <Link to={ book.board }>Сервер: { server }</Link>
                        ) : (
                            <Link to={ book.login }>Увійти</Link>
                        ) }
                        { server && (
                            <CopyToClipboard
                                text={ getGameConnectionString(
                                    server,
                                    code,
                                    email,
                                ) }
                            >
                                <button className={ Styles.copyButton }>
                                    Copy
                                </button>
                            </CopyToClipboard>
                        ) }
                    </li>
                    { server && (
                        <li className={ Styles.link }>
                            <div
                                className={ Styles.action }
                                onClick={ () => logout() }
                            >
                                Вийти
                            </div>
                        </li>
                    ) }
                    { !server && (
                        <li className={ Styles.link }>
                            <Link to={ book.register }>Реєстрація</Link>
                        </li>
                    ) }
                </ul>
            </div>
        );
    }
}
