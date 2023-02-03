package progetto.parser.ast;
import static java.util.Objects.requireNonNull;
import progetto.visitors.Visitor;

public class DoWhileStmt implements Stmt{
    private final Block block;
    private final Exp exp;

    public DoWhileStmt(Block block, Exp exp){
        this.block = requireNonNull(block);
        this.exp = requireNonNull(exp);

    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + "(" + block + ","+ exp + ")";

    }

    @Override
    public <T> T accept(Visitor<T> visitor){
        return visitor.visitDoWhileStmt(block,exp);
    }
}
