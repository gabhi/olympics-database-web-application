package mono;

import java.util.HashMap;
import java.util.Collection;


class MonopolyPlayer{
	private String Username;
	private int UserId;
	private int NGoldMedals;
	private HashMap<Integer,Athlete> AthleteCard;
	private int Position;//index, start from 0
	private int Money;

	
	public Collection<Athlete> GetAthlete(){
		return AthleteCard.values();
		
	}
	
	
	public int GetPosition(){
		return Position;
	}
	
	public int GetMoney(){
		return Money;
	}
	
	public void SetPlayer(String Username, int NGoldMedals, int Position, int InitMoney){
		this.Username = Username;
		this.NGoldMedals = NGoldMedals;
		this.AthleteCard = new HashMap<Integer,Athlete>();
		this.Position = Position;
		this.Money = InitMoney;
	}
	
	public void SetPlayerPosition(int newPosition){
		Position = newPosition;
	}
	
	public void InsertAthlete(Athlete a){
		AthleteCard.put(a.GetId(),a);
	}
	
	public Athlete GetAthlete(int Athlete_id){
		return AthleteCard.get(Athlete_id);
	}
	
	public void RemoveAthlete(int Athlete_id){
		AthleteCard.remove(Athlete_id);
	}
	
	public void EarnMoney(int Amount){
		Money += Amount;
	}
	
	public void LoseMoney(int Amount){
		Money -= Amount;
	}
	public void changeMoney(int current_money){
		Money = current_money;
	}
	
	public boolean IsBankrupted(){
		return Money<=0;
	}
	
}