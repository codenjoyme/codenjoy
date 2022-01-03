import icancode from './icancode/index';
import mollymage from './mollymage/index';
import rawelbbub from './rawelbbub/index';

const result = function(){
    switch (process.env.REACT_APP_GAME) {
        case 'icancode' : return icancode;
        case 'mollymage' : return mollymage;
        case 'rawelbbub' : return rawelbbub;
        default : Error('Wrong or missed param REACT_APP_GAME');
    }
}();

export default result;
