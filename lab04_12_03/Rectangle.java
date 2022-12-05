package lab04_12_03;

/*
 * Implementa rettangoli con lati paralleli agli assi
 */
public class Rectangle implements Shape {
	/* invariant width > 0 && height > 0 */
	public static final double defaultSize = 1;
	private double width = Rectangle.defaultSize;
	private double height = Rectangle.defaultSize;

	private final Point center = new Point();

	/*
	 * Rettangolo con centro sull'origine degli assi
	 */
	private Rectangle(double width, double height) {
	    this.width=Rectangle.ifNegative(width);
		this.height=Rectangle.ifNegative(height);
	}

	private Rectangle(double width, double height, Point center) {
	    this(width,height); //larghezza e altezza rettangolo
		this.center.move(center.getX(),center.getY()); //il centro del rettangolo è in posizione x,y
	}

	/*
	 * Rettangolo con dimensioni di default e centro sull'origine degli assi
	 */
	public Rectangle() {
	}

	/*
	 * Factory method
	 */
	public static Rectangle ofWidthHeight(double width, double height) { //creazione rettangolo senza specificare centro
	    return new Rectangle(width,height);
	}

	/*
	 * Factory method
	 */
	public static Rectangle ofWidthHeightCenter(double width, double height, Point center) {//creazione + centro
	    return new Rectangle(width,height,center);
	}

	public void move(double dx, double dy) { //trasla verso dx,dy
		this.center.move(dx,dy);
	}

	public void scale(double factor) {
	    this.width=Rectangle.ifNegative(this.width * factor);
		this.height=Rectangle.ifNegative(this.height * factor);
	}

	public Point getCenter() { //copia del centro quindi nuovo punto
	    return new Point(center);
	}

	public double perimeter() {
	    return this.width + this.height + this.width + this.width; //somma lati
	}

	public double area() {
	    return this.width * this.height; //base*altezza
	}

	/*METODI AUSILIARI*/
	public static double ifNegative(double value){
		if(value <= 0 )
			throw new IllegalArgumentException();
		return value;
	}
}
