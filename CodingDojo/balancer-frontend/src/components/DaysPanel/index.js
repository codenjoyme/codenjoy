// vendor
import React, { Component } from 'react';
import moment from 'moment-timezone';
import classNames from 'classnames/bind';

// own
import Styles from './styles.module.css';
const cx = classNames.bind(Styles);
const DATE_FORMAT = 'YYYY-MM-DD';
const EXCLUDED_DAYS = process.env.REACT_APP_EXCLUDED_DAYS;

class DaysPanelHandler extends Component {
    _getDaysRangeConfig(dates) {
        const currentDate = moment().startOf('day');
        const excludedDays = EXCLUDED_DAYS.split(',')

        return dates.map((date, index) => {
            const label = String(index + 1);
            let disabledTitle = '';
            const day = date.format(DATE_FORMAT);

            const startOfDayDate = moment(date).startOf('day');
            const isDayExcluded = excludedDays.includes(day);
            if (isDayExcluded) {
                disabledTitle = 'Вихідний день'
            }
            const disabled = startOfDayDate.isAfter(currentDate) || isDayExcluded;

            return { disabled, label, day, disabledTitle };
        });
    }

    _dayButtonStyles = (selected, disabled) =>
        cx({
            dayButtonDisabled: disabled,
            dayButtonSelected: selected,
            dayButton:         true,
        });

    render() {
        const {
            day,
            period: { start, end },
            onDaySelect,
        } = this.props;
        const startDate = moment(start).startOf('day');
        const endDate = moment(end).startOf('day');

        const duration = moment.duration(endDate.diff(startDate));
        const days = Math.ceil(duration.asDays()) + 1;
        const dates =
            days > 0
                ? [ ...Array(days) ].map((value, index) =>
                    moment(startDate).add(index, 'd'))
                : [];

        const daysRangeConfig = this._getDaysRangeConfig(dates);

        return (
            <div className={ Styles.dayPanel }>
                <div className={ Styles.dayName }>День №</div>
                { daysRangeConfig.map(({ label, disabled, day: configDay, disabledTitle }) => (
                    <button
                        title={ disabledTitle || configDay }
                        className={ this._dayButtonStyles(
                            day === configDay,
                            disabled,
                        ) }
                        key={ configDay }
                        onClick={ () => onDaySelect(configDay) }
                        disabled={ day === configDay || disabled }
                    >
                        { label }
                    </button>
                )) }
            </div>
        );
    }
}

export const DaysPanel = DaysPanelHandler;
