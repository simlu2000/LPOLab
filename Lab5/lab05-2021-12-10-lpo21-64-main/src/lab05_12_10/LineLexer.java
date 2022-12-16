package lab05_12_10;

import java.util.NoSuchElementException;
import java.util.regex.*; //così puo usare le classi del pacchetto da usare x implementare analizzatore lessicale

/*ANALIZZATORE-> permette di riconoscere i lessemi che conpongono una linea
una linea di testo, usando un matcher creato a partire dall'espressione regolare 
passata ai factory method.
*/
public class LineLexer implements Lexer {
	private final Matcher matcher;
	private MatchResult result; // risultato dell'ultimo match che ha avuto successo; definito solo se il metodo 'reset()' non è stato chiamato

	private MatchResult getResult() {
		if (this.result == null)
			throw new IllegalStateException();
		return this.result;
	}

	// crea un matcher con il pattern ottenuto compilando 'regEx' e la sequenza di input uguale a 'line'
	private LineLexer(String line, String regEx) {
		this.matcher = Pattern.compile(regEx).matcher(line);
	}

	// crea un matcher con il pattern ottenuto compilando 'regEx' e la sequenza di input uguale alla stringa vuota
	private LineLexer(String regEx) {
		this.matcher = Pattern.compile(regEx).matcher("");

	}

	// factory method che usa il costruttore LineLexer(String line, String regEx) 
	public static LineLexer withLineRegex(String line, String regEx) {
		return new LineLexer(line,regEx);
	}

	// factory method che usa il costruttore LineLexer(String regEx)
	public static LineLexer withRegex(String regEx) {
	    return new LineLexer(regEx);
	}

	public void next() {
	    /* 
      cerca di riconoscere il prossimo lessema a partire dalla posizione corrente nella linea; 
	  in caso positivo, avanza nella lettura della linea
	  solleva java.lang.RuntimeException se nessun lessema viene riconosciuto  
	  solleva java.lang.NoSuchElementException se la linea e` finita	 
		 */

		if(!this.hasNext()) //se l'elemento attuale non ha un successivo (quindi linea finita)
		 	throw new NoSuchElementException("End Line");
		if(!this.matcher.lookingAt()) //se non viene riconosciuto il lessema successivo
			throw new RuntimeException("NO recognized the next lexeme, in the position" + matcher.regionStart());
		this.result = this.matcher.toMatchResult(); //se questa linea viene raggiunta, il match ha avuto successo
		this.matcher.region(this.matcher.end(), this.matcher.regionEnd()); //viene consumato il lessema, questo implica al reset del matcher
	}

	public String lexemeString() { //lessema appena riconosciuto
		return this.getResult().group();
	}

	public int lexemeGroup() { //gruppo del lessema appena riconosciuto
		MatchResult res = this.getResult(); //cosi prendo lessema riconosciuto
		int groups = res.groupCount(); //numero di elem nel gruppo
		for(int group = 1; group <= groups ; group++) //itera fino alla fine del gruppo
			if(res.group(group) != null)  //se il risultato del gruppo (elem attuale) non è nullo
				return group; //lo ritorno
		throw new AssertionError("Unexpected error"); //non raggiungibile
			}

	public boolean hasNext() { //true se linea non ancora tutta letta
		return  this.matcher.regionStart () < this.matcher.regionEnd(); //se regionStart del mather dell'oggetto minore della regionEnd, vuol dire che non è stata tutta letta
	}

	public void reset(String line) { //resetta lexer e linea da leggere, la lettura continua dal primo carattere della linea
	    this.result = null; //risultato nullo
		this.matcher.reset(line); //resetto linea
	}
}
