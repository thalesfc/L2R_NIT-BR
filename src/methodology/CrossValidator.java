package methodology;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import common.Config;
import common.Data;


public class CrossValidator {
	public static void randomCrossV(int fold_number){
		/**
		 * testing the parameter
		 */
		if(fold_number >= Config.K_FOLD || fold_number < 0){
			System.err.println("O numero de folders deve estar entre 0 e " + Config.K_FOLD);
			System.exit(1);
		}

		// setting all books to old
		Data.setAllBooksFlag(false);

		System.out.println("\n<> Cross Validation: " + Config.K_FOLD + " - Fold: " + fold_number);

		/**
		 * getting all elements and storing then in a list
		 */
		List<String> books = Data.getBOOKS();

		// list that store books unsighned
		List<String> elementsNotAssigned = new ArrayList<String>();

		for( String ISBN : books){
			elementsNotAssigned.add(ISBN);
		}

		// structus used
		int numElements2Assign = elementsNotAssigned.size() / Config.K_FOLD;
		Random rnd = new Random(Config.SEED);

		for(int fold = 0; fold < Config.K_FOLD; ++ fold){

			boolean value2assign = (fold == fold_number)? true : false;

			Set<Integer> selectedBooksID = RandomListGenerator.randomSample4(rnd,
					elementsNotAssigned.size(), numElements2Assign);

			List<String> elements2Remove = new ArrayList<String>(numElements2Assign);

			// 1 for: setting all values to corresponding value
			for (Integer selectedBookID : selectedBooksID) {

				String ISBN2update = elementsNotAssigned.get(selectedBookID);

				elements2Remove.add(ISBN2update);

				Data.setBookFlagValue(ISBN2update, value2assign);
			}
			// removing all elements from list
			elementsNotAssigned.removeAll(elements2Remove);
		}
	}
}
