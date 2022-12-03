public class Person {
   
    //1)

    //oggetto Person -> variabili
    public final String name; 	//siccome è un valore obbligatorio e non modificabile, metto final così non può essere modificato
	public final String surname;
    
    public final long cf; //identifica ogni oggetto della classe (ogni persona ed ogni persona ha un proprio codice fiscale)
    private static long nextcf; //codice fiscale persona successiva

    private Person spouse; //coniuge di tipo Person -> opzionale (così il coniuge avrà le informazioni identificate con la classe Person)


    //Nome e cognome -> stringhe valide rispetto all'espr regolare [A-Z][a-z]+( [A-Z][a-z]+)*
    private static String validName="[A-Z][a-z]+( [A-Z][a-z]+)*"; //espressione regolare validità
    private static String validationName(String name){ //metodo per la validazione della Persona
        if(!name.matches(Person.validName)) //se il nome della persona non corrisponde ad un nome valido della persona, allora eccezione
            throw new IllegalArgumentException (name + " is an Illegal Name!");
        return name;
        
    }

    //metodo cf succ
    private static long nextCf() {
		if (Person.nextcf < 0)
			throw new RuntimeException("No more available numbers");
		return Person.nextcf++;
	}


    /*METODI OGGETTO */
    //metodo isSingle che restituisce true se la persona non ha un coniuge (spouse)
    public boolean isSingle(){
        return this.spouse == null; //return il campo spouse di questo oggetto come nullo
    }


    /*METODI DI CLASSE */
    /*metodo per sposare due persone p1 e p2, con la pre-condizione che non siano la stessa persona e 
    nessuno dei due ha già un coniuge*/
    static void join(Person p1, Person p2){
        if(p1 == p2 || p1.spouse != null || p2.spouse != null ) /*se p1 e p2 sono la stessa persona || il campo 
            spouse di p1 non è nullo (ha già coniuge) || campo spouse di p2 non è nullo (già coniuge) */
            throw new IllegalArgumentException (" p1 and p2 can't join");

        //se il controllo di prima non è vero, allora possono sposarsi ed attribuiamo i coniugi tra di loro
        p1.spouse = p2; //coniuge di p1 è p2
        p2.spouse = p1;//coniuge di p2 è p1
    }

    
    //metodo divorzio, pre-condizione che siano coniugi
    static void divorce(Person p1, Person p2){
        if(p1 != p2.spouse) //se non sono coniugi l'uno dell'altro
            throw new IllegalArgumentException ("p1 and p2 are not married"); //eccezione
        
        p1.spouse = null;//altrimenti li divorzio e pongo i campi coniuge nulli
        p2.spouse = null;
    }


    /*INVARIANTI CLASSE E COSTRUTTORE CHE NE GARANTISCA LA VALIDITA'*/
    //nome valido, cognome valido, codice fiscale di ogni oggetto che deve essere unico
    public Person(String name, String surname){ //costruttore di classe
        //dentro al costruttore di classe abbiamo dei metodi di classe
        this.name = Person.validationName(name); //al nome dell'oggetto chiamo il metodo di validazione che confronta il nome con la stringa valida (espr regolare)
        this.surname = Person.validationName(surname);
        this.cf = Person.nextCf();

    }





}
