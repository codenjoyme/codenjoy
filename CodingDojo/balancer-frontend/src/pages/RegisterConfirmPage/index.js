// vendor
import React, { Component } from 'react';
import DocumentTitle from 'react-document-title';

// proj
import { RegisterConfirmContainer } from '../../containers';
import { Layout } from '../../layouts';
const eventName = process.env.REACT_APP_EVENT_NAME;

export default class RegisterConfirmPage extends Component {
    render() {
        return (
            <DocumentTitle title={ eventName }>
                <Layout>
                    <RegisterConfirmContainer />
                </Layout>
            </DocumentTitle>
        );
    }
}
