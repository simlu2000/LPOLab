package progetto.parser.ast;

import progetto.visitors.Visitor;

public class MoreStmt extends More<Stmt, StmtSeq> implements StmtSeq {

	public MoreStmt(Stmt first, StmtSeq restSeq) {
		super(first, restSeq);
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitMoreStmt(first, restSeq);
	}
}
