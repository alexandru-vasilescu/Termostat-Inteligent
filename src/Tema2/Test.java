package Tema2;


import java.util.ArrayList;
/**
 * @author Alexandru Madalin Vasilescu 321CB
 *	Clasa Test contine implementarea celor mai imporante metode precum si unele auxiliare folosita la citire
 */
public class Test {
	// numarul de camere citit din fisir
	private int numarCamere;
	// timpul de referinta citit din fisier
	private int timeStampReferinta;
	// temperatura de referinta citita din fisier
	private double temperatureReferinta;
	// umiditatea de referinta citita din fisier
	private double umiditateReferinta;
	//ArrayList de camere pentru a stoca toate camerele citite din fisier
	private ArrayList<Room> apartament=new ArrayList<>();
	//Getter pentru toate variabilele clasei
	public int getNumarCamere() {
		return numarCamere;
	}
	public int getTimeStampReferinta() {
		return timeStampReferinta;
	}
	public double getTemperature() {
		return temperatureReferinta;
	}
	public ArrayList<Room> getApartament() {
		return apartament;
	}
	public double getUmiditateReferinta() {
		return umiditateReferinta;
	}
	/**
	 * Metoda primeste stringul de inceput(prima linie din fisier) si initializeaza toate variabilele
	 * Sunt initializate temperatura,numarul de camere,timpul de referinta si umiditatea(daca e cazul)
	 * @param line este un String cu informatii generale citite din fisier
	 */
	public void citireStare(String line) {
		String[] words;
		/**indexul este folosit pentru a stabili de unde se citeste timpul de referinta
		*  Astfel daca nu avem umiditate indexul este 2 si in cazul bonsului indexul va deveni 3
		*/
		int index=2;
			words=line.split(" ");
			numarCamere=Integer.parseInt(words[0]);
			temperatureReferinta=Double.parseDouble(words[1]);
			if(Double.parseDouble(words[2])<1000) {
				umiditateReferinta=Double.parseDouble(words[2]);
				index=3;
			}
			timeStampReferinta=Integer.parseInt(words[index]);
	}
	/**
	 * Metoda primeste un string, instantiaza un obiect de tip Room si ii seteaza parametrii in functie de ce s-a citit din fisier
	 * Dupa ce s-au setat toti parametrii obiectului este adaugat la ArrayList
	 * @param line este un String cu informatii despre un obiect de tip camera citite din fisier
	 */
	public void citireCamera(String line) {
		Room room=new Room();
		String[] words;
		words=line.split(" ");
		room.setName(words[0]);
		room.setId(words[1]);
		room.setArea(Integer.parseInt(words[2]));
		apartament.add(room);
	}
	/**
	 * Metoda primeste un string si verifica ce comanda se cere si apeleaza metoda corespunzatoare
	 * @param line este un String cu o comanda citita din fisier
	 */
	public void citireComenzi(String line) {
		if(line.contains("OBSERVE ")) 
			observe(line);
		else if(line.contains("OBSERVEH "))
			observeH(line);
		else if(line.contains("TRIGGER")) 
			triggerHeat();
		else if(line.contains("TEMPERATURE")) 
			changeTemperature(line);
		else if(line.contains("LIST"))
				list(line);
	}
	/**
	 * Metoda observe adauga o temperatura la un Room din array list in functie de id-ul camerei si de indexul de timp
	 * @param s este un String cu comanda de tip "observe id time temperature"
	 */
	public void observe(String s) {
		//se separa stringurile in functie de spatii
		String[] words=s.split(" ");
		//indexul pentru camera din ArrayList unde trebuie adaugata temperatura.
		//Daca nu se gaseste Room-ul indexul ramane -1 si programul se incheie
		int index=-1;
		//ora este calculata in functie de timpul din comanda si timpul de referinta 
		double ora;
		//temperature data de comanda 
		double temperature;
		//Se cauta in ArrayList camera cu id-ul dat in comanda
		for(int i=0;i<numarCamere;i++) {
			if(apartament.get(i).getId().compareTo(words[1])==0) {
				index=i;
				break;
			}
		}
		//Daca s-a gasit camera se calculeaza ora si se seteaza temperatura si se apeleaza metoda 
		if(index>=0) {
		ora=Integer.parseInt(words[2]);
		temperature=Double.parseDouble(words[3]);
		//Ora se imparte la 3600 timpul fiind dat in secunde(60minute*60 secunde)
		ora=(this.timeStampReferinta-ora)/3600;
		//Daca timpul primit este mai mare ca timpul de referinta ora va fi mai mica ca 0
		//Daca se depasesc 24 de ore sau timpul este mai mare ca cel de referinta nu se apeleaza metoda
		if(ora>0 && ora<24)
			//Se apeleaza metoda de adaugare de temperatura la indexul ora cu temperatura citita din fisier
			apartament.get(index).addTree((int)ora, temperature);
		}
	}
	/**
	 * Metoda observeH adauga o umiditate la un Room din array list in functie de id-ul camerei si de indexul de timp
	 * Metoda functioneaza la fel ca cea pentru adaugarea temperaturii in functie de timp
	 * @param line este un String cu comanda de tip "observe id time umiditate"
	 */
	private void observeH(String line) {
		String[] words=line.split(" ");
		int index=-1;
		double ora;
		double umiditate;
		for(int i=0;i<numarCamere;i++) {
			if(apartament.get(i).getId().compareTo(words[1])==0) {
				index=i;
				break;
			}
		}
		ora=Integer.parseInt(words[2]);
		umiditate=Double.parseDouble(words[3]);
		ora=(this.timeStampReferinta-ora)/3600;
		if(ora>0 && ora<24)
			apartament.get(index).addHumiTree((int)ora, umiditate);
		
	}
	/**
	 * Metoda tiggerHeat verifica daca trebuie pornita centrala in functie de umiditate si temperatura
	 */
	public void triggerHeat() {
		//sumaTemperaturi este suma ponderata in functie de temperaturi si arie
		double sumaTemperaturi=0;
		//sumaArie este suma totala a ariilor din apartament
		double sumaArie=0;
		//sumaUmiditate este suma ponderata in functie de umiditati si arie
		double sumaUmiditate=0;
		//Daca umiditateReferinta este 0 adica nu s-a citit se trece direct la verificarea temperaturii
		if(umiditateReferinta!=0) {
			//Se ia pe rand fiecare Room din apartament
			for(int i=0;i<numarCamere;i++) {
				//Se aduna aria la suma 
				sumaArie+=apartament.get(i).getArea();
				//daca in acel Room nu s-a inregistrat nici o umiditate aria camerei se scade din suma inapoi
				if(apartament.get(i).getUmiditate()==0) sumaArie-=apartament.get(i).getArea();
				//Se aduna la sumaUmiditate produsul dintre aria camerei si cea mai mare umiditate din cel mai apropiat bucket de timp
				sumaUmiditate+=apartament.get(i).getArea()*apartament.get(i).getUmiditate();		
			}
			//Se calculeaza media ponderata
			if(sumaArie!=0) {
			sumaUmiditate=sumaUmiditate/sumaArie;
			//Daca media ponderata este mai mare ca umiditatea de referinta se afiseaza NO si se iese din metoda
			if(umiditateReferinta<sumaUmiditate) {
				System.out.println("NO");
				return;
			}
			}
		}
		sumaArie=0;
		//Daca conditia de umiditate nu este incalcata sau nu exista umiditate se verifica temperatura
		for(int i=0;i<numarCamere;i++) {
			//Se calculeaza suma ariilor
			sumaArie+=apartament.get(i).getArea();
			//Daca intr-o camera nu s-a inregistrat nici o temperatura se scade aria acesteia din suma
			if(apartament.get(i).getTemperature()==0) sumaArie-=apartament.get(i).getArea();
			//Se aduna la sumaTemperaturi produsul dintre arie si temperatura cea mai mica din cel mai apropiat bucket de timp
			sumaTemperaturi+=apartament.get(i).getArea()*apartament.get(i).getTemperature();

		}
		//Se calculeaza media ponderata
		sumaTemperaturi=sumaTemperaturi/sumaArie;
		//Daca media este mai mare ca timpul de referinta se afiseaza NO, altfel se afiseaza DA
		if(temperatureReferinta<sumaTemperaturi)
			System.out.println("NO");
		else System.out.println("YES");
	}
	/**
	 * Se afiseaza toate temperaturile dintr-un interval de timp dintr-o camera specificata
	 * @param s este un String cu o comanda de tipul "list roomName oraInceput oraFinal"
	 */
	public void list(String s) {
		//Se separa cuvintele in functie de spatii
		String[] words=s.split(" ");
		//Se seteaza ora de inceput in functie de cea primita la comanda
		double orainceput=Double.parseDouble(words[2]);
		//Se calculeaza ora de inceput in functie de timpul de referinta si se imparte la 3600(60 minute*60 secunde)
		orainceput=(timeStampReferinta-orainceput)/3600;
		//Se seteaza ora de final in fucntie de cea primita la comanda
		double orafinal=Double.parseDouble(words[3]);
		//Se calculeaza ora in functie de timpul de referinta si  se imparte 3600
		orafinal=(timeStampReferinta-orafinal)/3600;
		//Se afiseaza camera unde se fac masuratorile
		System.out.print(words[1]);
		//Se cauta camera unde trebuie afisat si se apeleaza metoda printare din clasa Room
		for(int i=0;i<numarCamere;i++) {
			if(apartament.get(i).getName().compareTo(words[1])==0) {
				apartament.get(i).printare((int)orainceput,(int)orafinal);
				break;
			}
		}
	}
	/**
	 * Metoda changeTemperature schimba temperatura primita ca referinta
	 * @param s este un String cu o comanda de tipul "TEMPERATURE temperaturaNoua"
	 */
	public void changeTemperature(String s) {
		String[] words=s.split(" ");
		temperatureReferinta=Double.parseDouble(words[1]);
	}
	
}
