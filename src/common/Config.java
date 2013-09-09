package common;

public class Config {
	public static int RATING = -1;
	
	public static void loadConfigs(String dataset){
		if(dataset.equals("bookCrossing")){
			RATING = 7;
		}
	}
}
