// vendor
import React, { Component } from 'react';

// proj
import { Layout } from 'layouts';
import { BoardContainer } from 'containers';

export default class BoardPage extends Component {
    render() {
        return (
            <Layout>
                <BoardContainer />
            </Layout>
        );
    }
}
