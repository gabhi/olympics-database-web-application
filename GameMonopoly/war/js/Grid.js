/*******************************************************************************
 * Definition of a grid with its index, x&y coordination, function, nation,
 * continent, owner and cost.
 * 
 * @param position
 *            Position of the grid
 * @param index
 *            Index of the grid
 * @param cost
 *            Cost of the grid
 * @returns {grid} An object represents a grid
 */

function Grid(index, position, func, cost) {
	this.index = index; // Index of the grid
	this.position = position; // Position of the grid
	this.func = func; // Function of the grid
	this.owner = null; // Owner of the grid
	this.cost = cost; // Cost of the grid
}