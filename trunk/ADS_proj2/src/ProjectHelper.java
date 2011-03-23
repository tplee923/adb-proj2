import java.util.ArrayList;


public class ProjectHelper {

	public static Category makeCategories() {
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
		return Root;
	}
}
