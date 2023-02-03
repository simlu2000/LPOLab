package progetto.parser.ast;

import progetto.visitors.Visitor;

public class MoreExp extends More<Exp, ExpSeq> implements ExpSeq {
    public MoreExp(Exp first, ExpSeq restSeq) {
        super(first, restSeq);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitMoreExp(first, restSeq);
    }
}
