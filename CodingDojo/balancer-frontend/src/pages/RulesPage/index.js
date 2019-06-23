// vendor
import React, { Component } from 'react';
import DocumentTitle from 'react-document-title';

// proj
import { Layout } from '../../layouts';
import { RulesContainer } from '../../containers';

export default class RulesPage extends Component {
    render() {
        return (
            <DocumentTitle title='Bot Challenge'>
                <Layout>
                    <RulesContainer />
                </Layout>
            </DocumentTitle>
        );
    }
}
