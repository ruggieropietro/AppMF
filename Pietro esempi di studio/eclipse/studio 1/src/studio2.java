
public class studio2 {
	//richiama il metodo,assegnando alla nuova variabile
	//calcoloarea il valore di return nel metodo(area)
	double calcoloarea=areaParallelogramma(6,4,4,4,4,4);
	
//IF,ELSE IF,SWITCH,WHILE,DO WHILE,FOR,FOR(;;),
//FOR EACH(ARRAY),BREAK(JUMPAETICHETTA),CONTINUE(JUMPFUORI),

	//metodo che prende in ingresso due variabili,base e altezza
	//String... desc è il vararg
	//non statico=associato ad ogni singola istanza
	//nomeIstanza.nomeMetodo(...)
	//statico=associato a una classe quindi interagiscono solo con variabili statiche
	//	NomeClasse.nomeMetodo(...)
public static double areaParallelogramma(double base, double altezza,int... desc) 
 {
	 //e restituisce una variabile,l'area.
    double area = base * altezza;
    return area;
  //public  sarà visibile dovunque,
  //private solo alla classe,
  //protected solo dalle classi che stanno nel medesimo package 
  //default  visibilità alle classi nel medesimo package.
  //final le sottoclassi lo erediteranno ma non potranno modificarlo 
 }

//ISTANZA==OGGETTI
//CLASSE==INSIEME DI ISTANZE
}

