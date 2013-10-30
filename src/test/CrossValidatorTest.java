package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import methodology.CrossValidator;


import common.Config;
import common.Data;

public class CrossValidatorTest {
	public static void main(String[] args) throws IOException {
		Config.loadConfigs("bookCrossing");

		for(int i = 0; i < 5; ++i){
			CrossValidator.randomCrossV(i);

			// load ratings list
			BufferedReader in = new BufferedReader(new FileReader(new File("/Users/thalesfc/Downloads/fold" + i + "-seed0")));

			Set<String> newItems = new HashSet<String>();
			Map<String, Set<String>> newItemsRatedUsers = new HashMap<String, Set<String>>();
			List<String> newBooks = Data.getNewItems();
			for (String newBook : newBooks) {
				newItemsRatedUsers.put(newBook, new HashSet<String>());
			}

			// testing if all item marked as new is realy new
			while (in.ready()) {
				String s = in.readLine();
				String[] tokens = s.split(" ");
				String newItem = tokens[0].toLowerCase();
				if (Data.getBookFlag(newItem.toLowerCase()) == Data.OLD_BOOK){
					System.err.println("# ERRO");
					System.exit(1);
				}

				newItems.add(newItem);
				newItemsRatedUsers.get(newItem).add(tokens[tokens.length-1]);
			}
			in.close();

			// testing if all item new in the system is new here
			List<String> books = Data.getBOOKS();
			for (String book : books) {
				if(Data.getBookFlag(book) == Data.NEW_BOOK){
					if(newItems.contains(book) == false){
						System.err.println("# ERRO");
						System.exit(1);
					}
				}
			}

			// testing if all users who rated the new items are okay
			for(Entry<String, Set<String>> entry : newItemsRatedUsers.entrySet()){
				String newItem = entry.getKey().toLowerCase();
				Set<String> users = entry.getValue();

				Set<String> check = Data.newItemsRatedUsers().get(newItem);

				if (users.equals(check) == false){
					System.out.println(newItem);
					System.err.println(users);
					System.err.println(check);
					System.exit(1);
				}
			}
		}
	}
}
