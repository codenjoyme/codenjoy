// vendor
import { combineReducers } from 'redux';

import { persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage';

// proj
import authReducer, { moduleName as authModule } from '../redux/auth';
import boardReducer, { moduleName as boardModule } from '../redux/board';
import settingsReducer, {moduleName as settingsModule} from '../redux/settings';
import registerReducer, {
    moduleName as registerModule,
} from '../redux/register';

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
    [ settingsModule ]: settingsReducer,
};

const rootReducer = persistReducer(
    persistConfig,
    combineReducers({ ...persistedState, ...appState }),
);

export default rootReducer;
