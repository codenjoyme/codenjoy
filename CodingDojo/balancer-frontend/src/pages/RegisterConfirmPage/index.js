// vendor
import React, { Component } from 'react';
import DocumentTitle from 'react-document-title';

// proj
import { RegisterConfirmContainer } from '../../containers';
import { Layout } from '../../layouts';

export default class RegisterConfirmPage extends Component {
    render() {
        return (
            <DocumentTitle title='Bot Challenge'>
                <Layout>
                    <RegisterConfirmContainer />
                </Layout>
            </DocumentTitle>
        );
    }
}
