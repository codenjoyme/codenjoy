// vendor
import React, { Component } from 'react';
import Modal from 'react-modal';

// proj
import { PrivacyPolicyContainer } from '../../containers';

// own
import Styles from './styles.module.css';

Modal.setAppElement('#root');

export class PrivacyPolicyModal extends Component {
    render() {
        const { isOpen, setVisible, action } = this.props;

        return (
            <Modal
                isOpen={ isOpen }
                contentLabel='Політика конфіденційності'
                className={ Styles.modalContent }
                overlayClassName={ Styles.modalOverlay }
                onRequestClose={ () => setVisible(false) }
            >
                <div className={ Styles.privacyModalContainer }>
                    <PrivacyPolicyContainer />

                    <div className={ Styles.privacyFooter }>
                        <div className={ Styles.buttonsPanel }>
                            <button
                                className={ Styles.agree }
                                onClick={ () => {
                                    action(true);
                                    setVisible(false);
                                } }
                            >
                                Погоджуюсь
                            </button>
                            <div className={ Styles.emptyBlock } />
                            <button
                                className={ Styles.disagree }
                                onClick={ () => {
                                    action(false);
                                    setVisible(false);
                                } }
                            >
                                Не згоден
                            </button>
                        </div>
                    </div>
                </div>
            </Modal>
        );
    }
}
