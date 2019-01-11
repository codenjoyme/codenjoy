// vendor
import { routerMiddleware as createRouterMiddleware } from 'connected-react-router';
import createSagaMiddleware from 'redux-saga';
import nprogress from 'nprogress';

// proj
import history from './history';

const routerMiddleware = createRouterMiddleware(history);
const sagaMiddleware = createSagaMiddleware();
const middleware = [ sagaMiddleware, routerMiddleware ];

if (process.env.NODE_ENV === 'development') {
    /**
     * redux-logger import with 'require' to prevent it in the compiled bundle
     */
    const { createLogger } = require("redux-logger"); // eslint-disable-line

    const logger = createLogger({
        duration:  true,
        timestamp: true,
        collapsed: true,
        diff:      true,
        colors:    {
            title:     () => 'deepskyblue',
            prevState: () => 'dodgerblue',
            action:    () => 'greenyellow',
            nextState: () => 'OliveDrab',
            error:     () => 'firebrick',
        },
    });

    middleware.push(logger);
}

history.listen(() => {
    nprogress.start();
    nprogress.done();
});

export { history, sagaMiddleware, middleware };
