

package mono;

import java.util.LinkedList;


class TimelyUpdateInfo{
	public LinkedList<Action> Info;
	
	public TimelyUpdateInfo(){
		Info = new LinkedList<Action>();
	}
	
}



class Action{
	private String ActionName;
	private int Player_id;
	private int Location;
	private boolean Bankrupt;
	private int Money;
	private int HouseLocation;
	private int HouseLevel;
	
	public void AddLocationAction(int player_id, int newLoc){
		ActionName = "Location";
		Player_id = player_id;
		Location = newLoc;
	}
	public void AddBankruptAction(int player_id, boolean newBankrupt){
		ActionName = "Bankrupt";
		Player_id = player_id;
		Bankrupt = newBankrupt;
	}
	public void AddMoneyAction(int player_id, int newMoney){
		ActionName = "Money";
		Player_id = player_id;
		Money = newMoney;
	}
	public void AddHouseAction(int player_id, int newHouseLoc, int newHouseLevel){
		ActionName = "House";
		Player_id = player_id;
		HouseLocation = newHouseLoc;
		HouseLevel = newHouseLevel;
	}
	public int GetLocation(){
		return Location;
	}
	public boolean GetBankrupt(){
		return Bankrupt;
	}
	public int GetMoney(){
		return Money;
	}
	public int GetHouseLocation(){
		return HouseLocation;
	}
	public int GetHouseLevel(){
		return HouseLevel;
	}
	public String GetActionName(){
		return ActionName;
	}
	public int GetPlayerId(){
		return Player_id;
	}
	
}