package crawler;
// Written by Itay Naor and Tomer Shani, from the Technion HS 2015 iGEM Team.
// We release it under the MIT license. 
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

//Auxiliary static functions class.
public class HtmlOperators {
	// checks if the string str represents an integer. auxiliary function. '42'
	// will return true and
	// '42x' will return false.
	public static boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException nfe) {
		}
		return false;
	}

	// prints an XML file, in the format solr (the platform we used for
	// searching)needs.
	// It gets the result of getPrintData in data, and print it to the xml in
	// the path given by file.
	//////////////////
	// explanation of the structure of the print data and the xml:
	// assume we have a single team: technion_hs, which is from 2015.
	// after we crawl their info page and process it using getPrintData,
	// the result will be a List<List<PrintData>> which has a one object which
	// is a list with two elements. The first one is a PrintData whose name is
	// 'name' and value
	// is 'technion hs', and the second one has 'year' and 2015 respectively.
	//////////////////
	public static void SaveToXML(List<List<PrintData>> data, String file) {
		// Explanation in the email
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			org.w3c.dom.Document doc = docBuilder.newDocument();
			org.w3c.dom.Element rootElement = doc.createElement("add");
			doc.appendChild(rootElement);

			// org.w3c.dom.Element add = doc.createElement("add");
			while (!data.isEmpty()) {
				List<PrintData> doc_list = data.remove(0);
				org.w3c.dom.Element docElement = doc.createElement("doc");
				rootElement.appendChild(docElement);
				while (!doc_list.isEmpty()) {
					PrintData pd = doc_list.remove(0);

					if (pd.value != null && pd.value != "") {
						org.w3c.dom.Element fieldElement = doc.createElement("field");
						fieldElement.setAttribute("name", pd.name);

						fieldElement.setTextContent(pd.value);
						docElement.appendChild(fieldElement);
					}

				}

			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			File f = new File(file);
			if (!f.exists()) {
				f.createNewFile();
			}
			StreamResult result = new StreamResult(f);

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Reads all the text from a file (which is given by it's path). returns the
	// result as a string.
	// Since it uses a buffer, it's appropriate for giant strings like we have
	// when we download wikis.
	// If the file doesn't exist, a null will be returned.
	//
	public static String ReadText(String File) {

		try (BufferedReader br = new BufferedReader(new FileReader(File))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String everything = sb.toString();
			return everything;
		} catch (Exception e) {
			return null;
		}

	}

	// Writes the string st to the file file (which is an object from the class
	// java.io.File).
	// Since it uses a buffer, it's appropriate for giant strings like we have
	// when we download wikis.
	//The file you want to write to doesn't need to be exist, as the function create it, but the  
	// folder in which it should be located needs to exist beforehand. You can create the folder
	// before calling the function using: "new File(folder_name).mkdirs()";
	static void writeToFile(File file, String st) {
		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(st);
			bw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block

		}

	}
// downloads the info pages and the results pages. Uses the result pages as seeds.
	public static void downloadAllFromInternetResultsSeeds(String Folder) {
		int last_index = 5000;
		TeamInfo.getInfoPages(1, last_index, Folder + "//raw//info//");
		List<String> seedList = TeamResults.getResultsPages("http://igem.org/Results", Folder + "//raw//results//");
		try {
			WikiData.getWikiPages((String[]) seedList.toArray(), Folder + "//raw//wikis//");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
// 2011 High school is a problematic competition.
	// This function downloads all the existing wikis from this year. Notice - the result will wait in the folder
	// "base_folder//raw//wikis//hs//2011", and you should move it to "base_folder//raw//wikis//2011hs" so the 
	// processing from file will work. 
	public static void download2011HS(String folder) {
		String[] wiki_urls = { "http://2011.igem.org/Team:SouthBend-Mishawaka-HS",
				"http://2011.igem.org/Team:WarrenCIndpls_IN-HS", "http://2011.igem.org/Team:Greenfield_IN-Schini-HS",
				"http://2011.igem.org/Team:SouthBend-Mishawaka-HS-2" };
		try {

			WikiData.getWikiPages(wiki_urls, folder + "//raw//wikis//hs//", "2011");
			// WikiData.getWikiPages(seeds, Folder + "//raw//wikis//",year);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
// Since most of the computers don't have enough memory to read and process 1.7GB of data at one go, this function
	// do it year by year.
	public static void processYearsOneByOne(String fldr) {
		HtmlOperators.processFromFile(fldr, "2008");
		HtmlOperators.processFromFile(fldr, "2009");
		HtmlOperators.processFromFile(fldr, "2010");
		HtmlOperators.processFromFile(fldr, "2011");
		HtmlOperators.processFromFile(fldr, "2012");
		HtmlOperators.processFromFile(fldr, "2013");
		HtmlOperators.processFromFile(fldr, "2014");
		HtmlOperators.processFromFile(fldr, "2015");
		HtmlOperators.processFromFile(fldr, "2012hs");
		HtmlOperators.processFromFile(fldr, "2013hs");
		HtmlOperators.processFromFile(fldr, "2014hs");
		HtmlOperators.processFromFile(fldr, "2012e");
		HtmlOperators.processFromFile(fldr, "2011hs");
	}
	// Processes only the competitions whose number is in competitions_nums;
	// competitions_nums include the numbers of the requested jamborees,
	// according to the following map.
	// 3 - 2011hs, 5- 2012e, 8 - 2013hs, 10 - 2014hs,11 - 2015. The holes in this map are filled by
	// regular competitions in an ascending order.
	public static void completeProcessOneYearResultsSeeds(String Folder, int[] competitions_nums) {
		// int last_index=5000;
		// TeamInfo.getInfoPages(1, last_index, Folder + "//raw//info//");
		List<String> seedList = TeamResults.getResultsPages("http://igem.org/Results", Folder + "//raw//results//");
		List<String> sd2 = new ArrayList<String>();
		getSeedsFromTeamWikis(2004, 2015, sd2);
		seedList.addAll(sd2);

		String[] seeds = new String[seedList.size()];
		seedList.toArray(seeds);
		for (int num : competitions_nums) {

			String[] short_list = { seeds[num] };

			Pattern pattern = Pattern.compile("year=(\\d+).*division=(.+)");
			Matcher matcher = pattern.matcher(short_list[0]);
			matcher.find();
			String year = matcher.group(1);

			String div = matcher.group(2).toLowerCase();

			if (div.equals("high_school"))
				div = "hs";
			else if (div.equals("ent"))
				div = "e";
			else
				div = "";
			try {
				String rule = year + div;
				WikiData.getWikiPages(seeds, Folder + "//raw//wikis//", rule);
				// WikiData.getWikiPages(seeds, Folder + "//raw//wikis//",year);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// processFromFile(Folder, year+div);
		}

	}
// download everything from the internet. Uses only the 
	public static void downloadAllFromInternet(String Folder) throws Exception {

		downloadSomeFromInternet(Folder);
		downloadWikisFromInternet(Folder);

		

	}
	// Downloads Team Results and Wikis from the internet, using all the available seeds - both  results pages 
	// and wiki lists. Finds more wikis then the regular downloadAllFromInternet and I believe it find all the wikis
	// except the 2011hs teams (because of the fact that in 2011 highschool teams didn't have results page and there is
	// a bug in the Teams_List of this year
	// USE THIS FUNCTION
	public static void downloadAllFromInternetFullScan(String Folder) throws Exception {

		List<String> seedList = TeamResults.getResultsPages("http://igem.org/Results", Folder + "//raw//results//");
		List<String> sd2 = new ArrayList<String>();
		getSeedsFromTeamWikis(2004, 2015, sd2);
		seedList.addAll(sd2);

		String[] seeds = new String[seedList.size()];
		seedList.toArray(seeds);
		try {
			WikiData.getWikiPages(seeds, Folder + "//raw//wikis//");
			// WikiData.getWikiPages(seeds, Folder + "//raw//wikis//",year);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// processFromFile(Folder, year+div);

	}

	// like downloadAllFromInternet, but updates only the team infos (from the
	// *.igem.org/Team_List) and
	// the jamboree results, for the teams from all the years. The data is saved
	// to Folder.
	// note: due to some technical limitations, last_index is the highest id the
	// function will search
	// team info for. That means, in order
	// to get all the teams, this number should be higher than the number of
	// teams from all the years.
	// currently about 1900 is enough, but I take more as a safety margin.
	// Remember to increase it when the number of teams is close to 5000.
	public static void downloadSomeFromInternet(String Folder) {
		int last_index = 5000;
		TeamInfo.getInfoPages(1, last_index, Folder + "//raw//info//");
		TeamResults.getResultsPages("http://igem.org/Results", Folder + "//raw//results//");

		// List<TeamInfo> tis = TeamInfo.readTeamInfo(Folder + "//raw//info//");
		// List<List<TeamResults>> trs = TeamResults.readTeamResults(Folder +
		// "//raw//results//");

		// TeamInfo.SaveTeamInformationToFile(tis, Folder +
		// "//final//info.xml");
		// TeamResults.SaveTeamAwardsToFile(trs, Folder +
		// "//final//results.xml");

	}

	// After you've downloaded all the data you want into the folder Folder, use
	// this function to process it and
	// to get a finilized xml file (and a constants table). These files will
	// wait you in Folder+"//final//final.xml"
	// and Folder+"//final//const.txt" respectively;
	public static void processFromFile(String Folder) {

		// TeamInfo.getInfoPages(1, 2000, Folder + "//raw//info//");

		List<TeamInfo> tis = TeamInfo.readTeamInfo(Folder + "//raw//info//");
		List<List<TeamResults>> trs = TeamResults.readTeamResults(Folder + "//raw//results//");
		List<WikiData> wds = new ArrayList<WikiData>();
		List<String[]> constTable = WikiData.readTeamWikis(Folder + "//raw//wikis//", wds);

		// TeamInfo.SaveTeamInformationToFile(tis, Folder +
		// "//final//info.xml");
		// TeamResults.SaveTeamAwardsToFile(trs, Folder +
		// "//final//results.xml");
		List<List<PrintData>> llpt = new ArrayList<List<PrintData>>();
		getPrintData(tis, trs, wds, llpt);
		new File(Folder + "//final//").mkdirs();
		SaveToXML(llpt, Folder + "//final//final.xml");
		saveConstants(constTable, Folder + "//final//const.txt");
	}

	// jamboree name is the name of the competition you want to process.
	// available names: 2008-2015, 2011hs - 2014hs, 2012e
	public static void processFromFile(String Folder, String jamboreeName) {

		// TeamInfo.getInfoPages(1, 2000, Folder + "//raw//info//");
System.out.println("processing "+jamboreeName);
		List<TeamInfo> tis = TeamInfo.readTeamInfo(Folder + "//raw//info//");
		List<List<TeamResults>> trs = TeamResults.readTeamResults(Folder + "//raw//results//");
		List<WikiData> wds = new ArrayList<WikiData>();
		List<String[]> constTable = WikiData.readTeamWikis(Folder + "//raw//wikis//" + jamboreeName, wds);

		// TeamInfo.SaveTeamInformationToFile(tis, Folder +
		// "//final//info.xml");
		// TeamResults.SaveTeamAwardsToFile(trs, Folder +
		// "//final//results.xml");
		List<List<PrintData>> llpt = new ArrayList<List<PrintData>>();
		getPrintData(tis, trs, wds, llpt, jamboreeName);
		new File(Folder + "//final//").mkdirs();
		SaveToXML(llpt, Folder + "//final//final_" + jamboreeName + ".xml");
		saveConstants(constTable, Folder + "//final//const_" + jamboreeName + ".txt");
	}

	// After you've read the constants using WikiData.readTeamWikis, this
	// function will print it in a table.
	// constTable is the table you've got and file is where you want to save the
	// table.
	// in the printed table, line break (\n) separates rows and a tab (\t)
	// separates columns.
	private static void saveConstants(List<String[]> constTable, String file) {
		// TODO Auto-generated method stub
		String st = "name\tdescription\tvalue\tvalue_max\tunit\tSource\tsensitivity\turl\tteam\t";
		for (String[] row : constTable) {
			st += "\n";
			for (int i = 0; i < row.length; i++) {
				if (row[i] == null || row[i].equals("null") || row[i].equals(""))
					st += "\t";
				else
					st += row[i] + "\t";
			}
		}
		writeToFile(new File(file), st);
	}

	// obsolete function (since all the different kinds of data are printed
	// together now).
	public static void updateWikisFromInternetAndProcess(String Folder) throws Exception {

		int year_start = 2014, year_end = 2014;
		String[] crawling_seeds = new String[year_end - year_start + 1];
		for (int i = year_start; i <= year_end; i++) {
			crawling_seeds[i - year_start] = "http://igem.org/Team_Wikis?year=" + i;
		}
		WikiData.getWikiPages(crawling_seeds, Folder + "//raw//wikis//");
		List<WikiData> wds = new ArrayList<WikiData>();
		List<String[]> constTable = WikiData.readTeamWikis(Folder + "//raw//wikis//", wds);

		// TeamInfo.SaveTeamInformationToFile(tis, Folder +
		// "//final//info.xml");
		// TeamResults.SaveTeamAwardsToFile(trs, Folder +
		// "//final//results.xml");
		List<List<PrintData>> llpt = new ArrayList<List<PrintData>>();
		getPrintData(null, null, wds, llpt);
		SaveToXML(llpt, Folder + "//final//final.xml");
		saveConstants(constTable, Folder + "//final//const.txt");

		// WikiData.SaveWikiDataToFile(wds, Folder + "//final//wiki//");

	}

	// Downloads all the wikis from 2008 until the year that is states in
	// year_end.
	// It doesn't work yet on wikis from before 2008 because of structural
	// changes in the wikis
	// that happened that year.
	// misses some functions because of problems in the Team_List pages. Use the downloadAllFromInternetFullScan
	// instead.
	// In the future, even those years should be covered.
	// I can't guarantee that this function will work flawlessly in the years to
	// come (since another structural changes
	// are possible), but I don't think that it'll happen because this function
	// relies only on the structure of the url.
	// If you encounter problems with this function after the year I wrote it
	// (2015) check first the function MyCrawler.shouldVisit
	// and if it doesn't work, you can contact me.
	public static void downloadWikisFromInternet(String Folder) throws Exception {
		String[] crawling_seeds = getSeedsFromTeamWikis(2004, 2015);
		WikiData.getWikiPages(crawling_seeds, Folder + "//raw//wikis//");

		// WikiData.SaveWikiDataToFile(wds, Folder + "//final//wiki//");

	}
// generates a list of the team wikis list pages. Adds the urls to the sl List. 
	public static void getSeedsFromTeamWikis(int year_start, int year_end, List<String> sl) {

		for (int i = year_start; i <= year_end; i++) {
			sl.add("http://igem.org/Team_Wikis?year=" + i);

		}
		for (int i = 2011; i <= 2014; i++) {
			sl.add("http://igem.org/Team_Wikis?year=" + i + "&division=high_school");
			sl.add("http://igem.org/Team_Wikis?year=" + i + "&division=High_School");

		}
		sl.add("http://igem.org/Team_Wikis?year=" + 2012 + "&division=ent");

	}
	// generates a list of the team wikis list pages. Returns the urls in an array.
	public static String[] getSeedsFromTeamWikis(int year_start, int year_end) {

		String[] crawling_seeds = new String[year_end - year_start + 1 + 4 + 1];
		for (int i = year_start; i <= year_end; i++) {
			crawling_seeds[i - year_start] = "";// "http://igem.org/Team_Wikis?year="
												// + i;

		}
		for (int i = 2011; i <= 2014; i++) {
			crawling_seeds[i - 2011 + year_end - year_start + 1] = "http://igem.org/Team_Wikis?year=" + i
					+ "&division=high_school";

		}
		crawling_seeds[year_end - year_start + 1 + 4] = "http://igem.org/Team_Wikis?year=" + 2012 + "&division=ent";
		return crawling_seeds;

	}

	// lists files in a folder directoryName and put them in files; auxiliary
	// function.
	public static void listf(String directoryName, ArrayList<File> files) {
		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				files.add(file);
			} else if (file.isDirectory()) {
				listf(file.getAbsolutePath(), files);
			}
		}
	}

	// Derives the data that is needed for the xml printing from the data that
	// has been read from file,
	// which is the lists of TeamInfo, TeamResults and WikiData. You get these
	// lists from TeamInfo.readTeamInfo
	// TeamResults.readTeamResults and WikiData.readTeamWikis. You need to send
	// a List<List<PrintData>> as the last parameter, and the
	// print data will be added there.
	public static void getPrintData(List<TeamInfo> di, List<List<TeamResults>> results, List<WikiData> wds,
			List<List<PrintData>> llpt) {
		// gets team information class and uses SaveToXML to print to file the
		// important fields.
		// List<List<PrintData>> llpt = new ArrayList<List<PrintData>>();

		while (!di.isEmpty()) {
			List<PrintData> lpt = new ArrayList<PrintData>();
			TeamInfo ti = di.remove(0);
			boolean wikiFound = false;
			boolean prizeFound = false;
			lpt.add(new PrintData("id", Integer.toString(ti.id)));
			lpt.add(new PrintData("name", ti.Name));
			lpt.add(new PrintData("school", ti.School));
			lpt.add(new PrintData("section", ti.Section));
			lpt.add(new PrintData("region", ti.Region));
			lpt.add(new PrintData("title", ti.Title));
			lpt.add(new PrintData("abstract", ti.Abstract));
			lpt.add(new PrintData("studentsnum", Integer.toString(ti.StudentsNum)));
			lpt.add(new PrintData("advisorsnum", Integer.toString(ti.AdvisorsNum)));
			lpt.add(new PrintData("instructorsnum", Integer.toString(ti.InstructorsNum)));
			lpt.add(new PrintData("track", ti.Track));
			lpt.add(new PrintData("lastPart", ti.LastPart));
			lpt.add(new PrintData("wikiurl", ti.WikiURL));
			lpt.add(new PrintData("firstpart", ti.FirstPart));
			lpt.add(new PrintData("partsubmissionsurl", ti.PartSubmissionsURL));
			lpt.add(new PrintData("year", Integer.toString(ti.Year)));
			for (List<TeamResults> ltr : results) {
				if (ltr.get(0).Year == ti.Year) {
					for (TeamResults tr : ltr)
						if (tr.Name.equalsIgnoreCase(ti.Name)) {
							lpt.add(new PrintData("medal", Integer.toString(tr.Medal)));
							// lpt.add(new PrintData("year",
							// Integer.toString(tr.Year)));
							for (int i = 0; i < tr.Awards.size(); i++) {
								lpt.add(new PrintData("award", (tr.Awards.get(i))));
							}
							lpt.add(new PrintData("presentation", tr.Products.Pres));
							lpt.add(new PrintData("poster", tr.Products.Post));
							lpt.add(new PrintData("video", tr.Products.Vid));
							lpt.add(new PrintData("division", tr.Division));
							prizeFound = true;
						}
				}
			}
			for (WikiData wd : wds) {
				if (wd.url.equals("http://2014.igem.org/team:iit_delhi") && ti.Name.equals("IIT Delhi")) {

				}
				if (wd.teamName.toLowerCase().equals(ti.Name.toLowerCase())) {
					// add formated wiki data to lpt
					// TODO: after I'll implement categorization, each page
					// should be saved to appropriate field
					lpt.add(new PrintData("wP_" + wd.category, wd.text));
					lpt.add(new PrintData("logo", wd.logo_url));
					wikiFound = true;
				}

			}
			if (!wikiFound || !prizeFound) {

				
				System.out.println("wiki or prize not found, " + ti.Name + " from " + ti.Year);
				continue;

			}
			llpt.add(lpt);

		}
		// HtmlOperators.SaveToXML(llpt, file);
	}
// Prepares the data that has been downloaded from the wikis for save the XML. the llpt holds the result in a structure
	// that will be conveyed to the XML.
	private static void getPrintData(List<TeamInfo> di, List<List<TeamResults>> results, List<WikiData> wds,
			List<List<PrintData>> llpt, String jamboreeName) {
		// TODO Auto-generated method stub
		while (!di.isEmpty()) {
			List<PrintData> lpt = new ArrayList<PrintData>();
			TeamInfo ti = di.remove(0);
			String[] sts = ti.WikiURL.split("\\.");
			String st1 = sts[0].toLowerCase();
			String st2 = "http://" + jamboreeName.toLowerCase();
			if (st1.equals(st2)) {
				boolean wikiFound = false;
				boolean prizeFound = false;
				lpt.add(new PrintData("id", Integer.toString(ti.id)));
				lpt.add(new PrintData("name", ti.Name));
				lpt.add(new PrintData("school", ti.School));
				lpt.add(new PrintData("section", ti.Section));
				lpt.add(new PrintData("region", ti.Region));
				lpt.add(new PrintData("title", ti.Title));
				lpt.add(new PrintData("abstract", ti.Abstract));
				lpt.add(new PrintData("studentsnum", Integer.toString(ti.StudentsNum)));
				lpt.add(new PrintData("advisorsnum", Integer.toString(ti.AdvisorsNum)));
				lpt.add(new PrintData("instructorsnum", Integer.toString(ti.InstructorsNum)));
				lpt.add(new PrintData("track", ti.Track));
				lpt.add(new PrintData("lastPart", ti.LastPart));
				lpt.add(new PrintData("wikiurl", ti.WikiURL));
				lpt.add(new PrintData("firstpart", ti.FirstPart));
				lpt.add(new PrintData("partsubmissionsurl", ti.PartSubmissionsURL));
				lpt.add(new PrintData("year", Integer.toString(ti.Year)));
				for (List<TeamResults> ltr : results) {
					if (ltr.get(0).Year == ti.Year) {
						for (TeamResults tr : ltr)
							if (tr.Name.equalsIgnoreCase(ti.Name)) {
								lpt.add(new PrintData("medal", Integer.toString(tr.Medal)));
								// lpt.add(new PrintData("year",
								// Integer.toString(tr.Year)));
								for (int i = 0; i < tr.Awards.size(); i++) {
									lpt.add(new PrintData("award", (tr.Awards.get(i))));
								}
								lpt.add(new PrintData("presentation", tr.Products.Pres));
								lpt.add(new PrintData("poster", tr.Products.Post));
								lpt.add(new PrintData("video", tr.Products.Vid));
								lpt.add(new PrintData("division", tr.Division));
								prizeFound = true;
							}
					}
				}
				for (WikiData wd : wds) {
					boolean isProbablyValid = wd.url.toLowerCase().contains(ti.Name.toLowerCase())
							&& wd.url.contains(Integer.toString(ti.Year));
					boolean foundConstants = false;
					if (wd.teamName.toLowerCase().equals(ti.Name.toLowerCase())
							|| wd.url.toLowerCase().replace("/", "^")
									.replace(":", "&").replace(".", "#").startsWith(ti.WikiURL.toLowerCase()
											.replace("/", "^").replace(":", "&").replace(".", "#"))
							|| isProbablyValid) {
						// add formated wiki data to lpt
						// TODO: after I'll implement categorization, each page
						// should be saved to appropriate field
						if (wd.category == "map")
							continue;
						lpt.add(new PrintData("wP_" + wd.category, wd.text));
						if (wd.logo_url != "")
							lpt.add(new PrintData("logo", wd.logo_url));
						if (wd.hasConstants)
							foundConstants = true;

						wikiFound = true;
					}
					if (foundConstants)
						lpt.add(new PrintData("constants", "posistive"));
				}
				if (!wikiFound || !prizeFound) {

					System.out.println("wiki or prize not found,\t" + ti.WikiURL);

				}
				llpt.add(lpt);
			}

		}
	}
}
