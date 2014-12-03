package util;

import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import entity.Page;

public class LinkApi {
	private String plcontinue = "";
	private final String LINKS = "http://de.wikipedia.org/w/api.php?action=query&prop=links&format=json&pllimit=500&pageids=%s&%s";
	JSONObject json = null;
	PageApi p = new PageApi();

	public LinkApi() {

	}

	public boolean getPLcontinue(JSONObject json) throws JSONException {
		if (!json.has("query-continue")) {
			plcontinue = "";
			return false;
		}
		plcontinue = CommonFunctions.getSubJSON(json, "query-continue")
				.getJSONObject("links").getString("plcontinue");

		return true;
	}

	public void getOutgoingLinks(Page page) throws Exception {
		List<Page> list = new ArrayList<Page>();

		do {
			String query;
			if (plcontinue.equals("")) {
				query = String.format(LINKS, page.getPageid(), "");
			} else {
				query = String.format(LINKS, page.getPageid(),"plcontinue="+ CommonFunctions
						.getEncoded(plcontinue));
				

			}
			HttpUtil h = new HttpUtil();
			String result = h.sendGet(query);
			
			json = CommonFunctions.getJSON(result);
			list.addAll(p.getPageInfoFromLinkList(json,page));

		} while (getPLcontinue(json));
		page.setLinkList(list);
	}
	
}
