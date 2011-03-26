import java.util.ArrayList;


public class Category {
	private boolean isLeaf;
	private String name;	//	store category's name
	private ArrayList<Category> subcat;	//	the subcategories of this category
	private String [] queries;
	private Category parent;
	
	
	public Category(String name){
		this.name = name;
	}
	
	public boolean getisLeaf(){
		return isLeaf;
	}
	public void setisLeaf(Boolean b){
		this.isLeaf = b;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public ArrayList<Category> getSubcat(){
		return subcat;
	}
	public void setSubcat(ArrayList<Category> subcat){
		this.subcat = subcat;
	}
	
	public String [] getQueries(){
		return queries;
	}
	
	public void setQueries(String [] queries){
		if (queries != null) {
			int i = queries.length;
			this.queries = new String[i];
			System.arraycopy(queries, 0, this.queries, 0, queries.length);
		}
	}
	
	public Category getParent(){
		return parent;
	}
	
	public void setParent(Category parent) {
		this.parent = parent;
	}
	
	public String toString() {
		return this.getName();
	}
	
}

