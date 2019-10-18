// vendor
import React, { Component } from 'react';

// own
import Public from './Public';
import Unavailable from './Unavailable';

export default class Routes extends Component {
    render() {
        const isUnavailable = process.env.REACT_APP_IS_UNAVAILABLE === 'true';

        return isUnavailable ? <Unavailable /> : <Public />;
    }
}
