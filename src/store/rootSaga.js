// vendor
import { all } from 'redux-saga/effects';

// proj
// import your sagas
import { saga as boardSaga } from 'redux/board';

export default function* rootSaga() {
    yield all([ boardSaga() ]);
}
