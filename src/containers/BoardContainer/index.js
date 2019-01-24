// vendor
import React, { Component } from 'react';
import { connect } from 'react-redux';
import _ from 'lodash';
import moment from 'moment';
import { withRouter } from 'react-router-dom';
import qs from 'qs';

// proj
import {
    setDay,
    setParticipantId,
    startBackgroundSync,
    stopBackgroundSync,
} from '../../redux/board';
import { BattleFrame, DaysPanel, RatingTable } from '../../components';

// own
import Styles from './styles.module.css';

const QS_PARSE_OPTIONS = { ignoreQueryPrefix: true };
const period = {
    start: process.env.REACT_APP_EVENT_START || '2019-01-01T10:00:00.000Z',
    end:   process.env.REACT_APP_EVENT_END || '2019-01-31T10:00:00.000Z',
};

class BoardContainer extends Component {
    componentWillUnmount() {
        // eslint-disable-next-line no-sync
        this.props.stopBackgroundSync();
    }

    componentDidMount() {
        const queryParams = qs.parse(
            this.props.location.search,
            QS_PARSE_OPTIONS,
        );
        // eslint-disable-next-line no-sync
        this.props.startBackgroundSync(queryParams);
    }

    componentDidUpdate(prevProps) {
        const { rating, participantId } = this.props;
        if (!participantId && rating && rating !== prevProps.rating) {
            const { setParticipantId } = this.props;

            const currentParticipant = this._getCurrentRatingParticipant();
            const participantToSet = currentParticipant || _.get(rating, 0);
            const participantId = _.get(participantToSet, 'id'); 

            if (participantId) {
                setParticipantId(participantId);
            }
        }
    }

    _getCurrentRatingParticipant() {
        const { id, rating } = this.props;
        
        return _.isNil(id)
            ? void 0
            : _.find(rating, { id });
    }

    _getSelectedRatingParticipant() {
        const { rating, participantId } = this.props;

        return _.isNil(participantId)
            ? void 0
            : _.find(rating, { id: participantId });
    }

    render() {
        const { setDay, setParticipantId } = this.props; // actions
        const { day, rating, id } = this.props;

        const currentParticipant = this._getCurrentRatingParticipant();
        const participant = this._getSelectedRatingParticipant();

        const battleParticipant =
            participant || currentParticipant || _.get(rating, 0);

        return (
            <div className={ Styles.boardContainer }>
                <DaysPanel day={ day } onDaySelect={ setDay } period={ period } />

                <div className={ Styles.wrapper }>
                    <div className={ Styles.rating }>
                        <RatingTable
                            id={ id }
                            watchId={ _.get(battleParticipant, 'id') }
                            rating={ rating }
                            setParticipant={ ({ id }) => setParticipantId(id) }
                        />
                    </div>
                    <div className={ Styles.frame }>
                        <BattleFrame
                            participant={
                                moment(day).isSame(moment(), 'day')
                                    ? battleParticipant
                                    : void 0
                            }
                        />
                    </div>
                </div>
            </div>
        );
    }
}

const mapStateToProps = state => ({
    day:           state.board.day,
    participantId: state.board.participantId,
    rating:        state.board.rating,
    id:            state.auth.id,
    server:        state.auth.server,
});

const mapDispatchToProps = {
    setDay,
    setParticipantId,
    startBackgroundSync,
    stopBackgroundSync,
};

export default withRouter(
    connect(
        mapStateToProps,
        mapDispatchToProps,
    )(BoardContainer),
);
