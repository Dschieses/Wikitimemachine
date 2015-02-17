package util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entity.Category;
import entity.Person;

public class CategoryApi {

	private final String CATMEMBERS = "http://de.wikipedia.org/w/api.php?action=query&cmlimit=500&format=json&list=categorymembers&cmtitle=Category:%s&cmcontinue=%s";
	private final String CATEGORIES ="http://de.wikipedia.org/w/api.php?format=json&action=query&prop=categories&cllimit=500&pageids=%s";
	PageApi p;
	private String cmcontinue = "";
	JSONObject json = null;

	public CategoryApi() {
		p = new PageApi();
	}
/**
 * The method imports categories for each identified person from the Wikipedia web page to the person object.
 * @param p The person object where the categories will be temporaly stored.
 */
	@SuppressWarnings("unchecked")
	public void getCategories(Person p) throws Exception {
		HttpUtil h = new HttpUtil();
		String result =h.sendGet(String.format(CATEGORIES,p.getPageid()));
		
		JSONObject json = CommonFunctions.getJSON(result);
		JSONArray jsonArray = CommonFunctions.getSubJSON(
				CommonFunctions.getSubJSON(
						CommonFunctions.getSubJSON(json, "query"), "pages"),
				String.valueOf(p.getPageid())).getJSONArray("categories");
		Gson gson = new Gson();
		p.setCategoryList((List<Category>)gson.fromJson(jsonArray.toString(),new TypeToken<List<Category>>() {}.getType()));

	}
/**
 * The method gets a list of people who belong to a certain category from Wikipedia.
 * @param c The category which is common to the result list of people
 * @return The list of people who belong to a certain category
 * 
 */
	public List<Person> getCategoryMembers(Category c) throws Exception {
		List<Person> list = new ArrayList<Person>();
		

		do {
			String query = String.format(CATMEMBERS, CommonFunctions.getEncoded(c.getTitle()),
					CommonFunctions.getEncoded(cmcontinue));
			HttpUtil h = new HttpUtil();
			String result = h.sendGet(query);
			json = CommonFunctions.getJSON(result);
			list.addAll(p.getPageInfoFromCategoryList(json));
			
		} while (getCmContinue());
		return list;
	}

	/**
	 * A method for determining whether a json query has more results
	 * @return The method returns false if the end is reached and true if the end is not reached
	 */
	public boolean getCmContinue() throws JSONException {
		if (!json.has("query-continue")) {
			cmcontinue = "";
			return false;
		}
		cmcontinue = CommonFunctions.getSubJSON(json, "query-continue")
				.getJSONObject("categorymembers").getString("cmcontinue");

		return true;
	}
}
