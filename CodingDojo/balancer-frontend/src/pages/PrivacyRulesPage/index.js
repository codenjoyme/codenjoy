// vendor
import React from 'react';
import DocumentTitle from 'react-document-title';

// proj
import { Layout } from '../../layouts';
import { PrivacyRulesContainer } from '../../containers';
const eventName = process.env.REACT_APP_EVENT_NAME;

const PrivacyPolicyPage = () => (
    <DocumentTitle title={ eventName }>
        <Layout>
            <PrivacyRulesContainer />
        </Layout>
    </DocumentTitle>
);

export default PrivacyPolicyPage;
