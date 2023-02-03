package progetto.parser.ast;
import progetto.visitors.Visitor;
import static java.util.Objects.requireNonNull;


public class ArrayNull implements Exp{
    private final Exp exp;

    public ArrayNull(Exp exp){
        this.exp = requireNonNull(exp);
    }

    @Override
    public <T> T accept(Visitor<T> visitor){
        return visitor.visitArrayNull(exp);
    }
    
}
