import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIAccess {
	public static void main(String[] args) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/job/asd/api/xml").openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line = in.readLine();
		while (null != line) {
			System.out.println(line);
			line = in.readLine();
		}
	}
}
