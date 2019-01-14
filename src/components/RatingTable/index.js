// vendor
import React, { Component } from 'react';
import { Column, Table } from 'react-virtualized';
import classNames from 'classnames/bind';
import _ from 'lodash';

// own
import Styles from './styles.module.css';
const cx = classNames.bind(Styles);

class RatingTableHandler extends Component {
    _rowStyles(rowIndex, selectedIndex, ownIndex) {
        return cx({
            ownRow:      ownIndex !== -1 && ownIndex === rowIndex,
            selectedRow: selectedIndex === rowIndex,
        });
    }

    render() {
        const { setSelectedParticipant } = this.props;
        const { rating, email, selectedParticipant } = this.props;

        const participantEmail = _.get(selectedParticipant, 'email');
        const ownIndex = _.findIndex(rating, { email }); // Index of logged in user

        // Index of selected participant
        const selectedRatingIndex = _.findIndex(rating, {
            email: participantEmail || email,
        });

        const selectedIndex =
            selectedRatingIndex === -1 ? 0 : selectedRatingIndex;

        return rating ? (
            <div className={ Styles.rating }>
                <Table
                    rowClassName={ ({ index }) =>
                        this._rowStyles(index, selectedIndex, ownIndex)
                    }
                    scrollToIndex={ selectedIndex }
                    width={ 720 }
                    height={ 400 }
                    headerHeight={ 20 }
                    rowHeight={ 30 }
                    rowCount={ rating.length }
                    rowGetter={ ({ index }) => rating[ index ] }
                    onRowClick={ ({ rowData }) =>
                        setSelectedParticipant(rowData)
                    }
                >
                    <Column label='#' dataKey='index' width={ 100 } />
                    <Column label='Name' dataKey='email' width={ 220 } />
                    <Column label='Score' dataKey='score' width={ 100 } />
                    <Column label='Server' dataKey='server' width={ 300 } />
                </Table>
            </div>
        ) : (
            <div>Oooops, no rating yet</div>
        );
    }
}

export const RatingTable = RatingTableHandler;
