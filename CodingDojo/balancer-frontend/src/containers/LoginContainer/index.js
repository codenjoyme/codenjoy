// vendor
import React, { Component } from 'react';
import { connect } from 'react-redux';

// proj
import { LoginForm } from '../../forms';

// own
import Styles from './styles.module.css';

class AuthContainer extends Component {
    render() {
        return (
            <div className={ Styles.loginContainer }>
                <LoginForm />
            </div>
        );
    }
}

const mapStateToProps = () => ({});

export default connect(
    mapStateToProps,
    {},
)(AuthContainer);
