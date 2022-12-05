package lab04_12_03;

public class Circle implements Shape { //classe che implementa l'interfaccia creata
	/* invariant radius > 0 */
	public static final double defaultSize = 1;
	private double radius = Circle.defaultSize;
	private final Point center = new Point();

	/*
	 * Cerchio con centro sull'origine degli assi
	 */    
	private Circle(double radius) { //specifica raggio del cerchio
	    this.radius = Circle.ifNegative(radius);
	}

	private Circle(double radius, Point center) {
	    this(radius); //specifico il raggio
		this.center.move(center.getX(), center.getY());//centro del cerchio -> centro.move(coordinata x, coordinatay)
	}
    
	/*
	 * Cerchio con dimensioni di default e centro sull'origine degli assi
	 */
	public Circle() {
	}

	/*
	 * Factory method
	 */
	public static Circle ofRadius(double radius) { //creazione nuovo cerchio con specifica raggio
	    return new Circle(radius); 
	}

	/*
	 * Factory method
	 */
	public static Circle ofRadiusCenter(double radius, Point center) { //creazione nuovo cerchio specificando raggio e centro
	    return new Circle(radius,center);
	}
	
	public void move(double dx, double dy) { //trasla il cerchio lungo il vettore (dx,dy)
	    this.center.move(dx,dy);  //considero il centro dell'oggetto, devo spostare tutta la figura
	}

	public void scale(double factor) { //scala la figura del fattore, senza traslare il suo centro;  requires factor > 0
	    this.radius= Circle.ifNegative(factor * this.radius); //controllo se positivo + formula
	}

	public Point getCenter() { //restituisce una copia del centro, quindi siccome è una copia
	    return new Point(center); //creiamo nuovo punto center (center è di tipo Point) e lo restituiamo
	}

	public double perimeter() {
	    return this.radius * 2 * Math.PI;  //perimetro = raggio * 2 * pigreco
	}

	public double area() {
	    return this.radius * this.radius * Math.PI; //area = raggio^2 * pigreco
	}


	/*METODI AUSILIARI*/
	public static double ifNegative(double value){
		if(value <= 0 )
			throw new IllegalArgumentException("Negative value!");
		return value;
	}
}
