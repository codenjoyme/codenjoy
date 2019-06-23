// vendor
import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Scrollbars } from 'react-custom-scrollbars';

class CustomScrollbars extends Component {
    _getScrollbarsRef(scrollbars) {
        this.scrollbars = scrollbars;
    }

    componentDidUpdate(prevProps) {
        if (this.props.location.pathname !== prevProps.location.pathname) {
            this.scrollbars.scrollToTop();
        }
    }

    render() {
        return (
            <Scrollbars
                ref={ this._getScrollbarsRef.bind(this) }
                autoHide
                style={ { width: '100%', height: '100vh' } }
            >
                { this.props.children }
            </Scrollbars>
        );
    }
}

export default withRouter(CustomScrollbars);
