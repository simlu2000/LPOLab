package lab06_12_17.accounts;

public class HistoryCreditAccount extends CreditAccount implements HistoryAccount {
        /* history > 0 means previous operation was deposit(history) 
	 * history < 0 means previous operation was withdraw(-history)
	 * history == 0 means no previous operation */
        private int history; 

        // private object method to be used in undo() and redo()
	private int operation(int amount) {
		if (amount >= 0)
			return this.deposit(amount);
		return this.withdraw(-amount);
	}

        // check to be used in undo() and redo()
	protected int requireNonZeroHistory() {
		if (this.history == 0)
			throw new IllegalStateException("Operation history is empty");
		return this.history;
	}

	protected HistoryCreditAccount(int limit, int balance, Client owner) {
	    super(limit, balance, owner); //visto che historyccreditaccount estende creditaccount significa che creditaccount è la classe genitore e quindi per accedere alla classe genitore della sottoclasse devo usare super

		/* Differenze tra this e super:
		 * this ->
		 * 			la parola chiave punta a un riferimento della classe corrente
		 * 			può essere utilizzato per accedere a variabili e metodi della classe corrente
		 * 			La parola chiave è comunemente usata quando una variabile di istanza è ombreggiata da un parametro di un metodo
		 * 
		 * super -> 
		 * 			 la parola chiave punta a un riferimento della classe genitore
		 * 			 può essere utilizzato per accedere a variabili e metodi della classe genitore dalla sottoclasse
		 * 			 La parola chiave consente l'accesso a metodi e variabili non privati (pubblici, protetti o predefiniti) della classe padre. Ma non è possibile accedere ai membri privati della classe genitore all'interno della sottoclasse.
		 */
	}

	protected HistoryCreditAccount(int balance, Client owner) {
	    //anche qua devo accedere alla classe genitore creditaccount quindi uso super
		super(balance, owner);
	}

        // factory methods for the corresponding constructors
    
	public static HistoryCreditAccount newOfLimitBalance(int limit, int balance, Client owner) { //creazione historycreditaccount con balance e limite
	    return new HistoryCreditAccount(balance,limit,owner);
	}

	public static HistoryCreditAccount newOfBalance(int balance, Client owner) { //historycreditaccount senza limite
		return new HistoryCreditAccount(balance,owner);
	}

        // public object methods

	@Override
	public int deposit(int amount) { //deposito
	    int dep = super.deposit(amount); //inserisco in res il risultato dell'operazione di aggiunta (già implementatata in CreditAccount) dell'oggetto preso con super
		history = amount; //in history metto l'importo aggiunto
		return dep;//saldo del creditaccount dopo l'operazione di deposito
	}

	@Override
	public int withdraw(int amount) { //prelievo
	    int withd = super.withdraw(amount); //metto in withd il risultato dell'operazione di prelievo fatta con withdraw (implementata in creditaccount) prendendo l'oggetto con super
		history = -amount; //metto in history l'importo prelevato
		return withd;
	}

	@Override
	public long undo() { //annulla precedente operazione
		int undoResult = operation(-requireNonZeroHistory()); //in undoResult richiamo l'operazione implementata sopra per il controllo di History. Quindi se = 0 eccezione perche non è stata effettuata alcuna operazione, altimenti posso annullare operazione effettuata precdentemente
		history = 0; //setto history 0 così annullo operazione effettuata
		return undoResult;
	}

	@Override
	public long redo() { //ripeti precedente operazione
		int redoResult = operation(requireNonZeroHistory());
		return redoResult;
	}
}
