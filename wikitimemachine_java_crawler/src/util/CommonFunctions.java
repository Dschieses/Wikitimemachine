package util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * 
 * This class contains all common functions, which are used in the unit package and are not bound to an object.
 *
 */
public class CommonFunctions {
/**
 * The methods create a Json object from a String
 * @param input a String, which is converted to a Json object
 * @return Json object which represents the input String 
 *
 */
	public static JSONObject getJSON(String input) throws JSONException {
		return new JSONObject(input);
	}
/**
 * The method returns a subJson object from a Json object.
 * @param json A Json object which contains a subJson
 * @param subObject The name of the subJson object
 * @return a Json object which matches to the name of the subJson object
 * 
 */
	public static JSONObject getSubJSON(JSONObject json, String subObject)
			throws JSONException {
		return json.getJSONObject(subObject);
	}

	/**
	 * The method encodes an URL name into URL standard format.
	 * @param input A text which should be converted to URL format
	 * @return URL formatted text
	 * 
	 */
	public static String getEncoded(String input)
			throws UnsupportedEncodingException {
		return URLEncoder.encode(input, "UTF-8");
	}
/**
 * The method prints current time stamp into the standard console. This is mainly used for observing the progress of a specific application process.
 */
	public static void printCurrentTimestamp() {
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
				.format(new Date()));
	}
/**
 * The method creates a simple object of the current date. 
 * @return An object of the current date.
 */
	public static String returnCurrentTimestamp() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
				.format(new Date());
	}
/**
 * The method splits a list into a list of a specified length and a remainder if exists
 * @param list a list which should be splitted
 * @param numberOfLists a number of objects which should build a sublist
 * @return a list of lists: a cropped list and if exists a remainder
 */
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
