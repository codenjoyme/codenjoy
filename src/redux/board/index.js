// vendor
import { all, put, call, take } from 'redux-saga/effects';
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
export const SET_SELECTED_DAY_SUCCESS = `${prefix}/SET_SELECTED_DAY_SUCCESS`;

export const SET_SELECTED_PARTICIPANT = `${prefix}/SET_SELECTED_PARTICIPANT`;

export const FETCH_RATING = `${prefix}/FETCH_RATING`;
export const FETCH_RATING_SUCCESS = `${prefix}/FETCH_RATING_SUCCESS`;

/**
 * Reducer
 * */

const ReducerState = {
    selectedDay: moment().format('YYYY-MM-DD'),
};

export default function reducer(state = ReducerState, action) {
    const { type, payload } = action;

    switch (type) {
        case SET_SELECTED_DAY_SUCCESS:
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

export const setSelectedDaySuccess = selectedDay => ({
    type:    SET_SELECTED_DAY_SUCCESS,
    payload: selectedDay,
});

export const setSelectedParticipant = selectedParticipant => ({
    type:    SET_SELECTED_PARTICIPANT,
    payload: selectedParticipant,
});

export const fetchRating = selectedDay => ({
    type:    FETCH_RATING,
    payload: { selectedDay },
});

export const fetchRatingSuccess = data => ({
    type:    FETCH_RATING_SUCCESS,
    payload: data,
});

/**
 * Saga
 **/

function* fetch(selectedDay) {
    const data = yield call(fetchAPI, 'GET', `rest/score/day/${selectedDay}`);

    const processedData = _.chain(data)
        .filter('server')
        .orderBy('score', 'desc')
        .map((value, index) => ({ ...value, index: index + 1 }))
        .value();

    yield put(fetchRatingSuccess(processedData));
}

export function* setSelectedDaySaga() {
    while (true) {
        const { payload: selectedDay } = yield take(SET_SELECTED_DAY);
        yield call(fetch, selectedDay);
        yield put(setSelectedDaySuccess(selectedDay));
    }
}

export function* fetchRatingSaga() {
    while (true) {
        const {
            payload: { selectedDay },
        } = yield take(FETCH_RATING);
        yield call(fetch, selectedDay);
    }
}

export function* saga() {
    yield all([ call(setSelectedDaySaga), call(fetchRatingSaga) ]);
}
