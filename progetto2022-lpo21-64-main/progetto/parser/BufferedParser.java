//Il parser prenderà la sequenza di token dalla memoria bufferedparser per fare dei controlli e una rappresentazione concreta della sintassi
package progetto.parser;

import progetto.parser.ast.*;
import static progetto.parser.TokenType.*;

import java.util.concurrent.atomic.AtomicBoolean;
import static java.util.Objects.requireNonNull;
import static java.lang.System.err;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.text.ParseException;

/*
Prog ::= StmtSeq EOF
StmtSeq ::= Stmt (';' StmtSeq)?
Stmt ::= 'var'? IDENT '=' Exp | 'print' Exp |  'if' '(' Exp ')' Block ('else' Block)? 
Block ::= '{' StmtSeq '}'
Exp ::= And (',' And)*    And::= Exp (',' Exp)* 
And ::= Eq ('&&' Eq)*     Exp ::= Eq ('&&' Eq)* 
Eq ::= Add ('==' Add)*  
Add ::= Mul ('+' Mul)* 
Mul::= Atom ('*' Atom)*
AtomOrArrayAcc ::= Atom ('[' Exp ']')?
Atom ::= 'fst' Atom | 'snd' Atom | '-' Atom | '!' Atom | BOOL | NUM | IDENT | '(' Exp ')' | '[' ExpSeq ']'
ExpSeq ::= Exp (';' ExpSeq)?
ProdScal ::= Unary ('x' Unary)*
Unary ::= 'fst' Unary | 'snd' Unary | '-' Unary | '!' Unary | 'length' Unary | AtomOrArrayAcc | 'sum' Unary | 'isnull' Unary
*/

public class BufferedParser implements Parser {

	private final BufferedTokenizer buf_tokenizer; // the buffered tokenizer used by the parser

	/*
	 * reads the next token through the buffered tokenizer associated with the
	 * parser; TokenizerExceptions are chained into corresponding ParserExceptions
	 */
	private void nextToken() throws ParserException {
		try {
			buf_tokenizer.next();
		} catch (TokenizerException e) {
			throw new ParserException(e);
		}
	}

	// decorates error message with the corresponding line number
	private String line_err_msg(String msg) {
		return "on line " + buf_tokenizer.getLineNumber() + ": " + msg;
	}

	/*Match() verifica se il tipo del token del token attualmente riconosciuto match 'expected'. Se no, solleva eccezione ParserException
	 */
	private void match(TokenType expected) throws ParserException {
		final var found = buf_tokenizer.tokenType();
		if (found != expected)
			throw new ParserException(line_err_msg(
					"Expecting " + expected + ", found " + found + "('" + buf_tokenizer.tokenString() + "')"));
	}

	/*
	 * Verifica se il tipo del token riconosciuto corrisponde 'expected', se sì, legge il token successivo (da riconoscere), altrimenti eccezione ParserException
	 */
	private void consume(TokenType expected) throws ParserException {
		match(expected);
		nextToken();
	}

	// unexpectedTokenError() vede se il token non viene riconosciuto, in tal caso errore ParserException
	private void unexpectedTokenError() throws ParserException {
		throw new ParserException(line_err_msg(
				"Unexpected token " + buf_tokenizer.tokenType() + "('" + buf_tokenizer.tokenString() + "')"));
	}

	//BufferedParser() è un costruttore che associa ad un parser un tokenizer salvato in memoria temporanea buffer
	public BufferedParser(BufferedTokenizer tokenizer) {
		this.buf_tokenizer = requireNonNull(tokenizer);
	}

	/*
	 * parses a program
	 * Prog ::= StmtSeq EOF
	 */
	@Override
	public Prog parseProg() throws ParserException {
		nextToken(); // one look-ahead symbol
		var prog = new SimpleProg(parseStmtSeq());
		match(EOF); // last token must have type EOF-> quindi l'ultimo token deve segnare la fine del file
		return prog;
	}

	@Override
	public void close() throws IOException { //metodo close() che solleva IOException. Se il buffered tokenizer non è nullo, lo chiude. Chiude il controllo della sintassi
		if (buf_tokenizer != null)
			buf_tokenizer.close();
	}

	/*
	 * parses a non empty sequence of statements, MoreStmt binary operator is right
	 * associative
	 * StmtSeq ::= Stmt (';' StmtSeq)?
	 */
	private StmtSeq parseStmtSeq() throws ParserException { //analizza una sequenza non vuota di statements, l'operatore binario MoreStmt associativo dx
		var stmt = parseStmt();
		if (buf_tokenizer.tokenType() == STMT_SEP) { //se il tipo token corrisponde a STMT_SEP
			nextToken(); //vado al token successivo
			return new MoreStmt(stmt, parseStmtSeq());//restituisco nuovo statement con il metodo MoreStmt
		}
		return new SingleStmt(stmt);
	}

	/*
	 * parses a statement
	 * Stmt ::= 'var'? IDENT '=' Exp | 'print' Exp | 'if' '(' Exp ')' Block ('else'
	 * Block)? | 'while' '(' Exp ')' Block | 'do' Block 'while' '(' Exp ')'
	 */
	private Stmt parseStmt() throws ParserException { //parseStmt() per la definizione di STATEMENT tramite switch() : PRINT, VAR, IF, WHILE, DO, IDENT
		switch (buf_tokenizer.tokenType()) {
			default:
				unexpectedTokenError();
			case PRINT:
				return parsePrintStmt();// stampa stmt
			case VAR:
				return parseVarStmt(); // x variabile stmt
			case IDENT:
				return parseAssignStmt();// assegnazione stmt
			case IF:
				return parseIfStmt(); //se stmt
			case WHILE:
				return parseWhileStmt();// ciclo while
			case DO:
				return parseDoWhileStmt();// ciclo do while
		}
	}

	/*
	 * parses the 'print' statement
	 * Stmt ::= 'print' Exp
	 */
	private PrintStmt parsePrintStmt() throws ParserException { //PrintStmt analizza lo statement 'PRINT' 
		consume(PRINT); // or nextToken() since PRINT has already been recognized
		return new PrintStmt(parseExp());
	}

	/*
	 * parses the 'var' statement
	 * Stmt ::= 'var' IDENT '=' Exp
	 */
	private VarStmt parseVarStmt() throws ParserException { //analizza statement 'VAR'
		consume(VAR); // or nextToken() since VAR has already been recognized
		var simpleVar = parseSimpleVariable();
		consume(ASSIGN);
		return new VarStmt(simpleVar, parseExp());
	}

	/*
	 * parses the assignment statement
	 * Stmt ::= IDENT '=' Exp
	 */
	private AssignStmt parseAssignStmt() throws ParserException { //parseAssignStmt() analizza lo statement 'IDENT'
		var simpleVar = parseSimpleVariable();
		consume(ASSIGN);
		return new AssignStmt(simpleVar, parseExp());
	}

	/*
	 * parses the 'if' statement
	 * Stmt ::= 'if' '(' Exp ')' Block ('else' Block)?
	 */
	private IfStmt parseIfStmt() throws ParserException { //parseIfStmt() analizza lo statement 'IF'
		consume(IF); // or nextToken() since IF has already been recognized
		var exp = parseRoundPar();
		var thenBlock = parseBlock();
		if (buf_tokenizer.tokenType() != ELSE)
			return new IfStmt(exp, thenBlock);
		consume(ELSE); // or nextToken() since ELSE has already been recognized
		var elseBlock = parseBlock();
		return new IfStmt(exp, thenBlock, elseBlock);
	}

	// Stmt ::= 'while' '(' Exp ')' Block
	private WhileStmt parseWhileStmt() throws ParserException { //statement 'WHILE'
		consume(WHILE);
		var exp = parseRoundPar();
		var block = parseBlock();
		return new WhileStmt(exp, block);
	}

	// Stmt ::= 'do' Block 'while' '(' Exp ')'
	private DoWhileStmt parseDoWhileStmt() throws ParserException { //statement 'DO WHILE'
		consume(DO);
		var block = parseBlock();
		consume(WHILE);
		var exp = parseRoundPar();
		return new DoWhileStmt(block, exp);
	}

	/*
	 * parses a block of statements
	 * Block ::= '{' StmtSeq '}'
	 */
	private Block parseBlock() throws ParserException { //parseBlock() analizza un blocco di stmt, quindi una sequenza di statement
		consume(OPEN_BLOCK);
		var stmts = parseStmtSeq();
		consume(CLOSE_BLOCK);
		return new Block(stmts);
	}

	/*
	 * parses expressions, starting from the lowest precedence operator PAIR_OP
	 * which is left-associative
	 * Exp ::= And (',' And)*
	 */

	private Exp parseExp() throws ParserException { //parseExp analizza le espressioni, a partire dall'operatore PAIR_OP con precedenza più bassa, che è associativo sx
		var exp = parseAnd();
		while (buf_tokenizer.tokenType() == PAIR_OP) {
			nextToken();
			exp = new PairLit(exp, parseAnd());
		}
		return exp;
	}

	/*
	 * parses expressions, starting from the lowest precedence operator AND which is
	 * left-associative
	 * And ::= Eq ('&&' Eq)*
	 */
	private Exp parseAnd() throws ParserException { //parseAnd() analizza le espressioni, a partire dall'operatore AND con prec + bassa, associativo sx
		var exp = parseEq();
		while (buf_tokenizer.tokenType() == AND) {
			nextToken();
			exp = new And(exp, parseEq());
		}
		return exp;
	}

	/*
	 * parses expressions, starting from the lowest precedence operator EQ which is
	 * left-associative
	 * Eq ::= Add ('==' Add)*
	 */
	private Exp parseEq() throws ParserException { //parseEq() analizza le espressioni, a partire dall'operatore EQ con prec + bassa, associativo sx
		var exp = parseAdd();
		while (buf_tokenizer.tokenType() == EQ) {
			nextToken();
			exp = new Eq(exp, parseAdd());
		}
		return exp;
	}

	/*
	 * parses expressions, starting from the lowest precedence operator PLUS which
	 * is left-associative
	 * Add ::= Mul ('+' Mul)*
	 */
	private Exp parseAdd() throws ParserException { //parseAdd() analizza espressioni, a partire dall'operatore PLUS con prec + bassa, associativo sx
		var exp = parseMul();
		while (buf_tokenizer.tokenType() == PLUS) {
			nextToken();
			exp = new Add(exp, parseMul());
		}
		return exp;
	}

	/*
	 * parses expressions, starting from the lowest precedence operator TIMES which
	 * is left-associative
	 * Mul::= Atom ('*' Atom)*
	 */
	private Exp parseMul() throws ParserException { //parseMul() analizza espressioni, partendo dall'operatore TIMES con prec + bassa, associativo sx
		var exp = parseProdScal();
		while (buf_tokenizer.tokenType() == TIMES) {
			nextToken();
			exp = new Mul(exp, parseProdScal());
		}
		return exp;
	}

	/*
	 * (array con parametro di accumulazione)
	 * AtomOrArrayAcc ::= Atom ('[' Exp ']')?
	 */
	private Exp parseAtomOrArrayAcc() throws ParserException { //parseAtomOrArrayAcc() analizza un array con parametro di accumulazione
		var exp = parseAtom();
		if (buf_tokenizer.tokenType() == OPEN_SPAR) {
			nextToken();
			exp = new AtomOrArrayAcc(exp, parseExp());
			consume(CLOSE_SPAR);
		}
		return exp;
	}

	/*
	 * parses expressions of type Atom
	 * Atom ::= 'fst' Atom | 'snd' Atom | '-' Atom | '!' Atom | BOOL | NUM | IDENT |
	 * '(' Exp ')'
	 */
	private Exp parseAtom() throws ParserException { //parseAtom() per analizzare le espressioni di tipo Atom
		switch (buf_tokenizer.tokenType()) {
			default:
				unexpectedTokenError();
			case NUM:
				return parseNum();
			case IDENT:
				return parseSimpleVariable();
			case MINUS:
				return parseMinus();
			case OPEN_PAR:
				return parseRoundPar();
			case BOOL:
				return parseBoolean();
			case NOT:
				return parseNot();
			case FST:
				return parseFst();
			case SND:
				return parseSnd();
			case OPEN_SPAR:
				return parseSquarePar();
		}
	}

	// ExpSeq ::= Exp (';' ExpSeq)?
	private ExpSeq parseExpSeq() throws ParserException { //parseExpSeq() analizza un'espressione (?)
		var exp = parseExp();
		if (buf_tokenizer.tokenType() == STMT_SEP) {
			nextToken();
			return new MoreExp(exp, parseExpSeq());
		}
		return new SingleExp(exp);
	}

	/*
	 * Mul ::= ProdScal ('*' ProdScal)*
	 * ProdScal ::= Unary ('x' Unary)*
	 */
	private Exp parseProdScal() throws ParserException { // parseProdScal() analizza un prodotto scalare
		var exp = parseUnary();
		while (buf_tokenizer.tokenType() == PROD_SCAL) {
			nextToken();
			exp = new ProdScal(exp, parseUnary());
		}
		return exp;
	}

	/*
	 * parses expressions of type Unary
	 * Unary ::= 'fst' Unary | 'snd' Unary | '-' Unary | 'length' Unary |
	 * AtomOrArrayAcc
	 */
	private Exp parseUnary() throws ParserException { //parseUnary() per le espressioni di tipo Unary
		switch (buf_tokenizer.tokenType()) {
			default:
				return parseAtomOrArrayAcc();
			case NOT:
				return parseNot();
			case MINUS:
				return parseMinus();
			case FST:
				return parseFst();
			case SND:
				return parseSnd();
			case LENGTH:
				return parseLength();
			case SUM:
				return parseSum();
			case NULL:
				return parseNull();
		}
	}

	// parses number literals
	private IntLiteral parseNum() throws ParserException { //parseNum() per analizzare numero letterali (?)
		var val = buf_tokenizer.intValue();
		consume(NUM); // or nextToken() since NUM has already been recognized
		return new IntLiteral(val);
	}

	// parses boolean literals
	private BoolLiteral parseBoolean() throws ParserException { //parseBoolean() per analizzare i booleani 
		var val = buf_tokenizer.boolValue();
		consume(BOOL); // or nextToken() since BOOL has already been recognized
		return new BoolLiteral(val);
	}

	// parses variable identifiers
	private SimpleVariable parseSimpleVariable() throws ParserException { //parseSimpleVariable() per analizzare una variabile semplice
		var name = buf_tokenizer.tokenString();
		consume(IDENT); // this check is necessary for parsing correctly the 'var' statement
		return new SimpleVariable(name);
	}

	/*
	 * parses expressions with unary operator MINUS
	 * Atom ::= '-' Atom
	 */
	private Sign parseMinus() throws ParserException { //parseMinus() per analizzare espressioni con operatore unario MINUS
		consume(MINUS); // or nextToken() since MINUS has already been recognized
		return new Sign(parseUnary());
	}

	/*
	 * parses expressions with unary operator FST
	 * Atom ::= 'fst' Atom
	 */
	private Fst parseFst() throws ParserException { //parseFst() per analizzare espressioni con operatore unario FST
		consume(FST); // or nextToken() since FST has already been recognized
		return new Fst(parseUnary());
	}

	/*
	 * parses expressions with unary operator SND
	 * Atom ::= 'snd' Atom
	 */
	private Snd parseSnd() throws ParserException { //parseSnd() per analizzare espressioni con operatore unario Snd
		consume(SND); // or nextToken() since SND has already been recognized
		return new Snd(parseUnary());
	}

	/*
	 * parses expressions with unary operator NOT
	 * Atom ::= '!' Atom
	 */
	private Not parseNot() throws ParserException { //parseNot() per analizzare espressioni con operatore unario NOT
		consume(NOT); // or nextToken() since NOT has already been recognized
		return new Not(parseUnary());
	}

	// parses expressions with unary operator LENGTH
	private Length parseLength() throws ParserException {//parseLength() per analizzare espressioni con operatore unario LENGTH
		consume(LENGTH);
		return new Length(parseUnary());
	}

	// parses expressions with unary operator SUM
	private Sum parseSum() throws ParserException { //parseSum() per analizzare espressioni con operatore unario SUM
		consume(SUM);
		return new Sum(parseUnary());
	}

	// parses expressions with unary operator NULL
	private ArrayNull parseNull() throws ParserException { //parseNull() per analizzare espressioni con operatore unario NULL
		consume(NULL);
		return new ArrayNull(parseUnary());
	}

	/*
	 * parses expressions delimited by round parentheses
	 * Atom ::= '(' Exp ')'
	 */
	private Exp parseRoundPar() throws ParserException { //parseRoundPar() per analizzare espressioni delimitate tra ()
		consume(OPEN_PAR); // this check is necessary for parsing correctly the 'if' statement
		var exp = parseExp();
		consume(CLOSE_PAR);
		return exp;
	}

	/*
	 * parses expressions delimited by square parentheses
	 * Atom ::= '[' ExpSeq ']'
	 */
	private Exp parseSquarePar() throws ParserException { //parseSquarePar() per analizzare espressioni delimitate tra []
		consume(OPEN_SPAR);
		var exp = parseExpSeq();
		consume(CLOSE_SPAR);
		return new ArrayLiteral(exp);
	}

	private static BufferedReader tryOpenInput(String inputPath) throws FileNotFoundException {
		return new BufferedReader(inputPath == null ? new InputStreamReader(System.in) : new FileReader(inputPath));
	}

	public static void main(String[] args) {
		try (var buf_reader = tryOpenInput(args.length > 0 ? args[0] : null);
				var buf_tokenizer = new BufferedTokenizer(buf_reader);
				var buf_parser = new BufferedParser(buf_tokenizer);) {
			var prog = buf_parser.parseProg();
			System.out.println(prog);
		} catch (IOException e) {
			err.println("I/O error: " + e.getMessage());
		} catch (ParserException e) {
			err.println("Syntax error " + e.getMessage());
		} catch (Throwable e) {
			err.println("Unexpected error.");
			e.printStackTrace();
		}

	}

}