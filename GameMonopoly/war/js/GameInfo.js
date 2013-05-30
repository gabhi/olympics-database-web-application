/*******************************************************************************
 * Define all constants of the game.
 */

var IMAGES = new Array("image_1.jpg", "image_2.jpg", "image_3.jpg",
		"image_4.jpg"); // All player's images
var GRID_LENGTH = 20; // Length of a grid
var GRID_WIDTH = 20; // Width of a grid
var GRID_NUMBER = 30; // Total number of grids

var MAP = new Array(new Grid(0, 0, "land", 0), new Grid(1, 1, "land", 0),
		new Grid(2, 2, "land", 0), new Grid(3, 3, "land", 0), new Grid(4, 4,
				"land", 0), new Grid(5, 5, "land", 0),
		new Grid(6, 6, "land", 0), new Grid(7, 7, "land", 0), new Grid(8, 8,
				"land", 0), new Grid(9, 9, "stop", 0), new Grid(10, 10, "land",
				0), new Grid(11, 11, "money", 0), new Grid(12, 12, "land", 0),
		new Grid(13, 13, "land", 0), new Grid(14, 14, "bomb", 0), new Grid(15,
				15, "land", 0), new Grid(16, 16, "land", 0), new Grid(17, 17,
				"land", 0), new Grid(18, 18, "land", 0), new Grid(19, 19,
				"land", 0), new Grid(20, 20, "money", 0), new Grid(21, 21,
				"land", 0), new Grid(22, 22, "land", 0), new Grid(23, 23,
				"bomb", 0), new Grid(24, 24, "money", 0), new Grid(25, 25,
				"land", 0), new Grid(26, 26, "land", 0), new Grid(27, 27,
				"bomb", 0), new Grid(28, 28, "land", 0), new Grid(29, 29,
				"land", 0), new Grid(30, 30, "land", 0), new Grid(31, 31,
				"stop", 0), new Grid(32, 32, "land", 0), new Grid(33, 33,
				"land", 0), new Grid(34, 34, "land", 0), new Grid(35, 35,
				"land", 0), new Grid(36, 36, "bomb", 0), new Grid(37, 37,
				"money", 0), new Grid(38, 38, "land", 0), new Grid(39, 39,
				"land", 0), new Grid(40, 0, "land", 0), new Grid(41, 1, "land",
				0), new Grid(42, 2, "land", 0), new Grid(43, 3, "land", 0),
		new Grid(44, 4, "land", 0), new Grid(45, 5, "land", 0), new Grid(46, 6,
				"land", 0), new Grid(47, 40, "land", 0), new Grid(48, 41,
				"land", 0), new Grid(49, 42, "money", 0), new Grid(50, 43,
				"land", 0), new Grid(51, 44, "land", 0), new Grid(52, 45,
				"land", 0), new Grid(53, 46, "land", 0), new Grid(54, 13,
				"land", 0), new Grid(55, 14, "bomb", 0), new Grid(56, 15,
				"land", 0), new Grid(57, 16, "land", 0), new Grid(58, 17,
				"land", 0), new Grid(59, 18, "land", 0), new Grid(60, 19,
				"land", 0), new Grid(61, 20, "money", 0), new Grid(62, 21,
				"land", 0), new Grid(63, 22, "land", 0), new Grid(64, 23,
				"bomb", 0), new Grid(65, 24, "money", 0), new Grid(66, 25,
				"land", 0), new Grid(67, 26, "land", 0), new Grid(68, 27,
				"bomb", 0), new Grid(69, 28, "land", 0), new Grid(70, 29,
				"land", 0), new Grid(71, 30, "land", 0), new Grid(72, 31,
				"stop", 0), new Grid(73, 32, "land", 0), new Grid(74, 33,
				"land", 0), new Grid(75, 34, "land", 0), new Grid(76, 35,
				"land", 0), new Grid(77, 36, "bomb", 0), new Grid(78, 37,
				"money", 0), new Grid(79, 38, "land", 0), new Grid(80, 39,
				"land", 0));

var moveX = [ '60px', '105px', '45px', '80px', '45px', '95px', '70px', '70px',
		'100px', '30px', '80px', '65px', '50px', '60px', '25px', '-70px',
		'-80px', '20px', '70px', '-30px', '-70px', '-60px', '-50px', '-70px',
		'-70px', '-50px', '-30px', '0px', '-60px', '-70px', '-70px', '-80px',
		'-50px', '0px', '-70px', '15px', '5px', '-30px', '-50px', '-30px',
		'60px', '105px', '45px', '80px', '45px', '95px', '65px', '45px',
		'40px', '50px', '70px', '75px', '65px', '55px', '60px', '25px',
		'-70px', '-80px', '20px', '70px', '-30px', '-70px', '-60px', '-50px',
		'-70px', '-70px', '-50px', '-30px', '0px', '-60px', '-70px', '-70px',
		'-80px', '-50px', '0px', '-70px', '15px', '5px', '-30px', '-50px',
		'-30px' ];

var moveY = [ '-40px', '-35px', '20px', '55px', '30px', '-40px', '-35px',
		'-40px', '-55px', '20px', '40px', '35px', '50px', '45px', '40px',
		'30px', '40px', '50px', '60px', '40px', '45px', '-5px', '-50px', '0px',
		'40px', '-20px', '-50px', '-60px', '-20px', '40px', '40px', '40px',
		'-10px', '-60px', '-50px', '-50px', '-40px', '-30px', '-40px', '-30px',
		'-40px', '-35px', '20px', '55px', '30px', '-40px', '30px', '40px',
		'-10px', '-30px', '-40px', '30px', '10px', '-15px', '45px', '40px',
		'30px', '40px', '50px', '60px', '40px', '45px', '-5px', '-50px', '0px',
		'40px', '-20px', '-50px', '-60px', '-20px', '40px', '40px', '40px',
		'-10px', '-60px', '-50px', '-50px', '-40px', '-30px', '-40px', '-30px' ];

var houseX = [ '30px', '105px', '180px', '318px', '370px', '330px', '445px',// 7
'505px', '575px', 'stop', '813px', 'money', '930px', '985px', 'bomb',// 15
'1055px', '985px', '853px', '850px', '1015px', 'money', '840px',// 22
'770px', 'bomb', 'money', '655px', '510px', 'bomb', '590px', '455px',// 30
'380px', 'stop', '295px', '157px', '236px', '78px', 'bomb', 'money',// 38
'65px', '10px', '30px', '105px', '180px', '318px', '370px', '330px',// 46
'445px', '480px', '539px', 'money', '735px', '725px', '850px', '912px',// 54
'985px', 'bomb', '1055px', '985px', '853px', '850px', '1015px', 'money',
		'840px', '770px', 'bomb', 'money', '655px', '510px', 'bomb', '590px',
		'455px', '380px', 'stop', '295px', '157px', '236px', '78px', 'bomb',
		'money', '65px', '10px' ];

var houseY = [ '160px', '120px', '80px', '105px', '150px', '245px', '145px',// 7
'113px', '70px', 'stop', '35px', 'money', '120px', '170px', 'bomb',// 15
'325px', '360px', '320px', '430px', '440px', 'money', '505px', '590px',// 23
'bomb', 'money', '575px', '540px', 'bomb', '395px', '355px', '390px',// 31
'stop', '550px', '528px', '414px', '405px', 'bomb', 'money', '298px',// 39
'257px', '160px', '120px', '80px', '105px', '150px', '245px', '145px',
'247px', '295px', 'money', '250px', '155px', '182px', '252px', '170px',
'bomb', '325px', '360px', '320px', '430px', '440px', 'money', '505px', '590px',
		'bomb', 'money', '575px', '540px', 'bomb', '395px', '355px', '390px',
		'stop', '550px', '528px', '414px', '405px', 'bomb', 'money', '298px',
		'257px' ];

var index = [ 0, 10, 19, 32 ];