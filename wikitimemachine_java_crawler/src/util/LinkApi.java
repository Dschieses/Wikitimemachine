/*
 * Created Oct 2014 - Feb 2015 during COINS
 * by Peter Praeder, Michael Koetting, Vladimir Trajt
 */
package util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import entity.Person;

/**
 * The Class LinkApi.
 */
public class LinkApi {

	/** The pagination index. */
	private String plcontinue = "";

	/** The API link. */
	private final String LINKS = "http://de.wikipedia.org/w/api.php?action=query&prop=links&format=json&pllimit=500&pageids=%s&%s";

	/** The json. */
	JSONObject json = null;

	/** The p. */
	PageApi p = new PageApi();

	/**
	 * Instantiates a new link api.
	 */
	public LinkApi() {

	}

	/**
	 * Gets the paginating index for multiple results
	 *
	 * @param json
	 *            the json
	 * @return the p lcontinue
	 * @throws JSONException
	 *             the JSON exception
	 */
	public boolean getPLcontinue(JSONObject json) throws JSONException {
		if (!json.has("query-continue")) {
			plcontinue = "";
			return false;
		}
		plcontinue = CommonFunctions.getSubJSON(json, "query-continue").getJSONObject("links").getString("plcontinue");

		return true;
	}

	/**
	 * Gets the outgoing links from a specific page
	 *
	 * @param page
	 *            the page
	 * @return the outgoing links
	 * @throws Exception
	 *             the exception
	 */
	public void getOutgoingLinks(Person page) throws Exception {
		List<Person> list = new ArrayList<Person>();

		do {
			String query;
			if (plcontinue.equals("")) {
				query = String.format(LINKS, page.getPageid(), "");
			} else {
				query = String.format(LINKS, page.getPageid(), "plcontinue=" + CommonFunctions.getEncoded(plcontinue));

			}
			HttpUtil h = new HttpUtil();
			String result = h.sendGet(query);

			json = CommonFunctions.getJSON(result);
			list.addAll(p.getPageInfoFromLinkList(json, page));

		} while (getPLcontinue(json));
		page.setLinkList(list);
	}

}
