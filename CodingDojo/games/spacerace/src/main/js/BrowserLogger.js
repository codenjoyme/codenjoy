var module;

var BrowserLogger = function(ElementsClass, isUiEnabled = false){
    var t1 = undefined;
    var t2 = undefined;

    var storage = window.localStorage;
    var isLogging = storage.getItem("isLogging") == "true";
    var isScrolling = storage.getItem("isScrolling") == "true";
    
    var directionBtns = undefined;
    var actBtn = undefined; 
    
    var initBtns = function(){
        actBtn = document.getElementById("act");
        directionBtns =      {
            2: document.getElementById("up"),
            3: document.getElementById("down"),
            0: document.getElementById("left"),
            1: document.getElementById("right")
         }
     
    }
    
    var updateStorage=function(){
        storage.setItem("isLogging", isLogging);
        storage.setItem("isScrolling", isScrolling);
    }

    var logInternal = function(data){
        if(!isLogging) return;
        var textarea = document.getElementById("log-area");
        if (!textarea) return;
        var val = textarea.value;
        if(val) {
            textarea.value = val + '\n' + data;
        } else {
            textarea.value = data;
        }
        if(isScrolling) {
            textarea.scrollTop = textarea.scrollHeight;
        }
    }
    
    var clearDirections= function(){
       if(!actBtn) initBtns(); 
       actBtn.className = "inactive";
       var keys = Object.keys(directionBtns);
       for(id in keys){
           directionBtns[id].className = "inactive";
       }
    }

    var clearCycleLog = function(){
        var textarea = document.getElementById("log-area-local");
        if (!textarea) return;
        textarea.value = "";
    }

    var classNames = function(){
        var results = [];
        var keys = Object.keys(ElementsClass);
        for(k in keys){
            var key = keys[k];
            results[ElementsClass[key]] = key;
        }
        return results;
    }();
      
    function getClassNameByElement(element){
         return classNames[element];
    }

    var formBoard = function(board){
        var table = document.createElement('table');
        table.id = "table-game";
        for (var y = board.size - 1; y >=0; y--) {
            var row = document.createElement('tr');
            for (var x = 0; x < board.size; x++) {
                var cell = document.createElement('td');
                cell.className = getClassNameByElement(board.getAt(x,y));
                row.appendChild(cell);
            }
            table.appendChild(row);
        }
        var tableDiv = document.getElementById('board-table');
        var tableItself = document.getElementById('table-game');
    
        tableItself ?
            tableDiv.replaceChild(table, tableItself) :
            tableDiv.appendChild(table);
    }
    
    var formDirection = function(direction){
        if(!direction) return;
        if(direction.isAction){
            actBtn.className ="active";
        }
        var btn = directionBtns[direction.id];
        if(btn){
            btn.className ="active";
        }
        if(t1 && t2){
           var dmts = document.getElementById("dmt");
           if(dmts){
               dmts.innerHTML = (t2-t1) + "ms"
           }
        }
    }

    var logToCycleLog = function(message){
        var textarea = document.getElementById("log-area-local");
        if (!textarea) return;
        var val = textarea.value;
        if(val) {
            textarea.value = val + '\n' + message;
        } else {
            textarea.value = message;
        }
    }

    return {
        log: function(message) {
            logInternal(message);
            if(isUiEnabled){
                logToCycleLog(message);
            }
        },

        logBoard: function(board){
           logInternal("Board:");
           logInternal(board.toString())
           logInternal("");
           if(isUiEnabled) {
               t1 = Date.now();
               clearCycleLog();
               clearDirections();
               formBoard(board);
           }
        },

        logCommand: function(command){
           logInternal(command ? command.toString(): " ");
           if(isUiEnabled){
               t2 = Date.now();
               formDirection(command);
           }
        },

        getIsLogging: function(){return isLogging;},
        setIsLogging: function(value){isLogging = !!value; updateStorage();},
        getIsScrolling: function(){return isScrolling;},
        setIsScrolling: function(value){isScrolling = !!value; updateStorage();},
        init: function(){
            initBtns();
        }
    }
}