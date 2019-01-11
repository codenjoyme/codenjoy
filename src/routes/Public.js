// vendor
import React, { Component } from 'react';
import { Switch, Route, Redirect } from 'react-router-dom';

// proj
import { LoginPage, BoardPage, ExceptionPage } from 'pages';
import { book } from './index.js';

export default class Public extends Component {
    render() {
        return (
            <Switch>
                <Route exact component={ BoardPage } path={ book.board } />
                <Route exact component={ LoginPage } path={ book.login } />
                <Route exact component={ ExceptionPage } path={ book.error } />
                <Route component={ ExceptionPage } path={ book.exceptionStatusCode } />
                <Redirect to={ book.login } />
            </Switch>
        );
    }
}
