import BoardContainer from './BoardContainer';
import LoginContainer from './LoginContainer';
import RegisterContainer from './RegisterContainer';
import HomeContainer from './HomeContainer';
import PrivacyPolicyContainer from './PrivacyPolicyContainer';
import PrivacyRulesContainer from './PrivacyRulesContainer';
import UnavailableContainer from './UnavailableContainer';
import ForgotPasswordContainer from './ForgotPasswordContainer';
import RegisterConfirmContainer from './RegisterConfirmContainer';

import RulesContainerICanCode from './RulesContainer/icancode';
import RulesContainerBomberman from './RulesContainer/bomberman';

const RulesContainer = function getRulesContainer() {
    switch (process.env.REACT_APP_GAME) {
        case 'icancode' :
            return RulesContainerICanCode;
        case 'bomberman' :
            return RulesContainerBomberman;
    }
}();

export {
    BoardContainer,
    LoginContainer,
    RegisterContainer,
    RulesContainer,
    HomeContainer,
    PrivacyPolicyContainer,
    PrivacyRulesContainer,
    UnavailableContainer,
    ForgotPasswordContainer,
    RegisterConfirmContainer,
};
