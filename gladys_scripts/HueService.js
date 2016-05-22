/** 
  * Gladys Project
  * http://gladysproject.com
  * Software under licence Creative Commons 3.0 France 
  * http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
  * You may not use this software for commercial purposes.
  * @author : Mickaël Forgh
  */
  
  
var hue, HueApi, lightState, host, usernmae, api, state;

if(sails.config.machine.soundCapable){
	var hue = require("node-hue-api"),
    HueApi = hue.HueApi,
    lightState = hue.lightState;
	
	var host = "192.168.1.5",
    username = "QCRzu0Ozcu8JNHKd5gtfV2Kmidjh7A57SDyTR6AW",
    api = new HueApi(host, username),
    state = lightState.create();
}

module.exports = {
	


	/**
	 * Switch on the lights
	 * @method switchOn
	 * @return 
	 */
	switchOn:function(){
		api.lights(function(err, results) {
			state = lightState.create().turnOn();
			if (err) throw err;
			for (var i = 0; i < results.lights.length; i++) { 
				api.setLightState(results.lights[i].id, state, function(err, result) {
					if (err) throw err;
					//displayResult(result);
				});
			}
		});
	},

	/**
	 * Switch off the lights
	 * @method switchOff
	 * @return 
	 */
	switchOff:function(){
		state = lightState.create().turnOff();
		api.lights(function(err, results) {
			if (err) throw err;
			for (var i = 0; i < results.lights.length; i++) { 
				api.setLightState(results.lights[i].id, state, function(err, result) {
					if (err) throw err;
					//displayResult(result);
				});
			}
		});
	},

	/**
	 * Toggle the lights
	 * @method toggle
	 * @return 
	 */
	toggle:function(){
		//We create a state for the lamps
		state = lightState.create();
		api.lights(function(err, results) {
			if (err) throw err;
			//for every lights connected to the bridge
			for (var i = 0; i < results.lights.length; i++) { 
				//we invert its current state
				api.setLightState(results.lights[i].id, state.on(!results.lights[i].state.on), function(err, result) {
					if (err) throw err;
				});
			}
		});
	},
	
	/**
	 * Set light state to on with the chosen color
	 * The color is converted from hex to rgb
	 * @method setColor
	 * @return 
	 */
	setColor:function(color){
		var bigint = parseInt(color, 16);
		var r = (bigint >> 16) & 255;
		var g = (bigint >> 8) & 255;
		var b = bigint & 255;
		state = lightState.create().on().rgb(r, g, b);
		api.lights(function(err, results) {
			if (err) throw err;
			for (var i = 0; i < results.lights.length; i++) { 
				
				api.setLightState(results.lights[i].id, state, function(err, result) {
					if (err) throw err;
					//displayResult(result);
				});
			}
		});
	},
	
	/**
	 * Set light state to on with the chosen brightness
	 * @method setBrightness
	 * @return 
	 */
	setBrightness:function(bri){

		state = lightState.create().on().brightness(bri);
		api.lights(function(err, results) {
			if (err) throw err;
			for (var i = 0; i < results.lights.length; i++) { 
				api.setLightState(results.lights[i].id, state, function(err, result) {
					if (err) throw err;
					//displayResult(result);
				});
			}
		});
	},

	/**
	 * Get states of the lamps
	 * @method states
	 * @return 
	 */
	states:function(){
		return api.lights(function(err, results) {
			if (err) throw err;
			return results;
		});
	},
};
