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
// class for the information downloaded from the info pages.
public class TeamInfo {
//The variable names are pretty self-explanatory.
		String Name; // done
//		Team name
	String School; // done
//		School/university name
	// Division;
//		Highschool/igem/Entrepreneur
	String Section; // done
//		graduate/under graduate
	String Region; // done
//		//continent or a little more specific region
	String Title; // done
//		abstract title
	String Abstract; // done
//		
	String State;
//		//currently not inferred
	String City;
//		//currently not inferred
	int Year;
//		year in which the team participated
	int StudentsNum; // done
//		number of students in the team
	int AdvisorsNum; // done
//		number of Advisors in the team
	int InstructorsNum; // done
//		number of Instructors in the team
	List<String> Tracks;
//		not used
	String Track; // done
//		
	String FirstPart; // done
//		first biobrick number in the range gave to the group
	String LastPart; // done
//		last biobrick number in the range gave to the group
	String WikiURL; // done
//		
	String PartSubmissionsURL; // done
//		//url to send biobricks to
	String html;
//		//the html of the info page
	int id;
//		//the id that iGEM gave each team. Determined by the url of the info page: 
//		//If the info page is http://igem.org/Team.cgi?id=1767, then this team 
//		// get the id 1767. Used as a unique valued key in the search serve.
	
//simple constructor
	public TeamInfo(String name, String school, String section, String region, String title, String abstract1,
			String state, String city, int year, int studentsNum, int advisorsNum, int mentorsNum, List<String> tracks,
			String firstPart, String lastPart, String wikiURL, String partSubmissionsURL) {
		this.Name = name;
		this.School = school;
		this.Section = section;
		this.Region = region;
		this.Title = title;
		this.Abstract = abstract1;
		this.State = state;
		this.City = city;
		this.Year = year;
		this.StudentsNum = studentsNum;
		this.AdvisorsNum = advisorsNum;
		this.InstructorsNum = mentorsNum;
		this.Tracks = tracks;
		this.FirstPart = firstPart;
		this.LastPart = lastPart;
		this.WikiURL = wikiURL;
		this.PartSubmissionsURL = partSubmissionsURL;
	}

	// public TeamInfo(){};
	// null constructor
	public TeamInfo() {
		// TODO Auto-generated constructor stub
	}
//download Info pages from the internet. start and end define the range in which id will be scanned.
	// you probably want this range to contain all the igem teams from over the years.
	public static List<TeamInfo> getInfoPages(int start, int end, String Folder) {
		List<TeamInfo> tis = new ArrayList<TeamInfo>();
		for (int i = start; i <= end; i++) {

			try {
				Document page = Jsoup.connect("http://igem.org/Team.cgi?id=" + i).get();
				if (!page.select(".firstHeading").isEmpty()) {
					if (page.select(".firstHeading").first().text().startsWith("Teams Registered for iGEM ")) {
						continue;
					}
					TeamInfo ti = new TeamInfo();
					// ti.html=page.html();
					// ti.id=i;
					HtmlOperators.writeToFile(new File(Folder + i + ".html"), ti.html);

					// tis.add(ti);
					TimeUnit.MILLISECONDS.sleep(100);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return tis;
	}
// infers all the data that is given in a certain info page. Includes:
	//for explanation about the inferred fields, take a look in the class's variables.
	public static TeamInfo InferTeamInformation(int docid, String url, String html) {
		TeamInfo ti = new TeamInfo();
		Document doc = Jsoup.parse(html);
		// abstract
		Element abst_td = doc.select("#table_abstract > tbody > tr").last();
		ti.Abstract = abst_td.text();
		if (ti.Abstract.equals("-- No abstract provided yet --")) {
			ti.Abstract = "";
		}
		//
		Element name_td = doc.select("#table_info > tbody > tr > td:eq(1)").first();
		ti.Name = name_td.ownText();
		ti.Name=ti.Name.replace('_', ' ');

		Element school_td = doc.select("#table_info > tbody > tr:eq(2) > td:eq(1)").first();
		ti.School = school_td.text().split("\r")[0];

		Element section_td = doc.select("#table_info > tbody > tr:eq(4) > td:eq(1)").first();
		ti.Section = section_td.ownText();
		if (ti.Section.equals("Unspecified")) {
			ti.Section = "";
		}
		Element region_td = doc.select("#table_info > tbody > tr:eq(5) > td:eq(1)").first();
		ti.Region = region_td.ownText();
		if (ti.Region.equals("Unspecified")||ti.Region.contains("Specify")||ti.Region.contains("Specify")) {
			ti.Region = "";
		}
		Element wikiURL_td = doc.select("#table_info > tbody > tr > td:eq(1) > div > a").first();
		ti.WikiURL = wikiURL_td.attr("href");

		Element partSubmissionURL_td = doc.select("#table_info > tbody > tr > td:eq(1) > div > a:eq(1)").first();
		ti.PartSubmissionsURL = partSubmissionURL_td.attr("href");

		Element title_td = doc.select("#table_abstract > tbody > tr > td").first();
		ti.Title = title_td.ownText();
		if (ti.Title.equals("-- Not provided yet --")) {
			ti.Title = "";
		}

		Element track_td = doc.select("#table_tracks > tbody > tr > td").first();
		ti.Track = track_td.ownText().replace("Assigned Track: ", "");
		if (ti.Track.equals("This team has not been assigned to a track.")) {
			ti.Track = "";
		}
		if(ti.Region.contains("graduate")) 
		{
			Element region_td2 = doc.select("#table_info > tbody > tr:eq(6) > td:eq(1)").first();
			ti.Region = region_td2.ownText();
			if (ti.Region.equals("Unspecified")||ti.Region.contains("Specify")||ti.Region.contains("accepted")) {
				ti.Region = "";
			}
		}
		Element parts_td = doc.select("#table_ranges > tbody > tr > td > span").first();
		if (parts_td != null) {
			String[] parts = parts_td.ownText().split(" ");
			ti.FirstPart = parts[0];
			ti.LastPart = parts[2];
		}

		Element Instructors_td = doc.select("#table_roster > tbody").first();
		if (Instructors_td != null) {
			ti.InstructorsNum = (Instructors_td.children().size() - 1);
		} else {
			ti.InstructorsNum = 0;
		}
		Element Students_td = doc.select("#table_roster > tbody").eq(1).first();
		if (Students_td != null) {
			ti.StudentsNum = (Students_td.children().size() - 1);
		} else {
			ti.StudentsNum = 0;
		}
		Element Advisors_td = doc.select("#table_roster > tbody").eq(2).first();
		if (Advisors_td != null) {
			ti.AdvisorsNum = (Advisors_td.children().size() - 1);
		} else {
			ti.AdvisorsNum = 0;
		}

		Element Year_td = doc.select("#content > h1").first();
		String[] headline = Year_td.ownText().split(" ");
		ti.Year = Integer.parseInt(headline[1]);

		return ti;
	}

	static List<TeamInfo> readTeamInfo(String folderName) {
		List<TeamInfo> tis = new ArrayList<TeamInfo>();
		
		File folder = new File(folderName);
		if(!folder.isDirectory())
			new File(folderName).mkdirs();
		File[] listOfFiles = folder.listFiles();
		
		for (File file : listOfFiles) {
			if (file.isFile()) {
				String html = HtmlOperators.ReadText(file.getAbsolutePath());
				if(html==null) continue;
				String st = file.getName();
				String nm_prts = st.substring(0, st.length() - 5);
				int id = Integer.parseInt(nm_prts);
				TeamInfo ti = new TeamInfo();
				ti = InferTeamInformation(id, "", html);
				ti.id = id;
				ti.html = html;
				tis.add(ti);
			}

		}

		return tis;
	}
	// obselete function. This functionality moved to HtmlOperators.getPrintData, although you still can use
		// this function if you want to print only the info pages.
	public static void prepareTeamInfoPrintData(List<TeamInfo> di, List<List<PrintData>> llpt) {
		// gets team information class and uses SaveToXML to print to file the
		// important fields.
		//List<List<PrintData>> llpt = new ArrayList<List<PrintData>>();
		while (!di.isEmpty()) {
			List<PrintData> lpt = new ArrayList<PrintData>();
			TeamInfo ti = di.remove(0);
			lpt.add(new PrintData("id",Integer.toString( ti.id)));
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
			llpt.add(lpt);

		}
		//HtmlOperators.SaveToXML(llpt, file);
	}
}
