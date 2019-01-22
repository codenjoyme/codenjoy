// vendor
import React, { Component } from 'react';
import { connect } from 'react-redux';
import _ from 'lodash';
import moment from 'moment';

// proj
import {
    setSelectedDay,
    setSelectedParticipant,
    startBackgroundSync,
    stopBackgroundSync,
} from '../../redux/board';
import { BattleFrame, DaysPanel, RatingTable } from '../../components';

// own
import Styles from './styles.module.css';

const period = {
    start: process.env.REACT_APP_EVENT_START || '2019-01-01T10:00:00.000Z',
    end:   process.env.REACT_APP_EVENT_END || '2019-01-31T10:00:00.000Z',
};

const eventStart = moment(period.start);
const eventEnd = moment(period.end);

class BoardContainer extends Component {
    _isSelectedDateValid(selectedDay) {
        const currentDate = moment(selectedDay);

        return (
            selectedDay &&
            (currentDate.isSameOrAfter(eventStart, 'day') &&
                currentDate.isSameOrBefore(eventEnd, 'day'))
        );
    }

    componentWillUnmount() {
        // eslint-disable-next-line no-sync
        this.props.stopBackgroundSync();
    }

    componentDidMount() {
        // eslint-disable-next-line no-sync
        this.props.startBackgroundSync();
    }

    render() {
        const { setSelectedDay, setSelectedParticipant } = this.props;
        const { selectedDay, rating, selectedParticipant } = this.props;
        const { id, server } = this.props;

        const ratingContainsId = id && _.find(rating, { id });
        const currentParticipant =
            ratingContainsId && id && server ? { id, server } : void 0;

        const battleParticipant =
            selectedParticipant || currentParticipant || _.get(rating, 0);

        return (
            <div className={ Styles.boardContainer }>
                <DaysPanel
                    selectedDay={ selectedDay }
                    onDaySelect={ setSelectedDay }
                    period={ period }
                />

                <div className={ Styles.wrapper }>
                    <div className={ Styles.rating }>
                        <RatingTable
                            id={ id }
                            watchId={ _.get(battleParticipant, 'id') }
                            rating={ rating }
                            setSelectedParticipant={ setSelectedParticipant }
                        />
                    </div>
                    <div className={ Styles.frame }>
                        { moment(selectedDay).isSame(moment(), 'day') && (
                            <BattleFrame participant={ battleParticipant } />
                        ) }
                    </div>
                </div>
            </div>
        );
    }
}

const mapStateToProps = state => ({
    selectedDay:         state.board.selectedDay,
    selectedParticipant: state.board.selectedParticipant,
    rating:              state.board.rating,
    id:                  state.auth.id,
    server:              state.auth.server,
});

const mapDispatchToProps = {
    setSelectedDay,
    setSelectedParticipant,
    startBackgroundSync,
    stopBackgroundSync,
};

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(BoardContainer);
