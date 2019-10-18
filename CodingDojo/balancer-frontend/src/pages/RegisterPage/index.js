// vendor
import React, { Component } from 'react';
import DocumentTitle from 'react-document-title';

// proj
import { Layout } from '../../layouts';
import { RegisterContainer } from '../../containers';

export default class RegisterPage extends Component {
    render() {
        return (
            <DocumentTitle title='Bot Challenge'>
                <Layout>
                    <RegisterContainer />
                </Layout>
            </DocumentTitle>
        );
    }
}
