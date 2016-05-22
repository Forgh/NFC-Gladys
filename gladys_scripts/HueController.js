
/**
 * HueController
 *
 * @description :: Server-side logic for managing Philips Hue Lamps
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
	/**
	 * Index 
	 * List lamps
	 * @method index
	 * @param {} req
	 * @param {} res
	 * @param {} next
	 * @return 
	 */
	index:function(req, res, next){
		HueService.states();
		res.json('ok');
	},
	
	/**
	 * switch on the lamps
	 * @method switchOn
	 * @param {} req
	 * @param {} res
	 * @param {} next
	 * @return 
	 */
	switchOn: function(req,res,next){
		HueService.switchOn();
		res.json('ok');
	},
	
	/**
	 * switch off the lamps
	 * @method switchOff
	 * @param {} req
	 * @param {} res
	 * @param {} next
	 * @return 
	 */
	switchOff: function(req,res,next){
		HueService.switchOff();
		res.json('ok');
	},
	
	/**
	 * toggle the lamps
	 * @method toggle
	 * @param {} req
	 * @param {} res
	 * @param {} next
	 * @return 
	 */
	toggle: function(req,res,next){
		HueService.toggle();
		res.json('ok');
	},
	
	/**
	 * set color of the lamps
	 * @method setColor
	 * @param {} req
	 * @param {} res
	 * @param {} next
	 * @return 
	 */
	setColor: function(req,res,next){
		if(!req.param('color'))
			return res.json('Missing parametres');
		var color = req.param("color");
		HueService.setColor(color);
		res.json('ok');
	},
	
	/**
	 * set the brightness of the lamps
	 * @method setBrightness
	 * @param {} req
	 * @param {} res
	 * @param {} next
	 * @return 
	 */
	setBrightness: function(req,res,next){
		if(!req.param('bri'))
			return res.json('Missing parametres');
		var brightness = req.param('bri');
		HueService.setBrightness(brightness);
		res.json('ok');
	}
};