package crawler;

//class which holds the data for a one line of the xml page.
public class PrintData {
	String name;//the field name
	String value;//the field value
//constructor.
	public PrintData(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
}
