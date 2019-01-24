// vendor
import { all, takeEvery } from 'redux-saga/effects';
import { LOCATION_CHANGE } from 'connected-react-router';
import ReactGA from 'react-ga';

// proj
import { REGISTER_SUCCESS } from './../register';

const trackPage = page => {
    ReactGA.set({ page });
    ReactGA.pageview(page);
};

/**
 * Saga
 **/

function locationChangeSaga({ payload: { location } }) {
    trackPage(location.pathname);
}

function registerSuccessSaga() {
    ReactGA.event({
        category: 'User',
        action:   'Created an Account',
    });
}

export function* saga() {
    yield all([ takeEvery(LOCATION_CHANGE, locationChangeSaga), takeEvery(REGISTER_SUCCESS, registerSuccessSaga) ]);
}
