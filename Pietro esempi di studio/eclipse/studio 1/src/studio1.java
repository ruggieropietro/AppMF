
public class studio1 
{

	//variabile di istanza, dentro la classe fuori dal metodo
	int varinst=7;
//variabile statica o di classe,eliminata quando l'istanza muore,tutte allocate insieme
//	è uguale per tutte le istanze della classe
	static int varst=5;
	//vale per variabili di istanza o di classe,
	//oprivate,utile solo nella classe
	private int intpr=2;
	//protetta,acessibile da altre classi del package
	protected int intpro=3;
	//default,acessibile solo al package
	int intdef=8;
	//publica,acessibile da altre classi
	public int intp=3;

	
	

public void variabilelocale (int varinst)
{
	//variabile locale,scompare quando il metodo viene chiuso
	int varloc = varinst ;
	
}   

//tipi di variabili
//8 bit con segno tra -28 e 127
byte bytevar = 11;
//16 byte con segno da -32 a 32
short shortvar = 23;
//numeri interi -231, 231-1
int intvar= -33;
//64 bit [-263, 263-1].
long bigLUE = 4242424242L;
//32 bit singola precisione
float  milleFloat = 1000.0f;
//64 bit IEEE doppia precisione
double milleSci   = 1.0e3;
//due valori
boolean booleanvar = false;
//unicode
char charvar = 'ñ';
//stringhe
String author = "Douglas Noël Adams";
//CLASSI WARPPER servono a trasformare 
//variabili in classe con alcuni metodi,attraverso il 
//boxing o l'yunboxing RIVEDERE DOPO OOP

Integer x = new Integer (10);
Double y = new Double (5.5);
 
Boolean z = Boolean.parseBoolean("true");



}