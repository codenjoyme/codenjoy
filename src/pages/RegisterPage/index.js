// vendor
import React, { Component } from 'react';

// proj
import { Layout } from '../../layouts';
import { RegisterContainer } from '../../containers';

export default class RegisterPage extends Component {
    render() {
        return (
            <Layout>
                <RegisterContainer />
            </Layout>
        );
    }
}
