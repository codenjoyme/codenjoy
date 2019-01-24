// vendor
import { all, takeEvery } from 'redux-saga/effects';
import { LOCATION_CHANGE } from 'connected-react-router';
import ReactGA from 'react-ga';
import ReactPixel from 'react-facebook-pixel';

// proj
import { REGISTER_SUCCESS } from './../register';

const REPLACE = 'REPLACE';
const trackPage = page => {
    ReactGA.set({ page });
    ReactGA.pageview(page);
};

/**
 * Saga
 **/

function gaLocationChangeSaga({ payload: { location, action } }) {
    if (action !== REPLACE) {
        trackPage(location.pathname);
    }
}

function pixelLocationChangeSaga({ payload: { action } }) {
    if (action !== REPLACE && process.env.NODE_ENV !== 'development') {
        ReactPixel.pageView();
    }
}

function registerSuccessSaga() {
    ReactGA.event({
        category: 'User',
        action:   'Created an Account',
    });
}

export function* saga() {
    yield all([ takeEvery(LOCATION_CHANGE, gaLocationChangeSaga), takeEvery(LOCATION_CHANGE, pixelLocationChangeSaga), takeEvery(REGISTER_SUCCESS, registerSuccessSaga) ]);
}
