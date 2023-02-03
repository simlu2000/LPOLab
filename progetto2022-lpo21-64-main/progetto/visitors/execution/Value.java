package progetto.visitors.execution;

public interface Value {
	/* default conversion methods */
	default int toInt() {
		throw new InterpreterException("Expecting an integer");
	}

	default boolean toBool() {
		throw new InterpreterException("Expecting a boolean");
	}

	default PairValue toPair() {
		throw new InterpreterException("Expecting a pair");
	}

	default ArrayValue toArrayValue() {
		throw new InterpreterException("Expecting an array");
	}

	default EvaluateExpSeq toEvaluateExpSeq() {
		throw new InterpreterException("Expecting a list");
	}
}
