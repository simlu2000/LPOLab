package progetto.parser.ast;
import static java.util.Objects.requireNonNull;

import progetto.visitors.Visitor;

public class WhileStmt implements Stmt{

    private final Block block;
    private final Exp exp;

    public WhileStmt(Exp exp, Block block){
        this.exp = requireNonNull(exp);
        this.block = requireNonNull(block);

    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + "(" + exp + ","+ block + ")";

    }

    @Override
    public <T> T accept(Visitor<T> visitor){
        return visitor.visitWhileStmt(exp,block);
    }
}
