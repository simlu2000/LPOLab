package progetto.visitors;

import progetto.parser.ast.Block;
import progetto.parser.ast.Exp;
import progetto.parser.ast.Stmt;
import progetto.parser.ast.StmtSeq;
import progetto.parser.ast.Variable;
import progetto.parser.ast.ExpSeq;

public interface Visitor<T> {
	T visitAdd(Exp left, Exp right);

	T visitAssignStmt(Variable var, Exp exp);

	T visitIntLiteral(int value);

	T visitEq(Exp left, Exp right);

	T visitMul(Exp left, Exp right);

	T visitPrintStmt(Exp exp);

	T visitSimpleProg(StmtSeq stmtSeq);

	T visitSign(Exp exp);

	T visitSimpleVariable(Variable var); // the only corner case ...

	T visitVarStmt(Variable var, Exp exp);

	T visitNot(Exp exp);

	T visitAnd(Exp left, Exp right);

	T visitBoolLiteral(boolean value);

	T visitIfStmt(Exp exp, Block thenBlock, Block elseBlock);

	T visitBlock(StmtSeq stmtSeq);

	T visitPairLit(Exp left, Exp right);

	T visitFst(Exp exp);

	T visitSnd(Exp exp);

	// Add methods:

	T visitProdScal(Exp left, Exp right);

	T visitWhileStmt(Exp exp, Block block);

	T visitDoWhileStmt(Block block, Exp exp);

	T visitSingleStmt(Stmt stmt);

	T visitMoreStmt(Stmt first, StmtSeq restSeq);

	T visitSum(Exp exp);

	T visitLength(Exp exp);

	T visitSingleExp(Exp exp);

	T visitMoreExp(Exp first, ExpSeq restSeq);

	T visitArrayNull(Exp exp);

	T visitArrayLiteral(ExpSeq expSeq);

	T visitArrayAccess(Exp name, Exp index);
}
