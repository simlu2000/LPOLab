package progetto.visitors.execution;

import java.util.Arrays;
import java.util.LinkedList;

public class EvaluateExpSeq extends LinkedList<Value> implements Value {
    public EvaluateExpSeq(Value exp) {
        super();
        this.add(exp); // aggiunge in fondo alla lista exp
    }

    @Override
    public EvaluateExpSeq toEvaluateExpSeq() {
        return this; // restituisce la lista
    }
}
