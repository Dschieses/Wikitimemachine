/*
 * Created Oct 2014 - Feb 2015 during COINS
 * by Peter Praeder, Michael Koetting, Vladimir Trajt
 */
package util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Class CommonFunctions contains functions which are often used
 */
public class CommonFunctions {

	/**
	 * Gets the json of a string input
	 *
	 * @param input
	 *            the input
	 * @return the json
	 * @throws JSONException
	 *             the JSON exception
	 */
	public static JSONObject getJSON(String input) throws JSONException {
		return new JSONObject(input);
	}

	/**
	 * Gets the subjson of a string input
	 *
	 * @param json
	 *            the json
	 * @param subObject
	 *            the sub object
	 * @return the sub json
	 * @throws JSONException
	 *             the JSON exception
	 */
	public static JSONObject getSubJSON(JSONObject json, String subObject) throws JSONException {
		return json.getJSONObject(subObject);
	}

	/**
	 * Gets the URLEncoding of a string.
	 *
	 * @param input
	 *            the input
	 * @return the encoded
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	public static String getEncoded(String input) throws UnsupportedEncodingException {
		return URLEncoder.encode(input, "UTF-8");
	}

	/**
	 * Prints the current timestamp.
	 */
	public static void printCurrentTimestamp() {
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
	}

	/**
	 * Return current timestamp.
	 *
	 * @return the string
	 */
	public static String returnCurrentTimestamp() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
	}

	/**
	 * Splits a very long list into equal parts
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the list
	 * @param numberOfLists
	 *            the number of lists
	 * @return the list
	 */
	public static <T> List<List<T>> split(List<T> list, int numberOfLists) {
		if (list == null) {
			throw new NullPointerException("The list parameter is null.");
		}
		if (numberOfLists <= 0) {
			throw new IllegalArgumentException("The number of lists parameter must be more than 0.");
		}

		int sizeOfSubList = list.size() / numberOfLists;
		while (sizeOfSubList == 0) {
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
