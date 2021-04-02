// vendor
import React, { Component } from 'react';
import DocumentTitle from 'react-document-title';

// proj
import { ForgotPasswordContainer } from '../../containers';
import { Layout } from '../../layouts';
const eventName = process.env.REACT_APP_EVENT_NAME;

export default class ForgotPasswordPage extends Component {
    render() {
        return (
            <DocumentTitle title={ eventName }>
                <Layout>
                    <ForgotPasswordContainer />
                </Layout>
            </DocumentTitle>
        );
    }
}
