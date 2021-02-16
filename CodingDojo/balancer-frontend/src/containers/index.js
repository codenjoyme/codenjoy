import BoardContainer from './BoardContainer';
import LoginContainer from './LoginContainer';
import RegisterContainer from './RegisterContainer';
import HomeContainer from './HomeContainer';
import PrivacyPolicyContainer from './PrivacyPolicyContainer';
import PrivacyRulesContainer from './PrivacyRulesContainer';
import UnavailableContainer from './UnavailableContainer';
import ForgotPasswordContainer from './ForgotPasswordContainer';
import RegisterConfirmContainer from './RegisterConfirmContainer';

import RulesContainer_icancode from './RulesContainer/icancode';
import RulesContainer_bomberman from './RulesContainer/bomberman';

const RulesContainer = function() {
    switch (process.env.REACT_APP_GAME) {
        case 'icancode' : return RulesContainer_icancode;
        case 'bomberman' : return RulesContainer_bomberman;
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
