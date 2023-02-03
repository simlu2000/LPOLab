package progetto.visitors.typechecking;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.hash;

public class PairType implements Type {

	private final Type fstType; //dichiarazione tipo fst e snd
	private final Type sndType;

	public static final String TYPE_NAME = "PAIR";

	public PairType(Type fstType, Type sndType) { //metodo per associare fstType e sndType + controllo se sono nulli con il metodo requireNonNull()
		this.fstType = requireNonNull(fstType);
		this.sndType = requireNonNull(sndType);
	}

	public Type getFstType() { //restituzione tipo fst
		return fstType;
	}

	public Type getSndType() {//restituzione tipo snd
		return sndType;
	}

	@Override
	public final boolean equals(Object obj) { //metodo equals per confronto oggetto attuale con obj passato come parametro
		if (this == obj) //se corrispondono true
			return true;
		if (obj instanceof PairType pt) //se obj fa parte del PairType pt)
		return fstType.equals(pt.fstType) && sndType.equals(pt.sndType); //richiamo equals per confronto con fstType e sndType
		return false;
	}

	@Override
	public int hashCode() { //restituzione codice hash con parametri fstType e sndType
		return hash(fstType,sndType);
	}

	@Override
	public String toString() { //stampa una stringa con dei caratteri e i due tipo fst / snd
		return "(" + fstType + "*" + sndType + ")";
	}

}
