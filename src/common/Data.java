package common;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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
	private static Map<String, List<String>> trainPositiveRATINGS;

	/*
	 * For each new item
	 * get the list of users who rated it
	 * { new_item-ID -> [userID_1, user_ID_2, ... userID_m]}
	 */
	private static Map<String, Set<String>> newItemsRatedUsers;

	/*
	 *  { bookID -> flag_new }
	 *  
	 *  flag_new : 
	 *  		false == old book
	 *  		true == new book
	 */
	public final static boolean OLD_BOOK = false;
	public final static boolean NEW_BOOK = true;
	static Map<String, Boolean> booksFLAG;

	public static Boolean getBookFlag(String book){
		return booksFLAG.get(book);
	}

	public static void setBookFlagValue(String book, Boolean flag_value){
		booksFLAG.put(book, flag_value);
	}

	public static void setAllBooksFlag(boolean value) {
		newItemsRatedUsers = null;
		List<String> books = getBOOKS();
		booksFLAG = new HashMap<String, Boolean>(books.size());
		for( String book : books){
			booksFLAG.put(book, value);
		}
	}

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

	public static Map<String, List<String>> getTrainPositiveRATINGS(){
		if(trainPositiveRATINGS == null){
			List<List<String>> allRatings = loadMatrix( System.getProperty("user.dir") + 
					"/data/" + DATABASE + "/ratings.csv");

			// loading all ratings
			List<String> users = Data.getUSERS();

			// allocing resources
			trainPositiveRATINGS = new HashMap<String, List<String>>(users.size());

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
					if(rating >= Config.RATING && booksFLAG.get(ISBN) == OLD_BOOK){
						userRatings.add(ISBN);
					}
				}
				trainPositiveRATINGS.put(user, userRatings);
			}
		}
		return trainPositiveRATINGS;
	}

	/*
	 *  Private functions to load data from files
	 */
	private static List<List<String>> loadMatrix(String path) {
		File file = new File(path);
		try {
			List<String> temp = Files.readLines(file, Charset.defaultCharset());
			List<List<String>> returned = new ArrayList<List<String>>(temp.size());
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

	public static List<String> getNewItems(){
		List<String> newBooks = new ArrayList<String>();
		for(Entry<String, Boolean> entry : booksFLAG.entrySet()){
			if(entry.getValue() == NEW_BOOK)
				newBooks.add(entry.getKey());
		}
		return newBooks;
	}

	/**
	 *  @return for each item, the list of users who gave it a positive rating
	 */
	public static Map<String, Set<String>> newItemsRatedUsers() {
		if (newItemsRatedUsers == null){
			List<String> newItems = Data.getNewItems();
			List<List<String>> allRatings = loadMatrix( System.getProperty("user.dir") + 
					"/data/" + DATABASE + "/ratings.csv");

			newItemsRatedUsers = new HashMap<String, Set<String>>(newItems.size());
			List<String> users = Data.getUSERS();

			// excluding negative ratings and ratings on test-set
			for(int i=0; i < users.size(); ++i){
				String user = users.get(i);

				List<String> uRatings = allRatings.get(i);

				// bRating is ISBN:rate
				for(String bRating : uRatings){
					String[] tokens = bRating.split(":");
					String ISBN = tokens[0];
					int rating = Integer.parseInt(tokens[1]);
					if(rating >= Config.RATING && booksFLAG.get(ISBN) == NEW_BOOK){
						if(newItemsRatedUsers.containsKey(ISBN)){
							newItemsRatedUsers.get(ISBN).add(user);
						} else{
							Set<String> set = new HashSet<String>();
							set.add(user);
							newItemsRatedUsers.put(ISBN, set);
						}
					}
				}

			}
		}
		return newItemsRatedUsers;
	}
}
