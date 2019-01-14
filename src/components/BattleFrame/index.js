// vendor
import React, { Component } from 'react';
import Iframe from 'react-iframe';

class BattleFrameHandler extends Component {
    render() {
        const { participant } = this.props;

        return participant ? (
            <div>
                { participant }
                <br />
                <Iframe
                    url='http://www.youtube.com/embed/xDMP3i36naA'
                    width='400px'
                    height='400px'
                    id='myId'
                    display='initial'
                    position='relative'
                    allowFullScreen
                />
            </div>
        ) : null;
    }
}

export const BattleFrame = BattleFrameHandler;
