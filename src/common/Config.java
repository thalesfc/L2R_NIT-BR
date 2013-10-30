package common;

public class Config {
	public static Integer RATING = null;
	public static Integer K_FOLD = null;
	public static Integer SEED = null;
	
	public static void loadConfigs(String dataset){
		if(dataset.equals("bookCrossing")){
			System.out.println("# loading" + dataset);
			RATING = 7;
			K_FOLD = 5;
			SEED = 0;
		}
		Data.setDATABASE(dataset);
	}
}
