/**
 * @author Itay Naor and Tomer Shani
 * From the Technion HS 2015 iGEM Team
 * Released under The MIT License. For further information, see LICENSE.txt
 */
package crawler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TeamResults {

	// String Name; //team name
	// int[] Medal; //1 stands for gold, 2 silver 3 bronze
	// List<String> Awards; //
	// List<Product> Products; //links to teams final products (presentation,
	// video and poster)
	String Name; // team name
	int Medal; // 1 stands for gold, 2 silver 3 bronze 0 none
	List<String> Awards; // List of awards
	Product Products; // links to teams final products (presentation, video and
						// poster)
	int Year; // The year in which the group participated in iGEM
	String Division; //one of the following: iGEM, High School and entrepreneur.

	// constructor
	public TeamResults(String name, int medal, List<String> awards, Product products) {
		this.Name = name;
		this.Medal = medal;
		this.Awards = awards;
		this.Products = products;
	}
//null constructor
	public TeamResults() {
		this.Name = null;
		this.Medal = 0;
		this.Awards = new ArrayList<String>();
		this.Products = new Product();
	}
// retrieve the results pages from the web, starting from a page all the results pages are linked
	// from (igem.org/Results should suffice)
	// saves the result as an html file.
	// a result page from the year x and the division y is saved to: x_y.html
	public static List<String> getResultsPages(String base_url, String folder) {
List<String> pagesUrls=new ArrayList<String>();
File fd=new File(folder);
if (!fd.exists()) {
	fd.mkdirs();
}
		try {
			Document base_page = Jsoup.connect(base_url).timeout(10 * 1000).get();
			Elements years_a = base_page.select("div:containsOwn(Change) > a");
			for (int i = 0; i <= years_a.size(); i++) {
				
				if (HtmlOperators.isInteger(years_a.eq(i).text())) {
					String url_year = years_a.eq(i).attr("href");
					try {
						String year_full_url = "http://igem.org/" + url_year;
						Document year_page = Jsoup.connect(year_full_url).timeout(10 * 1000).get();
						Elements divisions_a = year_page.select("div:containsOwn(Division:) > a");
						for (int j = 0; j <= divisions_a.size(); j++) {
							String url_divisions = divisions_a.eq(j).attr("href");
							try {
								Document page = Jsoup.connect("http://igem.org/" + url_divisions).timeout(10 * 1000)
										.get();
								if (!page.select("title").isEmpty()) {
									String Title = page.select("title").first().text();
									if (!Title.startsWith("Jamboree Results")) {
										continue;
									}
									
									String file = folder + years_a.eq(i).text() + "_" + divisions_a.eq(j).text()
											+ ".html";
//									File f=new File(file);
//									if (!f.exists()) {
//										f.createNewFile();
//									}
									HtmlOperators.writeToFile(new File(file), page.html());
									pagesUrls.add("http://igem.org/" + url_divisions);
									// tis.add(ti);
									TimeUnit.MILLISECONDS.sleep(300);
								}
							} catch (IOException e) {
							} catch (InterruptedException e) {
							}
						}
					} catch (IOException e) {
					}
				}

			}

		} catch (IOException e) {
		}
return pagesUrls;
	}
// reads the html files of all the result pages from all the years which have been retrieved.
	// get the year and division from the file name.
	// each read result page is sent to inferTeamResults.
	static List<List<TeamResults>> readTeamResults(String folderName) {
		
		File folder = new File(folderName);
		File[] listOfFiles = folder.listFiles();
		List<List<TeamResults>> results_over_years = new ArrayList<List<TeamResults>>();
		if(listOfFiles.length==0) System.out.println("No results files were found");
		for (File file : listOfFiles) {
			if (file.isFile()) {
				String html = HtmlOperators.ReadText(file.getAbsolutePath());
				if(html==null) continue;
				String st = file.getName();
				String nm_prts = st.substring(0, st.length() - 5);
				String[] nm_subparts = nm_prts.split("_");

				int year = Integer.parseInt(nm_subparts[0]);
				String divsn = nm_subparts[1];
				List<TeamResults> tr = new ArrayList<TeamResults>();
				tr = InferTeamAwards(year, "", html, divsn);
				results_over_years.add(tr);
			}

		}

		return results_over_years;
	}
// infers the following data about each team from the results page:
	//medals
	//awards
	//presentation, video, poster
//keeps the latest result for each group, ie. the presentation from the jamboree is taken where available
// and so on.
	public static List<TeamResults> InferTeamAwards(int year, String url, String html, String division) {
		
		Document doc = Jsoup.parse(html);

		Elements Names_div = doc.select(".teambar");
		Elements Names_td = new Elements();

		TeamResults[] tr = new TeamResults[Names_div.size()];

		for (int i = 0; i < Names_div.size(); i++) {
			Names_td.add(Names_div.eq(i).parents().first());
		}

		for (int i = 0; i < Names_td.size(); i++) {
			tr[i] = new TeamResults();
			// name
			Element name_td = Names_td.eq(i).first().select("a").first();
			tr[i].Name = name_td.attr("href").split("Team:")[1];
			tr[i].Name=tr[i].Name.replace('_', ' ');
			// medal
			Element medal_td = Names_td.eq(i).first().select("img").first();
			if (medal_td == null) {
				tr[i].Medal = 0;
			} else if (medal_td.attr("alt").equals("gold medal,")) {
				tr[i].Medal = 1;
			} else if (medal_td.attr("alt").equals("silver medal,")) {
				tr[i].Medal = 2;
			} else if (medal_td.attr("alt").equals("bronze medal,")) {
				tr[i].Medal = 3;
			} else
			{
				tr[i].Medal = 0;
			}
			
			// awards
			Elements awards_td = Names_td.eq(i).first().select(".awardbar > p");
			for (int j = 0; j < awards_td.size(); j++) {
				tr[i].Awards.add(awards_td.eq(j).first().ownText());
				// System.out.println(awards_td.eq(j).first().ownText());
			}
			// products
			Element products_td = Names_td.eq(i).first().select(".resulticons").last();
			Product temp_product = new Product();
			Elements as=products_td.select("a[href]");
			temp_product.Pres = as.eq(0).attr("href");
			temp_product.Post = as.eq(1).attr("href");
			temp_product.Vid = as.eq(2).attr("href");
			//temp_product.Vid = Names_td.eq(i).first().select(".championshipbar").select("div > a:eq(3)").attr("href");
			tr[i].Products = temp_product;

		}
		List<TeamResults> filtered_tr = new ArrayList<TeamResults>();
		for (int k = 0; k < tr.length; k++) {
			boolean already_in = false;
			for (TeamResults x : filtered_tr) {
				if (x.Name == tr[k].Name) {
					already_in = true;
					break;
				}
			}
			if (!already_in) {
				tr[k].Year = year;
				tr[k].Division = division;
				filtered_tr.add(tr[k]);

			}
		}
		return filtered_tr;
	}

	// obselete function. This functionality migrated to HtmlOperators.getPrintData, although you still can use
		// this function if you want to print only the teamResults pages.
	public static void prepareTeamResultsPrintData(List<List<TeamResults>> results, List<List<PrintData>> llpt) {
		// gets team results class and uses SaveToXML to print to file the
		// important fields.

		//List<List<PrintData>> llpt = new ArrayList<List<PrintData>>();
		while (!results.isEmpty()) {
			List<TeamResults> ltr = results.remove(0);
			while (!ltr.isEmpty()) {
				TeamResults tr = ltr.remove(0);
				List<PrintData> lpt = new ArrayList<PrintData>();
				//lpt.add(new PrintData("name", tr.Name));
				lpt.add(new PrintData("medal", Integer.toString(tr.Medal)));
				lpt.add(new PrintData("year", Integer.toString(tr.Year)));
				for (int i = 0; i < tr.Awards.size(); i++) {
					lpt.add(new PrintData("award", (tr.Awards.get(i))));
				}
				lpt.add(new PrintData("presentation", tr.Products.Pres));
				lpt.add(new PrintData("poster", tr.Products.Post));
				lpt.add(new PrintData("video", tr.Products.Vid));
				llpt.add(lpt);
			}

		}
		//HtmlOperators.SaveToXML(llpt, file);
	}
}
