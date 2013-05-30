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

function Card(name, id, gold, sport, country, continent) {
	this.name = name; // Athlete's name
	this.id = id; // Athlete's id
	this.gold = gold; // Number of athlete's gold medals
	this.sport = sport;
	this.country = country; // Athlete's country
	this.continent = continent; // Country's continent
	this.type = "athlete";
}
