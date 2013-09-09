package common;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.io.Files;

public class Data {
	private static String DATABASE;
	private static final int K = 10;
	private static List<String> DESCRIPTION; // [ Descriptions ] __ bookID
	private static List<String> BOOKS; // [ bookID ]
	private static List<String> USERS; // [ userID ]
	private static List<List<String>> CLASSES; // [ [Class1, Class2 ... ClassN] ] __bookID
	
	/*
	 * Positive ratings ( >= Config.RATINGS ) on train
	 * { user-ID -> [bookID_1, bookID_2 ... bookID_n] }
	 */
	private static Map<String, List<String>> testPositiveRATINGS;

	/*
	 *  { bookID -> flag_new }
	 *  
	 *  flag_new : 
	 *  		false == old book
	 *  		true == new book
	 */
	static Map<String, Boolean> booksFLAG;
	// TODO implement booksFLAG functions
	

	public static int getK() {
		return K;
	}

	public static String getDATABASE() {
		return DATABASE;
	}

	public static void setDATABASE(String dATABASE) {
		DATABASE = dATABASE;
	}


	public static List<String> getDESCRIPTION() {
		if(DESCRIPTION == null){
			DESCRIPTION = loadList( System.getProperty("user.dir") + 
					"/data/" + DATABASE + "/descriptions.csv");
		}
		return DESCRIPTION;
	}
	
	public static List<String> getBOOKS() {
		if(BOOKS == null){
			BOOKS = loadList( System.getProperty("user.dir") + 
					"/data/" + DATABASE + "/books.csv");
		}
		return BOOKS;
	}
	
	public static List<String> getUSERS(){
		if(USERS == null){
			USERS = loadList( System.getProperty("user.dir") + 
					"/data/" + DATABASE + "/users.csv");
		}
		return USERS;
	}

	public static List<List<String>> getCLASSES(){
		if(CLASSES == null){
			CLASSES = loadMatrix( System.getProperty("user.dir") + 
					"/data/" + DATABASE + "/classes.csv");
		}
		return CLASSES;
	}
	
	public static Map<String, List<String>> getTestPositiveRATINGS(){
		if(testPositiveRATINGS == null){
			List<List<String>> allRatings = loadMatrix( System.getProperty("user.dir") + 
					"/data/" + DATABASE + "/ratings.csv");
			
			// loading all ratings
			List<String> users = Data.getUSERS();
			
			// allocing resources
			testPositiveRATINGS = new HashMap<String, List<String>>(users.size());
			
			// excluding negative ratings and ratings on test-set
			for(int i =0; i < users.size(); ++i){
				String user = users.get(i);
				
				List<String> userRatings = new ArrayList<String>(4);
				List<String> uRatings = allRatings.get(i);
				
				// bRating is ISBN:rate
				for(String bRating : uRatings){
					String[] tokens = bRating.split(":");
					String ISBN = tokens[0];
					int rating = Integer.parseInt(tokens[1]);
					if(rating >= Config.RATING && booksFLAG.get(ISBN) == false){
						userRatings.add(ISBN);
					}
				}
				testPositiveRATINGS.put(user, userRatings);
			}
		}
		return testPositiveRATINGS;
	}
	
	/*
	 *  Private functions to load data from files
	 */
	
	private static List<List<String>> loadMatrix(String path) {
			File file = new File(path);
			try {
				List<String> temp = Files.readLines(file, Charset.defaultCharset());
				List<List<String>> returned = new ArrayList<>(temp.size());
				for(String line : temp){
					returned.add(Arrays.asList(line.split(",")));
				}
				return returned;
			} catch (IOException e) {
				e.printStackTrace();
			}
		return null;
	}
	
	private static List<String> loadList(String path) {
		File file = new File(path);
		try {
			List<String> returned = Files.readLines(file, Charset.defaultCharset());
			return returned;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
