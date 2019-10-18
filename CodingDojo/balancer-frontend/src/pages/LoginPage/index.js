// vendor
import React, { Component } from 'react';
import DocumentTitle from 'react-document-title';

// proj
import { LoginContainer } from '../../containers';
import { Layout } from '../../layouts';

export default class LoginPage extends Component {
    render() {
        return (
            <DocumentTitle title='Bot Challenge'>
                <Layout>
                    <LoginContainer />
                </Layout>
            </DocumentTitle>
        );
    }
}
