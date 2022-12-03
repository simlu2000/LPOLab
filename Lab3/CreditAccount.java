public class CreditAccount {
    //2)
    private int limit; //limite minimo di giacenza, in cents
    private int balance;//saldo del conto, in cents. Non può essere inferiore al limite minimo di giacenza
    public final Person owner; //intestatario conto
    
    public final long id;//codice conto. due oggetti di CreditAccount hanno stesso codice conto se e solo sono lo stesso oggetto(conto)
    private static long nextId;

    public static final int default_limit = 0;

    /*COSTRUTTORI*/
    //COSTRUTTORE CONTO CORRENTE CON LIMITE DATO
    public CreditAccount(int limit, int balance, Person owner) {
        this.limit = CreditAccount.ifNegative(limit); //chiamo metodo per controllo limite negativo
        this.balance = CreditAccount.BalanceLowerLimit(balance,limit); //chiamo metodo per controllo balanca<limit
        this.owner= CreditAccount.ifNull(owner);//controllo se è statodato owner nullo
        this.id=CreditAccount.nextId();
    }

    //COSTRUTTORE CONTO CORRENTE CON LIMITE PREDEFINITO (O)
    public CreditAccount(int balance, Person owner){
		this(CreditAccount.default_limit, balance, owner);
    }

    /*METODI OGGETTO */
    //metodo deposito di una somma positiva in centesimi e che restituisce il saldo totale dopo il versamento
    public int deposit(int amount){ 
        /*if(amount <= 0)
            throw new IllegalArgumentException("Negative amount!");*/
        return this.balance = Math.addExact(this.balance, CreditAccount.ifNegative(amount)); //restituisco il balance dell'oggetto con in aggiunta (Math.addExact) amount dopo aver controllato che non sia negativo
    }

    public int withdraw(int amount){ //prelevo dal conto somma positiva e restituisce il saldo
        int withdrawBalance = Math.subtractExact(amount, CreditAccount.ifNegative(amount)); //metto in una variabile il saldo dopo aver sottratto e aver controllato che amount sia positivo
        CreditAccount.BalanceLowerLimit(withdrawBalance, this.limit); //controllo se il nuovo balance < limit
        return withdrawBalance; //nuovo balance
    }

    public void setLimit(int limit){ //modifica limite minimo di giacenza conto ma solo se il balance non scende < nuovo limite
        this.limit = CreditAccount.BalanceLowerLimit(this.balance, limit);
    }

    public int getBalance() {
		return this.balance;
	}

	public int getLimit() {
		return this.limit;
	}


    /*METODI FACTORY */
    public static CreditAccount newOfLimitBalanceOwner(int limit, int balance,Person owner) //apertura conto, restituisce nuovo oggetto di CreditAccount con limite di giacenza limit, saldo iniziale balance e owner. Può essere aperto solo se il saldo iniziale è positivo.
    {
        //int balanceOk = CreditAccount.ifNegative(balance); da non fare perchè viene fatta nel costruttore
        return new CreditAccount(limit,balance,owner);
    }

    public static CreditAccount newOfBalanceOwner(int balance,Person owner) //apertura conto restituendo nuovo oggetto di CreditAccount con limite giacenza predefinito, saldo balance, owner. Conto apribile solo se balance positivo.
    {
        return new CreditAccount(balance,owner);
    }


    /*METODI AUSILIARI */
    private static int ifNegative(int amount){ //controllo importo negativo
        if(amount <= 0 )
            throw new IllegalArgumentException("Negative amount");
        return amount;
    }

    private static int BalanceLowerLimit(int balance, int limit){ //controllo se balance < limit
        if (balance < limit)
            throw new IllegalArgumentException();
        return limit;
    }

    private static Person ifNull(Person p1){ //controllo se persona nulla
        if (p1 == null) //se persona nulla
            throw new NullPointerException(); //errore puntatore nullo
        return p1;
    }

    private static long nextId() {
		if (CreditAccount.nextId < 0) //se negativo
			throw new RuntimeException("No existing ids");
		return CreditAccount.nextId++; //incremento
	}

}
