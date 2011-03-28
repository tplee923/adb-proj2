import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
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
	private static int coverageCache;
	private static File rootDir;
	private static File urlsDir;
	private static File hostDir;
	private static Hashtable<String, String> ht = new Hashtable<String, String>();
	private static Category rootcat = ProjectHelper.makeCategories();
	private static boolean removeDuplicat = false;
	
	/**
	 * 
	 * @param array
	 * @return true if the input is valid, otherwise return false
	 * to judge whether the input is valid
	 */
	public static boolean isInputValid(String[] array) {
		int length = array.length;
		if (length != 3 || !(isFractionValid(array[1])) || !(isInteger(array[2]))) {
			System.out
					.println("Input format error,the correct input format is\n<host> <t_es> <t_ec>.");
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param s
	 * @return true if it is integer, otherwise return false
	 * to judge whether the input is integer
	 */
	public static boolean isInteger(String s) {
		try{
			Integer.parseInt(s);
			return true;
		}catch(NumberFormatException e){
		}
		return false;
	}
	
	/**
	 * 
	 * @param s
	 * @return true if it is fraction, otherwise return false
	 * to judge whether the precision specified is valid
	 */
	public static boolean isFractionValid(String s) {
		Pattern p = Pattern.compile("0+.\\d+");
		Matcher m = p.matcher(s);
		return m.matches();
	}
	
	/**
	 * 
	 * @param host
	 * @param query
	 * @return url string
	 * to form the url with host and query
	 */
	public static String formURL(String host, String query) {
		String urlString =  "http://boss.yahooapis.com/ysearch/web/v1/"
			+ query + "?appid=" + yahooID + "&format=xml&sites=" + host;
		return urlString;
	}
	
	/**
	 * 
	 * @param urlt
	 * @return url result
	 * to return the result of this specified url, for our case, the 
	 * format of result is xml
	 */
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
			return resultstring;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return "NA";
	}
	
	/**
	 * 
	 * @param xmlResult
	 * @return coverage number
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * to parse the result string and return the coverage number
	 */
	public static int getCoverage(String xmlResult) throws ParserConfigurationException, SAXException, IOException{
		int coverage = -1;
		try{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource inStream = new org.xml.sax.InputSource();
		inStream.setCharacterStream(new java.io.StringReader(xmlResult));
		Document doc = builder.parse(inStream);
		NodeList nodeList = doc.getElementsByTagName("resultset_web");
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

	/**
	 * 
	 * @param xmlresult
	 * @param query
	 * @param host
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * to cache the related urls
	 */
	public static void cacheUrls(String xmlresult, String query, String host)
			throws SAXException, IOException, ParserConfigurationException {
		rootDir = new File("."+File.separator+"cache");
		if(!rootDir.exists()){
			rootDir.mkdirs();
		}
		urlsDir = new File(rootDir,"urls");
		if(!urlsDir.exists()){
			urlsDir.mkdirs();
		}
		hostDir = new File(urlsDir, host);
		if(hostDir.exists()){
		//	hostDir.delete();
		}else{
			hostDir.mkdir();
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource inStream = new org.xml.sax.InputSource();
		inStream.setCharacterStream(new java.io.StringReader(xmlresult));
		Document doc = builder.parse(inStream);
		NodeList list = doc.getElementsByTagName("url");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.getLength(); i++) {
			Element nameElement = (Element) list.item(i);
			String theURL = nameElement.getFirstChild().getNodeValue().trim();
			sb.append(theURL+"\n");
		}
		File queryFile = new File(hostDir, query);
		FileWriter fw = new FileWriter(queryFile);
	    fw.write(sb.toString());
	    fw.flush();
	    fw.close();
	}

	/**
	 * 
	 * @param host
	 * @param cate
	 * @return coverage for the specified category
	 */
	public static int ECoverage(String host, Category cate) {
		int cov = 0;
		
		try {
			for (String query : cate.getQueries()) {
				String searchURL = formURL(host,query);
				String xmlresult = search(searchURL);
				if(!xmlresult.equals("NA") ){
					cacheUrls(xmlresult,query,host);
					cov += getCoverage(xmlresult);
				}else{
					System.out.println("Timeout when query "+query);
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
		coverageCache = cov;
		return cov;
	}
	
	/**
	 * 
	 * @param host
	 * @param cate
	 * @param es
	 * @return the specificity for the specified category
	 */
	public static double ESpecificity(String host, Category cate, double es) {
		double numerator = 0;
		double denominator = 0;
		if (cate.getParent() == null) {
			return 1.0;
		}
		else {
			numerator = es * coverageCache;
			denominator = coverageCache;
			for (Category Cj : cate.getParent().getSubcat()) {
				if (!(Cj.getName().equals(cate.getName())))
					denominator += ECoverage(host,Cj);
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
	
	
	/**
	 * 
	 * @param cate
	 * @param host
	 * @param tec
	 * @param tes
	 * @param es
	 * @return the category for the host
	 */
	public static ArrayList<Category> Classify(Category cate, String host, int tec, double tes, double es) {
		ArrayList<Category> Result = new ArrayList<Category>();
		if (cate.getisLeaf()) {
			ArrayList<Category> set = new ArrayList<Category>();
			set.add(cate);
			return set;
		}
			
		for (Category Ci : cate.getSubcat()) {
			int coverage = ECoverage(host,Ci);
			double specificity = ESpecificity(host,Ci,es);
			System.out.println("Category:"+Ci.getName()+"	Coverage:"+coverage+"	Specificity:"+specificity);
			if (specificity >= tes && coverage >= tec) {
				ArrayList<Category> newList = Classify(Ci,host,tec,tes,specificity);
				Result.addAll(newList);
			}
		}
		
		if (Result.isEmpty()) {
			ArrayList<Category> set = new ArrayList<Category>();
			set.add(cate);
			return set;
		}
		else {
			return Result;
		}
	}
	
	/**
	 * 
	 * @param host
	 * @param array
	 * @return the hashmap for the specified host and array
	 */
	public static HashMap<String, Integer> samplingCategory(String host, String[] array) {

		HashMap<String, Integer> doc = new HashMap<String, Integer>();
		for (int i = 0; i < array.length; i++) {
			Set<String> temp = null;
			File f = new File(hostDir, array[i]);
			if (!f.exists()) {
				System.out.println(array[i] + " is NOT cached!");
			} else {
				System.out.println("Getting pages for " + array[i]);
				int k = 4;
				try {
					FileReader reader = new FileReader(f);
					BufferedReader br = new BufferedReader(reader);
					String s1 = null;
					while ((k != 0) && (s1 = br.readLine()) != null) {
						if (!s1.equals("")) {
							if (removeDuplicat && ht.containsKey(s1)) {
								System.out.println(" Skipping "+s1);
								continue;
							}
							System.out.println(s1);
							temp = getWordsLynx.runLynx(s1);
							if (temp != null && temp.size() != 0) {
								ht.put(s1, "");
								k--;
								for (String word : temp) {
									if (doc.containsKey(word)) {
										doc.put(word, doc.get(word) + 1);
									} else {
										doc.put(word, 1);
									}
								}
							}
						}
					}
					br.close();
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		System.out.println();
		}
		removeDuplicat = true;
		return doc;
	}
	
	/**
	 * 
	 * @param host
	 * @param cat
	 * @param map
	 * to output the sample file
	 */
	public static void outputResult(String host, Category cat,
			HashMap<String, Integer> map) {
		String name = cat.getName() + "-" + host + ".txt";
		File file = new File("./"+name);
		FileWriter fw;
		try {
			fw = new FileWriter(file);
			StringBuffer sb = new StringBuffer();
			Object[] key = map.keySet().toArray();
			Arrays.sort(key);
			for (int i = 0; i < key.length; i++) {
				sb.append(key[i]+"#"+map.get(key[i])+"\n");
			}
			fw.write(sb.toString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return merged string array
	 * to merge the two array to a new array
	 */
	public static String[] mergeArrays(String[]a, String[]b){
		int lengthA = a.length;
		int lengthB = b.length;
		String[] result=new String[lengthA+lengthB]; 
		System.arraycopy(a,0,result,0,lengthA); 
		System.arraycopy(b,0,result,lengthA,lengthB);
		return result;
	}
	
	/**
	 * 
	 * @param host
	 * @param node
	 * to analyze the category and call related method to generate the sample file
	 */
	public static void docSampling(String host, ArrayList<Category> node) {
		Category c = node.get(0);
		if(c.getisLeaf()){
			c = c.getParent(); //if c is leaf node, we donot need to sample it
		}
		HashMap<String, Integer> subMap = null;
		if(c.getParent()!=null){//meaning it's not root node
			System.out.println("Sampling "+c);
			ArrayList<Category> list = c.getSubcat();
			String[] childArray1 = list.get(0).getQueries();
			String[] childArray2 = list.get(1).getQueries();
			String[] tmpArray = mergeArrays(childArray1, childArray2);
			subMap = samplingCategory(host, tmpArray);
			outputResult(host,c,subMap);
		}
		//it's root node
		System.out.println("Sampling Root");
		ArrayList<Category> list = rootcat.getSubcat();
		String[] childArray1 = list.get(0).getQueries();
		String[] childArray2 = list.get(1).getQueries();
		String[] childArray3 = list.get(2).getQueries();
		String[] tmpArray = mergeArrays(childArray1, childArray2);
		String[] finalSamplingArray = mergeArrays(tmpArray, childArray3);
		HashMap<String, Integer> parentMap = samplingCategory(host, finalSamplingArray);
		if(subMap!=null){//meaning that we need to merge its child category
			//System.out.println("Merging..."+parentMap.size()+" "+subMap.size());
			Iterator<String> i = subMap.keySet().iterator();
			while(i.hasNext()){
				String key = i.next();
				if(parentMap.containsKey(key)){
					parentMap.put(key, parentMap.get(key)+subMap.get(key));
				}else{
					parentMap.put(key, subMap.get(key));
				}
			}
			//System.out.println("Merging result "+parentMap.size());
		}
		outputResult(host,rootcat,parentMap);
	}
	
	public static void main(String[] args) {
		if (!(isInputValid(args))) {
			return; // input error, simply return.
		}
		String host = args[0];
		host=host.replaceAll("http://", ""); //if the input includes http://, we just remove it 
		int t_es = new Integer(args[2]).intValue();
		double t_ec = new Double(args[1]).doubleValue();
		
		Category rootcat = ProjectHelper.makeCategories();
		coverageCache = 0;
		System.out.println();
		System.out.println("Classifying...");
		ArrayList<Category> result = Classify(rootcat, host, t_es, t_ec, 1.0);
		Category cat = result.get(0);
		System.out.println("************************************");
		System.out.print("The Category:");
		StringBuffer sb = new StringBuffer();
		while (cat.getParent()!=null){
			sb.append(cat+" ");
			cat = cat.getParent();
		}
		sb.append("Root");
		String[] st = sb.toString().split(" ");
		for(int i=st.length-1; i>=0; i--){
			System.out.print(st[i]+"\\");
		}
		System.out.print("\n");
		System.out.println("************************************");
		docSampling(host,result);		
	}
}
