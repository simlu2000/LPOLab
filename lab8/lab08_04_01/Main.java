package lab08_04_01;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.HashMap;

public class Main {
	// class fields for managing options
	private static final String INPUT_OPT = "-i";
	private static final String OUTPUT_OPT = "-o";
	private static final String SORTED_OPT = "-sort";

	/* maps options to their string values, if any
	options with no argument can be null (option is not set) or an array of length 0 (option is set)
	options with argument are initialized with an array of length 1 containing null */
	private static final Map<String, String[]> options = new HashMap<>();
	static {
		options.put(INPUT_OPT, new String[1]); // one argument, initially null
		options.put(OUTPUT_OPT, new String[1]); // one argument, initially null
		options.put(SORTED_OPT, null); // no arguments, option not active by default
	}

	// prints 'msg' on the standard error and exits with status code 1
	private static void error(String msg) {
	    // to be completed
		System.err.println(msg); //stampa msg
		System.exit(1); //esco con codice status 1
	}

	// processes all options and their arguments, if any, by using class field 'options' 
	private static void processArgs(String[] args) {
	    // to be completed
		for (var i = 0; i < args.length; i++) { //siccome deve processare tutte le opzioni ed argomenti, ciclo tutto
			
			var opt = args[i]; //opzione attuale
			if (!options.containsKey(opt)) //se l'opzione inserita opt non contiene una chiave
				error("Option error.\nValid options:\n\t-i <input>\n\t-o <output>\n\t-sort"); //errore
			
			var val = options.get(opt); //prendi valore/argomento
		
			if (val == null) //imposto opzione senza argomento
				options.put(opt, new String[0]);
			else if (val.length > 0) //altrimenti se non nullo
			{
				if (i + 1 == args.length)
					error("Missing argument for option " + opt);
				val[0] = args[++i];
			}
		}
		 
	}

	// tries to open the input stream or the standard input if 'inputPath' is null; returns a corresponding buffered reader
	private static BufferedReader tryOpenInput(String inputPath) throws FileNotFoundException {
	    //se inputPath null restituisco nuovo inputstreamreader altrimentii filereader
		return new BufferedReader(inputPath == null ? new InputStreamReader(System.in) : new FileReader(inputPath) );
	}

	// tries to open the output stream or the standard output if 'outputPath' is null; returns a corresponding print writer
	private static PrintWriter tryOpenOutput(String outputPath) throws FileNotFoundException {
		return outputPath == null ? new PrintWriter(System.out) : new PrintWriter(outputPath);

	}

	public static void main(String[] args) {
	    
	    // manages streams and exceptions with try-with-resources
	    // calls 'count' or 'countSorted' methods
	    // prints the result on the output stream with method 'println()'

		processArgs(args); // processo argomenti
		// manages streams and exceptions with try-with-resources and calls 'count' or 'countSorted' methods
		try (var rd = tryOpenInput(options.get(INPUT_OPT)[0]); var pw = tryOpenOutput(options.get(OUTPUT_OPT)[0]);) {
			if (options.get(SORTED_OPT) != null)
				pw.println(new WordCounterSorted().countSorted(rd));
			else
				pw.println(new WordCounterUnsorted().count(rd));

		} catch (FileNotFoundException e) {
			error("Output error: " + e.getMessage());
		} catch (Throwable t) {
			error("Unexpected error: " + t.getMessage());
		}
	}

}
