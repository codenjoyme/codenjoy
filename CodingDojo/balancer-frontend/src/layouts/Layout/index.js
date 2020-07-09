// vendor
import React, { Component } from 'react';
import { connect } from 'react-redux';

// proj
import { logout } from '../../redux/auth';

import { Header, Footer } from '../../layouts';
import { ToastContainer } from 'react-toastify';

// own
import 'react-toastify/dist/ReactToastify.css';
import Styles from './styles.module.css';

class LayoutComponent extends Component {
    render() {
        const { children, logout, email, server, code } = this.props;

        return (
            <div className={ Styles.layoutWrapper }>
                <div className={ Styles.layout }>
                    <Header
                        code={ code }
                        email={ email }
                        server={ server }
                        logout={ logout }
                    />
                    <ToastContainer />
                    <div className={ Styles.content }>{ children }</div>
                    <Footer />
                </div>
            </div>
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
