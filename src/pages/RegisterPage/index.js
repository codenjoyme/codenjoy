// vendor
import React, { Component } from 'react';

// proj
import { RegisterContainer } from '../../containers';

// own
import Styles from './styles.module.css';

export default class RegisterPage extends Component {
    render() {
        return (
            <div className={ Styles.page }>
                <RegisterContainer />
            </div>
        );
    }
}
