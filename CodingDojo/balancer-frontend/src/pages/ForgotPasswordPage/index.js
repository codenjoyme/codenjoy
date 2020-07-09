// vendor
import React, { Component } from 'react';
import DocumentTitle from 'react-document-title';

// proj
import { ForgotPasswordContainer } from '../../containers';
import { Layout } from '../../layouts';

export default class ForgotPasswordPage extends Component {
    render() {
        return (
            <DocumentTitle title='Bot Challenge'>
                <Layout>
                    <ForgotPasswordContainer />
                </Layout>
            </DocumentTitle>
        );
    }
}
