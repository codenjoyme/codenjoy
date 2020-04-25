// vendor
import { all, call, put, takeLatest, select } from 'redux-saga/effects';
import md5 from 'md5';
import { toast } from 'react-toastify';
// proj
import { history } from '../../store';
import { book } from '../../routes';
import { fetchAPI } from '../../utils';
import { authenticate } from '../auth';
import {watchReset, watchResetValidate} from './reset-password';
/**
 * Constants
 **/
export const moduleName = 'register';
const prefix = `codenjoy/${moduleName}`;

export const REGISTER = `${prefix}/REGISTER`;
export const REGISTER_SUCCESS = `${prefix}/REGISTER_SUCCESS`;
export const REGISTER_FAIL = `${prefix}/REGISTER_FAIL`;
export const SET_VISIBLE_PRIVACY_MODAL = `${prefix}/SET_VISIBLE_PRIVACY_MODAL`;

export const REGISTER_CONFIRM_START = `${prefix}/REGISTER_CONFIRM_START`;
export const REGISTER_CONFIRM_SUCCESS = `${prefix}/REGISTER_CONFIRM_SUCCESS`;
export const REGISTER_CONFIRM_FAIL = `${prefix}/REGISTER_CONFIRM_FAIL`;

export const REGISTER_RESEND_START = `${prefix}/REGISTER_RESEND_START`;
export const REGISTER_RESEND_SUCCESS = `${prefix}/REGISTER_RESEND_SUCCESS`;
export const REGISTER_RESEND_FAIL = `${prefix}/REGISTER_RESEND_FAIL`;

export const RESET_START = `${prefix}/RESET_START`;
export const RESET_SUCCESS = `${prefix}/RESET_SUCCESS`;
export const RESET_FAIL = `${prefix}/RESET_FAIL`;

export const RESET_VALIDATE_START = `${prefix}/RESET_VALIDATE_START`;
export const RESET_VALIDATE_SUCCESS = `${prefix}/RESET_VALIDATE_SUCCESS`;
export const RESET_VALIDATE_FAIL = `${prefix}/RESET_VALIDATE_FAIL`;

/**
 * Reducer
 **/
const ReducerState = {
    registerErrors:            void 0,
    isLoading:                 false,
    visiblePrivacyModal:       false,
    shouldConfirmRegistration: false,
    submittedPhone:            null,
    isResetValidate:           false,
};

// eslint-disable-next-line complexity
export default function reducer(state = ReducerState, action) {
    const { type, payload } = action;

    switch (type) {
        case REGISTER:
            return { ...state, isLoading: true, registerErrors: void 0, submittedPhone: action.payload.phone };

        case REGISTER_SUCCESS:
            return { ...state, isLoading: false, registerErrors: void 0, shouldConfirmRegistration: true };

        case REGISTER_FAIL:
            return { ...state, isLoading: false, registerErrors: payload };

        case SET_VISIBLE_PRIVACY_MODAL:
            return { ...state, visiblePrivacyModal: payload };

        case REGISTER_CONFIRM_START:
        case REGISTER_RESEND_START:
        case RESET_VALIDATE_START:
        case RESET_START:
            return { ...state, isLoading: true, registerErrors: void 0 };

        case REGISTER_RESEND_SUCCESS:
        case REGISTER_CONFIRM_SUCCESS:
            return { ...state, isLoading: false, registerErrors: void 0 };

        case REGISTER_RESEND_FAIL:
        case REGISTER_CONFIRM_FAIL:
        case RESET_FAIL:
        case RESET_VALIDATE_FAIL:
            return { ...state, isLoading: false, registerErrors: payload };

        case RESET_SUCCESS:
            return { ...state, isLoading: false, error: null, isResetValidate: true };

        case RESET_VALIDATE_SUCCESS:
            return { ...state, isLoading: false, error: null, isResetValidate: false };

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

export const registerConfirmStart = payload => ({
    type: REGISTER_CONFIRM_START,
    payload,
});

export const registerConfirmSuccess = () => ({
    type: REGISTER_CONFIRM_SUCCESS,
});

export const registerConfirmFail = errors => ({
    type:    REGISTER_CONFIRM_FAIL,
    payload: errors,
});

export const registerResendStart = () => ({
    type: REGISTER_RESEND_START,
});

export const registerResendSuccess = () => ({
    type: REGISTER_RESEND_SUCCESS,
});

export const registerResendFail = errors => ({
    type:    REGISTER_RESEND_FAIL,
    payload: errors,
});

const getPhone = state => state.register.submittedPhone;

const generalErrorNotification = () => {
    toast.error('Щось пішло не так, спробуйте ще раз!', {
        position: toast.POSITION.TOP_RIGHT,
    })
};

/**
 * Saga
 **/

export function* registerFormSaga({payload}) {
    const {password, ...restPayload} = payload;
    const body = { ...restPayload, password: md5(password) };
    try {
        const response = yield call(
            fetchAPI,
            'POST',
            'rest/register',
            null,
            body,
        );
        if (!response.code) {
            yield call(generalErrorNotification);
            yield put(registerFail({ credentials: true }));
        } else {
            yield put(registerSuccess());
        }
    } catch (err) {
        const failParams =
            err.status === 400 ? { credentials: true } : { system: true };
        yield call(generalErrorNotification);
        yield put(registerFail(failParams));
    }
}

export function* registerConfirmTask({payload}) {
    const phone = yield select(getPhone);
    try {
        const response = yield call(
            fetchAPI,
            'POST',
            'rest/register/confirm',
            null,
            {...payload, phone},
        );
        if (!response.code) {
            yield put(registerConfirmFail({ credentials: true }));
        } else {
            yield put(authenticate(response));
            yield put(registerConfirmSuccess());
            yield call(history.replace, book.board);
        }
    } catch (err) {
        const failParams =
            err.status === 400 ? { credentials: true } : { system: true };
        yield call(generalErrorNotification);
        yield put(registerConfirmFail(failParams));
    }
}

export function* registerResendTask() {

    const phone = yield select(getPhone);
    try {
        const response = yield call(
            fetchAPI,
            'POST',
            'rest/register/resend',
            null,
            {phone},
        );
        if (!response.code) {
            yield call(generalErrorNotification);
            yield put(registerResendFail({ credentials: true }));
        } else {
            yield call(toast.success, 'Код успішно надісланий на Ваш телефон!', {
                position: toast.POSITION.TOP_RIGHT,
            });
            yield put(registerResendSuccess());
        }
    } catch (err) {
        const failParams =
            err.status === 400 ? { credentials: true } : { system: true };
        yield call(generalErrorNotification);
        yield put(registerResendFail(failParams));
    }
}

function* watchRegisterSaga() {
    yield takeLatest(REGISTER, registerFormSaga);
}

function* watchConfirmRegisterSaga() {
    yield takeLatest(REGISTER_CONFIRM_START, registerConfirmTask);
}

function* watchRegisterResendSaga() {
    yield takeLatest(REGISTER_RESEND_START, registerResendTask);
}

export function* saga() {
    yield all(
        [
            call(watchRegisterSaga),
            call(watchConfirmRegisterSaga),
            call(watchRegisterResendSaga),
            call(watchReset),
            call(watchResetValidate),
        ],
    );
}

export { resetPasswordStart, resetPasswordValidateStart } from './reset-password';
