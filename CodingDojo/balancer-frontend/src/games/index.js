import icancode from './icancode';
import bomberman from './bomberman';

const result = function(){
    switch (process.env.REACT_APP_GAME) {
        case 'icancode' : return icancode;
        case 'bomberman' : return bomberman;
    }
}();

export default result;
