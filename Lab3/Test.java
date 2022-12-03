
public class Test {

	public static void main(String[] args) {

		//TEST PERSON
		Person guido = new Person("Guido", "Guerrieri");
		assert guido.name.equals("Guido");
		assert guido.surname.equals("Guerrieri");
		assert guido.cf == 0;
		assert guido.isSingle();

		Person lorenza = new Person("Lorenza", "Delle Foglie");
		assert lorenza.cf == 1;
		assert lorenza.name.equals("Lorenza");
		assert lorenza.surname.equals("Delle Foglie");
		assert lorenza.isSingle();

		Person.join(lorenza, guido);
		assert !lorenza.isSingle() && !guido.isSingle(); //asserzione che lorenza e guido non sono single
		Person.divorce(guido, lorenza); //divorzio guido-lorenza
		assert lorenza.isSingle(); //asserzione lorenza single
		assert guido.isSingle(); //asserzione guido signle

		//TEST CREDITACCOUNT
		//creo CA
		CreditAccount guidoCA = CreditAccount.newOfBalanceOwner(10_00, guido); //CA con limite predefinito 0
		CreditAccount lorenzaCA = CreditAccount.newOfLimitBalanceOwner(-500_00, 100_00, lorenza);//CA con limite dato
		
		//in base ai costruttori creati, assert
		assert guidoCA.id == 0;
		assert guidoCA.owner == guido;
		assert guidoCA.getBalance()==10_00;
		
		assert lorenzaCA.id == 1;
		assert lorenzaCA.owner == lorenza;
		assert lorenzaCA.getBalance()==100_00;
		assert lorenzaCA.getLimit()==-500_00;

		//operazioni deposito, limite e prelievo
		assert guidoCA.deposit(100_00) == 110_00; //prima balance 10_00, ora con il deposito = 110_00
		assert lorenzaCA.deposit(200_00) == 300_00; //100_00+200_00=300_00
		assert guidoCA.withdraw(110_00) == 0; //-110_00 = 0
		lorenzaCA.setLimit(100_00); //cambio limite
		assert lorenzaCA.withdraw(200_00) == 100_00;

	}

}
