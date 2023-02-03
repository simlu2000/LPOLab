package progetto.visitors.typechecking;

import static progetto.visitors.typechecking.SimpleType.*;

import progetto.environments.EnvironmentException;
import progetto.environments.GenEnvironment;

import progetto.parser.ast.Block;
import progetto.parser.ast.Exp;
import progetto.parser.ast.Stmt;
import progetto.parser.ast.StmtSeq;
import progetto.parser.ast.Variable;
import progetto.parser.ast.ExpSeq;
import progetto.parser.ast.ArrayAccess;
import progetto.parser.ast.AtomOrArrayAcc;

import progetto.visitors.Visitor;

public class Typecheck implements Visitor<Type> {

	private final GenEnvironment<Type> env = new GenEnvironment<>();

	// useful to typecheck binary operations where operands must have the same type
	private void checkBinOp(Exp left, Exp right, Type type) {
		type.checkEqual(left.accept(this));
		type.checkEqual(right.accept(this));
	}

	// static semantics for programs; no value returned by the visitor

	@Override
	public Type visitSimpleProg(StmtSeq stmtSeq) {
		try {
			stmtSeq.accept(this);
		} catch (EnvironmentException e) { // undeclared variable
			throw new TypecheckerException(e);
		}
		return null;
	}


	// SEMANTICA STATICA STATEMENTS; nessun valore ritornato dal visitor
	@Override
	public Type visitAssignStmt(Variable v, Exp exp) { //cerca v nel'espressione/albero
		var found = env.lookup(v);
		found.checkEqual(exp.accept(this));
		return null;
	}

	@Override
	public Type visitPrintStmt(Exp exp) { //visita exp, altrimenti null
		exp.accept(this);
		return null;
	}

	@Override
	public Type visitVarStmt(Variable v, Exp exp) { 
		env.dec(v, exp.accept(this));
		return null;
	}

	@Override
	public Type visitIfStmt(Exp exp, Block thenBlock, Block elseBlock) { 
		BOOL.checkEqual(exp.accept(this));//verifica che il risultato prodotto da accept su exp sia un booleano
		thenBlock.accept(this); //fai blocco thenBlock
		if (elseBlock != null) //se elseBlock non nullo allora fai elseBlock
			elseBlock.accept(this);
		return null;
	}

	@Override
	public Type visitBlock(StmtSeq stmtSeq) { //visita il blocco stmtSeq e poi elimina lo scope dopo aver visitato
		env.enterScope(); //aggiunta scope vuoto in environment
		stmtSeq.accept(this);//accetta statement stmtSeq
		env.exitScope();//togli primo scope
		return null;
	}

	//SEMANTICA STATICA ESPRESSIONI; visitor ritorna un tipo

	@Override
	public SimpleType visitAdd(Exp left, Exp right) { //visita , con nodo sx e dx e ritorna INT
		checkBinOp(left, right, INT); //controllo operazione binaria, il tipo INT
		return INT; 
	}

	@Override
	public SimpleType visitIntLiteral(int value) { //visita valore intero e ritorna
		return INT;
	}

	@Override
	public SimpleType visitMul(Exp left, Exp right) { 
		checkBinOp(left, right, INT); //controllo operazione binaria e return INT
		return INT;
	}

	@Override
	public SimpleType visitSign(Exp exp) { 
		INT.checkEqual(exp.accept(this));//verifica che il visitato sia di tipo INT
		return INT;
	}

	@Override
	public Type visitSimpleVariable(Variable var) { //ricerca della variabile var
		return env.lookup(var); 
	}

	@Override
	public SimpleType visitNot(Exp exp) {
		BOOL.checkEqual(exp.accept(this));
		return BOOL;
	}

	@Override
	public SimpleType visitAnd(Exp left, Exp right) {
		checkBinOp(left, right, BOOL);
		return BOOL;
	}

	@Override
	public SimpleType visitBoolLiteral(boolean value) {
		return BOOL;
	}

	@Override
	public SimpleType visitEq(Exp left, Exp right) {
		left.accept(this).checkEqual(right.accept(this));
		return BOOL;
	}

	@Override
	public PairType visitPairLit(Exp left, Exp right) {
		return new PairType(left.accept(this), right.accept(this));
	}

	@Override
	public Type visitFst(Exp exp) { //visita fst di exp
		return exp.accept(this).getFstPairType();
	}

	@Override
	public Type visitSnd(Exp exp) {//visita snd di exp
		return exp.accept(this).getSndPairType();
	}

	// METODI AGGIUNTI

	//STATEMENTS
	@Override
	public Type visitWhileStmt(Exp exp, Block block) { //visita stmt ciclo while
		BOOL.checkEqual(exp.accept(this)); //verifico che il ris sia bool
		block.accept(this);//accetto blocco
		return null;
	}

	@Override
	public Type visitDoWhileStmt(Block block, Exp exp) {//visita stmt ciclo do while
		block.accept(this);
		BOOL.checkEqual(exp.accept(this));
		return null;
	}

	//SEQUENZE DI ESPRESSIONI
	@Override
	public SimpleType visitProdScal(Exp left, Exp right) { //visita prodotto scalare
		checkBinOp(left, right, ARRAY); //verifico operazione binaria, considerando il tipo ARRAY perchè il prodotto scalare viene fatto su ARRAY
		return INT; //risultato
	}

	@Override
	public Type visitSingleStmt(Stmt stmt) { //visita statement
		stmt.accept(this);
		return null;
	}

	@Override
	public Type visitMoreStmt(Stmt first, StmtSeq restSeq) { //visita statement aggiuntivo, dove first è il primo elemento, e restSeq indica il resto della sequenza
		first.accept(this); //primo elem
		restSeq.accept(this);//resto sequenza
		return null;
	}

	@Override
	public Type visitSingleExp(Exp exp) { //visita espressione e verifico che il ris corrisponda ad un tipo INT
		INT.checkEqual(exp.accept(this));
		return null;
	}

	@Override
	public Type visitMoreExp(Exp first, ExpSeq restSeq) { //visita espressione aggiuntiva, dove first è il primo elemento, e restSeq indica il resto della sequenza
		first.accept(this);
		restSeq.accept(this);
		return null;
	}

	//ESPRESSIONI CON TIPO RITORNATO DA VISITOR
	@Override
	public Type visitLength(Exp exp) { //visita espressione e restituisce lunghezza
		ARRAY.checkEqual(exp.accept(this)); //controlla sia di tipo ARRAY
		return INT;//ritorna lunghezza array
	}

	@Override
	public Type visitSum(Exp exp) { 
		ARRAY.checkEqual(exp.accept(this));
		return INT;
	}

	@Override
	public Type visitArrayNull(Exp exp) {
		ARRAY.checkEqual(exp.accept(this));
		return BOOL; //siccome deve ritornare se array nullo, restituirà un valore booleano
	}

	@Override
	public Type visitArrayLiteral(ExpSeq expSeq) {
		expSeq.accept(this);
		return ARRAY;
	}

	@Override
	public SimpleType visitArrayAccess(Exp name, Exp index) {
		ARRAY.checkEqual(name.accept(this));
		INT.checkEqual(index.accept(this));
		return INT;
	}
}
