var players = new Array();
var currentTurn = -1;
// var id = $.cookie("id");
var id = request("id");
var totalPlayers = 0;
var cardLength = 0;
var update_timer;
var popUpPlayer = -1;
var rolled = true;
var terminate = false;

$(document).ready(
		function() {
			/*
			 * if (!$.cookie("user")) { alert("Please register username!");
			 * window.location.href = "login.html"; }
			 */
			if (id == "") {
				alert("Please register!");
				window.location.href = "login.html";
			}
			new net.AjaxRequest(requestInitialization(id), initialization,
					onerror, "GET");
		});

function sleep(ms) {
	var dt = new Date();
	dt.setTime(dt.getTime() + ms);
	while (new Date().getTime() < dt.getTime())
		;
}

function countLosers() {
	var losers = 0;
	for ( var i = 0; i < players.length; i++) {
		if (players[i].bankruptcy == true) {
			losers++;
		}
	}
	if (losers >= players.length - 1) {
		clearInterval(update_timer);
		return true;
	}
	return false;
}

function terminateGame() {
	if (players[id].bankruptcy == false) {
		// new net.AjaxRequest(requestQuit(), quitGame, onerror,
		// "GET");
		$("#img" + id).animate({
			left : '+=' + '0px',
			top : '+=' + '0px'
		}, 1000, function() {
			setMessage("Game ended! You are the winner!", "red");
		});
	} else {
		$("#img" + id).animate({
			left : '+=' + '0px',
			top : '+=' + '0px'
		}, 1000, function() {
			setMessage("Game ended. You Lose :(", "red");
		});
	}
	terminate = true;
}

function move(userid, diceValue) {
	for ( var i = 0; i < diceValue; i++) {
		$("#img" + userid).animate({
			left : '+=' + moveX[(index[userid] + i) % 81],
			top : '+=' + moveY[(index[userid] + i) % 81]
		}, 500);
	}
	index[userid] = (parseInt(index[userid]) + parseInt(diceValue)) % 81;
	var position = indexToPosition(MAP, index[userid]);
	if (userid == id) {
		switch (MAP[index[userid]].func) {
		case "land":
			// checkMove(position);
			$("#img" + userid).animate({
				left : '+=' + '0px',
				top : '+=' + '0px'
			}, 500, function() {
				checkMove(position);
			});
			break;
		case "money":
			// changeMoney(position, 10, "gain");
			$("#img" + userid).animate({
				left : '+=' + '0px',
				top : '+=' + '0px'
			}, 500, function() {
				changeMoney(position, 5, "gain");
			});
			break;
		case "bomb":
			// changeMoney(position, 10, "lose");
			$("#img" + userid).animate({
				left : '+=' + '0px',
				top : '+=' + '0px'
			}, 500, function() {
				changeMoney(position, 10, "lose");
			});
			break;
		case "stop":
			$("#img" + userid).animate({
				left : '+=' + '0px',
				top : '+=' + '0px'
			}, 500, function() {
				stopNoAction(position);
			});
			// stopNoAction(position);
			break;
		}
	}
}

function buildHouse(userid, level) {
	setMessage(players[userid].name + " build a house with level " + level,
			"yellow");
	var x = houseX[index[userid]];
	var y = houseY[index[userid]];
	switch (level) {
	case 1:
		$('#houselist_' + userid).prepend(
				'<img id="house_' + userid + '_' + index[userid]
						+ '" src="images/house_a_' + userid
						+ '.png" style="left:' + x + ';top:' + y
						+ ';position:absolute;visibility:visible" /> ');
		break;
	case 2:
		$('img[style*="left:' + x + ';top:' + y + '"]').attr('src',
				'images/house_b_' + userid + '.png');

		break;
	case 3:
		$('img[style*="left:' + x + ';top:' + y + '"]').attr('src',
				'images/house_c_' + userid + '.png');
		break;
	default:
		break;
	}
}

function updateDeposit(userid, money) {
	$('#deposite_' + userid).text(money);
}

function updateName(userid, name) {
	$('#player_name' + userid).text(name);
}

function setCard(cardId) {
	var card = players[id].cards[cardId];
	var container = $("#hint_container");
	if (container.is(":visible")) {
		container
				.slideUp(800,
						function() {
							if (popUpPlayer != cardId) {
								$("#outer").empty();
								$("#inner").empty();
								$("#outer").append(card.name);
								$("#inner").append(
										"<div>Athlete Sport: " + card.sport
												+ "</div>");
								$("#inner").append(
										"<div>Athlete Gold: " + card.gold
												+ "</div>");
								$("#inner").append(
										"<div>Athlete Country: " + card.country
												+ "</div>");
								$("#inner").append(
										"<div>Athlete Continent: "
												+ card.continent + "</div>");
								container.slideDown(800);
								popUpPlayer = cardId;
							}
						});
	} else {
		container.slideDown(800);
		$("#outer").empty();
		$("#outer").append(card.name);
		$("#inner").empty();
		$("#inner").append("<div>Athlete Sport: " + card.sport + "</div>");
		$("#inner").append("<div>Athlete Gold: " + card.gold + "</div>");
		$("#inner").append("<div>Athlete Country: " + card.country + "</div>");
		$("#inner").append(
				"<div>Athlete Continent: " + card.continent + "</div>");
	}
}

function updateCards(cards) {
	if (cardLength != cards.length) {
		cardLength = cards.length;
		$("#card_container").empty();
		for (i = 0; i < cardLength; i++) {
			$("#card_container").append(
					'<button id = "card_' + i
							+ '" class = "card" onclick = "setCard(' + i
							+ ');" >' + parseName(cards[i].name) + '</button>');
		}
	}
}

function updateBankruptcy(userid) {
	$('#img' + userid).animate({
		opacity : 0,
		top : '-=500px'
	}, 3000, function() {
		$("#player_name" + userid).css('text-decoration', 'line-through');
		updateDeposit(userid, 0);
		if (userid != id) {
			setMessage(players[userid].name + " is bankrupt!", "red");
		}
	});
	if (userid == id) {
		players[userid].bankruptcy = true;
		if (countLosers()) {
			terminateGame();
		}
	}
}

function initialize(players) {
	for ( var i = 0; i < players.length; i++) {
		$("#img" + players[i].id).show();
		$("#name_container").append(
				'<div id = "player_name' + players[i].id + '" class = "name">'
						+ players[i].name + '</div>');
		$("#deposite_container")
				.append(
						'<div id = "deposite_' + players[i].id
								+ '" class = "deposite">' + players[i].money
								+ '</div>');
		updateName(players[i].id, players[i].name);
		updateDeposit(players[i].id, players[i].money);

	}
	updateCards(players[id].cards);
}

function initialization() {
	$xml = xmlParse(this.req.responseText);
	$xml.find("player").each(function() {
		players.push(playerParse($(this)));
	});
	totalPlayers = players.length;
	initialize(players);
	update_timer = window.setInterval("update()", 2000);
}

function changeMoney(place_id, money, type) {
	if (type == "gain") {
		players[id].money += money;
		setMessage("Good Luck! $" + money + " has been added to your account!",
				"white");
		new net.AjaxRequest(
				requestChangeMoney(id, place_id, players[id].money), getMoney,
				onerror, "GET");
	} else if (type == "lose") {
		players[id].money -= money;
		if (players[id].money > 0) {
			setMessage("Unfortunately you have lost $" + money + ".", "white");
			new net.AjaxRequest(requestChangeMoney(id, place_id,
					players[id].money), getMoney, onerror, "GET");
		} else {
			new net.AjaxRequest(requestChangeMoney(id, place_id, 0),
					bankruptcy, onerror, "GET");
			// updateDeposit(id, 0);
			updateBankruptcy(id);
		}
	} else {
		alert("Invalid command!");
		return;
	}
}

function getMoney() {
	setMessage("You have $" + players[id].money + " now.", "white");
}

function checkMove(place_id) {
	new net.AjaxRequest(requestMove(place_id), getPlaceInfo, onerror, "GET");
}

function getPlaceInfo() {
	$xml = xmlParse(this.req.responseText);
	var owner = parseInt($xml.find("owner").text());
	var place = parseInt($xml.find("place_id").text());
	if (owner == -1) {
		makePurchase(place, 0, "purchase");
	} else if (owner == id) {
		var level = parseInt($xml.find("level").text());
		if (level < 3) {
			var cost = parseInt($xml.find("cost").text());
			makePurchase(place, cost, "upgrade");
		} else {
			new net.AjaxRequest(requestNoPurchase(id, place), highestLevel,
					onerror, "GET");
		}
	} else {
		var cost = parseInt($xml.find("cost").text());
		if (players[id].money > cost) {
			new net.AjaxRequest(requestPay(id, owner, cost, place), payPlace,
					onerror, "GET");
			players[id].money -= cost;
			setMessage("This place belongs to " + players[owner].name
					+ "\nYou have paid " + cost, "white");
			updateDeposit(id, players[id].money);
		} else {
			new net.AjaxRequest(
					requestPay(id, owner, players[id].money, place),
					bankruptcy, onerror, "GET");
			players[id].money -= cost;
			// updateDeposit(id, 0);
			updateBankruptcy(id);
		}
	}
}

function makePurchase(place, cost, type) {
	if (players[id].cards.length > 0) {
		var hint = "";
		if (type == "purchase") {
			hint = "Want to purchase this place?";
		} else if (type == "upgrade") {
			hint = "Want to upgrade your place? The cost is now " + cost + ".";
		} else {
			alert("Invalid command!");
			// return null;
		}

		var athlete = null;

		apprise(
				hint,
				{
					'verify' : true
				},
				function(buy) {
					if (buy) {
						apprise(
								"Please select an athlete(1 - "
										+ players[id].cards.length + "):",
								{
									'input' : true
								},
								function(num) {
									if (num) {
										num = parseInt(num);
										if (!isNaN(num)
												&& (num > 0 && num <= players[id].cards.length)) {
											if (players[id].money > players[id].cards[num - 1].gold) {
												athlete = num;
												setMessage(
														"You have choosen "
																+ players[id].cards[athlete - 1].name,
														"white");
												if (type == "purchase") {
													new net.AjaxRequest(
															requestPurchase(
																	id,
																	place,
																	players[id].cards[athlete - 1].id),
															purchasePlace,
															onerror, "GET");
													players[id]
															.removeCard(athlete - 1);
													players[id].updateHouse(
															place, 1);
													buildHouse(id, 1);
													updateCards(players[id].cards);
												} else if (type == "upgrade") {
													players[id].getLevel(place);
													var level = players[id].getLevel(place);
													if (level < 3 && level > 0) {
														new net.AjaxRequest(
																requestUpgrade(
																		id,
																		place,
																		players[id].cards[athlete - 1].id),
																upgradePlace,
																onerror, "GET");
														players[id]
																.removeCard(athlete - 1);
														players[id].updateHouse(
																place, level + 1);
														buildHouse(id, level + 1);
														updateCards(players[id].cards);
													} else {
														setMessage("It is already highest level!", "white");
													}
												}
											} else {
												setMessage(
														"You do not have enough money for this place - choose another athlete!",
														"red");
												new net.AjaxRequest(
														requestNoPurchase(id,
																place),
														noPurchase, onerror,
														"GET");
											}
										} else {
											alert("Invalid athlete number. It should be between 1 and "
													+ players[id].cards.length
													+ ".");
											new net.AjaxRequest(
													requestNoPurchase(id, place),
													noPurchase, onerror, "GET");
										}
									} else {
										if (type == "purchase") {
											new net.AjaxRequest(
													requestNoPurchase(id, place),
													noPurchase, onerror, "GET");
										} else if (type == "upgrade") {
											new net.AjaxRequest(
													requestNoPurchase(id, place),
													noUpgrade, onerror, "GET");
										}
									}
								});
					} else {
						if (type == "purchase") {
							new net.AjaxRequest(requestNoPurchase(id, place),
									noPurchase, onerror, "GET");
						} else if (type == "upgrade") {
							new net.AjaxRequest(requestNoPurchase(id, place),
									noUpgrade, onerror, "GET");
						}
					}
				});
	} else {
		setMessage("Sorry, you cannot " + type
				+ " this place now - you have no cards available!", "white");
		new net.AjaxRequest(requestNoPurchase(id, place), noPurchase, onerror,
				"GET");
	}
	// alert(choice);
	// return choice;
}

function noPurchase() {
	setMessage("You decide not to purchase this place.", "yellow");
}

function noUpgrade() {
	setMessage("You decide not upgrading your place.", "yellow");
}

function highestLevel() {
	setMessage(
			"This place is in its highest level that you cannot upgrade it anymore.",
			"white");
}

function purchasePlace() {
	setMessage("You have bought this place!", "white");
}

function upgradePlace() {
	setMessage("You have upgraded this place!", "white");
}

function payPlace() {

}

function bankruptcy() {
	setMessage("You are bankrupt!", "red");
}

function quitGame() {
	setMessage("Game ended", "red");
}

function stopNoAction(place_id) {
	new net.AjaxRequest(requestNoPurchase(id, place_id), stopPlace, onerror,
			"GET");
}

function stopPlace() {
	setMessage("Take a rest!", "white");
}

function update() {
	new net.AjaxRequest(requestUpdate(id), updateStatus, onerror, "GET");
}

function updateStatus() {
	if (this.req.responseText != "") {
		$xml = xmlParse(this.req.responseText);
		var turn = parseInt($xml.find("current_player_id").text());
		if (turn >= 0) {
			$xml.find("player").each(
					function() {
						var player = $(this);
						var player_id = parseInt(player.attr("id"));
						if (player.find("location").length > 0) {
							players[player_id].currentPos = parseInt(player
									.find("location").text());
							var currentIndex = positionToIndex(MAP,
									players[player_id].currentPos,
									index[player_id]);
							if (player_id != id) {
								move(player_id, (currentIndex
										- index[player_id] + 81) % 81);
							}
						}
						if (player_id != id) {
							if (player.find("house").length > 0) {
								player.find("house").each(
										function() {
											var place = parseInt($(this).find(
													"place").text());
											var level = parseInt($(this).find(
													"level").text());
											players[player_id].updateHouse(
													place, level);
											$("#img" + player_id).animate({
												left : '+=' + '0px',
												top : '+=' + '0px'
											}, 100, function() {
												buildHouse(player_id, level);
											});
										});
							}
						}
						if (player.find("money").length > 0) {
							players[player_id].money = parseInt(player.find(
									"money").text());
							$("#img" + player_id).animate(
									{
										left : '+=' + '0px',
										top : '+=' + '0px'
									},
									100,
									function() {
										updateDeposit(player_id,
												players[player_id].money);
									});
						}
						if (player.find("bankruptcy").length > 0) {
							players[player_id].bankruptcy = player.find(
									"bankruptcy").text() == "Y";
							if (player_id != id) {
								updateDeposit(player_id, 0);
								updateBankruptcy(player_id);
							}
						}
					});
			if (currentTurn != turn) {
				currentTurn = turn;
				if (turn == id) {
					$("#img0").animate({
						left : '+=' + '0px',
						top : '+=' + '0px'
					}, 200, function() {
						$("#img1").animate({
							left : '+=' + '0px',
							top : '+=' + '0px'
						}, 200, function() {
							$("#img2").animate({
								left : '+=' + '0px',
								top : '+=' + '0px'
							}, 200, function() {
								$("#img3").animate({
									left : '+=' + '0px',
									top : '+=' + '0px'
								}, 200, function() {
									setMessage("It is your turn!", "white");
									rolled = false;
								});
							});
						});
					});

				} else {
					$("#img0")
							.animate(
									{
										left : '+=' + '0px',
										top : '+=' + '0px'
									},
									200,
									function() {
										$("#img1")
												.animate(
														{
															left : '+=' + '0px',
															top : '+=' + '0px'
														},
														200,
														function() {
															$("#img2")
																	.animate(
																			{
																				left : '+='
																						+ '0px',
																				top : '+='
																						+ '0px'
																			},
																			200,
																			function() {
																				$(
																						"#img3")
																						.animate(
																								{
																									left : '+='
																											+ '0px',
																									top : '+='
																											+ '0px'
																								},
																								200,
																								function() {
																									setMessage(
																											"It's "
																													+ players[turn].name
																													+ "'s turn!",
																											"white");
																									currentTurn = turn;
																								});
																			});
														});
									});
				}
			}
			if (countLosers()) {
				$("#img0").animate({
					left : '+=' + '0px',
					top : '+=' + '0px'
				}, 300, function() {
					$("#img1").animate({
						left : '+=' + '0px',
						top : '+=' + '0px'
					}, 300, function() {
						$("#img2").animate({
							left : '+=' + '0px',
							top : '+=' + '0px'
						}, 300, function() {
							$("#img3").animate({
								left : '+=' + '0px',
								top : '+=' + '0px'
							}, 300, function() {
								terminateGame();
							});
						});
					});
				});
			}
		}
	}
}