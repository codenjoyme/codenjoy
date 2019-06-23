// vendor
import { all, takeEvery } from 'redux-saga/effects';
import ReactGA from 'react-ga';

// proj
import { REGISTER_SUCCESS } from './../register';

/**
 * Saga
 **/

function registerSuccessSaga() {
    ReactGA.event({
        category: 'User',
        action:   'Created an Account',
    });
}

export function* saga() {
    yield all([ takeEvery(REGISTER_SUCCESS, registerSuccessSaga) ]);
}
