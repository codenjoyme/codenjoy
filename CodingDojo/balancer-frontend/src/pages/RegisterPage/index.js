// vendor
import React, { Component } from 'react';
import DocumentTitle from 'react-document-title';

// proj
import { Layout } from '../../layouts';
import { RegisterContainer } from '../../containers';
const eventName = process.env.REACT_APP_EVENT_NAME;

export default class RegisterPage extends Component {
    render() {
        return (
            <DocumentTitle title={ eventName }>
                <Layout>
                    <RegisterContainer />
                </Layout>
            </DocumentTitle>
        );
    }
}
