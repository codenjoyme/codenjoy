// vendor
import React, { Component } from 'react';
import { Column, Table } from 'react-virtualized';
import _ from 'lodash';

// own
import Styles from './styles.module.css';

class RatingTableHandler extends Component {
    render() {
        const { rating, setSelectedParticipant, email } = this.props;
        const index = _.findIndex(rating, { email });

        return rating ? (
            <div className={ Styles.rating }>
                <Table
                    scrollToIndex={ index === -1 ? 0 : index }
                    width={ 720 }
                    height={ 400 }
                    headerHeight={ 20 }
                    rowHeight={ 30 }
                    rowCount={ rating.length }
                    rowGetter={ ({ index }) => rating[ index ] }
                    onRowClick={ ({ rowData }) => setSelectedParticipant(rowData) }
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
