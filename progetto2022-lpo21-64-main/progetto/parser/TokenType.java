package progetto.parser;

public enum TokenType { 
	// symbols
	ASSIGN, MINUS, PLUS, TIMES, NOT, AND, EQ, STMT_SEP, PAIR_OP, OPEN_PAR, CLOSE_PAR, OPEN_BLOCK, CLOSE_BLOCK, OPEN_SPAR, CLOSE_SPAR,
	// keywords
	PRINT, VAR, BOOL, IF, ELSE, FST, SND, WHILE, DO, LENGTH, SUM, PROD_SCAL, NULL,
	// non singleton categories
	SKIP, IDENT, NUM,   
	// end-of-file
	EOF, 	
}
