// vendor
import React, { Component } from 'react';
import { connect } from 'react-redux';
import DocumentTitle from 'react-document-title';

// proj
import { logout } from 'redux/auth';

import { Header } from 'layouts';

class LayoutComponent extends Component {
    render() {
        const { children, logout } = this.props;

        return (
            <DocumentTitle title='Codenjoy event'>
                <div>
                    <Header logout={ logout } />
                    { children }
                </div>
            </DocumentTitle>
        );
    }
}

// const mapStateToProps = state => ({});

const mapDispatchToProps = { logout };

export const ConnectedLayout = connect(
    null,
    mapDispatchToProps,
)(LayoutComponent);
