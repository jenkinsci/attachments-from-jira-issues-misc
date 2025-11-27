import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Utils {
	public static Properties P = new Properties();
	static
	{
		Utils.loadProperties(".//test//UnitTests.properties");
	}
	
	public static void  loadProperties(String propertyFile)
	{
		try
		{
			FileInputStream input= new FileInputStream(new File(propertyFile));
			P.load(input );
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
