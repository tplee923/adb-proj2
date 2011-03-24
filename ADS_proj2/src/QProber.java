import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;


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
	private static String outputStr;
	private static String database="java.sun.com";
	private static double coverageCache;
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
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		return "NA";
	}
	

	public static int getCoverage(String xmlResult) throws ParserConfigurationException, SAXException, IOException{
		int coverage = -1;
		try{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource inStream = new org.xml.sax.InputSource();
		inStream.setCharacterStream(new java.io.StringReader(xmlResult));
		Document doc = builder.parse(inStream);
		NodeList nodeList = doc.getElementsByTagName("resultset_web");
//		/Node node = doc.getElementsByTagName("resultset_web");
		Node node = nodeList.item(0);	//totalhits tag only have one element
		//int coverage = -1;
		
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element)node;
			coverage  = Integer.parseInt(element.getAttribute("totalhits").trim());
		}	
		}catch(Exception e){
			e.printStackTrace();
		}
		return coverage;
	}

	public static ArrayList<String> getTopFour(String xmlresult) throws SAXException, IOException, ParserConfigurationException {
		ArrayList<String> top = new ArrayList<String>();
		//String searchURL = formURL(db,query);
		//String xmlresult = search(searchURL);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource inStream = new org.xml.sax.InputSource();
		inStream.setCharacterStream(new java.io.StringReader(xmlresult));

		Document doc = builder.parse(inStream);
		NodeList nodeList = doc.getElementsByTagName("result");
		int i=0;
		for (int index = 0; i<4 && index < nodeList.getLength(); index++) {
			String theurl = null;
			Node node = nodeList.item(index);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				NodeList nameNode = element.getElementsByTagName("url");
				if (nameNode.item(0).getNodeType() == Node.ELEMENT_NODE) {
					Element nameElement = (Element) nameNode.item(0);
					theurl = nameElement.getFirstChild().getNodeValue().trim();
					top.add(theurl);
				}
			}
			i++;
		}
		return top;	//this variable may also contain less than 4 results
	}
	
	public static int ECoverage(String D, Category C) {
		int cov = 0;
		
		try {
			ArrayList<String> thetopurls = new ArrayList<String>();
			for (String query : C.getQueries()) {
				String searchURL = formURL(D,query);
				//System.out.println(searchURL);
				String xmlresult = search(searchURL);
				//System.out.println(xmlResult);
				if(!xmlresult.equals("NA") ){
					thetopurls.addAll(getTopFour(xmlresult));	//这里应该要避免重复文件
					cov += getCoverage(xmlresult);
				}			
			}
			C.setTopURLs(thetopurls);
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
		coverageCache = cov;
		return cov;
	}
	
	public static double ESpecificity(String D, Category Ci, double es) {
		double numerator = 0;
		double denominator = 0;
		if (Ci.getParent() == null) {
			return 1.0;
		}
		else {
			numerator = es * coverageCache;
			denominator = coverageCache;
			for (Category Cj : Ci.getParent().getSubcat()) {
				if (!(Cj.getName().equals(Ci.getName())))
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
			int coverage = ECoverage(D,Ci);
			double specificity = ESpecificity(D,Ci,es);
			//System.out.println("Category:"+Ci.getName()+",Converage:"+coverage+",Specificity:"+specificity);
			if (specificity >= tes && coverage >= tec) {
				
				ArrayList<Category> newList = Classify(Ci,D,tec,tes,specificity);
				//for(Category c : newList){
				//	System.out.print("!!!"+c);
				//}
				//System.out.println("!!!");
				Result.addAll(newList);
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
	
	public static boolean docSampling(String db, ArrayList<Category> result) {
		Category c = result.get(0);
		HashMap<String, Integer> doc = new HashMap<String, Integer>();
		Set<String> temp = new TreeSet<String>();
		while (c != null) {
			for (String url : c.getTopURLs()) {
				temp = getWordsLynx.runLynx(url);//这里的警告不知道怎么解决。
				for (String word : temp) {
					if (doc.containsKey(word)) {
						doc.put(word, doc.get(word)+1);
					}
					else {
						doc.put(word, 1);
					}
				}			
			}
			c = c.getParent();
		}
		//now doc waits to be written to harddrive...
	}
	
	public static void main(String[] args) {
		//String testQuery = "http://boss.yahooapis.com/ysearch/web/v1/"
		//	+ "avi%20file" + "?appid=" + yahooID + "&format=xml&sites=" + database;
		String db = "hardwarecentral.com";
		Category rootcat = ProjectHelper.makeCategories();
		coverageCache = 0;
		ArrayList<Category> result = Classify(rootcat,db,100,0.6,1.0);
		if (!docSampling(db,result)) {
			System.out.println("Making summary Success!");
		}
		else
			System.out.println("Making summary Error");
		//for(Category c: result)
		//	System.out.println(c);
		
	}
}
