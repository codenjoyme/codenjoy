// vendor
import React, { Component } from 'react';
import moment from 'moment-timezone';
import classNames from 'classnames/bind';

// own
import Styles from './styles.module.css';
const cx = classNames.bind(Styles);
const DATE_FORMAT = 'YYYY-MM-DD';

class DaysPanelHandler extends Component {
    _getDaysRangeConfig(dates) {
        const currentDate = moment().startOf('day');

        return dates.map((date, index) => {
            const label = String(index + 1);
            const day = date.format(DATE_FORMAT);

            const startOfDayDate = moment(date).startOf('day');
            const disabled = startOfDayDate.isAfter(currentDate);

            return { disabled, label, day };
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
                { daysRangeConfig.map(({ label, disabled, day: configDay }) => (
                    <button
                        title={ configDay }
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
