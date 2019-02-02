// vendor
import React, { Component } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faStar } from '@fortawesome/free-solid-svg-icons';

// own
import Styles from './styles.module.css';

export class StarIndex extends Component {
    _starStyle(position) {
        if (position === 0) {
            return Styles.firstPlace;
        } else if (position < 3) {
            return Styles.topThree;
        } else if (position < 10) {
            return Styles.topTen;
        }
    }

    render() {
        const { rowIndex } = this.props;

        return rowIndex < 10 ? (
            <div className={ Styles.ratingStar }>
                <span className='fa-layers fa-fw fa-3x'>
                    <FontAwesomeIcon
                        className={ this._starStyle(rowIndex) }
                        icon={ faStar }
                    />
                    <span
                        className={ `fa-layers-text fa-inverse ${
                            Styles.starLabel
                        }` }
                    >
                        { rowIndex + 1 }
                    </span>
                </span>
            </div>
        ) : (
            <div className={ Styles.ratingIndex }>{ rowIndex + 1 }</div>
        );
    }
}
