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
	
	public static String formURL(String D, String Query) {
		String urlString =  "http://boss.yahooapis.com/ysearch/web/v1/"
			+ Query + "?appid=" + yahooID + "&format=xml&sites=" + D;
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
	
	
	public static String search(String urlt) {
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
//		/Node node = doc.getElementsByTagName("resultset_web");
		Node node = nodeList.item(0);	//totalhits tag only have one element
		int coverage = -1;
		
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element)node;
			coverage  = Integer.parseInt(element.getAttribute("totalhits").trim());
		}	
		return coverage;
	}

	
	
	public static int ECoverage(String D, Category C) {
		int cov = 0;
		
		try {
			for (String query : C.getQueries()) {
				String searchURL = formURL(D,query);
				System.out.println(searchURL);
				String xmlResult = search(searchURL);
				System.out.println(xmlResult);
				cov += getCoverage(xmlResult);
			}		
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
			for (Category Cj : Ci.getParent().getSubcat()) {
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
				//Result.addAll(Classify(Ci,D,tec,tes,ESpecificity(D,Ci)));
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
		//String testQuery = "http://boss.yahooapis.com/ysearch/web/v1/"
		//	+ "avi%20file" + "?appid=" + yahooID + "&format=xml&sites=" + database;
		//System.out.println("Final UR2: " + lll);
		//String theResult = Search(testQuery);
		
		Category rootcat = ProjectHelper.makeCategories();
		ArrayList<Category> result = Classify(rootcat,"diabetes.org",100,0.5,1.0);
		
		//System.out.println(cov);
		
	}
	
}
