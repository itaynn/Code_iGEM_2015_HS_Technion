package crawler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

//crawling supervision class.
public class MyCrawler extends WebCrawler {
	static String Folder;
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg" + "|png|mp3|mp3|zip|gz))$");
	public static int crawledSites;
	public static String ruleDiv;

	/**
	 * This method receives two parameters. The first parameter is the page in
	 * which we have discovered this new url and the second parameter is the new
	 * url. You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic). In this example,
	 * we are instructing the crawler to ignore urls that have css, js, git, ...
	 * extensions and to only accept urls that start with
	 * "http://www.ics.uci.edu/". In this case, we didn't need the referringPage
	 * parameter to make the decision.
	 */
	@SuppressWarnings("unused")
	// decides if we want to visit this page. currently visits only pages which
	// are detected as wiki pages.
	// The unused booleans in it can be used for detection of different situations, 
	//as described in their name. 
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();

		int i = href.indexOf(".");
		if (i < 2)
			return false;
		String sb = href.substring(i);
		boolean isWiki = sb.startsWith(".igem.org/team:") || sb.startsWith(".igem.org/wiki/index.php/team:");
		// the following conditions are useful for limiting the crawler to only
		// highschool or Entrepreneurship
		boolean isHS = (href.charAt(i - 2) == 'h') && (href.charAt(i - 1) == 's');
		boolean isEnt = (href.charAt(i - 1) == 'e');
		boolean lastChance = href.contains("igem.org") && href.contains("php/team:");
		boolean is2008 = (href.charAt(i - 1) == '8');
		boolean is2015 = (href.charAt(i - 1) == '5');
		boolean holdRule = true;
		
		if (ruleDiv != null) {
		ruleDiv = ruleDiv.toLowerCase();
			if (i - ruleDiv.length() < 0) {
				holdRule = false;

			} else
				for (int i1 = 0; i1 < ruleDiv.length(); i1++) {
					if (href.charAt(i + i1 - ruleDiv.length()) != ruleDiv.charAt(i1)) {
						holdRule = false;
						break;
					}
				}
		}
		boolean holdRule2 = false;
		if (ruleDiv.endsWith("hs")) {
			holdRule2=true;
			String rule2 = ruleDiv.substring(0, ruleDiv.length() - 2);

			if (rule2 != null) {
				if (i - rule2.length() < 0) {
					holdRule2 = false;

				} else
					for (int i1 = 0; i1 < rule2.length(); i1++) {
						if (href.charAt(i + i1 - rule2.length()) != rule2.charAt(i1)) {
							holdRule2 = false;
							break;
						}
					}
			}
		}

		return !FILTERS.matcher(href).matches() && ((isWiki || lastChance) );//&& (holdRule || holdRule2)
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	// when we visit a wiki page, we save everything we can into files which are
	// stored in 'the folder the
	// user chose/raw/wikis".
	@Override
	public void visit(Page page) {
		@SuppressWarnings("unused")
		int docid = page.getWebURL().getDocid();
		String url = page.getWebURL().getURL();
		String domain = page.getWebURL().getDomain();
		String path = page.getWebURL().getPath();
		String subDomain = page.getWebURL().getSubDomain();
		String parentUrl = page.getWebURL().getParentUrl();
		String anchor = page.getWebURL().getAnchor();
		// System.out.println("URL: " + url);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			Set<WebURL> links = htmlParseData.getOutgoingUrls();
			if (!url.toLowerCase().startsWith("http://igem.org/team_wikis?")) {
				printCrawlDataToFile(url, domain, path, subDomain, parentUrl, anchor, text, html, links, Folder);

				// System.out.println("Text length: " + text.length());
				// System.out.println("Html length: " + html.length());
				// System.out.println("Number of outgoing links: " +
				// links.size());
			}
		}

	}
// This function prints the information that the crawler found about a certain wiki page to file.
	// Description for the different kinds of this information can be found in the variables of the
	// WikiData class.
	// For each wiki page, three files are printed in the following structure:
	// Serial_Number.links:
		// outgoing links, each in a new line.
	// Serial_Number.txt
		//the text in the page.
	// Serial_Number.html
		//url
		//domain
		//path
		//subdomain
		//parentUrl
		//anchor
		//retrieval date
		//html
	private void printCrawlDataToFile(String url, String domain, String path, String subDomain, String parentUrl,
			String anchor, String text, String html, Set<WebURL> links, String folder) {
		// TODO Auto-generated method stub
		String htmlFile = "", txtFile = text, linksFile = "";
		String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
		htmlFile = url + "\n" + domain + "\n" + path + "\n" + subDomain + "\n" + parentUrl + "\n";
		htmlFile += anchor + "\n" + date + "\n\n" + html;
		for (WebURL wu : links) {
			linksFile += wu.getURL() + "\n";
		}

		String file = folder + subDomain + "/" + crawledSites;
		new File(folder + subDomain).mkdirs();
		HtmlOperators.writeToFile(new File(file + ".html"), htmlFile);
		HtmlOperators.writeToFile(new File(file + ".txt"), txtFile);
		HtmlOperators.writeToFile(new File(file + ".links"), linksFile);
		crawledSites++;
	}
}
