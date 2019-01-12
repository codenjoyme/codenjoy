// vendor
import React, { Component } from 'react';
import { connect } from 'react-redux';

// proj
import { LoginForm } from 'forms';

class AuthContainer extends Component {
    render() {
        return <LoginForm />;
    }
}

const mapStateToProps = () => ({});

export default connect(
    mapStateToProps,
    {},
)(AuthContainer);
