// athlete class

public class Athlete {
	
	public int id;
	public String name;
	public String country;
	public String sport;
	public int numOfGold;
	
	public Athlete()
	{
		
	}
	
	public Athlete(int id, String name, String country, String sport, int numOfGold)
	{
		this.id = id;
		this.name = name;
		this.country = country;
		this.sport = sport;
		this.numOfGold = numOfGold;
	}
	
	public int GetID()
	{
		return id;
	}
	
	public String GetName()
	{
		return name;
	}
	
	public String GetCountry()
	{
		return country;
	}
	
	public String GetSport()
	{
		return sport;
	}
	
	public int GetNumOfGold()
	{
		return numOfGold;
	}
	
	public void SetID(int id)
	{
		this.id = id;
	}
	
	public void SetName(String name)
	{
		this.name = name;;
	}
	
	public void SetCountry(String country)
	{
		this.country = country;
	}
	
	public void SetSport(String sport)
	{
		this.sport = sport;
	}
	
	public void SetNumOfGold(int numOfGold)
	{
		this.numOfGold = numOfGold;
	}
}
