// vendor
import React, { Component } from 'react';
import { connect } from 'react-redux';
import DocumentTitle from 'react-document-title';

// proj
import { logout } from 'redux/auth';

import { Header } from 'layouts';

class LayoutComponent extends Component {
    render() {
        const { children, logout, email, server } = this.props;

        return (
            <DocumentTitle title='Codenjoy event'>
                <div>
                    <Header email={ email } server={ server } logout={ logout } />
                    { children }
                </div>
            </DocumentTitle>
        );
    }
}

const mapStateToProps = state => ({
    email:  state.auth.email,
    server: state.auth.server,
    code:   state.auth.code,
});

const mapDispatchToProps = { logout };

export const ConnectedLayout = connect(
    mapStateToProps,
    mapDispatchToProps,
)(LayoutComponent);
