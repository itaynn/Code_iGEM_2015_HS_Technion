/**
 * @author Itay Naor and Tomer Shani
 * From the Technion HS 2015 iGEM Team
 * Released under The MIT License. For further information, see LICENSE.txt
 */
package crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
// class for the data downloaded from the wikis and related functions.
public class WikiData {

	// docid - id of the document
	// Example for the difference between url, domain, subdomain and path:
	// for url="http://igem.org/Team_Wikis?year=2014", the domain is
	// "igem.org", the path is "/Team_Wikis" and there is no subdomain
	// (subdomain is 2014 in 2014.igem.org)
	// parentUrl is the url of the page that've led you to this page
	// anchor is the text of the link that've led you to this page.
	// text is the text in the page
	// html is the html of the page
	// links is a list of outgoing links
	// category is the category the algorithm matched to the wikipage.
	// tags isn't used currently.
	// ordinal number is used as a name for the file.
	int docid;
	String url;
	String path;
	String domain;
	String subDomain;
	String parentURL;
	String anchor;
	String text;
	String html;
	String[] Links;
	String category;
	String teamName;
	List<String> tags;
	String logo_url;
	int ordinalNumber;
boolean hasConstants;
//retrieve the wiki pages from the web, starting from the seeds that are listed in crawling_seeds
	// currently the crawler isn't resumable, but if you want it
	// it to be, uncomment line (1)
	// saves the wikipages as explained before MyCrawler.printCrawlDataToFile.
	// a result page from the year x and the division y is saved to: x_y.html

	public static void getWikiPages(String[] crawling_seeds, String folder) throws Exception {
		// TODO Auto-generated method stub
		String crawlStorageFolder = ".temp1";
		int numberOfCrawlers = 4;

		CrawlConfig config = new CrawlConfig();

//(1)	config.setResumableCrawling(true);
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setUserAgentString("Technion HS 2015 iGEM Team's Crawler (2015.igem.org/Team:Technion_HS_Israel)");
		// config.setMaxDepthOfCrawling(-1);
		// config.setMaxPagesToFetch(50);
		// crawlConfig.setPolitenessDelay(200);
		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		for (int i = 0; i < crawling_seeds.length; i++) {
			if (!crawling_seeds[i].equals(""))
				controller.addSeed(crawling_seeds[i]);
		}
		MyCrawler.Folder = folder;
		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(MyCrawler.class, numberOfCrawlers);

	}
		// reads the files of all the wiki pages from all the years that have been retrieved.
		// get all the information that was saved. 
		// cleans the texts by removing:
			// \r, \t, \f, \n. the first three are replaced with a white space.
			// sequences of 2 or more consecutive white spaces are trimmed to a single one.
			// if the beginning of a text from a certain page from a wiki of a certain team is identical to an other page from the wiki, only one of them
			//	 keeps it and the common beginning of the second is removed. Helps to remove menu bars. The same goes for the ending of the texts.
			// the text before the first occurrence of 'Log In' is trimmed, and the text after the last 'Recent Changes' is trimmed too.
	public static List<String[]> readTeamWikis(String folderName, List<WikiData> wds) {
		// TODO Auto-generated method stub
		Map<String,Integer> wiki_examples=new HashMap<String,Integer>();
		Map<String,Tuple> wiki_examples_limits=new HashMap<String,Tuple>();
		ArrayList<File> files = new ArrayList<>();
		HtmlOperators.listf(folderName, files);
		Set<Integer> numbersDone = new HashSet<Integer>();
		int index=0;
		for (File f : files) {
			if (f.isFile()) {

				String st_name = f.getName();
				String nm_prts = st_name.substring(0, st_name.lastIndexOf("."));
				int num = Integer.parseInt(nm_prts);
				if (numbersDone.contains(num))
					continue;
				numbersDone.add(num);
				String full_path = f.getAbsolutePath();
				int dot_ind = full_path.lastIndexOf(".");
				String partial_path = full_path.substring(0, dot_ind);
				WikiData wd = new WikiData();
				String html_file;
				html_file = HtmlOperators.ReadText(partial_path + ".html");
				wd.text = HtmlOperators.ReadText(partial_path + ".txt");
				String links_file = HtmlOperators.ReadText(partial_path + ".links");

				wd.Links = links_file.replace("\r","").split("\n");
				int ln1 = html_file.indexOf("\n");
				int ln2 = html_file.indexOf("\n", ln1 + 1);
				int ln3 = html_file.indexOf("\n", ln2 + 1);
				int ln4 = html_file.indexOf("\n", ln3 + 1);
				int ln5 = html_file.indexOf("\n", ln4 + 1);
				int ln6 = html_file.indexOf("\n", ln5 + 1);
				int ln7 = html_file.indexOf("\n", ln6 + 1);
				if(ln1==-1)
				{
					System.out.println("That's bad.");
				}

					wd.url = html_file.substring(0, ln1);
					wd.domain = html_file.substring(ln1 + 1, ln2);
					wd.path = html_file.substring(ln2 + 1, ln3);
					wd.subDomain = html_file.substring(ln3 + 1, ln4);
					wd.parentURL = html_file.substring(ln4 + 1, ln5);
					wd.anchor = html_file.substring(ln5 + 1, ln6);
					wd.html = html_file.substring(ln7);
					wd.ordinalNumber = num;
					wd.teamName = wd.path.split(":")[1].split("/")[0];
					
					
					if(wiki_examples.containsKey(wd.subDomain+"$"+wd.teamName))
					{
						int ex_i=wiki_examples.get(wd.subDomain+"$"+wd.teamName);
						String ex=wds.get(ex_i).text;
						int min_len=Math.min(wd.text.length(),ex.length());
						int counter_start=0,counter_end=0;
						for(int i = 0; i < min_len; i++)
						{
						        if (ex.charAt(i) == wd.text.charAt(i));
						        else
						        {
									counter_start=i;
									break;
						        }
						}
						for(int i = 0; i < min_len; i++)
						{
						        if (ex.charAt(ex.length()-i-1) == wd.text.charAt(wd.text.length()-i-1));
						        else
						        {
						        	counter_end=wd.text.length()-i-1;
									break;
						        }
						}
						int lgin=wd.text.indexOf("Log in");
						if(counter_start<lgin)counter_start=lgin; 
						int rcnt=wd.text.lastIndexOf("Recent changes");
						if(counter_end>rcnt&&rcnt>0)counter_end=rcnt;
						if(counter_start>counter_end-50)
						{
							continue;
						}
						if(counter_start<0||counter_end<0||counter_start>counter_end||counter_end>wd.text.length())
						{
							continue;
						}
						wd.text=wd.text.substring(counter_start, counter_end);
						wd.text=wd.text.replace("\r"," ");
						wd.text=wd.text.replace("\n","");
						wd.text=wd.text.replace("\t"," ");
						wd.text=wd.text.replace("\f"," ");
						
						wd.text=wd.text.trim().replaceAll(" +", " ");
						if(!wiki_examples_limits.containsKey(wd.subDomain+"$"+wd.teamName))
						{
							wiki_examples_limits.put(wd.subDomain+"$"+wd.teamName,new Tuple(counter_start,counter_end));
						}
					}
					else
					{
//							wd.text=wd.text.replace("\n\t    \n\t  \n\t\n\n\n\n \t\n \t  Page               Discussion               View source               History               teams\n \t \n\n\t\n\t    Log in\n\t\n\n\t\n\t\t\n\t\t\n\t\t\t  \n\t\t       \n\t\t\n\t \n     \n    ","");
//							wd.text=wd.text.replace("\n\t\t\t\t\t\t\n\t\t\t\n\t\t\n    \n\n\n    \n        \n               \t       \t     Recent changesWhat links hereRelated changesSpecial pages\n\t                 My preferences\n         \n        \n\t     Printable version\n\t             Permanent link\n\t             Privacy policyDisclaimers\n","");
						wiki_examples.put(wd.subDomain+"$"+wd.teamName,index);
					}
					
					wds.add(wd);
					index++;

			}

		}
		for(int x:wiki_examples.values())
		{
			WikiData ewd=wds.get(x);
			ewd=ewd.modifyText(wds.get(x));
			ewd.text=ewd.text.replace("\r","");
			ewd.text=ewd.text.replace("\n"," ");
			ewd.text=ewd.text.replace("\t"," ");
			ewd.text=ewd.text.replace("\f"," ");
			ewd.text=ewd.text.trim().replaceAll(" +", " ");
			wds.set(x, ewd);
			}
		List<String[]> constTable = inferTeamWikiDatas(wds);

		return constTable;

	}

	private WikiData modifyText(WikiData wikiData) {
		// TODO Auto-generated method stub
		wikiData.text=wikiData.text.replace("\n\t    \n\t  \n\t\n\n\n\n \t\n \t  Page               Discussion               View source               History               teams\n \t \n\n\t\n\t    Log in\n\t\n\n\t\n\t\t\n\t\t\n\t\t\t  \n\t\t       \n\t\t\n\t \n     \n    \n\t\t\n\t\t\t\tTeam:Acton-BoxboroughRHS/Research\n\t\t\n\t\t\t","");
		wikiData.text=wikiData.text.replace("\n\t\t\t\t\t\t\n\t\t\t\n\t\t\n    \n\n\n    \n        \n               \t       \t     Recent changesWhat links hereRelated changesSpecial pages\n\t                 My preferences\n         \n        \n\t     Printable version\n\t             Permanent link\n\t             Privacy policyDisclaimers\n","");
		int lgin=wikiData.text.indexOf("Log in");
		
		int rcnt=wikiData.text.lastIndexOf("Recent changes");
		
		if(lgin<0)
		{
			lgin=0;
		}
		if(rcnt<0)
		{
			rcnt=wikiData.text.length()-1;
		}
		 
		wikiData.text=wikiData.text.substring(lgin, rcnt);
		return wikiData;
	}
// infers the following properties about each page using the url and the anchor:
	//category
		//taken from the following list. This list describes the priority
		// order (if two categories match, the first one will be taken)
			// home
			// map (site map)
			//const (parameters page)
			//software
			//policy (AKA human practice)
			//judging forms
			//parts (biobricks)
			//safety
			//blog
			//team (team page)
			//notebook
			//bio (biology)
			//model
			//results
			//sponsor (sponsors page)
			//attributes
			//overview (project overview/introductions)
			//other (if didn't find a matching category)
	// logo.
		//tries to find a logo on the home page. Pretty primitive right now (it looks only for images
		//that have 'logo' in their file name.) TODO: Make more robust.
// for each constants page, it tries to extract the constants tables on the page.
	//works pretty well, although misses some pages because of the following:
		// the constants table is in a picture which is inaccesible to the crawler.
		// The constants are in a pdf, and currentyly pdfs are not crawled,
			//and even if crawled, the table structure isn't preserved, making it
			// imposible to crawl.
		// The constants are textual, but not in table.
	//each constants table is read and a the meaning of the columns is deduced from the from
		//the header row, from the following list:
			//name
			//description
			//value
			//units
			//value max
			//reference
			//sensitivity analysis results.
			
			
	// TODO: crawl images and pdfs, use OCR the extract text. Add constants extraction
		// from lists. Make it more robust. 
	public static List<String[]> inferTeamWikiDatas(List<WikiData> wds) {
		int number_of_const_attributes = 7;
		// Set<Integer> found = new HashSet<Integer>();
		Set<String> mp = new HashSet<String>();
		List<String[]> constTable = new ArrayList<String[]>();
		for (WikiData wd : wds) {
			if (wd.url.startsWith("/wiki/")) {
				wd.url = wd.subDomain + ".igem.org" + wd.url;
			}
			wd.teamName = wd.path.split(":")[1].split("/")[0];
			wd.teamName = wd.teamName.replace('_', ' ');
			Document doc = Jsoup.parse(wd.html);
			String addition = wd.anchor.toLowerCase();
			
			if (wd.anchor.startsWith("http:"))
				addition = "";
			int off = wd.path.indexOf("/", 3);

			String url = wd.path.substring(off + 1).toLowerCase() + "|" + addition;
			if (off == -1) {
				wd.category = "home";
			} else if (url.contains("map") && (url.contains("site")||url.contains("wiki"))) {
				wd.category = "map";
			} else if (url.contains("constant") || url.contains("parameter")) {
				wd.category = "const";
			} else if (url.contains("software") || url.contains("program")) {
				wd.category = "soft";
			} else if (url.contains("human") || url.contains("practice") || url.contains("policy")
					|| url.contains("collaborate")) {
				wd.category = "policy";
			} else if (url.contains("judge") || url.contains("judging") || url.contains("forms")
					|| url.contains("requirement") || url.contains("achievement") || url.contains("accomplishment")) {
				wd.category = "judging";
			} else if (url.contains("parts") || url.contains("biobrick")) {
				wd.category = "parts";
			} else if (url.contains("safety")) {
				wd.category = "safety";
			} else if (url.contains("blog")) {
				wd.category = "blog";
			} else if ((url.contains("team") || url.contains("member")) && !mp.contains(wd.teamName + "$" + "team")) {
				wd.category = "team";
				// mp.add(wd.teamName + "$" + "team");
				// mp.add(url);
			} else if (url.contains("notebook")) {
				wd.category = "notebook";
			} else if (url.contains("biology") || url.contains("wetlab") || url.contains("protocol")) {
				wd.category = "bio";
			} else if (url.contains("model") || url.contains("math")) {
				wd.category = "model";
			} else if (url.contains("results")) {
				wd.category = "results";
			} else if (url.contains("sponsor")) {
				wd.category = "sponsors";
			} else if (url.contains("acknowledge") || url.contains("attribut")) {
				wd.category = "attributes";
			} else if ((url.contains("project") || url.contains("overview") || url.contains("description")
					|| url.contains("introduction"))) {

				if (mp.contains(wd.teamName + "$" + "project")) {
					int sucss_count = 0;
					if (url.contains("project"))
						sucss_count++;
					if (url.contains("overview"))
						sucss_count++;
					if (url.contains("description"))
						sucss_count++;
					if (url.contains("introduction"))
						sucss_count++;
					if (sucss_count > 1) {
						wd.category = "overview";
					} else
						wd.category = "other";
				} else {
					wd.category = "overview";
					mp.add(wd.teamName + "$" + "project");
				}

			} else {
				wd.category = "other";
				/*
				 * String st2=url.split("/")[2]; String st=st2.split("|")[0];
				 * boolean tst1=(url.contains("team") || url.contains("member"))
				 * && !mp.contains(wd.teamName + "$" + "team"); boolean
				 * tst2=(url.split("/")[2].contains("team") ||
				 * url.split("/")[2].contains("member") ); boolean
				 * tst3=mp.contains(wd.teamName + "$" + "team");int ds=5;
				 */

			}
			if (wd.category.equals("const")) {
				Elements tables = doc.select("table");
				for (Element tbl : tables) {
					Elements trs = tbl.select("tr");
					Element header = trs.first();
					String header_text = header.text().toLowerCase();
					boolean isTD = (header_text.contains("contant") || header_text.contains("value")
							|| header_text.contains("reference") || header_text.contains("name")
							|| header_text.contains("description")) && header.select("td").size() >= 3
							&& header_text.length() < 150;
					boolean isTH = (header_text.contains("contant") || header_text.contains("value")
							|| header_text.contains("reference") || header_text.contains("name")
							|| header_text.contains("description")) && header.select("th").size() >= 3
							&& header_text.length() < 150;

					if (isTD || isTH) {
						// says in which column each attribute is placed, by the
						// following order:
						// 0 1 2 3 4 5 6
						// name | description | value | value_max (if exists) |
						// Unit | Source | sensitivity | url;
						int[] header_map = new int[number_of_const_attributes];
						Arrays.fill(header_map, -1);
						int not_recognized = 0;
						Elements header_tds = new Elements();
						if (isTD)
							header_tds = header.select("td");
						if (isTH)
							header_tds = header.select("th");
						for (int i = 0; i < header_tds.size(); i++) {
							Element header_cell = header_tds.get(i);
							String typ = header_cell.text().toLowerCase();
							boolean isName = typ.contains("name") || typ.contains("constant") || typ.contains("data")
									|| typ.contains("parameter") || typ.contains("attribute")
									|| typ.contains("Definition");
							boolean isDesc = typ.contains("description") || typ.contains("justification")
									|| typ.contains("formula") || typ.contains("introduction")|| typ.contains("type");
							boolean isVal = typ.contains("value") || typ.contains("gene size");
							boolean isRef = typ.contains("reference") || typ.contains("estimation")
									|| typ.contains("source") || typ.contains("reference") || typ.equals("ref")
									|| typ.contains("comment");
							boolean isUnits = typ.contains("unit");
							boolean isSense = typ.contains("sensitivity");

							if (isVal) {
								boolean isValMax = typ.contains("max");
								boolean isValMin = typ.contains("min");
								if (isValMax && isValMin)
									System.out.println(":(");
								if (isValMax) {
									header_map[3] = i;
								} else if (isValMin) {
									header_map[2] = i;
								} else {
									header_map[2] = i;
								}
							} else if (isDesc) {
								header_map[1] = i;
							} else if (isRef) {

								header_map[5] = i;
							} else if (isUnits) {

								header_map[4] = i;
							} else if (isName) {

								header_map[0] = i;
							} else {
								not_recognized++;
							}
							if (isSense) {

								header_map[6] = i;
							}

						}
						if (not_recognized > 3)
							continue;
						trs.remove(0);
						for (Element abcd : trs) {

							Elements tds = abcd.select("td");
							
							String[] constRow = new String[number_of_const_attributes + 2];
							for (int k = 0; k < number_of_const_attributes; k++) {
								if (header_map[k] != -1) {
									try
									{
										String ewd=tds.get(header_map[k]).html();

									ewd=ewd.replace("\r","");
									ewd=ewd.replace("\n","");
									ewd=ewd.replace("\t"," ");
									ewd=ewd.replace("\f","");
									ewd=ewd.trim().replaceAll(" +", " ");
									constRow[k] = ewd;						
									if (k == 5) // source
									{
										Document src_cell = Jsoup.parse(constRow[k]);
										if (src_cell.select("a").size() == 0 && src_cell.text().matches("\\[\\d\\]")) {
											constRow[k] = "<a href='" + wd.url + "'>" + constRow[k] + "</a>";
										}
									}
									}catch (IndexOutOfBoundsException e)
									{
										
									}
									
								}

							}
							constRow[number_of_const_attributes] = wd.url;
							constRow[number_of_const_attributes + 1] = wd.teamName;
							constTable.add(constRow);
							wd.hasConstants=true;
						}

					} else
						System.out.println("couldn't find any constants in " + wd.url);
				}

				// System.out.println("rows="+ e.select("tr").size());
			}
			if (wd.category.equals("home")) {
				Elements imgs = doc.select("img[src*=logo]");
				if (imgs.size() > 0) {
					wd.logo_url = imgs.first().attr("src");
					if (wd.logo_url.toLowerCase().contains("sponsor"))
						wd.logo_url = "";
					if (wd.logo_url.startsWith("/wiki/")) {
						String t = wd.subDomain + ".igem.org" + wd.logo_url;
						wd.logo_url = t;
					}
				}
				Elements Lmgs = doc.select("img[src*=Logo]");
				if (Lmgs.size() > 0) {
					wd.logo_url = Lmgs.first().attr("src");
					if (wd.logo_url.startsWith("/wiki/")) {
						String t = wd.subDomain + ".igem.org" + wd.logo_url;
						wd.logo_url = t;
					}
				}

			}

		}

		/*
		 * boolean
		 * url_param=(wd.url.toLowerCase().indexOf("param")>=0)||(wd.url.
		 * toLowerCase().indexOf("const")>=0); boolean
		 * anch_param=(wd.anchor.toLowerCase().indexOf("paramr")>=0)||(wd.anchor
		 * .toLowerCase().indexOf("const")>=0); if(url_param||anch_param) {
		 * count++; System.out.println(wd.url); for(Element e : tables) {
		 * System.out.println("rows="+ e.select("tr").size()); } }
		 */

		return constTable;
	}
//getters and setters for the Text variable.
	public String getText() {
		return text;
	}

	public WikiData setText(String text) {
		this.text = text;
		return this;
	}
// like getWikiPages(String[], String), but enables to limit the subdomain from which
	//the pages will be downloaded. For example:  rull="2012hs" will download only wikis from 2012 
	//High School competition.
	public static void getWikiPages(String[] seeds, String folder, String rule) {
		// TODO Auto-generated method stub
		MyCrawler.ruleDiv=rule;
		try {
			getWikiPages(seeds, folder);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MyCrawler.ruleDiv=null;
	}

}
