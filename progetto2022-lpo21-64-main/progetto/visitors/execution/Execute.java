package progetto.visitors.execution;

import progetto.environments.EnvironmentException;
import progetto.environments.GenEnvironment;

import progetto.parser.ast.Block;
import progetto.parser.ast.Exp;
import progetto.parser.ast.Stmt;
import progetto.parser.ast.StmtSeq;
import progetto.parser.ast.Variable;
import progetto.parser.ast.ExpSeq;
import progetto.parser.ast.Length;

import progetto.visitors.Visitor;

import static java.util.Objects.requireNonNull;
import java.util.LinkedList;
import java.io.PrintWriter;

public class Execute implements Visitor<Value> {

	private final GenEnvironment<Value> env = new GenEnvironment<>();
	private final PrintWriter printWriter; // output stream used to print values

	public Execute() { //metodo esecuzione e avviene la stampa con l'output stream indicato qui sopra
		printWriter = new PrintWriter(System.out, true);
	}

	public Execute(PrintWriter printWriter) {
		this.printWriter = requireNonNull(printWriter);
	}

	// SEMANTICA DINAMICA PER PROGRAMMI; no value returned by the visitor

	@Override
	public Value visitSimpleProg(StmtSeq stmtSeq) { //visita programma, se riusciamo ok altrimenti solleva eccezione di variabile indefinita
		try {
			stmtSeq.accept(this);
			// possible runtime errors
			// EnvironmentException: undefined variable
		} catch (EnvironmentException e) {
			throw new InterpreterException(e);
		}
		return null;
	}

	// SEMANTICA DINAMICA STATEMENTS; no value returned by the visitor

	@Override
	public Value visitAssignStmt(Variable var, Exp exp) {
		env.update(var, exp.accept(this));
		return null;
	}

	@Override
	public Value visitPrintStmt(Exp exp) {
		printWriter.println(exp.accept(this));
		return null;
	}

	@Override
	public Value visitVarStmt(Variable var, Exp exp) {
		env.dec(var, exp.accept(this));
		return null;
	}

	@Override
	public Value visitIfStmt(Exp exp, Block thenBlock, Block elseBlock) {
		if (exp.accept(this).toBool())
			thenBlock.accept(this);
		else if (elseBlock != null)
			elseBlock.accept(this);
		return null;
		// exp.accept(this).toBool() ? exp.accept(this) : exp.accept(this)
		// return null
	}

	@Override
	public Value visitBlock(StmtSeq stmtSeq) {
		env.enterScope();
		stmtSeq.accept(this);
		env.exitScope();
		return null;
	}

	// SEMANTICA DINAMICA ESPRESSIONI; a value is returned by the visitor

	@Override
	public IntValue visitAdd(Exp left, Exp right) {
		return new IntValue(left.accept(this).toInt() + right.accept(this).toInt());
	}

	@Override
	public IntValue visitIntLiteral(int value) {
		return new IntValue(value);
	}

	@Override
	public IntValue visitMul(Exp left, Exp right) {
		return new IntValue(left.accept(this).toInt() * right.accept(this).toInt());
	}

	@Override
	public IntValue visitSign(Exp exp) {
		return new IntValue(-exp.accept(this).toInt());
	}

	@Override
	public Value visitSimpleVariable(Variable var) {
		return env.lookup(var);
	}

	@Override
	public BoolValue visitNot(Exp exp) {
		return new BoolValue(!exp.accept(this).toBool());
	}

	@Override
	public BoolValue visitAnd(Exp left, Exp right) {
		return new BoolValue(left.accept(this).toBool() && right.accept(this).toBool());
	}

	@Override
	public BoolValue visitBoolLiteral(boolean value) {
		return new BoolValue(value);
	}

	@Override
	public BoolValue visitEq(Exp left, Exp right) {
		return new BoolValue(left.accept(this).equals(right.accept(this)));
	}

	@Override
	public PairValue visitPairLit(Exp left, Exp right) {
		return new PairValue(left.accept(this), right.accept(this));
	}

	@Override
	public Value visitFst(Exp exp) {
		return exp.accept(this).toPair().getFstVal();
	}

	@Override
	public Value visitSnd(Exp exp) {
		return exp.accept(this).toPair().getSndVal();
	}

	// Added methods

	// ..for statement..
	@Override // implementazione ciclo WHILE per la visita di AST
	public Value visitWhileStmt(Exp exp, Block block) {
		while (exp.accept(this).toBool())
			block.accept(this);
		return null;
	}

	@Override // implementazione ciclo DO WHILE per la visita di AST
	public Value visitDoWhileStmt(Block block, Exp exp) {
		do
			block.accept(this);
		while (exp.accept(this).toBool());
		return null;
	}

	@Override
	public Value visitSingleStmt(Stmt stmt) {
		stmt.accept(this);
		return null;
	}

	@Override
	public Value visitMoreStmt(Stmt first, StmtSeq restSeq) {
		first.accept(this);
		restSeq.accept(this);
		return null;
	}

	// ..of expressions with a value returned by the visitor
	@Override // prodotto scalare di AST
	public IntValue visitProdScal(Exp left, Exp right) {
		int sum = 0;
		int sizeMin = 0;
		if (left.accept(this).toArrayValue().sizeArray() < right.accept(this).toArrayValue().sizeArray())
			sizeMin = left.accept(this).toArrayValue().sizeArray();
		else
			sizeMin = right.accept(this).toArrayValue().sizeArray();

		for (int i = 0; i < sizeMin; ++i) {
			sum += left.accept(this).toArrayValue().access(i).toInt()
					* right.accept(this).toArrayValue().access(i).toInt();
		}
		return new IntValue(sum);
	}

	@Override // restituisce la grandezza di un array.
	public IntValue visitLength(Exp exp) {
		return new IntValue(exp.accept(this).toArrayValue().sizeArray());
	}

	@Override // restituisce la somma dei valori presenti in un array.
	public IntValue visitSum(Exp exp) {
		return new IntValue(exp.accept(this).toArrayValue().toSum());
	}

	// restituisce True se ci sono elementi nulli in un array.
	public BoolValue visitArrayNull(Exp exp) {
		return new BoolValue(exp.accept(this).toArrayValue().toArrayNull());
	}

	@Override // restituisce un array..
	public ArrayValue visitArrayLiteral(ExpSeq expSeq) {
		return new ArrayValue(expSeq.accept(this).toEvaluateExpSeq());
	}

	@Override // restituisce il valore in posizione index all'interno di un array.
	public Value visitArrayAccess(Exp name, Exp index) {
		return name.accept(this).toArrayValue().access(index.accept(this).toInt());
	}

	@Override // ..double linked list.
	public Value visitSingleExp(Exp exp) {
		return new EvaluateExpSeq(exp.accept(this));
	}

	@Override // dll= double linked list.
	public EvaluateExpSeq visitMoreExp(Exp first, ExpSeq restSeq) {
		EvaluateExpSeq dll = new EvaluateExpSeq(first.accept(this));
		dll.addAll(restSeq.accept(this).toEvaluateExpSeq());
		return dll.toEvaluateExpSeq();
	}
}
