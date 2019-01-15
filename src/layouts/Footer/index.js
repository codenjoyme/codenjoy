// vendor
import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';

// proj
import { book } from '../../routes';
import Styles from './styles.module.css';

export class Footer extends Component {
    render() {
        return (
            <div className={ Styles.footer }>
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
                </ul>
            </div>
        );
    }
}
