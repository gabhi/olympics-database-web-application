// CountryMedals class

public class CountryMedals {
	
	public String name;
	public int year;
	public int gold;
	public int silver;
	public int bronze;
	
	public String GetName()
	{
		return name;
	}
	
	public int GetYear()
	{
		return year;
	}
	
	public int GetGold()
	{
		return gold;
	}
	
	public int GetSilver()
	{
		return silver;
	}
	
	public int GetBronze()
	{
		return bronze;
	}
	
	public void SetName(String name)
	{
		this.name = name;
	}
	
	public void SetYear(int year)
	{
		this.year = year;
	}
	
	public void SetGold(int gold)
	{
		this.gold = gold;
	}
	
	public void SetSilver(int silver)
	{
		this.silver = silver;
	}
	
	public void SetBronze(int bronze)
	{
		this.bronze = bronze;
	}
}
