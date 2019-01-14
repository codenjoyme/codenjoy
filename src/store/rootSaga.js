// vendor
import { all } from 'redux-saga/effects';

// proj
import { saga as authSaga } from '../redux/auth';
import { saga as boardSaga } from '../redux/board';
import { saga as registerSaga } from '../redux/register';

export default function* rootSaga() {
    yield all([ boardSaga(), authSaga(), registerSaga() ]);
}
