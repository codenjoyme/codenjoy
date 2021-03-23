var Stuff = module.exports = {
    random : function(n){
        return Math.floor(Math.random()*n);
    },

    printArray : function(array) {
        var result = [];
        for (var index in array) {
            var element = array[index];
            result.push(element.toString());
        }
        return "[" + result + "]";
    },

    clean : function(string) {
        if (typeof string.replaceAll == 'undefined') {
            // case node
            return string.replace('.js', '')
                        .replace('.', '')
                        .replace('/', '');

        } else {
            // case browser stub
            return string.replaceAll('.js', '')
                        .replaceAll('.', '')
                        .replaceAll('/', '');

        }
    }
}
