import util.ApiCaller;

public class Crawler {

	public static void main(String[] args) throws Exception {
		// String a =
		// "http://de.wikipedia.org/w/api.php?format=json&action=query&list=allpages&apfilterredir=nonredirects&aplimit=100&apfrom=%EA%9E%A8";
		String a = "http://de.wikipedia.org/w/api.php?format=json&action=query&list=allpages&apfilterredir=nonredirects&aplimit=500&apfrom=";
		ApiCaller api = new ApiCaller();
		api.start(a);

	}
}
