// vendor
import React, { Component } from 'react';

// proj
import { RegisterForm } from '../../forms';

// own
import Styles from './styles.module.css';

class RegisterContainer extends Component {
    render() {
        return (
            <div className={ Styles.registerContainer }>
                <RegisterForm />
            </div>
        );
    }
}

export default RegisterContainer;
