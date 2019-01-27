// vendor
import React, { Component } from 'react';
import { Column, Table, AutoSizer } from 'react-virtualized';
import classNames from 'classnames/bind';
import _ from 'lodash';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
    faStar,
    faArrowUp,
    faArrowRight,
    faAnchor,
} from '@fortawesome/free-solid-svg-icons';

// own
import Styles from './styles.module.css';
const cx = classNames.bind(Styles);

const ROW_HEIGHT = 50;
const HEADER_HEIGHT = 60;
const MIN_ITEMS = 10;

class RatingTableHandler extends Component {
    _starStyle(position) {
        if (position === 0) {
            return Styles.firstPlace;
        } else if (position < 3) {
            return Styles.topThree;
        } else if (position < 10) {
            return Styles.topTen;
        }
    }

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

    _getTableRef(table) {
        this.table = table;
    }

    _getSelectedIndex() {
        const { rating, watchId } = this.props;

        return _.isNil(watchId) ? -1 : _.findIndex(rating, { id: watchId });
    }

    _getOwnIndex() {
        const { rating, id } = this.props;

        return _.isNil(id) ? -1 : _.findIndex(rating, { id });
    }

    _getMinTableHeight() {
        const { rating } = this.props;

        return rating && rating.length < MIN_ITEMS
            ? Math.max(
                ROW_HEIGHT + HEADER_HEIGHT,
                rating.length * ROW_HEIGHT + HEADER_HEIGHT,
            )
            : Math.max(ROW_HEIGHT * MIN_ITEMS);
    }

    _scrollToPosition(selectedIndex) {
        if (this.table && selectedIndex !== -1) {
            this.table.scrollToRow(selectedIndex);
        }
    }

    componentDidUpdate(prevProps) {
        const { rating, watchPosition, watchId } = this.props;
        const selectedIndex = this._getSelectedIndex();

        if (prevProps.rating !== rating && watchPosition) {
            this._scrollToPosition(selectedIndex);
        }

        if (prevProps.watchId !== watchId) {
            this._scrollToPosition(selectedIndex);
        }
    }

    render() {
        const { setParticipant, setWatchPosition } = this.props;
        const { rating, watchPosition } = this.props;

        const ownIndex = this._getOwnIndex();
        const selectedIndex = this._getSelectedIndex();

        const minHeight = this._getMinTableHeight();

        return rating ? (
            <div className={ Styles.rating } style={ { minHeight } }>
                <AutoSizer>
                    { ({ width, height }) => (
                        <Table
                            ref={ this._getTableRef.bind(this) }
                            gridClassName={ Styles.ratingGrid }
                            className={ Styles.ratingTable }
                            headerClassName={ Styles.header }
                            rowClassName={ ({ index }) =>
                                this._rowStyles(index, selectedIndex, ownIndex)
                            }
                            height={ Math.max(height, minHeight) }
                            width={ width }
                            headerHeight={ HEADER_HEIGHT }
                            rowHeight={ ROW_HEIGHT }
                            rowCount={ rating.length }
                            rowGetter={ ({ index }) => rating[ index ] }
                            onRowClick={ ({ rowData }) =>
                                setParticipant(rowData)
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
                                                    className={ this._starStyle(
                                                        rowIndex,
                                                    ) }
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
                                headerRenderer={ () => (
                                    <div className={ Styles.participantHeader }>
                                        Учасник
                                        { ownIndex !== -1 && (
                                            <FontAwesomeIcon
                                                title='До моєї позиції'
                                                onClick={ () => {
                                                    setParticipant(
                                                        rating[ ownIndex ],
                                                    );
                                                    this._scrollToPosition(
                                                        ownIndex,
                                                    );
                                                } }
                                                className={ Styles.toMyPosition }
                                                icon={ faArrowRight }
                                            />
                                        ) }
                                        { !!rating.length && (
                                            <FontAwesomeIcon
                                                title='Показати лідерів'
                                                onClick={ () => {
                                                    setParticipant(
                                                        _.first(rating),
                                                    );
                                                    this._scrollToPosition(0);
                                                } }
                                                className={ Styles.toTop }
                                                icon={ faArrowUp }
                                            />
                                        ) }
                                        <FontAwesomeIcon
                                            title={
                                                watchPosition
                                                    ? 'Слідкувати за позицією'
                                                    : 'Вільно переглядати рейтинг'
                                            }
                                            onClick={ () => {
                                                setWatchPosition(
                                                    !watchPosition,
                                                );
                                                if (!watchPosition) {
                                                    this._scrollToPosition(
                                                        selectedIndex,
                                                    );
                                                }
                                            } }
                                            className={
                                                watchPosition
                                                    ? Styles.watchPosition
                                                    : Styles.freeMove
                                            }
                                            icon={ faAnchor }
                                        />
                                    </div>
                                ) }
                                dataKey='name'
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
                <div className={ Styles.spinner }>
                    <div className={ Styles.rect1 } />
                    <div className={ Styles.rect2 } />
                    <div className={ Styles.rect3 } />
                    <div className={ Styles.rect4 } />
                    <div className={ Styles.rect5 } />
                </div>
            </div>
        );
    }
}

export const RatingTable = RatingTableHandler;
