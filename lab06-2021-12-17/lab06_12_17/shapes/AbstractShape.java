package lab06_12_17.shapes;

public abstract class AbstractShape implements Shape {

	private final Point center = new PointClass();

	protected static final double defaultSize = 1;

	protected static double requirePositive(double size) {
	    if(size <= 0 )
			throw new IllegalArgumentException("Negative size!");
		return size;
	}

	protected AbstractShape(Point center) {
		center.move(center.getX(), center.getY());
	}

	protected AbstractShape() {
	}

	@Override
	public void move(double dx, double dy) { //spostamento forma
	    center.move(dx,dy);
	}

	@Override
	public Point getCenter() {
	    return new PointClass(center);
	}

}
