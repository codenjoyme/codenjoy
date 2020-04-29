// vendor
import React, { Component } from 'react';

// proj
import { RegisterConfirmForm } from '../../forms';

// own
import Styles from './styles.module.css';

export default class RegisterConfirmContainer extends Component {
    render() {
        return (
            <div className={ Styles.rcContainer }>
                <RegisterConfirmForm />
            </div>
        );
    }
}

