package util;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpUtil {

	public String sendGet(String url) throws Exception {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);

		
		BufferedReader in = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent()));
		String inputLine="";
		StringBuffer stringBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			stringBuffer.append(inputLine);
		}
		in.close();
		return stringBuffer.toString();

	}

}