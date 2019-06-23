import 'react-app-polyfill/ie11';
import cssVars from 'css-vars-ponyfill';
import React from 'react';
import ReactDOM from 'react-dom';
import 'react-virtualized/styles.css';

import './styles/css/stylesheet.css';
import './index.css';
import App from './App';

cssVars();

ReactDOM.render(<App />, document.getElementById('root'));
