// vendor
import React, { Component } from 'react';
import { Link } from 'react-router-dom';

// proj
import { book } from 'routes';
import Styles from './styles.module.css';

export class Header extends Component {
    render() {
        const { server, logout } = this.props;

        return (
            <>
                <ul className={ Styles.navigation }>
                    <li>
                        <Link to={ book.board }>Home</Link>
                    </li>
                    <li>
                        <Link to={ book.board }>About</Link>
                    </li>
                    <li>
                        <Link to={ book.board }>Products</Link>
                    </li>
                    <li>
                        <Link to={ book.board }>Contact</Link>
                    </li>
                    <li>
                        { server ? <Link to={ book.board }>Сервер: { server }</Link> : <Link to={ book.login }>Логін</Link> }
                    </li>
                    { server && (
                        <li>
                            <div className={ Styles.action } onClick={ () => logout() }>
                                Logout
                            </div>
                        </li>
                    ) }
                </ul>
            </>
        );
    }
}
