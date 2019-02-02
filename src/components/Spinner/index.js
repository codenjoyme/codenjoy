// vendor
import React from 'react';

// own
import Styles from './styles.module.css';

export default () => (
    <div>
        <div className={ Styles.spinner }>
            <div className={ Styles.rect1 } />
            <div className={ Styles.rect2 } />
            <div className={ Styles.rect3 } />
            <div className={ Styles.rect4 } />
            <div className={ Styles.rect5 } />
        </div>
    </div>
);
