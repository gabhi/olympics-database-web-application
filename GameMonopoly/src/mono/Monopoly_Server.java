package mono;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;
import javax.xml.xpath.*;

import java.util.Iterator;
import java.util.Collection;

class Monopoly_Server {
	// This is a singleton class, so only one object of this class can run at
	// one time.
	static public Monopoly_Server server = null;
	static public int MaxNumPlayers = 3;
	static private int NumPlayers = 0;
	static public int NumInitGoldMedals = 20;
	static public int NumInitAthleteCards = 8;
	static public int NumHouses = 47;
	static public int turn = 0;// which player is playing. [0, MaxNumPlayers-1]
	static public int Status = 0;
	// Status:
	// 0: Waiting for other players
	// 1: Initializing
	// 2: The gaming is going
	// 3: Game over
	static public int NumTold = 0;

	static public void AddPlayer(String PlayerName) {
		if (server == null) {
			server = new Monopoly_Server();
			server.userinfo[0] = new UserInfo();
			server.userinfo[0].SetUserName(PlayerName);
			// System.out.println("PlayerName is " + PlayerName);
			NumPlayers++;
		} else {
			if (NumPlayers < MaxNumPlayers) {
				server.userinfo[NumPlayers] = new UserInfo();
				server.userinfo[NumPlayers].SetUserName(PlayerName);
				NumPlayers++;
				if (NumPlayers == MaxNumPlayers) {
					Status = 1;
					server.StartGame();
				}
			}
		}
	}

	private Monopoly_Server() {
		NumPlayers = 0;
		userinfo = new UserInfo[MaxNumPlayers];
	};

	private UserInfo[] userinfo;
	private Athlete[] athlete_data;
	private MonopolyPlayer[] monoplayer;
	private int TotalNumAthleteCard;
	private Map map;
	private boolean[] Bankrupted;
	private int currentPlayer; // range: [0, MaxNumPlayers-1]
	public TimelyUpdateInfo[] UpdateInfoList;
	private int initComplete;

	private void StartGame() {
		// Assign user id
		Bankrupted = new boolean[MaxNumPlayers];
		for (int i = 0; i < MaxNumPlayers; i++) {
			userinfo[i].SetUserId(i);
			Bankrupted[i] = false;
		}
		//
		initComplete = 0;

		// Read in Athlete data from XML
		ReadAthleteData();

		// Initialize player data, including initial money, position, athlete
		// cards, etc.
		InitPlayer();

		// Initialize the map
		map = new Map();



		// Set Current Player
		currentPlayer = 0;

		// Set UpdateInfoQueue
		UpdateInfoList = new TimelyUpdateInfo[MaxNumPlayers];
		for (int i = 0; i < MaxNumPlayers; i++)
			UpdateInfoList[i] = new TimelyUpdateInfo();
		
		
		// Set Status to "the game is going"
		Status = 2;
	}

	public int GetCurrentPlayer() {
		return currentPlayer;
	}

	public void SetToNextCurrentPlayer() {
		do {
			currentPlayer = (currentPlayer + 1) % MaxNumPlayers;
		} while (Bankrupted[currentPlayer]);
	}

	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();

		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}

	private void ReadAthleteData() {
		// System.out.println("Start Reading XML");
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder db = factory.newDocumentBuilder();
			Document xmldoc = db.parse(new File("1.xml"));
			Element root = xmldoc.getDocumentElement();
			NodeList athletes = root.getChildNodes();
			int tempLength = athletes.getLength();
			TotalNumAthleteCard = 506;
			athlete_data = new Athlete[TotalNumAthleteCard];
			int i = 0;
			for (int j = 0; j < tempLength; j++) {

				Node a = athletes.item(j);
				if (a.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) a;
					if (getTagValue("name", eElement).equals(""))
						continue;
					athlete_data[i] = new Athlete();
					athlete_data[i].SetId(Integer.parseInt(getTagValue("id",
							eElement)));

					athlete_data[i].SetName(getTagValue("name", eElement));

					athlete_data[i]
							.SetCountry(getTagValue("country", eElement));

					athlete_data[i].SetSport(getTagValue("sport", eElement));

					athlete_data[i].SetNGoldMedals(Integer
							.parseInt(getTagValue("gold", eElement)));

					athlete_data[i].SetContinent(getTagValue("continent",
							eElement));
					i++;
				}

			}

			System.out.println("Total number of athletes in XML:"
					+ TotalNumAthleteCard);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println("End Reading XML");
	}

	private void InitPlayer() {
		monoplayer = new MonopolyPlayer[MaxNumPlayers];
		for (int i = 0; i < MaxNumPlayers; i++) {
			monoplayer[i] = new MonopolyPlayer();
			monoplayer[i].SetPlayer(userinfo[i].GetUserName(),
					NumInitGoldMedals, i * NumHouses / MaxNumPlayers,
					NumInitGoldMedals);
		}
		// random choose athlete cards
		int temp[] = new int[TotalNumAthleteCard];
		for (int j = 0; j < TotalNumAthleteCard; j++)
			temp[j] = -1;
		for (int j = 0; j < TotalNumAthleteCard; j++) {
			int r = (int) Math.round(Math.random() * TotalNumAthleteCard - 0.5);
			if (r == -1)
				r++;
			if (r == TotalNumAthleteCard)
				r--;
			while (true) {
				if (temp[r] == -1) {
					temp[r] = j;
					break;
				}
				r = (r + 1) % TotalNumAthleteCard;
			}
		}
		for (int i = 0; i < MaxNumPlayers; i++) {
			for (int j = i * NumInitAthleteCards; j < (i + 1)
					* NumInitAthleteCards; j++) {
				// System.out.println("i="+i+" j=" + j+"  temp[j]="+temp[j] +" "
				// + athlete_data[temp[j]].GetInfo());
				monoplayer[i].InsertAthlete(athlete_data[temp[j]]);

			}

		}
		// for(int j=0;j<TotalNumAthleteCard;j++){
		// System.out.println(athlete_data[j].GetInfo());
		// }
	}

	public Athlete GetAthlete(int Player_id, int Athlete_id) {
		return monoplayer[Player_id].GetAthlete(Athlete_id);
	}

	public void RemoveAthleteAfterUse(int Player_id, int Athlete_id) {
		monoplayer[Player_id].RemoveAthlete(Athlete_id);
	}

	public void Purchase(int PlayerId, int HouseIndex, int Athlete_Id) {
		map.Purchase(PlayerId, HouseIndex, Athlete_Id);
	}

	public void Upgrade(int PlayerId, int HouseIndex, int Athlete_Id) {
		map.Upgrade(PlayerId, HouseIndex, Athlete_Id);
	}

	public int GetCost(int HouseIndex) {
		if (GetOwnerId(HouseIndex) == -1)
			return 0;
		else if (Bankrupted[GetOwnerId(HouseIndex)])
			return 0;
		else
			return map.GetCost(HouseIndex);
	}

	public int GetOwnerId(int HouseIndex) {
		return map.GetOwnerId(HouseIndex);
	}

	public int GetHouseLevel(int HouseIndex) {
		return map.GetHouseLevel(HouseIndex);
	}

	public void EarnMoney(int PlayerId, int Amount) {
		monoplayer[PlayerId].EarnMoney(Amount);
	}

	public void LoseMoney(int PlayerId, int Amount) {
		monoplayer[PlayerId].LoseMoney(Amount);
	}

	public void ChangeMoney(int PlayerId, int CurrentMoney) {
		monoplayer[PlayerId].changeMoney(CurrentMoney);
		if (monoplayer[PlayerId].IsBankrupted())
			Bankrupted[PlayerId] = true;
	}

	public void Pay(int PayFrom, int PayTo, int ActualPayment) {
		monoplayer[PayFrom].LoseMoney(ActualPayment);
		monoplayer[PayTo].EarnMoney(ActualPayment);
		if (monoplayer[PayFrom].IsBankrupted())
			Bankrupted[PayFrom] = true;
	}

	public void destroy() {
		// close this game.

		try {
			Thread.sleep(10000);// Wait for 10 seconds
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		} finally {
			userinfo = null;
			athlete_data = null;
			monoplayer = null;
			map = null;
			Bankrupted = null;
			UpdateInfoList = null;
			Status = 0;
		}

	}

	static public void forceQuit() {
		server.userinfo = null;
		server.athlete_data = null;
		server.monoplayer = null;
		server.map = null;
		server.Bankrupted = null;
		server.UpdateInfoList = null;
		Status = 0;

	}

	static public synchronized String ProcessAjaxReq(PassInfo passinfo) {

		String action = passinfo.action;
		StringBuffer sb = new StringBuffer();

		sb.append("<root>");

		if (action.equals("register")) {
			if (Status == 0) {
				String username = passinfo.arg1;
				AddPlayer(username);
				sb.append("<your_id>");
				int your_id = NumPlayers - 1;
				sb.append("" + your_id);
				sb.append("</your_id>");
				sb.append("<total_player_num>" + MaxNumPlayers
						+ "</total_player_num>");
			} else {
				sb.append("");
			}

		} else if (action.equals("waiting")) {

			if (Status == 0 || Status == 1) {
				sb.append("<game_begin>N</game_begin>");

			} else if (Status == 2) {
				sb.append("<game_begin>Y</game_begin>");

				// for(int i=0;i<MaxNumPlayers;i++){
				// sb.append("<player>");
				// sb.append("<player_name>" + server.userinfo[i].GetUserName()
				// + "</player_name>");
				// sb.append("<player_id>" + server.userinfo[i].GetUserId() +
				// "</player_id>");
				// sb.append("<start_position>" +
				// server.monoplayer[i].GetPosition() + "</start_position>");
				// sb.append("<start_money>" + server.monoplayer[i].GetMoney() +
				// "</start_money>");
				// Collection<Athlete> ath = server.monoplayer[i].GetAthlete();
				// for(Athlete a : ath){
				// sb.append("<athlete>");
				// sb.append("<athlete_name>" + a.GetName() +
				// "</athlete_name>");
				// sb.append("<athlete_id>" + a.GetId() + "</athlete_id>");
				// sb.append("<gold_num>" + a.GetNGoldMedals() + "</gold_num>");
				// sb.append("<country>" + a.GetCountry() + "</country>");
				// sb.append("<continent>" +a.GetContinent() + "</continent>");
				// sb.append("</athlete>");
				// }
				// sb.append("</player>");
				// }
			}
			for (int i = 0; i < NumPlayers; i++) {
				sb.append("<player_name>");
				sb.append(server.userinfo[i].GetUserName());
				sb.append("</player_name>");
			}

		} else if (action.equals("query_place")) {
			int place_id = Integer.parseInt(passinfo.arg1);
			sb.append("<place_id>" + place_id + "</place_id>");
			sb.append("<owner>" + server.GetOwnerId(place_id) + "</owner>");
			sb.append("<cost>" + server.GetCost(place_id) + "</cost>");
			sb.append("<level>" + server.GetHouseLevel(place_id) + "</level>");

		} else if (action.equals("no_action")) {
			int player_id = Integer.parseInt(passinfo.arg1);
			int current_pos = Integer.parseInt(passinfo.arg2);
			server.monoplayer[player_id].SetPlayerPosition(current_pos);
			sb.append("");
			Action insertAction = new Action();
			insertAction.AddLocationAction(player_id, current_pos);
			for (int i = 0; i < MaxNumPlayers; i++)
				server.UpdateInfoList[i].Info.add(insertAction);

			server.SetToNextCurrentPlayer();

		} else if (action.equals("purchase")) {
			int player_id = Integer.parseInt(passinfo.arg1);
			int place_id = Integer.parseInt(passinfo.arg2);
			int athlete_id = Integer.parseInt(passinfo.arg3);
			server.Purchase(player_id, place_id, athlete_id);
			sb.append("");
			Action insertAction_1 = new Action();
			insertAction_1.AddMoneyAction(player_id,
					server.monoplayer[player_id].GetMoney());
			Action insertAction_2 = new Action();
			insertAction_2.AddHouseAction(player_id, place_id,
					server.GetHouseLevel(place_id));
			Action insertAction_3 = new Action();
			insertAction_3.AddLocationAction(player_id, place_id);
			for (int i = 0; i < MaxNumPlayers; i++) {
				server.UpdateInfoList[i].Info.add(insertAction_1);
				server.UpdateInfoList[i].Info.add(insertAction_2);
				server.UpdateInfoList[i].Info.add(insertAction_3);
			}

			server.SetToNextCurrentPlayer();

		} else if (action.equals("upgrade")) {
			int player_id = Integer.parseInt(passinfo.arg1);
			int place_id = Integer.parseInt(passinfo.arg2);
			int athlete_id = Integer.parseInt(passinfo.arg3);
			server.Upgrade(player_id, place_id, athlete_id);
			sb.append("");
			Action insertAction_1 = new Action();
			insertAction_1.AddMoneyAction(player_id,
					server.monoplayer[player_id].GetMoney());
			Action insertAction_2 = new Action();
			insertAction_2.AddHouseAction(player_id, place_id,
					server.GetHouseLevel(place_id));
			Action insertAction_3 = new Action();
			insertAction_3.AddLocationAction(player_id, place_id);
			for (int i = 0; i < MaxNumPlayers; i++) {
				server.UpdateInfoList[i].Info.add(insertAction_1);
				server.UpdateInfoList[i].Info.add(insertAction_2);
				server.UpdateInfoList[i].Info.add(insertAction_3);
			}
			server.SetToNextCurrentPlayer();

		} else if (action.equals("pay")) {
			int pay_from = Integer.parseInt(passinfo.arg1);
			int pay_to = Integer.parseInt(passinfo.arg2);
			int actual_payment = Integer.parseInt(passinfo.arg3);
			int place_id = Integer.parseInt(passinfo.arg4);
			server.Pay(pay_from, pay_to, actual_payment);
			sb.append("");
			Action insertAction_1 = new Action();
			insertAction_1.AddMoneyAction(pay_from,
					server.monoplayer[pay_from].GetMoney());

			Action insertAction_2 = new Action();
			insertAction_2.AddMoneyAction(pay_to,
					server.monoplayer[pay_to].GetMoney());

			Action insertAction_3 = new Action();
			if (server.Bankrupted[pay_from]) {
				insertAction_3.AddBankruptAction(pay_from, true);
			}
			Action insertAction_4 = new Action();
			insertAction_4.AddLocationAction(pay_from, place_id);
			for (int i = 0; i < MaxNumPlayers; i++) {
				server.UpdateInfoList[i].Info.add(insertAction_1);
				server.UpdateInfoList[i].Info.add(insertAction_2);
				if (server.Bankrupted[pay_from]) {
					server.UpdateInfoList[i].Info.add(insertAction_3);
				}
				server.UpdateInfoList[i].Info.add(insertAction_4);
			}

			server.SetToNextCurrentPlayer();

			int NumBankRupted = 0;
			for (int i = 0; i < MaxNumPlayers; i++)
				if (server.Bankrupted[i])
					NumBankRupted++;
			if (NumBankRupted >= MaxNumPlayers - 1) {
				Status = 3;
				NumTold = 1;
			}

		} else if (action.equals("initialize")) {
			if (server != null) {
				sb.append("<game_begin>Y</game_begin>");

				for (int i = 0; i < MaxNumPlayers; i++) {
					sb.append("<player>");
					sb.append("<player_name>"
							+ server.userinfo[i].GetUserName()
							+ "</player_name>");
					sb.append("<player_id>" + server.userinfo[i].GetUserId()
							+ "</player_id>");
					sb.append("<start_position>"
							+ server.monoplayer[i].GetPosition()
							+ "</start_position>");
					sb.append("<start_money>" + server.monoplayer[i].GetMoney()
							+ "</start_money>");
					Collection<Athlete> ath = server.monoplayer[i].GetAthlete();
					for (Athlete a : ath) {
						sb.append("<athlete>");
						sb.append("<athlete_name>" + a.GetName()
								+ "</athlete_name>");
						sb.append("<athlete_id>" + a.GetId() + "</athlete_id>");
						sb.append("<sport>" + a.GetSport() + "</sport>");
						sb.append("<gold_num>" + a.GetNGoldMedals()
								+ "</gold_num>");
						sb.append("<country>" + a.GetCountry() + "</country>");
						sb.append("<continent>" + a.GetContinent()
								+ "</continent>");
						sb.append("</athlete>");
					}
					sb.append("</player>");
				}
				server.initComplete++;
			}
		}

		else if (action.equals("timely_update")) {

			if (server != null) {
				int req_player_id = Integer.parseInt(passinfo.arg1);// used to
																	// choose
																	// which
																	// linkedlist
																	// to clear
				if (server.initComplete < MaxNumPlayers)
					sb.append("<current_player_id>" + "-1"
							+ "</current_player_id>");
				else
					sb.append("<current_player_id>" + server.GetCurrentPlayer()
							+ "</current_player_id>");

				int money[] = new int[MaxNumPlayers];
				int loc[] = new int[MaxNumPlayers];
				boolean br[] = new boolean[MaxNumPlayers];
				boolean[] flag = new boolean[MaxNumPlayers];
				int[][] HouseLevel = new int[MaxNumPlayers][NumHouses];

				for (int i = 0; i < MaxNumPlayers; i++) {
					money[i] = -1;
					loc[i] = -1;
					flag[i] = false;
					br[i] = false;
				}

				while (server.UpdateInfoList[req_player_id].Info.size() > 0) {
					Action ac = server.UpdateInfoList[req_player_id].Info
							.poll();

					flag[ac.GetPlayerId()] = true;
					if (ac.GetActionName().equals("Location")) {
						loc[ac.GetPlayerId()] = ac.GetLocation();
					} else if (ac.GetActionName().equals("Bankrupt")) {
						br[ac.GetPlayerId()] = true;
					} else if (ac.GetActionName().equals("Money")) {
						money[ac.GetPlayerId()] = ac.GetMoney();
					} else if (ac.GetActionName().equals("House")) {
						HouseLevel[ac.GetPlayerId()][ac.GetHouseLocation()] = ac
								.GetHouseLevel();
					}

				}

				for (int i = 0; i < MaxNumPlayers; i++) {
					if (flag[i]) {
						sb.append("<player id=\"" + i + "\">");
						if (loc[i] != -1) {
							sb.append("<location>" + loc[i] + "</location>");
						}
						if (money[i] != -1) {
							sb.append("<money>" + money[i] + "</money>");
						}
						if (br[i]) {
							sb.append("<bankruptcy>Y</bankruptcy>");
						}
						for (int j = 0; j < NumHouses; j++) {
							if (HouseLevel[i][j] > 0) {
								sb.append("<house>");
								sb.append("<place>" + j + "</place>");
								sb.append("<level>" + HouseLevel[i][j]
										+ "</level>");
								sb.append("</house>");
							}
						}

						sb.append("</player>");
					}
				}

				if (Status == 3) {
					NumTold++;
					if (NumTold >= MaxNumPlayers) {
						forceQuit();
						server = null;
					}
				}
			}

		} else if (action.equals("money_change")) {
			int player_id = Integer.parseInt(passinfo.arg1);
			int current_money = Integer.parseInt(passinfo.arg2);
			int place_id = Integer.parseInt(passinfo.arg3);
			server.ChangeMoney(player_id, current_money);
			server.monoplayer[player_id].SetPlayerPosition(place_id);

			Action insertAction = new Action();
			insertAction.AddLocationAction(player_id, place_id);
			Action insertAction_2 = new Action();
			insertAction_2.AddMoneyAction(player_id, current_money);
			Action insertAction_3 = new Action();
			if (server.Bankrupted[player_id]) {
				insertAction_3.AddBankruptAction(player_id, true);
			}

			for (int i = 0; i < MaxNumPlayers; i++) {
				server.UpdateInfoList[i].Info.add(insertAction);
				server.UpdateInfoList[i].Info.add(insertAction_2);
				if (server.Bankrupted[player_id]){
					server.UpdateInfoList[i].Info.add(insertAction_3);
				}
			}
			server.SetToNextCurrentPlayer();
			sb.append("");
			int NumBankRupted = 0;
			for (int i = 0; i < MaxNumPlayers; i++)
				if (server.Bankrupted[i])
					NumBankRupted++;
			if (NumBankRupted >= MaxNumPlayers - 1) {
				Status = 3;
				NumTold = 1;
			}

		} else if (action.equals("quit")) {
			if (server != null) {
				forceQuit();
				server = null;
			}
		} else {
			// Unknown action type
			sb.append("");
		}

		sb.append("</root>");
		return sb.toString();
	}

}
