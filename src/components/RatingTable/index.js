// vendor
import React, { Component } from 'react';
import Table from 'rc-table';

import Styles from './styles.module.css';

const columns = [
    {
        title:     '#',
        dataIndex: 'index',
        render:    (value, row, index) => index + 1,
    },
    {
        title:     'Name',
        dataIndex: 'name',
        key:       'name',
    },
    {
        title:     'Score',
        dataIndex: 'score',
        key:       'age',
    },
    {
        title:     'Operations',
        dataIndex: '',
        key:       'operations',
        render:    () => 'View',
    },
];

class RatingTableHandler extends Component {
    render() {
        const { rating } = this.props;

        return rating ? (
            <div className={ Styles.rating }>
                <Table columns={ columns } data={ rating } />
            </div>
        ) : (
            <div>Oooops, no rating yet</div>
        );
    }
}

export const RatingTable = RatingTableHandler;
