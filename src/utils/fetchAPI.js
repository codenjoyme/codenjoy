// vendor
import { trim, toUpper } from 'lodash/string';
import { replace } from 'connected-react-router';
import _ from 'lodash';
import qs from 'qs';

import store from '../store';
import { book } from '../routes';

const apiC = '/api';

/* eslint-disable */
export async function fetchAPI(
    method,
    endpoint,
    query,
    body,
    { rawResponse, url, headers } = {},
) {
    const endpointC = trim(endpoint, "/"); // trim all spaces and '/'
    const handler = endpointC ? `/${endpointC}` : ""; // be sure that after api will be only one /
    const methodU = toUpper(method);
    const omittedQuery = _.omitBy(query, value => _.isString(value) && _.isEmpty(value));
    const queryString = qs.stringify(omittedQuery, {
        skipNulls: true,
        arrayFormat: "repeat",
    });

    const request = {
        method: methodU,
        headers: headers,
    };

    if (methodU === "POST" || methodU === "PUT" || methodU === "DELETE") {
        request.body = JSON.stringify(body || {});
    }

    // async function response() {
    const response = await fetch.apply(null, [
        `${url || apiC}${handler}${queryString ? `?${queryString}` : ""}`,
        request,
        ...arguments,
    ]);

    const { status } = response;
    const { dispatch } = store;

    switch (true) {
        case status >= 200 && status < 300:
            return rawResponse ? await response : await response.json();

        case status === 400:
            dispatch(replace(`${book.exception}/400`));
            return;

        case status === 401:
            dispatch(logout());
            return;

        case status === 403:
            dispatch(replace(`${book.exception}/403`));
            return;

        case status >= 404 && status < 422:
            dispatch(replace(`${book.exception}/404`));
            return;

        case status >= 500 && status <= 504:
            dispatch(replace(`${book.exception}/500`));
            return;

        default:
            dispatch(replace(`${book.exception}/500`));
            return;
    }
}
