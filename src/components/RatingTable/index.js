// vendor
import React, { Component } from 'react';
import { Column, Table, AutoSizer } from 'react-virtualized';
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
                <AutoSizer disableHeight>
                    { ({ width }) => (
                        <Table
                            rowClassName={ ({ index }) =>
                                this._rowStyles(index, selectedIndex, ownIndex)
                            }
                            scrollToIndex={ selectedIndex }
                            height={ 300 }
                            width={ width }
                            headerHeight={ 20 }
                            rowHeight={ 30 }
                            rowCount={ rating.length }
                            rowGetter={ ({ index }) => rating[ index ] }
                            onRowClick={ ({ rowData }) =>
                                setSelectedParticipant(rowData)
                            }
                        >
                            <Column
                                label='#'
                                dataKey='index'
                                flexGrow={ 1 }
                                width={ 100 }
                            />
                            <Column
                                label='Name'
                                dataKey='email'
                                flexGrow={ 2 }
                                width={ 200 }
                            />
                            <Column
                                label='Score'
                                dataKey='score'
                                flexGrow={ 1 }
                                width={ 100 }
                            />
                            <Column
                                label='Server'
                                dataKey='server'
                                width={ 300 }
                                flexGrow={ 3 }
                            />
                        </Table>
                    ) }
                </AutoSizer>
            </div>
        ) : (
            <div>Oooops, no rating yet</div>
        );
    }
}

export const RatingTable = RatingTableHandler;
