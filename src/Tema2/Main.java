package Tema2;

import java.io.*;
/**
 * @author Alexandru Madalin Vasilescu 321CB
 *	In clasa main este doar metoda main. Se deschid fisierele pentru citire si scriere
 *	Se apeleaza metodele din clasa Test pentru executarea comenzilor primite la intrare
 */
public class Main {
	public static void main(String[] args) {
		//variabila first este folosita astfel ca la prima apelare a comenzii list sa nu se puna linie goala
		boolean first=false;
		//variabila reader este folosita pentru a citi din fisier
		BufferedReader reader=null;
		//variabila line este folosita pentru a pastra cate o linie citita din fisier
		String line;
		//variabila t este folosita pentru a apela metodele din clasa Test
		Test t=new Test();
		try {
			//Am deschis fisierul pentru a citi din el
			reader=new BufferedReader(new FileReader("therm.in"));
			//Se citeste prima linie si se apeleaza metoda citireStare din clasa Test care seteaza numarul de camere,
			//timpul de referinta si temperatura de referinta (si umiditate de referinta daca este cazul)
			line=reader.readLine();
			t.citireStare(line);
			//Se deschide fisierul pentru a scrie in el
			PrintStream o = new PrintStream(new File("therm.out"));
			//Se pastreaza consola intr-o variabila si se seteaza System.out pentru a afisa in fisier
			PrintStream console=System.out;
			System.setOut(o);
			//Se citesc pe rand camerele din fisier si se apeleaza metoda citireCamere care initializeaza fiecare camera
			for(int i=0;i<t.getNumarCamere();i++) {
				line=reader.readLine();
				t.citireCamera(line);
			}
			//Cat timp exista linii cu comenzii si citesc liniile si se apeleaza metoda citireComenzi pentru a verifica tipul comenzii
			while(true) {
				line=reader.readLine();
				if(line==null) break;
				t.citireComenzi(line);
				//Daca avem o comanda de list se verifica daca trebuie sau nu pus NEWLINE
				if(line.contains("LIST") && first==false)first=true;
				if(line.contains("LIST") && first==true) System.out.println();
			}
			//Se seteaza afisarea inapoi in consola
			System.setOut(console);
		}
		//Blocul de catch in caz ca nu exista fisier
		catch(FileNotFoundException ex) {
			System.out.println("Nu exista");
			System.exit(1);
		}
		//Blocul de catch in caz de IOException
		catch(IOException ex) {
			System.out.println("IOException");
			System.exit(1);
		}finally {
			//Se inchide readerul din fisier
			if(reader!=null)
				try {reader.close();}
				catch(IOException ex) {
					System.out.println("IOEXCEPTION");
					System.exit(1);
				}
		}
	}
}
