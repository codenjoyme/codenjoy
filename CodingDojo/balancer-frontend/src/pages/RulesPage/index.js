// vendor
import React, { Component } from 'react';
import DocumentTitle from 'react-document-title';

// proj
import { Layout } from '../../layouts';
import { RulesContainer } from '../../containers';
const eventName = process.env.REACT_APP_EVENT_NAME;

export default class RulesPage extends Component {
    render() {
        return (
            <DocumentTitle title={ eventName }>
                <Layout>
                    <RulesContainer />
                </Layout>
            </DocumentTitle>
        );
    }
}
