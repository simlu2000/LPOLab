package lab05_12_10;

import java.util.Scanner;
import static java.lang.System.*;

/* Nuova espressione regolare 1) permette all'analizzatore di riconoscere
 * 	-gruppo 1: sequenze non vuote di lettere, cifre decimali, carattere underscore _, che devono iniziare con una lettera(?)
 * 	-gruppo 2: ? sequenze non vuote di cifre decimali, dove 0 non può apparire in posizioni non significative
 * 	-gruppo 3: sequenze non vuote di spazi bianchi
 
 
 Espressione regolare 2):


 Espressione regolare 3):

 
 
 */




public class Main {

 //([a-zA-Z]+)|(\\d+)|(\\s+)  espressione regolare iniziale
	public static void main(String[] args) {
		Scanner scanner = new Scanner(in);
		Lexer lexer = LineLexer.withRegex("([a-zA-Z0-9_]+)|(\\d+)|(\\s+)");
		while (scanner.hasNextLine()) {
			lexer.reset(scanner.nextLine());
			while (lexer.hasNext()) {
				lexer.next();
				out.println("lexeme: '" + lexer.lexemeString() + "'" + " group: " + lexer.lexemeGroup());
			}
		}
		scanner.close();
	}
}
