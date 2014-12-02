package util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import entity.Category;
import entity.Page;

public class CategoryApi {

	private final String CATMEMBERS = "http://de.wikipedia.org/w/api.php?action=query&cmlimit=500&format=json&list=categorymembers&cmtitle=Category:%s&cmcontinue=%s";
	PageApi p;
	private String cmcontinue = "";
	JSONObject json = null;

	public CategoryApi() {
		p = new PageApi();
	}

	public List<Category> getCategories(Page p) throws Exception {
		String query = "http://de.wikipedia.org/w/api.php?format=json&action=query&prop=categories&cllimit=500&pageids=";
		HttpUtil.getInstance();
		String result = HttpUtil.sendGet(query + p.getPageid());
		JSONObject json = CommonFunctions.getJSON(result);
		JSONArray jsonArray = CommonFunctions.getSubJSON(
				CommonFunctions.getSubJSON(
						CommonFunctions.getSubJSON(json, "query"), "pages"),
				String.valueOf(p.getPageid())).getJSONArray("categories");
		List<Category> returnList = new ArrayList<>();
		for (int i = 0; i < jsonArray.length(); i++) {
			Gson gson = new Gson();
			Category c = gson.fromJson(jsonArray.getJSONObject(i).toString(),
					Category.class);

			returnList.add(c);
		}
		return returnList;

	}

	public List<Page> getCategoryMembers(Category c) throws Exception {
		List<Page> list = new ArrayList<Page>();
		Runtime rt = Runtime.getRuntime();

		do {
			String query = String.format(CATMEMBERS, c.getTitle(),
					CommonFunctions.getEncoded(cmcontinue));
			HttpUtil.getInstance();
			String result = HttpUtil.sendGet(query);
			json = CommonFunctions.getJSON(result);
			list.addAll(p.getPageInfoFromCategoryList(json));
			System.out.println(rt.totalMemory() - rt.freeMemory());
		} while (getCmContinue());
		return list;
	}

	public boolean getCmContinue() throws JSONException {
		if (!json.has("query-continue")) {
			cmcontinue = "undefined";
			return false;
		}
		cmcontinue = CommonFunctions.getSubJSON(json, "query-continue")
				.getJSONObject("categorymembers").getString("cmcontinue");

		return true;
	}
}
