// vendor
import React, { Component } from 'react';
import moment from 'moment';

import Styles from './styles.module.css';

const DATE_FORMAT = 'YYYY-MM-DD';

class DaysPanelHandler extends Component {
    _getDaysRangeConfig(dates) {
        const currentDate = moment().startOf('day');

        return dates.map(date => {
            const label = date.format(DATE_FORMAT);
            const day = date.format(DATE_FORMAT);

            const startOfDayDate = moment(date).startOf('day');
            const disabled = startOfDayDate.isAfter(currentDate);

            return { disabled, label, day };
        });
    }

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
        const dates =
            days > 0
                ? Array(days)
                    .fill(0)
                    .map((value, index) => moment(startDate).add(index, 'd'))
                : [];

        const daysRangeConfig = this._getDaysRangeConfig(dates);

        return (
            <div className={ Styles.wrapper }>
                <div className={ Styles.aside }>Leaderboard</div>
                <div className={ Styles.main }>
                    <div className={ Styles.dayPanel }>
                        { daysRangeConfig.map(({ label, disabled, day }) => (
                            <button
                                className={ Styles.item }
                                key={ day }
                                onClick={ () => onDaySelect(day) }
                                disabled={ selectedDay === day || disabled }
                            >
                                { label }
                            </button>
                        )) }
                    </div>
                </div>
            </div>
        );
    }
}

export const DaysPanel = DaysPanelHandler;
