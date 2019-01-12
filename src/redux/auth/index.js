// vendor
import { all, call, put, take } from 'redux-saga/effects';
import { replace } from 'connected-react-router';

// proj
import { book } from '../../routes';
import { fetchAPI, setCode, setEmail, removeCode, removeEmail, setServer, removeServer } from '../../utils';

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
const ReducerState = {};

export default function reducer(state = ReducerState, action) {
    const { type, payload } = action;

    switch (type) {
        case AUTHENTICATE:
            return { ...state, ...payload };

        case LOGOUT_SUCCESS:
            return ReducerState;

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

// TODO add try-catch
export function* loginFormSaga() {
    while (true) {
        const { payload: credentials } = yield take(LOGIN);
        // const user = yield call(
        //     fetchAPI,
        //     'POST',
        //     'login',
        //     null,
        //     credentials,
        //     // false,
        // );

        const user = {
            server: 'server.domain.ua',
            code:   '10073530731990011788',
            email:  'test@mail.com',
        };

        yield put(authenticate(user));
        yield put(loginSuccess());
        yield put(replace(book.board));
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
