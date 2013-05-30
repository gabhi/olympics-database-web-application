/*
	read data from XML file and import them to MySQL
*/

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.*;
import java.io.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Monopoly extends DefaultHandler {

	public Athlete athlete;
	public CountryMedals countryMedals;
	public static ArrayList<Athlete> athleteList = new ArrayList<Athlete>(); // store the information of athletes with least 3 gold medals
	public static ArrayList<CountryMedals> countryMedalsList = new ArrayList<CountryMedals>(); // store the medal information of countries
	public static Connection conn = null;
	public static ArrayList<String> country = new ArrayList<String>();
	public static ArrayList<String> continent = new ArrayList<String>();
	public String temp;
	public static String xmlFile;
	public int goldCnt;
	public String year;
	
	// parse plain text content
	public void characters(char[] buffer, int start, int length)
	{
		temp = new String(buffer, start, length);
	}
	
	// start to parse an element
	public void startElement(String uri, String localName, String qName, Attributes attributes)
	{
		temp = "";
		if (qName.equals("Record") && xmlFile.equals("Athletes.xml"))
		{
			athlete = new Athlete();
		}
		else if (qName.equals("record") && xmlFile.equals("Summer Olympics.xml"))
		{
			countryMedals = new CountryMedals();
		}
		else if (qName.contains("medals_") && xmlFile.equals("Summer Olympics.xml"))
		{
			year = qName.split("_")[1];
		}
		
	}
	
	// end to parse an element
	public void endElement(String uri, String localName, String qName)
	{
		temp = temp.trim();
		if (xmlFile.equals("Athletes.xml"))
		{
			if (qName.equals("Record"))
			{
				if (goldCnt >= 3)
					athleteList.add(athlete);
				goldCnt = 0;
			}
			else if (qName.equals("Full_Name"))
			{
				athlete.SetName(temp);
			}
			else if (qName.equals("Country"))
			{
				athlete.SetCountry(temp);
			}
			else if (qName.equals("Sport"))
			{
				athlete.SetSport(temp);
			}
			else if (qName.equals("Medal"))
			{
				if (temp.equals("Gold"))
					goldCnt++;
				athlete.SetNumOfGold(goldCnt);
			}
		}
		else if (xmlFile.equals("Summer Olympics.xml"))
		{
			if (qName.contains("medals_"))
			{
				year = "";
			}
			else if (qName.contains("record"))
			{
				countryMedals.year = Integer.parseInt(year);
				countryMedalsList.add(countryMedals);
			}
			else if (qName.equals("country"))
			{
				countryMedals.name = temp;
			}
			else if (qName.equals("gold"))
			{
				countryMedals.gold = Integer.parseInt(temp);
			}
			else if (qName.equals("silver"))
			{
				countryMedals.silver = Integer.parseInt(temp);
			}
			else if (qName.equals("bronze"))
			{
				countryMedals.bronze = Integer.parseInt(temp);
			}
		}
		
	}
	
	public static String ConvertToFileURL(String filename)
	{
        String path = new File(filename).getAbsolutePath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }

        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return "file:" + path;
    }
	
	// read a XML file
	public static void ReadXML(String filename)
	{
		try
		{
			SAXParserFactory sf = SAXParserFactory.newInstance();
			SAXParser sp = sf.newSAXParser();
			Monopoly monopolyHandler = new Monopoly();
			sp.parse(filename, monopolyHandler);
			//monopolyHandler.PrintAthlete();
			//monopolyHandler.PrintMedals();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	// connect to MySQL database
	public static void ConnectToDB()
	{
		int port = 3306;
		String host = "localhost";
		String user = "monopolyuser";
		String password = "monopolypassword";
		String url = "jdbc:mysql://" + host + ":" + port + "/monopoly";
		String driver = "com.mysql.jdbc.Driver";
		
		try
		{
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
	}
	
	// close connection
	public static void CloseConnection()
	{
		try
		{
			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	// write data to MySQL database
	public static void WriteToDB()
	{
		try
		{
			Statement st = conn.createStatement();
			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet rs = null;
			
			rs = dbmd.getTables(null, null, "ATHLETE", null);
			if (rs.next())
			{
				st.executeUpdate("DROP TABLE ATHLETE;");
			}
			rs = dbmd.getTables(null, null, "COUNTRY", null);
			if (rs.next())
			{
				st.executeUpdate("DROP TABLE COUNTRY;");
			}
			
			// create country table
			String sqlCountry = "CREATE TABLE COUNTRY" +
						 "(" +
						 "country VARCHAR(50)," +
						 "continent VARCHAR(20)," +
						 "PRIMARY KEY (country)" +
						 ")";
			st.executeUpdate(sqlCountry);
			
			// create athlete table
			String sqlAthlete = "CREATE TABLE ATHLETE" +
								"(" +
								"id INTEGER NOT NULL AUTO_INCREMENT," +
								"name VARCHAR(150)," +
								"country VARCHAR(50)," +
								"sport VARCHAR(30)," +
								"gold INTEGER," +
								"PRIMARY KEY (id)," +
								"FOREIGN KEY (country) REFERENCES COUNTRY (country)" +
								")";
			st.executeUpdate(sqlAthlete);
			
			rs = dbmd.getTables(null, null, "MEDALS", null);
			if (rs.next())
			{
				st.executeUpdate("DROP TABLE MEDALS;");
			}
			// create CountryMedals table
			String sqlCountryMedals = "CREATE TABLE MEDALS" +
									  "(" +
									  "country VARCHAR(50)," +
									  "year INTEGER," +
									  "gold INTEGER," +
									  "silver INTEGER," +
									  "bronze INTEGER," +
									  "PRIMARY KEY (country, year)" +
									  ")";
			st.executeUpdate(sqlCountryMedals);
			
			// create user table
			rs = dbmd.getTables(null, null, "USER", null);
			if (rs.next())
			{
				st.executeUpdate("DROP TABLE USER;");
			}
			String sqlUser = "CREATE TABLE USER" +
							 "(" +
							 "username VARCHAR(15)," +
							 "password VARCHAR(15)," +
							 "nickname VARCHAR(15)," +
							 "score INTEGER," +
							 "UNIQUE (nickname)," +
							 "PRIMARY KEY (username)" +
							 ")";
			st.executeUpdate(sqlUser);
			
			// insert tuples for every table
			String line;
			int cnt1 = 0, cnt2 = 0;
			LineNumberReader r1 = new LineNumberReader(new FileReader(new File("country.txt")));
			LineNumberReader r2 = new LineNumberReader(new FileReader(new File("continent.txt")));
			while ((line = r1.readLine()) != null)
			{
				country.add(line);
				cnt1++;
			}
			while ((line = r2.readLine()) != null)
			{
				continent.add(line);
				cnt2++;
			}
			r1.close();
			r2.close();
			
			for (int i = 0; i < cnt1; i++)
			{
				String sqlCountryTuple = "INSERT INTO COUNTRY (country, continent) VALUES (" +
										 "'" + country.get(i) + "'," +
										 "'" + continent.get(i) + "'" +
										 ")";
				st.executeUpdate(sqlCountryTuple);
			}
			
			for (CountryMedals cm : countryMedalsList)
			{
				if (cm.GetName().contains("'"))
				{
					String sqlCountryMedalsTuple = "INSERT INTO MEDALS (country, year, gold, silver, bronze) VALUES (" +
										 "\"" + cm.GetName() + "\"," + 
										 cm.GetYear() + "," + 
										 cm.GetGold() + "," + 
										 cm.GetSilver() + "," + 
										 cm.GetBronze() + ")";
					st.executeUpdate(sqlCountryMedalsTuple);
				}
				else
				{
					String sqlCountryMedalsTuple = "INSERT INTO MEDALS (country, year, gold, silver, bronze) VALUES (" +
							 "'" + cm.GetName() + "'," + 
							 cm.GetYear() + "," + 
							 cm.GetGold() + "," + 
							 cm.GetSilver() + "," + 
							 cm.GetBronze() + ")";
					st.executeUpdate(sqlCountryMedalsTuple);
				}
			}
			
			//HashSet<String> country = new HashSet<String>();
			for (Athlete al : athleteList)
			{
				//country.add(al.GetCountry());
				if (al.GetName().contains("'"))
				{
				String sqlAthleteTuple = "INSERT INTO ATHLETE (name, country, sport, gold) VALUES (" +
										 "\"" + al.GetName() + "\"," +
										 "'" + al.GetCountry() + "'," +
										 "'" + al.GetSport() + "'," +
										 al.GetNumOfGold() + ")";
				st.executeUpdate(sqlAthleteTuple);
				}
				/*else if (al.GetName().contains("\""))
				{
					String sqlAthleteTuple = "INSERT INTO ATHLETE (name, country, sport, gold) VALUES (" +
											 "'" + al.GetName() + "'," +
											 "'" + al.GetCountry() + "'," +
											 "'" + al.GetSport() + "'," +
											 al.GetNumOfGold() + ")";
					st.executeUpdate(sqlAthleteTuple);
				}*/
				else
				{
					String sqlAthleteTuple = "INSERT INTO ATHLETE (name, country, sport, gold) VALUES (" +
							 "'" + al.GetName() + "'," +
							 "'" + al.GetCountry() + "'," +
							 "'" + al.GetSport() + "'," +
							 al.GetNumOfGold() + ")";
					st.executeUpdate(sqlAthleteTuple);
				}
			}
			
			/* print out countries
			for(String str : country)
			{
				System.out.println(str);
			}
			*/
		}
		catch (SQLException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void WriteToXML()
	{
		try 
		{
			// create a xml file
			BufferedWriter bw = new BufferedWriter(new FileWriter("selected_athletes.xml"));
			bw.write("<Athletes>\n");
			
			String updateQuery = "ALTER TABLE ATHLETE ADD INDEX country_index (country);";
			//String query = "SELECT A.*, C.continent from ATHLETE AS A, COUNTRY AS C where A.country = C.country;";
			String query = "SELECT A.*, C.continent from ATHLETE AS A INNER JOIN COUNTRY AS C ON A.country = C.country ORDER BY id;";
			Statement st = conn.createStatement();
			st.executeUpdate(updateQuery);
			ResultSet rs = st.executeQuery(query);
			
			//String id, name, country, sport, gold, continent;
			while (rs.next())
			{
				bw.write("\t<Athlete>\n");
				
				bw.write("\t\t<id>");
				bw.write(rs.getString(1));
				bw.write("</id>\n");
				
				bw.write("\t\t<name>");
				bw.write(rs.getString(2));
				bw.write("</name>\n");
				
				bw.write("\t\t<country>");
				bw.write(rs.getString(3));
				bw.write("</country>\n");
				
				bw.write("\t\t<sport>");
				bw.write(rs.getString(4));
				bw.write("</sport>\n");
				
				bw.write("\t\t<gold>");
				bw.write(rs.getString(5));
				bw.write("</gold>\n");
				
				bw.write("\t\t<continent>");
				bw.write(rs.getString(6));
				bw.write("</continent>\n");
				
				bw.write("\t</Athlete>\n");
			}
			
			bw.write("</Athletes>");
			bw.close();
		}
		catch (IOException | SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void PrintAthlete()
	{
		for (int i=1;i < athleteList.size();i++)
		{
			Athlete a = athleteList.get(i);
			System.out.println(" Name:" + a.GetName() + " Country:" + a.GetCountry() + " Sport:" + a.GetSport() + " Golds:" + a.GetNumOfGold());
		}
	}
	
	public static void PrintMedals()
	{
		for (int i=0;i < countryMedalsList.size();i++)
		{
			CountryMedals cm = countryMedalsList.get(i);
			System.out.println(" Country:" + cm.GetName() + " Year:" + cm.GetYear() + " Golds:" + cm.GetGold() + " Silvers:" + cm.GetSilver() + " Bronzes:" + cm.GetBronze());
		}
	}
	
	// main function
	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException 
	{
		// read athletes data from XML files
		xmlFile = "Athletes.xml";
		ReadXML(xmlFile);
		//PrintAthlete();
		// read country medals data from XML files
		xmlFile = "Summer Olympics.xml";
		ReadXML(xmlFile);
		//xmlFile = "Winter Olympics.xml";
		//ReadXML(xmlFile);
		// connect to mysql
		ConnectToDB();
		// create tables and insert data into mysql
		WriteToDB();
		// write to xml
		WriteToXML();
		// close connection
		CloseConnection();
	}
	
}
