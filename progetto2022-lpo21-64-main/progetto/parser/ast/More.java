package progetto.parser.ast;

import static java.util.Objects.requireNonNull;

public abstract class More<FT, RT> {
	protected final FT first;
	protected final RT restSeq;

	protected More(FT first, RT restSeq) {
		this.first = requireNonNull(first);
		this.restSeq = requireNonNull(restSeq);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + first + "," + restSeq + ")";
	}
}
