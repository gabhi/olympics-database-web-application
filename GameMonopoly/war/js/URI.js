/*******************************************************************************
 * All related URLs for ajax.
 */

var BASE_URI = "MonoResponse?";

function requestMove(place_id) {
	return BASE_URI + "action=query_place&place_id=" + place_id;
}

function requestNoPurchase(player_id, current_pos) {
	return BASE_URI + "action=no_action&player_id=" + player_id
			+ "&current_pos=" + current_pos;
}

function requestPurchase(player_id, place_id, athlete_id) {
	return BASE_URI + "action=purchase&player_id=" + player_id + "&place_id="
			+ place_id + "&athlete_id=" + athlete_id;
}

function requestUpgrade(player_id, place_id, athlete_id) {
	return BASE_URI + "action=upgrade&player_id=" + player_id + "&place_id="
			+ place_id + "&athlete_id=" + athlete_id;
}

function requestPay(pay_from, pay_to, actual_payment, place_id) {
	return BASE_URI + "action=pay&pay_from=" + pay_from + "&pay_to=" + pay_to
			+ "&actual_payment=" + actual_payment + "&place_id=" + place_id;
}

function requestUpdate(player_id) {
	return BASE_URI + "action=timely_update&player_id=" + player_id;
}

function requestRegister(username) {
	return BASE_URI + "action=register&username=" + username;
}

function requestInitialization() {
	return BASE_URI + "action=initialize";
}

function requestWaiting() {
	return BASE_URI + "action=waiting";
}

function requestChangeMoney(player_id, place_id, current_money) {
	return BASE_URI + "action=money_change&player_id=" + player_id
			+ "&place_id=" + place_id + "&current_money=" + current_money;
}

function requestQuit() {
	return BASE_URI + "action=quit";
}