// vendor
import { all } from 'redux-saga/effects';
import { LOCATION_CHANGE } from 'connected-react-router';
import ReactGA from 'react-ga';

// proj

/**
 * Constants
 **/

export const moduleName = 'analytics';

/**
 * Reducer
 **/
const ReducerState = {};

const trackPage = page => {
    ReactGA.set({ page });
    ReactGA.pageview(page);
};

export default function reducer(state = ReducerState, action) {
    const { type, payload } = action;

    switch (type) {
        case LOCATION_CHANGE:
            trackPage(payload.location.pathname);

            return state;

        default:
            return state;
    }
}

/**
 * Action Creators
 **/

/**
 * Saga
 **/

export function* saga() {
    yield all([]);
}
