// vendor
import React, { Component } from 'react';

// proj
import { getIFrameLink } from '../../utils';

// own
import Styles from './styles.module.css';

class BattleFrameHandler extends Component {
    render() {
        const { participant: { id, server } = {} } = this.props;

        return id && server ? (
            <div className={ Styles.ratioWrapper }>
                <div className={ Styles.ratioSquare }>
                    <iframe
                        title='Ігрове поле'
                        scrolling='no'
                        className={ Styles.ratioInner }
                        src={ getIFrameLink(server, id) }
                    />
                </div>
            </div>
        ) : null;
    }
}

export const BattleFrame = BattleFrameHandler;
