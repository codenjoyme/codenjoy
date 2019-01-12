// vendor
import React, { Component } from 'react';

// proj
// import { book } from 'routes';

export default class ExceptionPage extends Component {
    _getErrorText = statusCode => {
        switch (statusCode) {
            case '400':
                return 'Bad Request';

            case '403':
                return 'Forbidden ';

            case '404':
                return 'Not Found';

            case '500':
                return 'Internal Server Error';

            default:
                return 'Error!';
        }
    };

    render() {
        const statusCode = this.props.match.params.statusCode;

        return (
            <div>
                <h1>{ statusCode }</h1>
                <span>{ this._getErrorText(statusCode) }</span>
            </div>
        );
    }
}
