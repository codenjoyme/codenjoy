// vendor
import { combineReducers } from 'redux';
import { connectRouter } from 'connected-react-router';

import { persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage';

// proj
import authReducer, { moduleName as authModule } from '../redux/auth';
import boardReducer, { moduleName as boardModule } from '../redux/board';
import registerReducer, {
    moduleName as registerModule,
} from '../redux/register';

// own
import { history } from './middleware';

export const persistConfig = {
    key:       'root',
    storage,
    whitelist: [ 'auth' ],
};

const persistedState = {
    [ authModule ]: authReducer,
};

const appState = {
    [ boardModule ]:    boardReducer,
    [ registerModule ]: registerReducer,
    router:             connectRouter(history),
};

const rootReducer = persistReducer(
    persistConfig,
    combineReducers({ ...persistedState, ...appState }),
);

export default rootReducer;
