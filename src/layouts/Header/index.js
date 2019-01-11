// vendor
import React, { Component } from 'react';
import Styles from './styles.module.css';

// proj

export class Header extends Component {
    render() {
        return (
            <ul className={ Styles.navigation }>
                <li>
                    <a href='/'>Home</a>
                </li>
                <li>
                    <a href='/'>About</a>
                </li>
                <li>
                    <a href='/'>Products</a>
                </li>
                <li>
                    <a href='/'>Contact</a>
                </li>
            </ul>
        );
    }
}
