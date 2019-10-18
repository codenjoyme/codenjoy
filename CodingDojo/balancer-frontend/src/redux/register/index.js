// vendor
import { all, call, put, take } from 'redux-saga/effects';
import md5 from 'md5';

// proj
import { history } from '../../store';
import { book } from '../../routes';
import { fetchAPI } from '../../utils';
import { authenticate } from '../auth';

/**
 * Constants
 **/
export const moduleName = 'register';
const prefix = `codenjoy/${moduleName}`;

export const REGISTER = `${prefix}/REGISTER`;
export const REGISTER_SUCCESS = `${prefix}/REGISTER_SUCCESS`;
export const REGISTER_FAIL = `${prefix}/REGISTER_FAIL`;
export const SET_VISIBLE_PRIVACY_MODAL = `${prefix}/SET_VISIBLE_PRIVACY_MODAL`;

/**
 * Reducer
 **/
const ReducerState = {
    registerErrors:      void 0,
    isLoading:           false,
    visiblePrivacyModal: false,
};

export default function reducer(state = ReducerState, action) {
    const { type, payload } = action;

    switch (type) {
        case REGISTER:
            return { ...state, isLoading: true, registerErrors: void 0 };

        case REGISTER_SUCCESS:
            return { ...state, isLoading: false, registerErrors: void 0 };

        case REGISTER_FAIL:
            return { ...state, isLoading: false, registerErrors: payload };

        case SET_VISIBLE_PRIVACY_MODAL:
            return { ...state, visiblePrivacyModal: payload };

        default:
            return state;
    }
}

/**
 * Action Creators
 **/

export const setVisiblePrivacyModal = visible => ({
    type:    SET_VISIBLE_PRIVACY_MODAL,
    payload: visible,
});

export const register = payload => ({
    type: REGISTER,
    payload,
});

export const registerSuccess = () => ({
    type: REGISTER_SUCCESS,
});

export const registerFail = errors => ({
    type:    REGISTER_FAIL,
    payload: errors,
});

/**
 * Saga
 **/

export function* registerFormSaga() {
    while (true) {
        const {
            payload: { password, ...restPayload },
        } = yield take(REGISTER);
        const payload = { ...restPayload, password: md5(password) };

        try {
            const response = yield call(
                fetchAPI,
                'POST',
                'rest/register',
                null,
                payload,
            );
            if (!response.code) {
                yield put(registerFail({ credentials: true }));
            } else {
                yield put(authenticate(response));
                yield put(registerSuccess());
                yield call(history.replace, book.board);
            }
        } catch (err) {
            const failParams =
                err.status === 400 ? { credentials: true } : { system: true };

            yield put(registerFail(failParams));
        }
    }
}

export function* saga() {
    yield all([ call(registerFormSaga) ]);
}
