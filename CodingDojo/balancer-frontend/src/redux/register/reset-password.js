// vendor
import { call, put, takeLatest } from 'redux-saga/effects';
import { delay } from 'redux-saga';
import { toast } from 'react-toastify';
// proj
import { history } from '../../store';
import { book } from '../../routes';
import { fetchAPI } from '../../utils';

import {
    RESET_START,
    RESET_SUCCESS,
    RESET_FAIL,
    RESET_VALIDATE_START,
    RESET_VALIDATE_SUCCESS,
    RESET_VALIDATE_FAIL,
} from './index';


export const resetPasswordStart = payload => ({
    type: RESET_START,
    payload,
});

export const resetPasswordSuccess = () => ({
    type: RESET_SUCCESS,
});

export const resetPasswordFail = errors => ({
    type:    RESET_FAIL,
    payload: errors,
});

export const resetPasswordValidateStart = payload => ({
    type: RESET_VALIDATE_START,
    payload,
});

export const resetPasswordValidateSuccess = () => ({
    type: RESET_VALIDATE_SUCCESS,
});

export const resetPasswordValidateFail = errors => ({
    type:    RESET_VALIDATE_FAIL,
    payload: errors,
});

const generalErrorNotification = () => {
    toast.error('Щось пішло не так, спробуйте ще раз!', {
        position: toast.POSITION.TOP_RIGHT,
    })
};

/**
 * Saga
 **/

export function* resetPasswordSaga({payload}) {
    const body = {phone: payload.phone};
    try {
        yield call(
            fetchAPI,
            'POST',
            'rest/register/reset',
            null,
            body,
            { rawResponse: true },
        );

        yield call(toast.success, 'Код підтвердження успішно надісланий на Ваш телефон!', {
            position: toast.POSITION.TOP_RIGHT,
        });
        yield put(resetPasswordSuccess());

    } catch (err) {
        const failParams =
            err.status === 400 ? { credentials: true } : { system: true };
        yield call(generalErrorNotification);
        yield put(resetPasswordFail(failParams));
    }
}

export function* resetPasswordValidateSaga({payload}) {
    try {
        yield call(
            fetchAPI,
            'POST',
            'rest/register/validate-reset',
            null,
            payload,
            { rawResponse: true },
        );
        yield call(toast.success, 'Новий пароль успішно надісланий на Ваш телефон!', {
            position: toast.POSITION.TOP_RIGHT,
        });
        yield delay(3000);
        yield put(resetPasswordValidateSuccess());
        yield call(history.replace, book.login);
    } catch (err) {
        const failParams =
            err.status === 400 ? { credentials: true } : { system: true };
        yield call(generalErrorNotification);
        yield put(resetPasswordValidateFail(failParams));
    }
}

export function* watchReset() {
    yield takeLatest(RESET_START, resetPasswordSaga);
}

export function* watchResetValidate() {
    yield takeLatest(RESET_VALIDATE_START, resetPasswordValidateSaga);
}

