package lab06_12_17.shapes;

public class Circle extends AbstractShape {
	/* invariant radius > 0 */
	private double radius = Circle.defaultSize;
    
        // private object method to be used in the constructors
	private void setRadius(double radius) {
		this.radius = requirePositive(radius);
	}

	/*
	 * Cerchio con centro sull'origine degli assi
	 */
	protected Circle(double radius) {
	    setRadius(radius);
	}

	protected Circle(double radius, Point center) {
	    super(center);
		setRadius(radius);
		
	}

	/*
	 * Cerchio con dimensioni di default e centro sull'origine degli assi
	 */
	public Circle() {
	}

	/*
	 * Factory method
	 */
	public static Circle ofRadius(double radius) {
	    return new Circle(radius);
	}

	/*
	 * Factory method
	 */
	public static Circle ofRadiusCenter(double radius, Point center) {
	    return new Circle(radius,center);
	}

	@Override
	public void scale(double factor) {
	    setRadius(factor * radius);
	}

	@Override
	public double perimeter() {
	    return radius * 2 * Math.PI;
	}

	@Override
	public double area() {
	    return radius * radius * Math.PI;
	}

}
