// vendor
import { all } from 'redux-saga/effects';

// proj
import { saga as authSaga } from '../redux/auth';
import { saga as boardSaga } from '../redux/board';
import { saga as registerSaga } from '../redux/register';
import { saga as settingsSaga } from '../redux/settings';
import { saga as analyticsSaga } from '../redux/analytics';

export default function* rootSaga() {
    yield all([
        analyticsSaga(),
        boardSaga(),
        authSaga(),
        registerSaga(),
        settingsSaga(),
    ]);
}
