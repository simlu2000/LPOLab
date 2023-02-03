package progetto.visitors.execution;

import progetto.visitors.execution.EvaluateExpSeq;

import java.util.LinkedList;
import static java.util.Arrays.equals;
import static java.util.Arrays.hashCode;

public class ArrayValue implements Value {
    private Value[] a; //array a di tipo Value
    private int length = 0; //lunghezza 0 

    public int sizeArray() { //lunghezza array
        return length;
    }

    // crea un array a con gli stessi elementi della lista doppiamente collegata
    public ArrayValue(EvaluateExpSeq dll) {
        a = new Value[dll.size()]; //creazione array con dimensione size
        dll.toArray(a); //aggiungo dll all'array a
        length = dll.size(); //ora la lunghezza array è la lunghezza della lista
    }

    // restituisce il valore nella posizione index dell'array e solleva eccezione
    // altrimenti solleva eccezione
    public Value access(int index) {
        try {
            return a[index];
        } catch (Exception exc) {
            throw new InterpreterException(exc);
        }
    }

    @Override
    public ArrayValue toArrayValue() {
        return this; // restituisce l'array uguale alla lista dll.
    }

    // restituisce true se due array a,b sono uguali.
    public final boolean equals(Value[] b) {
        return java.util.Arrays.equals(a, b); //confronta array
    }

    // true se elementi dell'array sono =0
    public boolean toArrayNull() {
        for (int i = 0; i < length; ++i) { //scorro array
            if (a[i].toInt() != 0) //se l'elemento attuale non è intero ritorno false (perchè non è nullo)
                return false;
        }
        return true; //altrimenti è nullo quindi true
    }

    // restituisce la somma dei valori presenti in un array
    public int toSum() {
        int sum = 0;
        for (int i = 0; i < length; ++i) { //scorro array
            sum += a[i].toInt(); //sommo elementi di tipo intero
        }
        return sum;
    }

    @Override
    public int hashCode() {
        return java.util.Arrays.hashCode(a); //ritorno hashcode
    }

    @Override
    public String toString() {
        String s = new String();
        for (var v : this.a) {
            s += v + ";";
        }
        if (length > 0)
            s = s.substring(0, s.length() - 1);
        return "[" + s + "]";
    }

}
