import java.io.InputStream;
import java.util.Properties;

public class Utils {

	public static Properties P = new Properties();
	public static InputStream in = Utils.class.getClassLoader().getResourceAsStream("UnitTests.properties");
	
	
	static 
	{
	
		try
		{
			P.load(in);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
		
	public String getProperty(String prop)
	{
		return(P.getProperty(prop));
	}

}
