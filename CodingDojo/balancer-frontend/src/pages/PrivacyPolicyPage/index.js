// vendor
import React from 'react';
import DocumentTitle from 'react-document-title';

// proj
import { Layout } from '../../layouts';
import { PrivacyPolicyContainer } from '../../containers';

const PrivacyPolicyPage = () => (
    <DocumentTitle title='Bot Challenge'>
        <Layout>
            <PrivacyPolicyContainer />
        </Layout>
    </DocumentTitle>
)

export default PrivacyPolicyPage;
