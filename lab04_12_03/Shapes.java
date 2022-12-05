package lab04_12_03;

public class Shapes {

	/*
	 * restituisce la prima figura maggiore o uguale alle altre in shapes rispetto al comparator comp,
	 * null se shapes e` vuoto
	 * requires shapes != null && comp != null
	 */    
	public static Shape max(Shape[] shapes, ShapeComparator comp) {
	    //controlli
		if(shapes.length == 0)
			return null;
		if(comp == null)
			throw new NullPointerException();

		Shape s = shapes[0];
		for(int i = 0; i < shapes.length; i++){ //scorro
			Shape t = shapes[i]; //prendo shapes attuale (pos i)
			if(comp.compare(t,s) > 0) //se forma attuale t (in i) è > di s, allora avremo un valore > 0 -> quindi in s metto t(che è piu grande)
			s = t;

		}
		return s; //restituisco il più grande
		
	}

	/*
	 * trasla tutte le figure lungo il vettore (dx,dy)
	 * requires shapes != null
	 */
	public static void moveAll(Shape[] shapes, double dx, double dy) {
	    for ( int i = 0; i < shapes.length; i++){
			Shape s = shapes[i];
			s.move(dx,dy);
		}
	}
    
	/*
	 * scala tutte le figure del fattore factor, senza traslare il loro centro
	 * requires shapes != null && factor > 0
	 */
	public static void scaleAll(Shape[] shapes, double factor) {
	    if(factor <= 0)
			throw new IllegalArgumentException();

		for(int i = 0; i< shapes.length; i++){
			Shape s = shapes[i];
			s.scale(factor);
		}
	}
    
	/*
	 * restituisce l'area totale di tutte le figure in shapes
	 * requires shapes != null
	 */
	public static double totalArea(Shape[] shapes) {
		double a = 0;
	    for(int i = 0; i < shapes.length; i++){
			Shape s = shapes[i];
			a = a + s.area();	
		}
		return a;
	}
}
