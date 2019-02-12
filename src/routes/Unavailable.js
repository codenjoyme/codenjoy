// vendor
import React, { Component } from 'react';
import { Switch, Route, Redirect } from 'react-router-dom';

// proj
import {
    RulesPage,
    HomePage,
    PrivacyPolicyPage,
    PrivacyRulesPage,
    UnavailablePage,
} from '../pages';
import { book } from './index.js';

export default class Unavailable extends Component {
    render() {
        return (
            <Switch>
                <Route exact component={ HomePage } path={ book.home } />
                <Route exact component={ RulesPage } path={ book.rules } />
                <Route
                    exact
                    component={ UnavailablePage }
                    path={ book.unavailable }
                />
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
                <Redirect to={ book.unavailable } />
            </Switch>
        );
    }
}
