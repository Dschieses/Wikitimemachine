package util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
				.format(new Date()));
	}

	public static String returnCurrentTimestamp() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
				.format(new Date());
	}

	public static <T> List<List<T>> split(List<T> list, int numberOfLists) {
		if (list == null) {
			throw new NullPointerException("The list parameter is null.");
		}
		if (numberOfLists <= 0) {
			throw new IllegalArgumentException(
					"The number of lists parameter must be more than 0.");
		}

		int sizeOfSubList = list.size() / numberOfLists;
		while(sizeOfSubList==0)
		{
			numberOfLists--;
			sizeOfSubList = list.size() / numberOfLists;
		}
		int remainder = list.size() % numberOfLists;

		List<List<T>> subLists = new ArrayList<List<T>>();

		for (int i = 0; i < numberOfLists; i++) {
			int from = i * sizeOfSubList;
			int to = Math.min((i + 1) * sizeOfSubList, list.size());
			subLists.add(list.subList(from, to));
		}
		if (remainder > 0) {
			subLists.add(list.subList(list.size() - remainder, list.size()));
		}

		return subLists;
	}
}
