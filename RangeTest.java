package lab07_03_18;

import java.util.Iterator;

public class RangeTest {

    public static void main(String[] args) {
	//Range r = new Range(2,10); //prova con start e end
	Range r = new Range(3); //prova con solo end

	/* prints with only the end
	 * 0 0
	 * 0 1
	 * 0 2
	 * 1 0
	 * 1 1
	 * 1 2
	 * 2 0
	 * 2 1
	 * 2 2
	 */

	for (int x : r)
	    for (int y : r)
		System.out.println(x + " " + y);
    }
}
