// vendor
import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { CopyToClipboard } from 'react-copy-to-clipboard';
import classNames from 'classnames/bind';

// proj
import { book } from '../../routes';
import { getGameConnectionString } from '../../utils';

// own
import Styles from './styles.module.css';
const cx = classNames.bind(Styles);

class HeaderComponent extends Component {
    _getMenuItemStyles(route) {
        return cx({
            active: this.props.location.pathname === route,
        });
    }

    render() {
        const { server, logout, email, code } = this.props;

        return (
            <header>
                <div className={ Styles.container }>
                    <div className={ Styles.logoContainer }>
                        EPAM BOT CHALLENGE
                    </div>

                    { server && (
                        <div className={ Styles.serverInfo }>
                            <div className={ Styles.serverName }>
                                Сервер: { server }
                            </div>
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
                        </div>
                    ) }

                    <ul>
                        { /* <li className={ Styles.navItem }>
                            <NavLink to={ book.board }>Головна</NavLink>
                        </li> */ }
                        <li className={ this._getMenuItemStyles(book.board) }>
                            <Link to={ book.board }>Трансляція</Link>
                        </li>

                        { !server && (
                            <li className={ this._getMenuItemStyles(book.login) }>
                                <Link to={ book.login }>Увійти</Link>
                            </li>
                        ) }

                        { server && (
                            <li>
                                <div onClick={ () => logout() }>Вийти</div>
                            </li>
                        ) }
                        { !server && (
                            <li
                                className={ this._getMenuItemStyles(
                                    book.register,
                                ) }
                            >
                                <Link to={ book.register }>Реєстрація</Link>
                            </li>
                        ) }
                    </ul>
                </div>
            </header>
        );
    }
}

export const Header = withRouter(HeaderComponent);
