// vendor
import React, { Component } from 'react';
import DocumentTitle from 'react-document-title';

// proj
import { Layout } from '../../layouts';
import { UnavailableContainer } from '../../containers';
const eventName = process.env.REACT_APP_EVENT_NAME;

export default class HomePage extends Component {
    render() {
        return (
            <DocumentTitle title={ eventName }>
                <Layout>
                    <UnavailableContainer />
                </Layout>
            </DocumentTitle>
        );
    }
}
