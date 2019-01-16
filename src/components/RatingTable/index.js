// vendor
import React, { Component } from 'react';
import { Column, Table, AutoSizer } from 'react-virtualized';
import classNames from 'classnames/bind';
import _ from 'lodash';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faStar } from '@fortawesome/free-solid-svg-icons';

// own
import Styles from './styles.module.css';
const cx = classNames.bind(Styles);

class RatingTableHandler extends Component {
    _rowStyles(rowIndex, selectedIndex, ownIndex) {
        const isHeader = rowIndex === -1;
        const selectedRow = selectedIndex !== -1 && selectedIndex === rowIndex;
        const ownRow = ownIndex !== -1 && ownIndex === rowIndex;

        return isHeader
            ? cx({ header: true })
            : cx({
                selectedRow,
                ownRow,
                row: !selectedRow && !ownRow,
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
                            gridClassName={ Styles.ratingGrid }
                            className={ Styles.ratingTable }
                            headerClassName={ Styles.header }
                            rowClassName={ ({ index }) =>
                                this._rowStyles(index, selectedIndex, ownIndex)
                            }
                            scrollToIndex={ selectedIndex }
                            height={ height }
                            width={ width }
                            headerHeight={ 60 }
                            rowHeight={ 50 }
                            rowCount={ rating.length }
                            rowGetter={ ({ index }) => rating[ index ] }
                            onRowClick={ ({ rowData }) =>
                                setSelectedParticipant(rowData)
                            }
                        >
                            <Column
                                label='#'
                                className={ Styles.ratingColumn }
                                dataKey='index'
                                flexGrow={ 0 }
                                flexShrink={ 0 }
                                width={ 40 }
                                cellRenderer={ ({ rowIndex }) =>
                                    rowIndex < 10 ? (
                                        <div className={ Styles.ratingStar }>
                                            <span className='fa-layers fa-fw fa-3x'>
                                                <FontAwesomeIcon
                                                    icon={ faStar }
                                                />
                                                <span
                                                    className={ `fa-layers-text fa-inverse ${
                                                        Styles.starLabel
                                                    }` }
                                                >
                                                    { rowIndex + 1 }
                                                </span>
                                            </span>
                                        </div>
                                    ) : (
                                        <div className={ Styles.ratingIndex }>
                                            { rowIndex + 1 }
                                        </div>
                                    )
                                }
                            />
                            <Column
                                label='Учасник'
                                className={ Styles.ratingColumn }
                                dataKey='email'
                                flexGrow={ 2 }
                                width={ 400 }
                            />
                            <Column
                                label='Бали'
                                className={ Styles.ratingColumn }
                                dataKey='score'
                                flexGrow={ 0 }
                                flexShrink={ 0 }
                                width={ 80 }
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
