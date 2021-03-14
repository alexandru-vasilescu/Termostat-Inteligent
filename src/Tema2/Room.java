package Tema2;

import java.util.*;
/**
 * @author Alexandru Madalin Vasilescu 321CB
 * Clasa Room este creata pentru a pastra insusirile unei camere(nume,id,suprafata)
 */


public class Room {
	private String name; //numele camerei de tip String
	private String id;	//id-ul camerei de tip String
	private int area;	//suprafata camerei de tip int
	/**	Multime(Set) creata pentru a stoca temperaturile dintr-o camera.
	 * 	Am folosit un vector de TreeSeturi(Elementele sunt sortate si apar doar o data) de tip Double
	 * 	Am initializat vectorul cu 24 de elemente, fiecare reprezentand cate o ora
	 * 	Elementul 0 al vectorului reprezinta inregistrarile dn aceeasi ora cu timpul de referinta(Ex: timp referinta 14:20, [13:20, 14:20])
	 * 	Elementul 1 reprezinta inregistrarile cu o ora in urma(Ex:timp referinta 14:20, [12:20, 13:20]) etc.
	 */
	@SuppressWarnings("unchecked")
	private Set<Double>[] temperatureSet=new TreeSet[24];	
	/**	Multime(Set) creata pentru a stoca umiditatile dintr-o camera.
	 * 	Am folosit un vector de TreeSeturi(Elementele sunt sortate si apar doar o data) de tip Double
	 * 	Am initializat vectorul cu 24 de elemente, fiecare reprezentand cate o ora. Elementele sunt pastrate la fel ca in temeratureSet
	 */
	@SuppressWarnings("unchecked")
	private Set<Double>[] umiditateSet=new TreeSet[24];
	//Constructor pentru Room fara parametri, variabilele obiectelor se initializeaza cu Settere
	public Room() {	
		//Se creeaza cate un TreeSet cu elemente de tip Double pentru fiecare element din temperatureSet si umiditateSet
		for(int i=0;i<24;i++) {
			temperatureSet[i]=new TreeSet<Double>();
			umiditateSet[i]=new TreeSet<Double>();
		}
	}
	//Getteri si Setteri pentru variabile
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}
	/**	Metoda addTree adauga o temperatura in temperatureSet corespunzator bucket-ului de ora
	 * 	Indexul corespunzator bucket-ului se calculeaza cand se apeleaza metoda
	 * @param index reprezinta ora la care s-a facut observatia
	 * @param value reprezinta temperatura care s-a observat
	 */
	public void addTree(int index,double value) {
		temperatureSet[index].add(value);
	}
	/**
	 * 	Metoda addHumiTree adauga o umiditate in umiditateSet corespunzator bucket-ului de ora
	 * 	Indexul corespunzator se calculeaza cand se apeleaza metoda
	 * @param index reprezinta ora la care s-a facut inregistrarea
	 * @param value reprezinta umiditatea inregistrata
	 */
	public void addHumiTree(int index,double value) {
		umiditateSet[index].add(value);
	}
	/**
	 * 	Metoda getTemperature cauta cea mai mica temperatura inregistrata cel mai recent fata de timpul de referinta
	 * @return temperatura cea mai mica din bucketul cel mai apropiat de timpul de referinta sau 0 daca nu sunt inregistrari
	 */
	public double getTemperature() {
		for(int i=0;i<24;i++) {
			if(temperatureSet[i].isEmpty()==false) {
				return temperatureSet[i].iterator().next();
			}
		}
		return 0;
	}
	/**
	 * 	Metoda getUmiditate cauta cea mai mare umiditate inregistrata cel mai recent fata de timpul de referinta
	 * 	Pentru obtinerea umiditatii cea mai mare se parcurge toata multimea si se ia ultima valoare
	 * @return cea mai mare umiditate din ultimul bucket de ora sau 0 daca nu s-a inregistrat nici o umiditate
	 */
	public double getUmiditate() {
		Iterator<Double> iterator;
		double x=0;
		for(int i=0;i<24;i++) {
			if(umiditateSet[i].isEmpty()==false) {
				iterator=umiditateSet[i].iterator();
				while(iterator.hasNext()) x=iterator.next();
				break;
			}
		}
		return x;
	}
	/**
	 * Metoda printare afiseaza toate temperaturile dintr-un interval de timp
	 * @param index1 reprezinta ora de inceput
	 * @param index2 reprezinta ora de final 
	 */
	public void printare(int index1,int index2) {
		Iterator<Double> iterator;
		for(int i=index2;i<index1;i++) {
			iterator=temperatureSet[i].iterator();
			while(iterator.hasNext()) {
				System.out.printf(" %.2f",iterator.next());
			}
		}
	}
}
