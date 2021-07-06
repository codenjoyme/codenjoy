var ConsoleLogger = function(){
    return {
        log: function(message) {
                console.log(message);
        }, 
        logBoard: function(board){
           console.log("Board:");
           console.log(board.toString())
           console.log();
        },
        logCommand: function(command){
            console.log(command ? command.toString(): " ");
        }
    }
}

if (module) module.exports = ConsoleLogger;