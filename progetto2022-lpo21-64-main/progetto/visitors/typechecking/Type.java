package progetto.visitors.typechecking;

public interface Type {
	default void checkEqual(Type found) throws TypecheckerException {
		if (!equals(found)) //se il tipo non è stato trovato (quindi non è tra quelli che sono stati dichiarati)
			throw new TypecheckerException(found.toString(), toString()); //eccezione e viene stampata una stringa con il tipo che abbiamo considerato
	}

	default PairType checkIsPairType() throws TypecheckerException {
		if (this instanceof PairType pt)
			return pt;
		throw new TypecheckerException(toString(), PairType.TYPE_NAME);
	}

	default Type getFstPairType() throws TypecheckerException {
		return checkIsPairType().getFstType();
	}

	default Type getSndPairType() throws TypecheckerException {
		return checkIsPairType().getSndType();
	}
}
