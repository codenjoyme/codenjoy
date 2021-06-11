class Cell {

    type;
    coordinateX;
    coordinateY;

    /**
     * 
     * @param {string} type
     * @param {int} coordinateX
     * @param {int} coordinateY
     */
    constructor(type, coordinateX, coordinateY) {
        this.type = type;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    /**
     * @param {Cell} otherCell
     * @return {boolean}
     * @constructor
     */
    IsEqualToCell(otherCell){
        if(otherCell.type !== this.type){
            return false
        }
        if(otherCell.coordinateX !== this.coordinateX){
            return false
        }
        if(otherCell.coordinateY !== this.coordinateY){
            return false
        }
        
        return true;
    }
}

module.exports = Cell;