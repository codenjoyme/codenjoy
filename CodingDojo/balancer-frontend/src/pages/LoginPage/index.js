// vendor
import React, { Component } from 'react';
import DocumentTitle from 'react-document-title';

// proj
import { LoginContainer } from '../../containers';
import { Layout } from '../../layouts';
const eventName = process.env.REACT_APP_EVENT_NAME;

export default class LoginPage extends Component {
    render() {
        return (
            <DocumentTitle title={ eventName }>
                <Layout>
                    <LoginContainer />
                </Layout>
            </DocumentTitle>
        );
    }
}
