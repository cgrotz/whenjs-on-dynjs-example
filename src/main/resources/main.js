var when = require("when.js");
function operation (message) {
	var deferred = when.defer();
	setTimeout(function(){
		deferred.resolve(message);
	},1000);
	return deferred.promise;
}

operation('Hello World').then(
	function gotIt(img) {
		console.log(img); 
	}
);