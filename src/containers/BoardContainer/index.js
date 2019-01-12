// vendor
import React, { Component } from 'react';
import { connect } from 'react-redux';
import _ from 'lodash';
import moment from 'moment';

// proj
import { setSelectedDay, setSelectedParticipant, fetchRating } from '../../redux/board';
import { BattleFrame, DaysPanel, RatingTable } from '../../components';

// own
import Styles from './styles.module.css';

// TODO set env variables
const period = {
    start: '2019-01-01T10:00:00.000Z',
    end:   '2019-01-16T10:00:00.000Z',
};

class BoardContainer extends Component {
    componentDidMount() {
        this.props.fetchRating(this.props.selectedDay);
    }

    render() {
        const { setSelectedDay, setSelectedParticipant } = this.props;
        const { selectedDay, rating, email, selectedParticipant } = this.props;
        const participant = _.get(selectedParticipant, 'email') || email || _.get(rating, '[0].email');

        return (
            <>
                <DaysPanel selectedDay={ selectedDay } onDaySelect={ setSelectedDay } period={ period } />
                <div className={ Styles.wrapper }>
                    <div className={ Styles.frame }>
                        { moment(selectedDay).isSame(new Date(), 'day') && <BattleFrame participant={ participant } /> }
                    </div>
                    <div className={ Styles.rating }>
                        <RatingTable email={ email } rating={ rating } setSelectedParticipant={ setSelectedParticipant } />
                    </div>
                </div>
            </>
        );
    }
}

const mapStateToProps = state => ({
    selectedDay:         state.board.selectedDay,
    selectedParticipant: state.board.selectedParticipant,
    rating:              state.board.rating,
    email:               state.auth.email,
});

const mapDispatchToProps = {
    setSelectedDay,
    setSelectedParticipant,
    fetchRating,
};

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(BoardContainer);
