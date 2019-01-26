// vendor
import createSagaMiddleware from 'redux-saga';
import ReactGA from 'react-ga';
import ReactPixel from 'react-facebook-pixel';

// proj
import history from './history';

const sagaMiddleware = createSagaMiddleware();
const middleware = [ sagaMiddleware ];

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

if (process.env.NODE_ENV !== 'development') {
    history.listen(({ pathname: page }) => {
        ReactGA.set({ page });
        ReactGA.pageview(page);
    });

    history.listen(() => {
        ReactPixel.pageView();
    });
}

export { history, sagaMiddleware, middleware };
