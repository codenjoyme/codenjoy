// vendor
import React, { Component } from 'react';
import { Column, Table } from 'react-virtualized';

// own
import Styles from './styles.module.css';

class RatingTableHandler extends Component {
    render() {
        const { rating, setSelectedParticipant } = this.props;

        return rating ? (
            <div className={ Styles.rating }>
                <Table
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
                    <Column label='Name' dataKey='name' width={ 220 } />
                    <Column label='Score' dataKey='score' width={ 100 } />
                    <Column
                        label='Operations'
                        dataKey='operations'
                        width={ 300 }
                    />
                </Table>
            </div>
        ) : (
            <div>Oooops, no rating yet</div>
        );
    }
}

export const RatingTable = RatingTableHandler;
