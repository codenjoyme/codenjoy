import icancode from './icancode/index';
import bomberman from './bomberman/index';
import battlecity from './battlecity/index';

const result = function(){
    switch (process.env.REACT_APP_GAME) {
        case 'icancode' : return icancode;
        case 'bomberman' : return bomberman;
        case 'battlecity' : return battlecity;
        default : Error('Wrong or missed param REACT_APP_GAME');
    }
}();

export default result;
