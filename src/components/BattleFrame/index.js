// vendor
import React, { Component } from 'react';

import Styles from './styles.module.css';

class BattleFrameHandler extends Component {
    render() {
        const { participant: { email, server } = {} } = this.props;

        return email && server ? (
            <div className={ Styles.ratioWrapper }>
                <div className={ Styles.ratioSquare }>
                    <iframe
                        className={ Styles.ratioInner }
                        src={
                            'http://www.youtube.com/embed/xDMP3i36naA' ||
                            `http://${server}/codenjoy-contest/board/player/${email}?only=true`
                        }
                    />
                </div>
            </div>
        ) : null;
    }
}

export const BattleFrame = BattleFrameHandler;
