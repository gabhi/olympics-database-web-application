package mono;

class Athlete{
	private int _id;
	private String _name;
	private String _country;
	private String _sport;
	private int _NGoldMedals;
	private String _continent;
	
	
	public void SetAthlete(int id, String name, String country, String sport, int NGoldMedals, String continent ){
		_id = id;
		_name = "" + name;
		_country = "" + country;
		_sport = "" + sport;
		_continent = continent;
		_NGoldMedals = NGoldMedals;
	}
	
	public int GetId(){return _id;}
	public String GetName(){return _name;}
	public String GetCountry(){return _country;}
	public String GetContinent(){return _continent;}
	public int GetNGoldMedals(){return _NGoldMedals;}
	public String GetSport(){return _sport;}
	public String GetInfo(){
		StringBuilder sb = new StringBuilder();
		sb.append(_id+" ");
		sb.append(_name + " ");
		sb.append(_country + " ");
		sb.append(_sport + " ");
		sb.append(_continent + " ");
		sb.append(_NGoldMedals + "");
		return sb.toString();
	}
	
	public void SetId(int id){_id = id;}
	public void SetName(String name) {_name=name;}
	public void SetCountry(String country){_country = country;}
	public void SetSport(String sport){_sport = sport;}
	public void SetNGoldMedals(int NGoldMedals){_NGoldMedals = NGoldMedals;}
	public void SetContinent(String continent){_continent = continent;}
	
}