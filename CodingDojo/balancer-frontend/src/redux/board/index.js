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
import _ from 'lodash';
import moment from 'moment-timezone';
import qs from 'qs';

// proj
import { fetchAPI } from '../../utils';
import { book } from '../../routes';
import { history } from '../../store';
import { selectUser } from '../auth';

/**
 * Constants
 * */
export const moduleName = 'board';
const prefix = `codenjoy/${moduleName}`;

export const SET_DAY = `${prefix}/SET_DAY`;
export const SET_PARTICIPANT_ID = `${prefix}/SET_PARTICIPANT_ID`;
export const SET_DEFAULTS = `${prefix}/SET_DEFAULTS`;
export const SET_WATCH_POSITION = `${prefix}/SET_WATCH_POSITION`;

export const FETCH_RATING = `${prefix}/FETCH_RATING`;
export const FETCH_RATING_SUCCESS = `${prefix}/FETCH_RATING_SUCCESS`;
export const FETCH_RATING_FAILED = `${prefix}/FETCH_RATING_FAILED`;

export const START_BACKGROUND_SYNC = `${prefix}/START_BACKGROUND_SYNC`;
export const STOP_BACKGROUND_SYNC = `${prefix}/STOP_BACKGROUND_SYNC`;

export const EXIT_ROOM = `${prefix}/EXIT_ROOM`;
export const EXIT_ROOM_SUCCESS = `${prefix}/EXIT_ROOM_SUCCESS`;
export const EXIT_ROOM_FAILED = `${prefix}/EXIT_ROOM_FAILED`;

export const JOIN_ROOM = `${prefix}/JOIN_ROOM`;
export const JOIN_ROOM_SUCCESS = `${prefix}/JOIN_ROOM_SUCCESS`;
export const JOIN_ROOM_FAILED = `${prefix}/JOIN_ROOM_FAILED`;

export const CLEAR_ROOM_CHANGE_HISTORY = `${prefix}/CLEAR_ROOM_CHANGE_HISTORY`;

const eventStart = moment(process.env.REACT_APP_EVENT_START);
const eventEnd = moment(process.env.REACT_APP_EVENT_END);
const DATE_FORMAT = 'YYYY-MM-DD';
/**
 * Reducer
 * */

const ReducerState = {
    day:           moment().format(DATE_FORMAT),
    participantId: void 0,
    watchPosition: true,
    isRoomExited:  false,
    isExiting:     false,
    isRoomJoined:  false,
    isJoining:     false,
};

// eslint-disable-next-line complexity
export default function reducer(state = ReducerState, action) {
    const { type, payload } = action;

    switch (type) {
        case SET_DAY:
            return {
                ...state,
                day:           payload,
                participantId: void 0,
                rating:        void 0,
            };

        case SET_PARTICIPANT_ID:
            return { ...state, participantId: payload };

        case SET_WATCH_POSITION:
            return { ...state, watchPosition: payload };

        case SET_DEFAULTS:
            return {
                ...state,
                participantId: payload.participantId,
                day:           payload.day,
            };

        case EXIT_ROOM:
            return { ...state, isExiting: true };

        case EXIT_ROOM_FAILED:
            return { ...state, isExiting: false };

        case EXIT_ROOM_SUCCESS:
            return { ...state, isExiting: false, isRoomExited: true };

        case JOIN_ROOM:
            return { ...state, isJoining: true };

        case JOIN_ROOM_FAILED:
            return { ...state, isJoining: false };

        case JOIN_ROOM_SUCCESS:
            return { ...state, isJoining: false, isRoomJoined: true };

        case CLEAR_ROOM_CHANGE_HISTORY:
            return {
                ...state,
                isJoining:    false,
                isRoomJoined: false,
                isRoomExited: false,
                isExiting:    false,
            };

        case FETCH_RATING_SUCCESS:
            return {
                ...state,
                rating:
                    state.day === payload.day ? payload.rating : state.rating,
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

export const fetchRatingSuccess = (rating, day) => ({
    type:    FETCH_RATING_SUCCESS,
    payload: { rating, day },
});

export const fetchRatingFailed = () => ({
    type: FETCH_RATING_FAILED,
});

export const startBackgroundSync = payload => ({
    type: START_BACKGROUND_SYNC,
    payload,
});

export const stopBackgroundSync = () => ({
    type: STOP_BACKGROUND_SYNC,
});

export const setDefaults = payload => ({
    type: SET_DEFAULTS,
    payload,
});

export const setWatchPosition = watch => ({
    type:    SET_WATCH_POSITION,
    payload: watch,
});

export const joinRoom = () => ({ type: JOIN_ROOM });
export const joinRoomSuccess = () => ({ type: JOIN_ROOM_SUCCESS });
export const joinRoomFailed = () => ({ type: JOIN_ROOM_FAILED });

export const exitRoom = () => ({ type: EXIT_ROOM });
export const exitRoomSuccess = () => ({ type: EXIT_ROOM_SUCCESS });
export const exitRoomFailed = () => ({ type: EXIT_ROOM_FAILED });

export const clearRoomChangeHistory = () => ({
    type: CLEAR_ROOM_CHANGE_HISTORY,
});
/**
 * Saga
 **/

function* fetchRatingSaga() {
    const day = yield select(state => state.board.day);
    try {
        const data = yield call(fetchAPI, 'GET', `rest/score/day/${day}`);

        const processedData = _.chain(data)
            .filter('server')
            .orderBy([ 'score', 'name' ], 'desc')
            .map((value, index) => ({ ...value, index: index + 1 }))
            .value();

        yield put(fetchRatingSuccess(processedData, day));
    } catch (err) {
        yield put(fetchRatingFailed());
    }
}

function* ratingSync() {
    const timeout = process.env.REACT_APP_SCORES_UPDATE_TIMEOUT;
    while (true) {
        yield put(fetchRating());
        yield delay(timeout);
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
        const defaultsPayload = {
            day: defaultDay.format(DATE_FORMAT),
            participantId,
        };
        yield put(setDefaults(defaultsPayload));

        const ratingSyncTask = yield fork(ratingSync);

        yield take(STOP_BACKGROUND_SYNC);
        yield cancel(ratingSyncTask);
    }
}

export function* updateQueryParamsSaga() {
    const query = yield select(state => ({
        participantId: state.board.participantId,
        day:           state.board.day,
    }));
    const stringifiedQuery = qs.stringify(query);

    if (stringifiedQuery) {
        history.replace(book.board + '?' + stringifiedQuery);
    }
}

export function* exitRoomSaga() {
    try {
        const { email, code } = yield select(selectUser);

        if (!email || !code) {
            throw new Error('EMPTY_EMAIL');
        }

        yield call(fetchAPI, 'GET', `/rest/player/${email}/exit/${code}`);
        yield put(exitRoomSuccess());
    } catch (err) {
        yield put(exitRoomFailed());
    }
}

export function* joinRoomSaga() {
    try {
        const { email, code } = yield select(selectUser);

        if (!email || !code) {
            throw new Error('EMPTY_EMAIL');
        }

        yield call(fetchAPI, 'GET', `/rest/player/${email}/join/${code}`);
        yield put(joinRoomSuccess());
    } catch (err) {
        yield put(joinRoomFailed());
    }
}

export function* saga() {
    yield all([
        call(ratingSyncSaga),
        takeLatest([ SET_DAY, FETCH_RATING ], fetchRatingSaga),
        takeLatest([ SET_PARTICIPANT_ID, SET_DAY ], updateQueryParamsSaga),
        takeLatest(EXIT_ROOM, exitRoomSaga),
        takeLatest(JOIN_ROOM, joinRoomSaga),
    ]);
}
