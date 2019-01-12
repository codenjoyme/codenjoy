const proxy = require('http-proxy-middleware');

module.exports = function(app) {
    app.use(proxy('/codenjoy-balancer', { target: process.env.REACT_APP_API_SERVER }));
};
