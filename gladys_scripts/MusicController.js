/** 
  * Gladys Project
  * http://gladysproject.com
  * Software under licence Creative Commons 3.0 France 
  * http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
  * You may not use this software for commercial purposes.
  * @author :: Pierre-Gilles Leymarie
  */
  
/**
 * MusicController
 *
 * @description :: Server-side logic for managing Musics
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {

	index:function(req, res,next){
		Music.find()
			.exec(function(err, musics){
				if(err) res.json(err);

				res.json(musics);
			});
	},
	play: function(req, res,next){
		MusicService.play('kawaiistep.mp3');
		res.json('ok');

	},
	
	pause: function(req, res,next){
		MusicService.pause();
		res.json('ok');

	},
	
	stop: function(req, res,next){
		MusicService.stop();
		res.json('ok');

	}
};