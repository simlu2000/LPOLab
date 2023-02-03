package progetto.visitors.execution;

public class IntValue extends SimpleValue<Integer> {

	public IntValue(Integer value) {
		super(value);
	}

	@Override
	public int toInt() {
		return value;
	}

}