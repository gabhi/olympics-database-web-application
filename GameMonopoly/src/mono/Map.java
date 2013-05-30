package mono;

class Map{
	private int NumberOfHouses;// the number of houses the map contains
	private String[] Continent;// which continent a house belongs to
	private int NumberOfPossibleLevels;//we can buy/upgrade the house for this number of times
	private int[] OwnerId;
	private int[] Cost;
	private int[] Level;
	private int[] Athlete_1;
	private int[] Athlete_2;
	private int[] Athlete_3;
	
	public Map(){
		NumberOfHouses = Monopoly_Server.NumHouses;
		NumberOfPossibleLevels = 3;
		OwnerId = new int[NumberOfHouses];
		Athlete_1 = new int[NumberOfHouses];
		Athlete_2 = new int[NumberOfHouses];
		Athlete_3 = new int[NumberOfHouses];
		for(int i=0;i<NumberOfHouses;i++){
			OwnerId[i] = -1;
			Athlete_1[i] = -1;
			Athlete_2[i] = -1;
			Athlete_3[i] = -1;
		}
		Cost = new int[NumberOfHouses];
		Level = new int[NumberOfHouses];
		
		Continent = new String[NumberOfHouses];
		
		for(int i=0;i<5;i++)
			Continent[i] = "North America";
		for(int i=5;i<11;i++)
			Continent[i] = "Europe";
		for(int i=11;i<16;i++)
			Continent[i] = "Asia";
		for(int i=16;i<25;i++)
			Continent[i] = "Australia";
		for(int i=25;i<31;i++)
			Continent[i] = "Africa";
		for(int i=31;i<38;i++)
			Continent[i] = "South America";
		for(int i=38;i<40;i++)
			Continent[i] = "North America";
		for(int i=40;i<42;i++)
			Continent[i] = "Africa";
		for(int i=42;i<47;i++)
			Continent[i] = "Asia";
	}
	
	public int GetCost(int HouseIndex){
			return Cost[HouseIndex];
	}
	
	public void Purchase(int PlayerId, int HouseIndex, int Athlete_Id){
		if (OwnerId[HouseIndex]==-1){
			OwnerId[HouseIndex] = PlayerId;
			Level[HouseIndex] = 1;
			Athlete_1[HouseIndex] = Athlete_Id;
			Athlete a = Monopoly_Server.server.GetAthlete(PlayerId, Athlete_Id);
			Monopoly_Server.server.LoseMoney(PlayerId, a.GetNGoldMedals());
			Cost[HouseIndex] = a.GetNGoldMedals();			
			if (a.GetContinent().equals(Continent[HouseIndex]))
				Cost[HouseIndex] += a.GetNGoldMedals();
			Monopoly_Server.server.RemoveAthleteAfterUse(PlayerId, Athlete_Id);
		}
	}
	
	public void Upgrade(int PlayerId, int HouseIndex, int Athlete_Id){
		if (OwnerId[HouseIndex]==PlayerId){
			if (Level[HouseIndex] < NumberOfPossibleLevels){
				if (Level[HouseIndex] == 1){
					Athlete_2[HouseIndex] = Athlete_Id;
					Level[HouseIndex] = 2;
				}
				else{ //if (Level[HouseIndex] == 2)
					Athlete_3[HouseIndex] = Athlete_Id;
					Level[HouseIndex] = 3;
				}
								
				Athlete a = Monopoly_Server.server.GetAthlete(PlayerId, Athlete_Id);
				Monopoly_Server.server.LoseMoney(PlayerId, a.GetNGoldMedals());
				Cost[HouseIndex] += a.GetNGoldMedals();
				if (a.GetContinent().equals(Continent[HouseIndex]))
					Cost[HouseIndex] += a.GetNGoldMedals();
				Monopoly_Server.server.RemoveAthleteAfterUse(PlayerId, Athlete_Id);
			}
		}
	}
	
	public int GetOwnerId(int HouseIndex){
		return OwnerId[HouseIndex];
	}
	
	public int GetHouseLevel(int HouseIndex){
		return Level[HouseIndex];
	}
	
}






