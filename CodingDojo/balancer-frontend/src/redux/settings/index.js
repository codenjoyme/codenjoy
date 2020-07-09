// vendor
import { all, call, put, take } from 'redux-saga/effects';

// proj
import {
    fetchAPI,
} from '../../utils';

/**
 * Constants
 **/
export const moduleName = 'settings';
const prefix = `codenjoy/${moduleName}`;

export const REQUEST_SETTINGS_START = `${prefix}/START`;
export const REQUEST_SETTINGS_SUCCESS = `${prefix}/SUCCESS`;
export const REQUEST_SETTINGS_FAIL = `${prefix}/FAIL`;


/**
 * Reducer
 **/
const ReducerState = {
    settings:  null,
    isLoading: false,
    error:     null,
};

export default function reducer(state = ReducerState, action) {
    const { type, payload } = action;

    switch (type) {
        case REQUEST_SETTINGS_START:
            return { ...state, isLoading: true, error: null };

        case REQUEST_SETTINGS_SUCCESS:
            return { ...state, isLoading: false, settings: payload };

        case REQUEST_SETTINGS_FAIL:
            return { ...state, isLoading: false, error: payload };
        default:
            return state;
    }
}

/**
 * Action Creators
 **/

export const requestSettingsStart = () => ({
    type: REQUEST_SETTINGS_START,
});

export const requestSettingsSuccess = (payload) => ({
    type: REQUEST_SETTINGS_SUCCESS,
    payload,
});

export const requestSettingsFail = payload => ({
    type: REQUEST_SETTINGS_FAIL,
    payload,
});

/**
 * Saga
 **/

export function* getSettingsSaga() {
    while (true) {
        yield take(REQUEST_SETTINGS_START);

        try {
            const response = yield call(
                fetchAPI,
                'GET',
                'rest/game/settings/get',
                null,
            );
            yield put(requestSettingsSuccess(response));

        } catch (err) {
            const failParams =
                err.status === 401 ? { credentials: true } : { system: true };

            yield put(requestSettingsFail(failParams));
        }
    }
}

export function* saga() {
    yield all([ call(getSettingsSaga) ]);
}
