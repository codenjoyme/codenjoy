// vendor
import React, { Component } from 'react';
import Iframe from 'react-iframe';

class BattleFrameHandler extends Component {
    render() {
        // const {
        //     playerName
        // } = this.props;

        return (
            <Iframe
                url='http://www.youtube.com/embed/xDMP3i36naA'
                width='450px'
                height='450px'
                id='myId'
                display='initial'
                position='relative'
                allowFullScreen
            />
        );
    }
}

export const BattleFrame = BattleFrameHandler;
