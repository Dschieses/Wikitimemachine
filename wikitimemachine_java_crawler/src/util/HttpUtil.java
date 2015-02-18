package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
/**
 * 
 * The class contains a method to build a connection to a http web page and to read its contents.
 *
 */
public class HttpUtil {
/**
 * A method to build a connection to a http web page and to read its contents.
 * @param url an URL of the web page
 * @return contents of the web page in text form
 * 
 */
	public String sendGet(String url) throws IOException {
		HttpClient client = null;
		HttpGet request = null;
		HttpResponse response = null;

		while (response == null) {
			try {
				client = HttpClientBuilder.create().build();
				request = new HttpGet(url);
				response = client.execute(request);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Try again");
				try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("Thread sleep interrupted");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Try again");
				try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("Thread sleep interrupted");
				}
			}
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String inputLine = "";
		StringBuffer stringBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			stringBuffer.append(inputLine);
		}
		in.close();
		return stringBuffer.toString();

	}

}