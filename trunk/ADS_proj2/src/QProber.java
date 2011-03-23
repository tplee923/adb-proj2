import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
public class QProber {
	private static String yahooID = "B8JQDzTV34FHmHMnQcwcEPiucXt.SFviHkJ6w.KxXhr37KFMJtaoV6D79K8Qlw--";
	private static String[] keywordsArray = {"avi","file"};
	private static String outputStr;
	private static String database="diabetes.org";
	/**
	 * @param keywordsArray
	 * @return	the search URL
	 */
	public static String formSearchURL(String[] keywordsArray) {
		String finalStr = "";
		outputStr = "";
		for (int i = 0; i < keywordsArray.length; i++) {
			outputStr = outputStr + " " + keywordsArray[i];
			if (i == keywordsArray.length - 1) {
				finalStr = finalStr + keywordsArray[i];
			} else {
				finalStr = finalStr + keywordsArray[i] + "%20";
			}
		}
		String urlString = "http://boss.yahooapis.com/ysearch/web/v1/"
				+ finalStr + "?appid=" + yahooID + "&format=xml&sites=" + database;
		System.out.println("Final URL: "+urlString);
		//System.out.println("Parameters:\nClient key  =" + yahooID
		//		+ "\nQuery       =" + outputStr + "\nPrecision   = "
		//		+ strPrecisioin + "\nURL: " + urlString);
		//System.out.println("Total number of results : 10\nYahoo! Search Results:\n"
		//		+ "======================");
		return urlString;
	}
	
	/**
	 * @param keywordsArray
	 * @return	the xml result in String format
	 */
	/*public static String search(String[] keywordsArray) {
		String resultstring = "";
		try {
			URL url = new URL(formSearchURL(keywordsArray));
			URLConnection connection = url.openConnection();
			connection.connect();
			InputStreamReader in = new InputStreamReader(
					connection.getInputStream());
			BufferedReader br = new BufferedReader(in);
			String s = br.readLine();
			while (s != null) {
				resultstring += s;
				s = br.readLine();
			}
			// System.out.println(resultstring);
			return resultstring;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return "";
	}*/
	
	public static void Search (String db) {
		
	}
	
	public static String Search(String urlt) {
		String resultstring = "";
		try {
			URL url = new URL(urlt);
			URLConnection connection = url.openConnection();
			connection.connect();
			InputStreamReader in = new InputStreamReader(
					connection.getInputStream());
			BufferedReader br = new BufferedReader(in);
			String s = br.readLine();
			while (s != null) {
				resultstring += s;
				s = br.readLine();
			}
			// System.out.println(resultstring);
			return resultstring;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return "";
	}
	
	/**
	 * @param prompt
	 * @return	user's input
	 */
	private static String readUserInput(String prompt) {
		Scanner scanner = new Scanner(System.in);
		System.out.print(prompt);
		return scanner.nextLine();
	}
	
	public static int getCoverage(String xmlResult) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource inStream = new org.xml.sax.InputSource();
		inStream.setCharacterStream(new java.io.StringReader(xmlResult));
		Document doc = builder.parse(inStream);
		NodeList nodeList = doc.getElementsByTagName("resultset_web");
		//Node node = doc.getElementsByTagName("resultset_web");
		Node node = nodeList.item(0);	//totalhits tag only have one element
		int coverage = -1;
		
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element)node;
			coverage  = Integer.parseInt(element.getAttribute("totalhits").trim());
		}	
		return coverage;
	}

	public static void makeCategories(Category rootcat) {
		//List<Category> leafCategories = new ArrayList<Category>();
		Category Hardware = new Category("Hardware");
		Hardware.setisLeaf(true);
		Hardware.setQueries(new String [] {
				"bios",
				"motherboard",
				"board%20fsb",
				"board%20overclocking",
				"fsb%20overclocking",
				"bios%20controller%20ide",
				"cables%20drive%20floppy"});
		Hardware.setSubcat(null);
		
		
		Category Programming = new Category("Programming");
		Programming.setisLeaf(true);
		Programming.setQueries(new String [] {
				"actionlistener",
				"algorithms",
				"alias",
				"alloc",
				"ansi",
				"api",
				"applet",
				"argument",
				"array",
				"binary",
				"boolean",
				"borland",
				"char",
				"class",
				"code",
				"compile",
				"compiler",
				"component",
				"container",
				"controls",
				"cpan",
				"java",
				"perl"
		});
		Programming.setSubcat(null);
		
		Category Diseases = new Category("Diseases");
		Diseases.setisLeaf(true);
		Diseases.setQueries(new String [] {
				"aids",
				"cancer",
				"dental",
				"diabetes",
				"hiv",
				"cardiology",
				"aspirin%20cardiologist",
				"aspirin%20cholesterol",
				"blood%20heart",
				"blood%20insulin",
				"cholesterol%20doctor",
				"cholesterol%20lipitor",
				"heart%20surgery",
				"radiation%20treatment"	
		});
		Diseases.setSubcat(null);
		
		Category Fitness = new Category("Fitness");
		Fitness.setisLeaf(true);
		Fitness.setQueries(new String [] {
				"aerobic",
				"fat",
				"fitness",
				"walking",
				"workout",
				"acid%20diets",
				"bodybuilding%20protein",
				"calories%20protein",
				"calories%20weight",
				"challenge%20walk",
				"dairy%20milk",
				"eating%20protein",
				"eating%20weight",
				"exercise%20protein",
				"exercise%20weight"
		});
		Fitness.setSubcat(null);
		
		Category Soccer = new Category("Soccer");
		Soccer.setisLeaf(true);
		Soccer.setQueries(new String [] {
				"uefa",
				"leeds",
				"bayern",
				"bundesliga",
				"premiership",
				"lazio",
				"mls",
				"hooliganism",
				"juventus",
				"liverpool",
				"fifa"
		});
		Soccer.setSubcat(null);
		
		Category Basketball = new Category("Basketball");
		Basketball.setisLeaf(true);
		Basketball.setQueries(new String [] {
				"nba",
				"pacers",
				"kobe",
				"laker",
				"shaq",
				"blazers",
				"knicks",
				"sabonis",
				"shaquille",
				"laettner",
				"wnba",
				"rebounds",
				"dunks"
		});
		Basketball.setSubcat(null);
		
		
		
		Category Computers = new Category("Computers");
		Computers.setisLeaf(false);
		Computers.setQueries(new String [] {
				"cpu",
				"java",
				"module",
				"multimedia",
				"perl",
				"vb",
				"agp%20card",
				"application%20windows",
				"applet%20code",
				"array%20code",
				"audio%20file",
				"avi%20file",
				"bios",
				"buffer%20code",
				"bytes%20code",
				"shareware",
				"card%20drivers",
				"card%20graphics",
				"card%20pc",
				"pc%20windows"
		});
		ArrayList<Category> compSub = new ArrayList<Category>();
		compSub.add(Hardware);
		compSub.add(Programming);
		Computers.setSubcat(compSub);
		
		Category Health = new Category("Health");
		Health.setisLeaf(false);
		Health.setQueries(new String [] {
				"acupuncture",
				"aerobic",
				"aerobics",
				"aids",
				"cancer",
				"cardiology",
				"cholesterol",
				"diabetes",
				"diet",
				"fitness",
				"hiv",
				"insulin",
				"nurse",
				"squats",
				"treadmill",
				"walkers",
				"calories%20fat",
				"carb%20carbs",
				"doctor%20health",
				"doctor%20medical",
				"eating%20fat",
				"fat%20muscle",
				"health%20medicine",
				"health%20nutritional",
				"hospital%20medical",
				"hospital%20patients",
				"medical%20patient",
				"medical%20treatment",
				"patients%20treatment"
		});
		ArrayList<Category> healthSub = new ArrayList<Category>();
		healthSub.add(Fitness);
		healthSub.add(Diseases);
		Health.setSubcat(healthSub);
		
		Category Sports = new Category("Sports");
		Sports.setisLeaf(false);
		Sports.setQueries(new String [] {
				"laker",
				"ncaa",
				"pacers",
				"soccer",
				"teams",
				"wnba",
				"nba",
				"avg%20league",
				"avg%20nba",
				"ball%20league",
				"ball%20referee",
				"ball%20team",
				"blazers%20game",
				"championship%20team",
				"club%20league",
				"fans%20football",
				"game%20league"
		});
		ArrayList<Category> sportsSub = new ArrayList<Category>();
		sportsSub.add(Basketball);
		sportsSub.add(Soccer);
		Sports.setSubcat(sportsSub);
		
		Category Root = new Category("Root");
		Root.setisLeaf(false);
		Root.setQueries(null);
		ArrayList<Category> rootSub = new ArrayList<Category>();
		rootSub.add(Computers);
		rootSub.add(Health);
		rootSub.add(Sports);
		Root.setSubcat(rootSub);
		//root.setSubcat(subcat)
		
		Root.setParent(null);
		Computers.setParent(Root);
		Health.setParent(Root);
		Sports.setParent(Root);
		Hardware.setParent(Computers);
		Programming.setParent(Computers);
		Fitness.setParent(Health);
		Diseases.setParent(Health);
		Basketball.setParent(Sports);
		Soccer.setParent(Sports);
		
		rootcat = Root;
	}
	
	public static int ECoverage(String D, Category C) {
		String s = Search(D);
		int cov = -1;
		try {
			cov = getCoverage(s);
		} 
		catch (ParserConfigurationException e) {
			e.printStackTrace();
		} 
		catch (SAXException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return cov;
	}
	
	public static double ESpecificity(String D, Category Ci) {
		double numerator = 0;
		double denominator = 0;
		if (Ci.getParent() == null) {
			return 1.0;
		}
		else {
			numerator = ESpecificity(D,Ci.getParent()) * ECoverage(D,Ci);
			for (Category Cj : Ci.getSubcat()) {
				denominator += ECoverage(D,Cj);
			}
			if (denominator != 0)
				return numerator/denominator;
			else
			{
				System.err.println("Denominator Zero! Wrong!");
				return -1;
			}
		}
	}
	
	public static ArrayList<Category> Classify(Category C, String D, int tec, double tes, double es) {
		ArrayList<Category> Result = new ArrayList<Category>();
		if (C.getisLeaf()) {
			ArrayList<Category> set = new ArrayList<Category>();
			set.add(C);
			return set;
		}
			
		for (Category Ci : C.getSubcat()) {
			if (ESpecificity(D,Ci) >= tes && ECoverage(D,Ci) >= tec) {
				Result.addAll(Classify(Ci,D,tec,tes,ESpecificity(D,Ci)));
			}
		}
		
		if (Result.isEmpty()) {
			ArrayList<Category> set = new ArrayList<Category>();
			set.add(C);
			return set;
		}
		else {
			return Result;
		}
	}
	
	
	public static void main(String[] args) {
		String testQuery = "http://boss.yahooapis.com/ysearch/web/v1/"
			+ "avi%20file" + "?appid=" + yahooID + "&format=xml&sites=" + database;
		//System.out.println("Final UR2: " + lll);
		String theResult = Search(testQuery);
		
		Category rootcat = new Category("root");
		makeCategories(rootcat);
		
		
		int cov = -1;
		try {
			cov = getCoverage(theResult);
		} 
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(theResult);
		//System.out.println(theResult2);
		
		System.out.println(cov);
		
		FileUtil fu = new FileUtil();
		fu.makeQueries("root.txt", "rootLeft.txt", "rootRight.txt");
		fu.makeQueries("sports.txt", "sportsLeft.txt", "sportsRight.txt");
		fu.makeQueries("health.txt", "healthLeft.txt", "healthRight.txt");
		fu.makeQueries("computers.txt", "computersLeft.txt", "computersRight.txt");
	}
	
}
