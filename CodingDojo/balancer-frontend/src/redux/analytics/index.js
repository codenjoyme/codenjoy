// vendor
import { all, takeEvery } from 'redux-saga/effects';
import ReactGA from 'react-ga';
import ReactPixel from 'react-facebook-pixel';

// proj
import { REGISTER_SUCCESS } from './../register';

/**
 * Saga
 **/

function registerSuccessSaga() {
  if (process.env.NODE_ENV !== 'development') {
    ReactGA.event({
      category: 'User',
      action: 'Created an Account',
    });
    ReactPixel.track('Contact');
  }
}

export function* saga() {
    yield all([ takeEvery(REGISTER_SUCCESS, registerSuccessSaga) ]);
}
