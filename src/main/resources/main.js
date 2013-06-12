var when = require("when.js");
function loadImage (src) {
	var deferred = when.defer();
	setTimeout(function(){
		deferred.resolve(src);
	},1000);
	return deferred.promise;
}

loadImage('http://google.com/favicon.ico').then(
	function gotIt(img) {
		console.log(img); 
	}
).then(
	function shout(img) {
		console.log('see my new ' + img + '?');
	}
);