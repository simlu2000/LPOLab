package lab08_04_01;

import java.util.Map;
import java.util.TreeMap;

public class WordCounterSorted extends WordCounterUnsorted {

	@Override
	public Map<String, Integer> countSorted(Readable readable) {
	    // call 'counts' with an initially empty instance of 'TreeMap<String,Integer>'
	    // with 'TreeMap' words are sorted according to the standard order on strings
		var newMapSorted = new TreeMap<String, Integer>();
		count(readable, newMapSorted);
		return newMapSorted;
	}

}
