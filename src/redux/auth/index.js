// vendor
import { all, call, put, take } from 'redux-saga/effects';
import { replace, LOCATION_CHANGE } from 'connected-react-router';
import md5 from 'md5';

// proj
import { book } from '../../routes';
import {
    fetchAPI,
    setCode,
    setEmail,
    removeCode,
    removeEmail,
    setServer,
    removeServer,
} from '../../utils';

/**
 * Constants
 **/
export const moduleName = 'auth';
const prefix = `codenjoy/${moduleName}`;

export const LOGIN = `${prefix}/LOGIN`;
export const LOGIN_SUCCESS = `${prefix}/LOGIN_SUCCESS`;
export const LOGIN_FAIL = `${prefix}/LOGIN_FAIL`;

export const AUTHENTICATE = `${prefix}/AUTHENTICATE`;
export const AUTHENTICATE_SUCCESS = `${prefix}/AUTHENTICATE_SUCCESS`;
export const AUTHENTICATE_FAIL = `${prefix}/AUTHENTICATE_FAIL`;

export const LOGOUT = `${prefix}/LOGOUT`;
export const LOGOUT_SUCCESS = `${prefix}/LOGOUT_SUCCESS`;
export const LOGOUT_FAIL = `${prefix}/LOGOUT_FAIL`;

/**
 * Reducer
 **/
const ReducerState = {
    loginErrors: void 0,
};

export default function reducer(state = ReducerState, action) {
    const { type, payload } = action;

    switch (type) {
        case AUTHENTICATE:
            return { ...state, ...payload };

        case LOGIN_SUCCESS:
            return { ...state, loginErrors: void 0 };

        case LOGIN_FAIL:
            return { ...state, loginErrors: payload };

        case LOGOUT_SUCCESS:
            return ReducerState;

        case LOCATION_CHANGE:
            return { ...state, loginErrors: void 0 };

        default:
            return state;
    }
}
/**
 * Selectors
 **/

export const selectToken = state => state.auth.token;

/**
 * Action Creators
 **/

export const login = credentials => ({
    type:    LOGIN,
    payload: credentials,
});

export const loginFail = payload => ({
    type: LOGIN_FAIL,
    payload,
});

export const loginSuccess = () => ({
    type: LOGIN_SUCCESS,
});

export const authenticate = user => ({
    type:    AUTHENTICATE,
    payload: user,
});

export const authenticateSuccess = () => ({
    type: AUTHENTICATE_SUCCESS,
});

export const logout = () => ({
    type: LOGOUT,
});

export const logoutSuccess = () => ({
    type: LOGOUT_SUCCESS,
});

export const logoutFail = error => ({
    type:    LOGOUT_FAIL,
    payload: error,
    error:   true,
});

/**
 * Saga
 **/

export function* loginFormSaga() {
    while (true) {
        const {
            payload: { email, password },
        } = yield take(LOGIN);
        const credentials = { email, password: md5(password) };
        const response = yield call(
            fetchAPI,
            'POST',
            'rest/login',
            null,
            credentials,
            { noRedirect: true },
        );
        if (response instanceof Error) {
            const failParams =
                response.status === 401
                    ? { credentials: true }
                    : { system: true };

            yield put(loginFail(failParams));
        } else {
            if (!response.code || !response.email) {
                yield put(loginFail({ credentials: true }));
            } else {
                yield put(authenticate(response));
                yield put(loginSuccess());
                yield put(replace(book.board));
            }
        }
    }
}

export function* authenticateSaga() {
    while (true) {
        const { payload: user } = yield take(AUTHENTICATE);

        yield setCode(user.code);
        yield setEmail(user.email);
        yield setServer(user.server);

        yield put(authenticateSuccess());
    }
}

export function* logoutSaga() {
    while (true) {
        yield take(LOGOUT);

        yield removeCode();
        yield removeEmail();
        yield removeServer();

        yield put(replace(`${book.login}`));
        yield put(logoutSuccess());
    }
}

export function* saga() {
    yield all([ call(loginFormSaga), call(authenticateSaga), call(logoutSaga) ]);
}
