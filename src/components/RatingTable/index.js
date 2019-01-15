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
            selectedRow: selectedIndex !== -1 && selectedIndex === rowIndex,
        });
    }

    render() {
        const { setSelectedParticipant } = this.props;
        const { rating, email, watchEmail } = this.props;

        const ownIndex = _.findIndex(rating, { email }); // Index of logged in user
        const selectedIndex = _.findIndex(rating, { email: watchEmail }); // Index of selected participant

        return rating ? (
            <div className={ Styles.rating }>
                <AutoSizer>
                    { ({ width, height }) => (
                        <Table
                            className={ Styles.ratingTable }
                            rowClassName={ ({ index }) =>
                                this._rowStyles(index, selectedIndex, ownIndex)
                            }
                            scrollToIndex={ selectedIndex }
                            height={ height }
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
                        </Table>
                    ) }
                </AutoSizer>
            </div>
        ) : (
            <div>
                Інформація про рейтинг відсутня. Спробуйте обрати інший день
            </div>
        );
    }
}

export const RatingTable = RatingTableHandler;
