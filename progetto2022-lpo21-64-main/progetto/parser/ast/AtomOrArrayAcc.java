package progetto.parser.ast;
import progetto.visitors.Visitor;


public class AtomOrArrayAcc extends BinaryOp{
    public AtomOrArrayAcc(Exp left, Exp right){
        super(left,right);
    }    

    @Override
    public <T> T accept(Visitor<T> visitor){
        return visitor.visitArrayAccess(left,right);
    }
}
