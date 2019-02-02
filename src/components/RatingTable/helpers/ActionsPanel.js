// vendor
import React, { Component } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
    faArrowUp,
    faArrowRight,
    faAnchor,
    faLightbulb,
} from '@fortawesome/free-solid-svg-icons';
import _ from 'lodash';

// own
import Styles from './styles.module.css';

export default class ActionsPanel extends Component {
    _getTitle() {
        if (this.props.isExiting) {
            return 'Відбувається вихід з кімнати...';
        } else if (this.props.isJoining) {
            return 'Відбувається підключення до кімнати...';
        } else if (this.props.isRoomExited) {
            return 'Вихід з кімнати здійснено!';
        } else if (this.props.isRoomJoined) {
            return 'Підключення до кімнати здійснено';
        } else if (this.props.ownIndex === -1) {
            return 'У Вас відсутня ігрова кімната';
        }

        return 'Ви знаходитеся в ігровій кімнаті';
    }

    _getLampStyle() {
        if (this.props.isExiting || this.props.isJoining) {
            return Styles.actionInProgress;
        } else if (this.props.isRoomExited) {
            return Styles.exitedRoomRequest;
        } else if (this.props.isRoomJoined) {
            return Styles.joinedRoomRequest;
        } else if (this.props.ownIndex === -1) {
            return Styles.exitedRoom;
        }

        return Styles.joinedRoom;
    }

    _renderToTopButton() {
        const { rating, setParticipant, scrollToPosition } = this.props;

        return (
            !!rating.length && (
                <FontAwesomeIcon
                    title='Показати лідерів'
                    onClick={ () => {
                        setParticipant(_.first(rating));
                        scrollToPosition(0);
                    } }
                    className={ Styles.toTop }
                    icon={ faArrowUp }
                />
            )
        );
    }

    _renderToMyPositionButton() {
        const {
            rating,
            ownIndex,
            setParticipant,
            scrollToPosition,
        } = this.props;

        return (
            ownIndex !== -1 && (
                <FontAwesomeIcon
                    title='До моєї позиції'
                    onClick={ () => {
                        setParticipant(rating[ ownIndex ]);
                        scrollToPosition(ownIndex);
                    } }
                    className={ Styles.toMyPosition }
                    icon={ faArrowRight }
                />
            )
        );
    }

    _isDisabled() {
        const { isJoining, isExiting, isRoomExited, isRoomJoined } = this.props;

        return isJoining || isExiting || isRoomExited || isRoomJoined;
    }

    render() {
        const {
            ownIndex,
            selectedIndex,
            watchPosition,
            id,
            active,

            setWatchPosition,
            scrollToPosition,
            exitRoom,
            joinRoom,
        } = this.props;

        return (
            <div className={ Styles.participantHeader }>
                Учасник
                { this._renderToMyPositionButton() }
                { this._renderToTopButton() }
                <FontAwesomeIcon
                    title={
                        watchPosition
                            ? 'Слідкувати за позицією'
                            : 'Вільно переглядати рейтинг'
                    }
                    onClick={ () => {
                        setWatchPosition(!watchPosition);
                        if (!watchPosition) {
                            scrollToPosition(selectedIndex);
                        }
                    } }
                    className={
                        watchPosition ? Styles.watchPosition : Styles.freeMove
                    }
                    icon={ faAnchor }
                />
                { id && active && (
                    <FontAwesomeIcon
                        icon={ faLightbulb }
                        title={ this._getTitle() }
                        className={ this._getLampStyle() }
                        onClick={
                            this._isDisabled()
                                ? void 0
                                : ownIndex === -1
                                    ? joinRoom
                                    : exitRoom
                        }
                    />
                ) }
            </div>
        );
    }
}
