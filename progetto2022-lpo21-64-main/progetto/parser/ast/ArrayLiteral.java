package progetto.parser.ast;

import progetto.visitors.Visitor;
import static java.util.Objects.requireNonNull;

public class ArrayLiteral implements Exp {
    private final ExpSeq expSeq;    

    public ArrayLiteral(ExpSeq expSeq){
        this.expSeq = requireNonNull(expSeq);

    }
    @Override
    public String toString(){
        return getClass().getSimpleName()+ "(" + expSeq + ")";

    }

    @Override
    public <T> T accept(Visitor<T> visitor){
        return visitor.visitArrayLiteral(expSeq);
    }
}
