// vendor
import React, { Component } from 'react';
import Iframe from 'react-iframe';

class BattleFrameHandler extends Component {
    render() {
        const { participant: { email, server } = {} } = this.props;

        return email && server ? (
            <Iframe
                url={ `http://${server}/codenjoy-contest/board/player/${email}?only=true` }
                width='400px'
                height='400px'
                //className={ }
                id='myId'
                display='initial'
                position='relative'
                allowFullScreen
            />
        ) : null;
    }
}

export const BattleFrame = BattleFrameHandler;
