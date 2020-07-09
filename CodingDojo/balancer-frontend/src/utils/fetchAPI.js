// vendor
import { trim, toUpper } from 'lodash/string';
import _ from 'lodash';
import qs from 'qs';

const apiC = '/codenjoy-balancer';

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
    const omittedQuery = _.omitBy(
        query,
        value => _.isString(value) && _.isEmpty(value),
    );
    const queryString = qs.stringify(omittedQuery, {
        skipNulls: true,
        arrayFormat: "repeat",
    });

    const request = {
        method: methodU,
        headers: headers || {
            "Content-Type": "application/json",
            "Cache-Control": "no-cache",
        },
    };

    if (methodU === "POST" || methodU === "PUT" || methodU === "DELETE") {
        request.body = JSON.stringify(body || {});
    }

    const response = await fetch.apply(null, [
        `${url || apiC}${handler}${queryString ? `?${queryString}` : ""}`,
        request,
        ...arguments,
    ]);

    const { status } = response;
    switch (true) {
        case status >= 200 && status < 300:
            return rawResponse ? await response : await response.json();

        default:
            throw response;
    }
}
