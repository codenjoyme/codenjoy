// vendor
import React, { Component } from 'react';
import { NavLink, Link } from 'react-router-dom';
import { CopyToClipboard } from 'react-copy-to-clipboard';

// proj
import { book } from '../../routes';
import Styles from './styles.module.css';

export class Header extends Component {
    render() {
        const { server, logout } = this.props;

        return (
            <>
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
                            Products
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
                            <Link to={ book.board }>Server: { server }</Link>
                        ) : (
                            <Link to={ book.login }>Login</Link>
                        ) }
                        <CopyToClipboard text={ server }>
                            <button className={ Styles.copyButton }>Copy</button>
                        </CopyToClipboard>
                    </li>
                    { server && (
                        <li className={ Styles.link }>
                            <div
                                className={ Styles.action }
                                onClick={ () => logout() }
                            >
                                Logout
                            </div>
                        </li>
                    ) }
                </ul>
            </>
        );
    }
}
