// vendor
import React from 'react';
import DocumentTitle from 'react-document-title';

// proj
import { Layout } from '../../layouts';
import { PrivacyPolicyContainer } from '../../containers';
const eventName = process.env.REACT_APP_EVENT_NAME;

const PrivacyPolicyPage = () => (
    <DocumentTitle title={ eventName }>
        <Layout>
            <PrivacyPolicyContainer />
        </Layout>
    </DocumentTitle>
)

export default PrivacyPolicyPage;
