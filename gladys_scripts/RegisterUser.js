var HueApi = require("node-hue-api").HueApi;
 
var hostname = "192.168.1.5",
    userDescription = "Gladys";
 
var displayUserResult = function(result) {
    console.log("Created user: " + JSON.stringify(result));
};
 
var displayError = function(err) {
    console.log(err);
};

console.log("Start");
 
var hue = new HueApi();
 

// -------------------------- 
// Using a callback (with default description and auto generated username) 
hue.createUser(hostname, function(err, user) {
console.log("Hi");	
if (err) throw err;
else {
	console.log("YASS");
	displayUserResult(user);
}
});
