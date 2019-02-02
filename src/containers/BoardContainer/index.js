// vendor
import React, { Component } from 'react';
import { connect } from 'react-redux';
import _ from 'lodash';
import moment from 'moment-timezone';
import { withRouter } from 'react-router-dom';
import qs from 'qs';

// proj
import {
    setDay,
    setParticipantId,
    setWatchPosition,
    startBackgroundSync,
    stopBackgroundSync,
    exitRoom,
    joinRoom,
    clearRoomChangeHistory,
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

    // eslint-disable-next-line complexity
    componentDidUpdate(prevProps) {
        const {
            rating,
            participantId,
            isRoomJoined,
            isRoomExited,
        } = this.props;
        const { clearRoomChangeHistory, setParticipantId } = this.props;

        const currentParticipant = this._getCurrentRatingParticipant();
        const isActive = this._isActiveDay();

        // Set participant if not set yet
        if (!participantId && rating && rating !== prevProps.rating) {
            const participantToSet = currentParticipant || _.get(rating, 0);
            const newParticipantId = _.get(participantToSet, 'id');

            if (newParticipantId) {
                setParticipantId(newParticipantId);
            }
        }

        // Clear room change history
        if (isActive && currentParticipant && isRoomJoined) {
            clearRoomChangeHistory();
        }

        if (isActive && !currentParticipant && isRoomExited) {
            clearRoomChangeHistory();
        }
    }

    _isActiveDay() {
        const { day } = this.props;

        return moment(day).isSame(moment(), 'day');
    }

    _getCurrentRatingParticipant() {
        const { id, rating } = this.props;

        return _.isNil(id) ? void 0 : _.find(rating, { id });
    }

    _getSelectedRatingParticipant() {
        const { rating, participantId } = this.props;

        return _.isNil(participantId)
            ? void 0
            : _.find(rating, { id: participantId });
    }

    render() {
        const { setDay, setParticipantId, setWatchPosition } = this.props; // actions
        const { day, rating, id, watchPosition } = this.props;

        const { exitRoom, joinRoom } = this.props;
        const { isExiting, isJoining, isRoomExited, isRoomJoined } = this.props;

        const changeRoomOptions = {
            exitRoom,
            joinRoom,
            isExiting,
            isJoining,
            isRoomExited,
            isRoomJoined,
        };

        const battleParticipant = this._getSelectedRatingParticipant();

        return (
            <div className={ Styles.boardContainer }>
                <DaysPanel day={ day } onDaySelect={ setDay } period={ period } />

                <div className={ Styles.wrapper }>
                    <div className={ Styles.rating }>
                        <RatingTable
                            { ...changeRoomOptions }
                            id={ id }
                            active={ this._isActiveDay() }
                            watchId={ _.get(battleParticipant, 'id') }
                            watchPosition={ watchPosition }
                            rating={ rating }
                            setParticipant={ ({ id }) => setParticipantId(id) }
                            setWatchPosition={ setWatchPosition }
                        />
                    </div>
                    <div className={ Styles.frame }>
                        <BattleFrame
                            battleCompleted={ !this._isActiveDay() }
                            participant={
                                this._isActiveDay() ? battleParticipant : void 0
                            }
                        />
                    </div>
                </div>
            </div>
        );
    }
}

const mapStateToProps = state => ({
    // rating related actions
    day:           state.board.day,
    participantId: state.board.participantId,
    watchPosition: state.board.watchPosition,
    rating:        state.board.rating,

    // auth
    id:     state.auth.id,
    server: state.auth.server,

    // room actions
    isExiting:    state.board.isExiting,
    isJoining:    state.board.isJoining,
    isRoomExited: state.board.isRoomExited,
    isRoomJoined: state.board.isRoomJoined,
});

const mapDispatchToProps = {
    // change room actions
    clearRoomChangeHistory,
    exitRoom,
    joinRoom,

    // rating related actions
    setDay,
    setParticipantId,
    setWatchPosition,

    // sync actions
    startBackgroundSync,
    stopBackgroundSync,
};

export default withRouter(
    connect(
        mapStateToProps,
        mapDispatchToProps,
    )(BoardContainer),
);
