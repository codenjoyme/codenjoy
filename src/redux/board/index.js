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
import { replace } from 'connected-react-router';
import { delay } from 'redux-saga';
import _ from 'lodash';
import moment from 'moment';
import qs from 'qs';

// proj
import { fetchAPI } from '../../utils';
import { book } from '../../routes';

/**
 * Constants
 * */
export const moduleName = 'board';
const prefix = `codenjoy/${moduleName}`;

export const SET_DAY = `${prefix}/SET_DAY`;

export const SET_PARTICIPANT_ID = `${prefix}/SET_PARTICIPANT_ID`;

export const FETCH_RATING = `${prefix}/FETCH_RATING`;
export const FETCH_RATING_SUCCESS = `${prefix}/FETCH_RATING_SUCCESS`;

export const START_BACKGROUND_SYNC = `${prefix}/START_BACKGROUND_SYNC`;
export const STOP_BACKGROUND_SYNC = `${prefix}/STOP_BACKGROUND_SYNC`;

const eventStart = moment(process.env.REACT_APP_EVENT_START);
const eventEnd = moment(process.env.REACT_APP_EVENT_END);
const DATE_FORMAT = 'YYYY-MM-DD';
/**
 * Reducer
 * */

const ReducerState = {
    day:           moment().format(DATE_FORMAT),
    participantId: void 0,
};

export default function reducer(state = ReducerState, action) {
    const { type, payload } = action;

    switch (type) {
        case SET_DAY:
            return {
                ...state,
                day:           payload,
                participantId: void 0,
            };

        case SET_PARTICIPANT_ID:
            return {
                ...state,
                participantId: payload,
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

export const setDay = day => ({
    type:    SET_DAY,
    payload: day,
});

export const setParticipantId = participantId => ({
    type:    SET_PARTICIPANT_ID,
    payload: participantId,
});

export const fetchRating = () => ({
    type: FETCH_RATING,
});

export const fetchRatingSuccess = data => ({
    type:    FETCH_RATING_SUCCESS,
    payload: data,
});

export const startBackgroundSync = payload => ({
    type: START_BACKGROUND_SYNC,
    payload,
});

export const stopBackgroundSync = () => ({
    type: STOP_BACKGROUND_SYNC,
});

/**
 * Saga
 **/

function* fetchRatingSaga() {
    const day = yield select(state => state.board.day);
    const data = yield call(fetchAPI, 'GET', `rest/score/day/${day}`);

    const processedData = _.chain(data)
        .filter('server')
        .orderBy('score', 'desc')
        .map((value, index) => ({ ...value, index: index + 1 }))
        .value();

    yield put(fetchRatingSuccess(processedData));
}

function* ratingSync() {
    while (true) {
        yield delay(10000);
        yield put(fetchRating());
    }
}

function* ratingSyncSaga() {
    while (true) {
        const {
            payload: { day, participantId },
        } = yield take(START_BACKGROUND_SYNC);

        const passedMomentDay = moment(day);
        const defaultMomentDay = passedMomentDay.isValid()
            ? passedMomentDay
            : moment();

        const defaultDay = _.max([ _.min([ defaultMomentDay, eventEnd ]), eventStart ]);
        yield put(setDay(defaultDay.format(DATE_FORMAT)));

        if (participantId) {
            yield put(setParticipantId(participantId));
        }

        const ratingSyncTask = yield fork(ratingSync);

        yield take(STOP_BACKGROUND_SYNC);
        yield cancel(ratingSyncTask);
    }
}

export function* setDaySaga() {
    yield put(fetchRating());
}

export function* updateQueryParamsSaga() {
    const queryParams = yield select(state => ({
        participantId: state.board.participantId,
        day:           state.board.day,
    }));

    yield put(replace(book.board + '?' + qs.stringify(queryParams)));
}

export function* saga() {
    yield all([
        call(ratingSyncSaga),
        takeLatest(SET_DAY, setDaySaga),
        takeLatest(FETCH_RATING, fetchRatingSaga),
        takeLatest([ SET_PARTICIPANT_ID, SET_DAY ], updateQueryParamsSaga),
    ]);
}
