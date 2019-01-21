// vendor
import React, { Component } from 'react';
import { ConnectedRouter } from 'connected-react-router';
import { Provider } from 'react-redux';
import 'reset-css';
import { Scrollbars } from 'react-custom-scrollbars';
import { PersistGate } from 'redux-persist/integration/react';
import { library } from '@fortawesome/fontawesome-svg-core';
import {
    faTimesCircle,
    faCheckCircle,
    faStar,
    faArrowUp,
    faArrowRight,
    faShareAlt,
    faUser,
} from '@fortawesome/free-solid-svg-icons';
import {
    faSquare as farSquare,
    faCheckSquare as farCheckSquare,
    faEnvelope as farEnvelope,
    faComments as farComments,
    faUserCircle as farUserCircle,
} from '@fortawesome/free-regular-svg-icons';

// proj
import store, { history, persistor } from './store';
import Routes from './routes/Routes';
import ScrollToTop from './ScrollToTop';

library.add(
    faTimesCircle,
    faCheckCircle,
    farCheckSquare,
    farSquare,
    faStar,
    faArrowUp,
    faArrowRight,
    farEnvelope,
    faShareAlt,
    farComments,
    faUser,
    farUserCircle,
);

class App extends Component {
    render() {
        return (
            <Scrollbars autoHide style={ { width: '100%', height: '100vh' } }>
                <Provider store={ store }>
                    <PersistGate loading={ null } persistor={ persistor }>
                        <ConnectedRouter history={ history }>
                            <ScrollToTop>
                                <Routes />
                            </ScrollToTop>
                        </ConnectedRouter>
                    </PersistGate>
                </Provider>
            </Scrollbars>
        );
    }
}

export default App;
