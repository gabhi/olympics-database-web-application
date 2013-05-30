/*******************************************************************************
 * Parse a request string and get the certain parameter value.
 * 
 * @param paras
 *            Parameter name to be fetched
 * @returns Parameter value
 */
function request(paras)
{
	var url = location.href; 
	var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&"); 
	var paraObj = {};
	for (i=0; j=paraString[i]; i++) {
		paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length); 
	}
	var returnValue = paraObj[paras.toLowerCase()];
	if (typeof(returnValue) == "undefined") { 
		return ""; 
	} else {
		return returnValue; 
	}
}

/*******************************************************************************
 * Parses a XML string into an XML DOM object.
 * 
 * @param XmlString
 *            A XML string
 * @returns A XML DOM object
 */
function xmlParse(XmlString) {
	xmlDoc = $.parseXML(XmlString);
	return $(xmlDoc);
}

/*******************************************************************************
 * Parse a XML string represents some player's information in to a Player object
 * 
 * @param player
 *            A XML string represents a player's information
 * @returns A Player object holds all his infomation
 */
function playerParse(player) {
	var name = player.find("player_name").text();
	var id = player.find("player_id").text();
	var money = parseInt(player.find("start_money").text());

	var cards = new Array();
	player.find("athlete").each(function(index) {
		cards.push(cardParse($(this)));
	});
	var currentPos = parseInt(player.find("start_position").text());
	return (new Player(name, id, money, cards, currentPos));
}

/*******************************************************************************
 * Parse a XML string represents an athlete card in to a Card object
 * 
 * @param card
 *            A XML string represents an athlete's card
 * @returns A Card object holds all the athlete's information
 */
function cardParse(card) {
	var name = card.find("athlete_name").text();
	var id = card.find("athlete_id").text();
	var gold = card.find("gold_num").text();
	var sport = card.find("sport").text();
	var country = card.find("country").text();
	var continent = card.find("continent").text();
	return (new Card(name, id, gold, sport, country, continent));
}

/*******************************************************************************
 * Mapping from place index to place position
 * 
 * @param map
 *            A map represents many grids
 * @param index
 *            Place index (0 - 80)
 * @returns Place position (0 - 46)
 */
function indexToPosition(map, index) {
	return map[index].position;
}

/*******************************************************************************
 * Mapping from place position to place index
 * 
 * @param map
 *            A map represents many grids
 * @param position
 *            Place position (0 - 46)
 * @param currentIndex
 *            Current place index (0 - 80)
 * @returns Place index (0 - 80)
 */
function positionToIndex(map, position, currentIndex) {
	var index = 0;
	for (var i = 1; i <= 6; i++) {
		if(map[(currentIndex + i) % 81].position == position) {
			index = (currentIndex + i) % 81;
			break;
		}
	}
	return index;
}

/*******************************************************************************
 * 
 * @param message
 */
function setMessage(message, color) {
	$("#history").append('<message style="color:' + color + '">' + message + '</message><br />');
}

/*******************************************************************************
 * Parse an athlete name into a more readable form
 * The first letter in his/her first name and the whole last name
 * 
 * @param name
 * @returns {String}
 */
function parseName(name){
	var words = name.split(' ');
	return (words[0].substr(0,1) + '. ' + words[words.length-1]);
}
