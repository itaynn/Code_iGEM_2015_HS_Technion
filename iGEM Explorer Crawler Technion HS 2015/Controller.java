package crawler;

public class Controller {
	// the main function of the programme.
	public static void main(String[] args) throws Exception {

		
		// Recommended course of action:
		HtmlOperators.downloadAllFromInternetFullScan("data//1");
		HtmlOperators.processYearsOneByOne("data//1");
		HtmlOperators.download2011HS("data//hs1");
		HtmlOperators.processFromFile("data//hs1", "2011");
		
		
		//example for usage of completeProcessOneYearResultsSeeds(String , int[] )
		//int[] comps={5,6,8,10,11};
		//HtmlOperators.completeProcessOneYearResultsSeeds("data//6",comps);
		
		// download info pages
		// HtmlOperators.getInfoPages(1,2000);

	}
}