// vendor
import { compose } from 'redux';

export const composeEnhancers = () => {
    const dev = process.env.NODE_ENV === 'development';

    const devtools = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__;

    return devtools && dev ? devtools : compose;
};
