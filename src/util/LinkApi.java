package util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import entity.Page;

public class LinkApi {
	private String gplcontinue = "";
	private final String LINKS = "http://de.wikipedia.org/w/api.php?action=query&format=json&pageids=%s&generator=links&gpllimit=500&%s";
	JSONObject json = null;
	PageApi p = new PageApi();

	public LinkApi() {

	}

	public boolean getGPLcontinue(JSONObject json) throws JSONException {
		if (!json.has("query-continue")) {
			gplcontinue = "undefined";
			return false;
		}
		gplcontinue = CommonFunctions.getSubJSON(json, "query-continue")
				.getJSONObject("links").getString("gplcontinue");

		return true;
	}

	public List<Page> getOutgoingLinks(Page page) throws Exception {
		List<Page> list = new ArrayList<Page>();

		do {
			String query;
			if (gplcontinue.equals("")) {
				query = String.format(LINKS, page.getPageid(), "");
			} else {
				query = String.format(LINKS, page.getPageid(), CommonFunctions
						.getEncoded("gplcontinue=" + gplcontinue));

			}

			HttpUtil.getInstance();
			String result = HttpUtil.sendGet(query);
			json = CommonFunctions.getJSON(result);
			list.addAll(p.getPageInfoFromLinkList(json));

		} while (getGPLcontinue(json));
		return list;
	}
	
}
