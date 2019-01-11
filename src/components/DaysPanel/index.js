// vendor
import React, { Component } from 'react';
import moment from 'moment';

import Styles from './styles.module.css';

const DATE_FORMAT = 'YYYY-MM-DD';

class DaysPanelHandler extends Component {
    render() {
        const {
            selectedDay,
            period: { start, end },
            onDaySelect,
        } = this.props;

        const startDate = moment(start);
        const endDate = moment(end);

        const duration = moment.duration(endDate.diff(startDate));
        const days = Math.ceil(duration.asDays());
        const daysRange =
            days > 0
                ? Array(days)
                    .fill(0)
                    .map((value, index) =>
                        moment(startDate)
                            .add(index, 'd')
                            .format(DATE_FORMAT))
                : [];

        return (
            <div className={ Styles.wrapper }>
                <div className={ Styles.aside }>Leaderboard</div>
                <div className={ Styles.main }>
                    <div className={ Styles.dayPanel }>
                        { daysRange.map(day => (
                            <button
                                className={ Styles.item }
                                key={ day }
                                onClick={ () => onDaySelect(day) }
                                disabled={ selectedDay === day }
                            >
                                { day }
                            </button>
                        )) }
                    </div>
                </div>
            </div>
        );
    }
}

export const DaysPanel = DaysPanelHandler;
