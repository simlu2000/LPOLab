package lab08_04_01;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WordCounterUnsorted implements WordCounter {

	//conta quante volte ogni parola appare nello stream di caratteri readable memorizzando queste info in map passato come secondo argomento.
		//inizialmente map è vuoto, una volta chiamato count vengono inserite in esso tutte le parole con un valore associato n (che indiica frequenza)
			//parola => sequenza non vuota di lettere dove maiuscole e minuscole sono considerate diverse
	protected void count(Readable readable, Map<String, Integer> map) {

		try (var input = new Scanner(readable)) { //input, uso java.util.Scanner per leggere le parole da readable
			input.useDelimiter("[^a-zA-Z]+"); //useDelimiter per saltare tutte le non parole
			while (input.hasNext()) { //finche input ha un successivo 
				var nextWord = input.next(); //prendo il successivo
				var n = map.get(nextWord); //prendo il numero di nextWord con il metodo get()
				map.put(nextWord, n != null ? n + 1 : 1); //inserisco in map il numero di nextWord trovato con il metodo put()
			}
		}
	}

	@Override
	public Map<String, Integer> count(Readable readable) { //crea un dizionario map nuovo, chiama count(readable,map) e restituisce map come risultato
	    // calls 'count(Readable readable, Map<String, Integer> map)' with an initially empty dictionary created from HashMap<String,Integer>
		var newMap = new HashMap<String, Integer>();
		count(readable, newMap);
		return newMap;
	}

}
