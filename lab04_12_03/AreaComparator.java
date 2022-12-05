package lab04_12_03;

/*
 * Classe singleton, ossia con una sola istanza.
 * 
 * Confronta le figure basandosi sulle loro aree.
 */

public class AreaComparator implements ShapeComparator {

	public static final AreaComparator instance = new AreaComparator();

	private AreaComparator() {
	}

	/* requires shape1 != null && shape2 != null */    
	public int compare(Shape shape1, Shape shape2) {
	    double area1 = shape1.area();
		double area2 = shape2.area();

		//evito di usare + condizioni(if):
		return area1 < area2 ? -1 : area1 == area2 ? 0 : 1; //restituisce -1 SE A1<A2, 0 SE A1=A2, 1 SE A1>A2. Uso i : per "separare" le condizioni


	}

	/*METODO AUSILIARIO */

}
