import icancode from './icancode/index';
import bomberman from './bomberman/index';

const result = function(){
    switch (process.env.REACT_APP_GAME) {
        case 'icancode' : return icancode;
        case 'bomberman' : return bomberman;
    }
}();

export default result;
