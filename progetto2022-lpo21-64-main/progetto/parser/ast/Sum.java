package progetto.parser.ast;

import progetto.visitors.Visitor;

public class Sum extends UnaryOp{
    public Sum(Exp exp){
        super(exp);
    }

    @Override
    public <T> T accept(Visitor<T> visitor){
        return visitor.visitSum(exp);
    }
    
}
