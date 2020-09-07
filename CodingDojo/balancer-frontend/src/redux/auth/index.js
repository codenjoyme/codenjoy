// vendor
import { all, call, put, take } from 'redux-saga/effects';
import md5 from 'md5';

// proj
import { book } from '../../routes';
import { history } from '../../store';
import {
    fetchAPI,
    setCode,
    setEmail,
    removeCode,
    removeEmail,
    setServer,
    removeServer,
    setId,
    removeId,
} from '../../utils';
import { getErrorObject } from 'helpers';

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
    isLoading:   false,
};

export default function reducer(state = ReducerState, action) {
    const { type, payload } = action;

    switch (type) {
        case AUTHENTICATE:
            return { ...state, ...payload };

        case LOGIN:
            return { ...state, isLoading: true };

        case LOGIN_SUCCESS:
            return { ...state, isLoading: false, loginErrors: void 0 };

        case LOGIN_FAIL:
            return { ...state, isLoading: false, loginErrors: payload };

        case LOGOUT_SUCCESS:
            return ReducerState;

        default:
            return state;
    }
}
/**
 * Selectors
 **/

export const selectUser = state => ({
    email:  state.auth.email,
    server: state.auth.server,
    code:   state.auth.code,
});

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

        try {
            const response = yield call(
                fetchAPI,
                'POST',
                'rest/login',
                null,
                credentials,
            );


            if (!response.code || !response.email || !response.server) {
                yield put(loginFail(yield getErrorObject(response)));
            } else {
                yield put(authenticate(response));
                yield put(loginSuccess());
                yield call(history.replace, book.board);
            }
        } catch (err) {
            yield put(loginFail(yield getErrorObject(err)));
        }
    }
}

export function* authenticateSaga() {
    while (true) {
        const { payload: user } = yield take(AUTHENTICATE);

        yield setCode(user.code);
        yield setEmail(user.email);
        yield setServer(user.server);
        yield setId(user.id);

        yield put(authenticateSuccess());
    }
}

export function* logoutSaga() {
    while (true) {
        yield take(LOGOUT);

        yield removeCode();
        yield removeEmail();
        yield removeServer();
        yield removeId();

        yield put(logoutSuccess());
        yield call(history.replace, book.login);
    }
}

export function* saga() {
    yield all([ call(loginFormSaga), call(authenticateSaga), call(logoutSaga) ]);
}
