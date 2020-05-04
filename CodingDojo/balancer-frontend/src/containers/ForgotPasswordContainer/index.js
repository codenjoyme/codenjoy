// vendor
import React, { Component } from 'react';

// proj
import { ForgotPasswordForm } from '../../forms';

// own
import Styles from './styles.module.css';

export default class ForgotPasswordContainer extends Component {
    render() {
        return (
            <div className={ Styles.fpContainer }>
                <ForgotPasswordForm />
            </div>
        );
    }
}

