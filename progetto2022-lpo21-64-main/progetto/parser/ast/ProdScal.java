package progetto.parser.ast;
import progetto.visitors.Visitor;

public class ProdScal extends BinaryOp{
    public ProdScal(Exp left, Exp right){
        super(left,right);
    }
    
    @Override 
    public <T> T accept(Visitor<T> visitor){
        return visitor.visitProdScal(left,right);
    }
}