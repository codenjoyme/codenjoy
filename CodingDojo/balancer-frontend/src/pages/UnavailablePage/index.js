// vendor
import React, { Component } from 'react';
import DocumentTitle from 'react-document-title';

// proj
import { Layout } from '../../layouts';
import { UnavailableContainer } from '../../containers';

export default class HomePage extends Component {
    render() {
        return (
            <DocumentTitle title='Bot Challenge'>
                <Layout>
                    <UnavailableContainer />
                </Layout>
            </DocumentTitle>
        );
    }
}
