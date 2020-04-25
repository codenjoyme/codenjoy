// vendor
import React, { Component } from 'react';
import { Switch, Route, Redirect } from 'react-router-dom';

// proj
import {
    LoginPage,
    BoardPage,
    ExceptionPage,
    RegisterPage,
    RulesPage,
    HomePage,
    PrivacyPolicyPage,
    PrivacyRulesPage,
    ForgotPasswordPage,
    RegisterConfirmPage,
} from '../pages';
import { book } from './index.js';

export default class Public extends Component {
    render() {
        return (
            <Switch>
                <Route exact component={ HomePage } path={ book.home } />
                <Route exact component={ RulesPage } path={ book.rules } />
                <Route exact component={ BoardPage } path={ book.board } />
                <Route exact component={ LoginPage } path={ book.login } />
                <Route exact component={ RegisterPage } path={ book.register } />
                <Route exact component={ ForgotPasswordPage } path={ book.forgotPassword } />
                <Route exact component={ RegisterConfirmPage } path={ book.registerConfirm } />
                <Route
                    exact
                    component={ PrivacyPolicyPage }
                    path={ book.privacyPolicy }
                />
                <Route
                    exact
                    component={ PrivacyRulesPage }
                    path={ book.privacyRules }
                />
                <Route
                    component={ ExceptionPage }
                    path={ book.exceptionStatusCode }
                />
                <Redirect to={ book.home } />
            </Switch>
        );
    }
}
