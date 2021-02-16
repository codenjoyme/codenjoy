// vendor
import React, { Component } from 'react';
import _ from 'lodash';

// proj
import { getIFrameLink } from '../../utils';

import battleComplete_icancode from '../../styles/images/layout/icancode/battleComplete.jpg';
import battleComplete_bomberman from '../../styles/images/layout/bomberman/battleComplete.jpg';

// own
import Styles from './styles.module.css';

const battleComplete = function(){
    switch (process.env.REACT_APP_GAME) {
        case 'icancode' : return battleComplete_icancode;
        case 'bomberman' : return battleComplete_bomberman;
    }
}();

class BattleFrameHandler extends Component {
    render() {
        const { participant, battleCompleted } = this.props;
        const id = _.get(participant, 'id');
        const server = _.get(participant, 'server');

        return (
            <div className={ Styles.ratioWrapper }>
                <div className={ Styles.ratioSquare }>
                    { id && server ? (
                        <iframe
                            title='Ігрове поле'
                            scrolling='no'
                            className={ Styles.ratioInner }
                            src={ getIFrameLink(server, id) }
                        />
                    ) :
                        battleCompleted && (
                            <img
                                className={ Styles.ratioInner }
                                src={ battleComplete }
                                alt=' Битва закінчена'
                            />
                        )
                    }
                </div>
            </div>
        );
    }
}

export const BattleFrame = BattleFrameHandler;
