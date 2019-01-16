// vendor
import React, { Component } from 'react';
import DocumentTitle from 'react-document-title';

// proj
import { LoginContainer } from '../../containers';
import { Layout } from '../../layouts';

export default class LoginPage extends Component {
    render() {
        return (
            <DocumentTitle title='EPAM Bot Challenge :: Login'>
                <Layout>
                    <LoginContainer />
                </Layout>
            </DocumentTitle>
        );
    }
}
