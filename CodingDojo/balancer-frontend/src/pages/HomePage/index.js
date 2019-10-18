// vendor
import React, { Component } from 'react';
import DocumentTitle from 'react-document-title';

// proj
import { Layout } from '../../layouts';
import { HomeContainer } from '../../containers';

export default class HomePage extends Component {
    render() {
        return (
            <DocumentTitle title='Bot Challenge'>
                <Layout>
                    <HomeContainer />
                </Layout>
            </DocumentTitle>
        );
    }
}
