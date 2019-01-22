// vendor
import {
    all,
    put,
    call,
    takeLatest,
    take,
    fork,
    cancel,
    select,
} from 'redux-saga/effects';
import { delay } from 'redux-saga';
import moment from 'moment';
import _ from 'lodash';

// proj
import { fetchAPI } from '../../utils';

/**
 * Constants
 * */
export const moduleName = 'board';
const prefix = `codenjoy/${moduleName}`;

export const SET_SELECTED_DAY = `${prefix}/SET_SELECTED_DAY`;

export const SET_SELECTED_PARTICIPANT = `${prefix}/SET_SELECTED_PARTICIPANT`;

export const FETCH_RATING = `${prefix}/FETCH_RATING`;
export const FETCH_RATING_SUCCESS = `${prefix}/FETCH_RATING_SUCCESS`;

export const START_BACKGROUND_SYNC = `${prefix}/START_BACKGROUND_SYNC`;
export const STOP_BACKGROUND_SYNC = `${prefix}/STOP_BACKGROUND_SYNC`;

/**
 * Reducer
 * */

const ReducerState = {
    selectedDay: moment().format('YYYY-MM-DD'),
};

export default function reducer(state = ReducerState, action) {
    const { type, payload } = action;

    switch (type) {
        case SET_SELECTED_DAY:
            return {
                ...state,
                selectedDay:         payload,
                selectedParticipant: void 0,
            };

        case SET_SELECTED_PARTICIPANT:
            return {
                ...state,
                selectedParticipant: payload,
            };

        case FETCH_RATING:
            return {
                ...state,
                // rating: void 0,
            };

        case FETCH_RATING_SUCCESS:
            return {
                ...state,
                rating: payload,
            };

        default:
            return state;
    }
}

/**
 * Actions
 * */

export const setSelectedDay = selectedDay => ({
    type:    SET_SELECTED_DAY,
    payload: selectedDay,
});

export const setSelectedParticipant = selectedParticipant => ({
    type:    SET_SELECTED_PARTICIPANT,
    payload: selectedParticipant,
});

export const fetchRating = () => ({
    type: FETCH_RATING,
});

export const fetchRatingSuccess = data => ({
    type:    FETCH_RATING_SUCCESS,
    payload: data,
});

export const startBackgroundSync = () => ({
    type: START_BACKGROUND_SYNC,
});

export const stopBackgroundSync = () => ({
    type: STOP_BACKGROUND_SYNC,
});

/**
 * Saga
 **/

function* fetchRatingSaga() {
    const selectedDay = yield select(state => state.board.selectedDay);
    const data = yield call(fetchAPI, 'GET', `rest/score/day/${selectedDay}`);

    const processedData = _.chain(data)
        .filter('server')
        .orderBy('score', 'desc')
        .map((value, index) => ({ ...value, index: index + 1 }))
        .value();

    yield put(fetchRatingSuccess(processedData));
}

function* ratingSync() {
    while (true) {
        yield put(fetchRating());
        yield delay(10000);
    }
}

function* ratingSyncSaga() {
    while (yield take(START_BACKGROUND_SYNC)) {
        const ratingSyncTask = yield fork(ratingSync);

        yield take(STOP_BACKGROUND_SYNC);
        yield cancel(ratingSyncTask);
    }
}

export function* setSelectedDaySaga() {
    yield put(fetchRating());
}

export function* saga() {
    yield all([ call(ratingSyncSaga), takeLatest(SET_SELECTED_DAY, setSelectedDaySaga), takeLatest(FETCH_RATING, fetchRatingSaga) ]);
}
