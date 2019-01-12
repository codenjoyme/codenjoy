// vendor
import { all, put, call, take } from 'redux-saga/effects';
import moment from 'moment';

// proj

/**
 * Constants
 * */
export const moduleName = 'board';
const prefix = `codenjoy/${moduleName}`;

export const SET_SELECTED_DAY = `${prefix}/SET_SELECTED_DAY`;

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
        case SET_SELECTED_DAY:
            return {
                ...state,
                selectedDay:         payload,
                rating:              void 0,
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
                rating: void 0,
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

export function* setSelectedDaySaga() {
    while (true) {
        const { payload: selectedDay } = yield take(SET_SELECTED_DAY);
        yield put(fetchRating(selectedDay));
    }
}

export function* fetchRatingSaga() {
    while (true) {
        const {
            payload: { selectedDay },
        } = yield take(FETCH_RATING);

        // const data = yield call(fetchAPI, "GET", "rating");
        const data = Array(1000)
            .fill(null)
            .map((_, index) => ({
                email:  'user_' + index + '_' + selectedDay,
                score:  Math.ceil(Math.random() * 10000),
                server: 'Таразед',
            }));

        yield put(fetchRatingSuccess(data));
    }
}

export function* saga() {
    yield all([ call(setSelectedDaySaga), call(fetchRatingSaga) ]);
}
