// vendor
import React, { Component } from 'react';

// proj
import { LoginContainer } from '../../containers';

// own
import Styles from './styles.module.css';

export default class LoginPage extends Component {
    render() {
        return (
            <div className={ Styles.page }>
                <LoginContainer />
            </div>
        );
    }
}
