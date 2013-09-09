package common;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ArgParser {
	
	@Override
	public String toString() {
		return ArgParser.print();
	};
	
	public static String print(){
		return "(database, '" + Data.getDATABASE() + "')";
	}

	public static void parse(String[] args) {
		// create the command line parser
		CommandLineParser parser = new BasicParser();
		
		// create the Options
		Options options = new Options();
		Option help = new Option( "help", "print this message" );
		options.addOption(help);
		
		OptionBuilder.withLongOpt("dataset");
		OptionBuilder.withDescription("The dataset for this execution.");
		OptionBuilder.isRequired();
		OptionBuilder.hasArg();
		Option dataset = OptionBuilder.create('d');
		options.addOption(dataset);
		
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		
		try {
			CommandLine line = parser.parse(options, args, true);
			
			/*
			 * threating args
			 */
			if(line.hasOption("d")){
				Data.setDATABASE(line.getOptionValue("d"));
			}
			
			if(line.hasOption("help")){
				formatter.printHelp( "L2R_NIT-BR", options );
			}
		} catch( ParseException exp ) {
			formatter.printHelp( "L2R_NIT-BR", options );
		    System.err.println( "\nERROR\t" + exp.getMessage() );
		    System.exit(1);
		}
		
		System.out.println("# args: " + ArgParser.print());
	}
	
	public static void main(String[] args) {
		args = new String[]{ "-d", "\"bookCrossing\"" };
		ArgParser.parse(args);
	}
}
