/***
 * Define an object constructor for players.
 * 
 * @param name Player's name
 * @param id Player's id
 * @param money Player's money
 * @param cards Player's cards
 * @param startPos Player's start position
 * @returns {player} An object represents the player
 */

function Player(name, id, money, cards, startPos) {
	this.name = name; // Player's name
	this.id = id; // Player's id
	this.money = money; // Player's money
	this.cards = cards; // Player's cards
	this.currentPos = startPos; // Player's current position
	this.bankruptcy = false;
	this.houses = new Array();
	this.updateHouse = updateHouse;
	this.removeCard = removeCard;
	this.getLevel = getLevel;
}

function getLevel(index) {
	var level = -1;
	for(var i = 0; i < this.houses.length; i++) {
		if (this.houses[i].index == index) {
			level = this.houses[i].level;
			break;
		}
	}
	return level;
}

/***
 * Update a specific house condition at some place
 * 
 * @param index The index of the house to be updated
 * @param level Level of the house
 */
function updateHouse(index, level) {
	var exist = false;
	for(var i = 0; i < this.houses.length; i++) {
		if (this.houses[i].index == index) {
			exist = true;
			this.houses[i].level = level;
		}
	}
	if (!exist) {
		this.houses.push(new House(index, level));
	}
}

/***
 * Remove a specific card from the player
 * 
 * @param cardIndex The index of the card to be removed
 */
function removeCard(cardIndex) {
	this.cards.splice(cardIndex, 1);
}