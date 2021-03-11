// vendor
import React, { Component } from 'react';
import DocumentTitle from 'react-document-title';

// proj
import { Layout } from '../../layouts';
import { BoardContainer } from '../../containers';
const eventName = process.env.REACT_APP_EVENT_NAME;

export default class BoardPage extends Component {
    render() {
        return (
            <DocumentTitle title={ eventName }>
                <Layout>
                    <BoardContainer />
                </Layout>
            </DocumentTitle>
        );
    }
}
