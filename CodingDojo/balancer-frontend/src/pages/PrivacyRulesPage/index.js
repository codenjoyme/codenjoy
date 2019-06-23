// vendor
import React from 'react';
import DocumentTitle from 'react-document-title';

// proj
import { Layout } from '../../layouts';
import { PrivacyRulesContainer } from '../../containers';

const PrivacyPolicyPage = () => (
    <DocumentTitle title='Bot Challenge'>
        <Layout>
            <PrivacyRulesContainer />
        </Layout>
    </DocumentTitle>
);

export default PrivacyPolicyPage;
