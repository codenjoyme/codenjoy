// vendor
import React, { Component } from 'react';

// proj
import { LoginContainer } from '../../containers';
import { Layout } from '../../layouts';

export default class LoginPage extends Component {
    render() {
        return (
            <Layout>
                <LoginContainer />
            </Layout>
        );
    }
}
