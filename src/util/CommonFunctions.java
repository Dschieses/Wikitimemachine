package util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class CommonFunctions {

	public static JSONObject getJSON(String input) throws JSONException {
		return new JSONObject(input);
	}

	public static JSONObject getSubJSON(JSONObject json, String subObject)
			throws JSONException {
		return json.getJSONObject(subObject);
	}

	public static String getEncoded(String input)
			throws UnsupportedEncodingException {
		return URLEncoder.encode(input, "UTF-8");
	}

	public static void printCurrentTimestamp() {
		System.out.println(new SimpleDateFormat("HH:mm:ss.SSS")
				.format(new Date()));
	}
}
