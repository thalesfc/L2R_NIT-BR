package termselection;

import java.util.List;

import common.ArgParser;
import common.Data;


public class TermSelection {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArgParser.parse(args);
		// loading the terms from the description of all items
		List<String> description = Data.getDESCRIPTION();

		
		// TODO compute the score for the given metrics (CHI2, KLD, MI, DICE)
		
		/* TODO compute the new representation of items and users
		 * ( using the k highly scored terms )
		*/
		
		// TODO save this terms for all items and all users
	}
}
