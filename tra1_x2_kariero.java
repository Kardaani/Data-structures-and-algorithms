// traI_12_x2_pohja.java SJ

import java.util.Iterator;
import java.util.LinkedList;
import fi.joensuu.cs.tra.*;

public class kariero {


    // P√§√§ohjelman k√§ytt√∂:
    // java traI_12_x2_pohja [N] [S] [MAX] [K]
    // miss√§ N on alkioiden m√§√§r√§
    // S on satunnaislukusiemen
    // MAX on suurin alkio
    // kun maksimiksi laittaa pienemm√§n kuin N, tulee moninkertaisia alkioita
    // K on parametri poistaMonet -metodille
    public static void main(String[] args) {

        // taulukoiden koko
        int N = 20;
        if (args.length > 0)
            N = Integer.valueOf(args[0]);

        // satunnaislukusiemen
        int siemen = 4;
        if (args.length > 1)
            siemen = Integer.valueOf(args[1]);

        // suurin arvottava alkio
        int max = N/3;
        if (args.length > 2)
            max = Integer.valueOf(args[2]);

        // montako alkiota j√§tet√§√§n
        int k = 1;
        if (args.length > 4)
            k = Integer.valueOf(args[3]);

        // luodaan esimerkkitaulukko
        Integer[] T1 = new Integer[N];

        // t√§ytet√§√§n alkioilla
        java.util.Random r = new java.util.Random(siemen);
        for (int i = 0; i < N; i++) {
            T1[i] = r.nextInt(max);
        }

        // taulukko kasvavaan j√§rjestykseen
        java.util.Arrays.sort(T1);

        // tulostetaan taulukko
        if (N <= 20) {
            System.out.print("T1: ");
            for (int i = 0; i < N; i++)
                System.out.print(" " + T1[i]);
            System.out.println();

        }

        // Muodostetaan taulukosta TraLinkedList ja java.util.LinkedList
        // valitse kumpaa k√§yt√§t
        TraLinkedList<Integer> L1 = new TraLinkedList<Integer>();
        LinkedList<Integer>L2 = new LinkedList<Integer>();
        for (Integer x : T1) {
            L1.insert(L1.EOL, x);
            L2.add(x);
        }


        // kutsutaan teht√§v√§√§ X1 

        poistaMonet(L1, k);
        poistaMonet(L2, k);

        if (N <= 20) {
            System.out.print("L1: ");
            for (Integer x : L1)
                System.out.print(" " + x);
            System.out.println();

            System.out.print("L2: ");
            for (Integer x : L2)
                System.out.print(" " + x);
            System.out.println();
        } else {
            int s = 0;
            for (Integer x : L1)
                s++;
            System.out.println("L1: " + s + " alkiota.");
            System.out.println("L2: " + L2.size() + " alkiota.");
        }


    } // main()

    /**
      * X2
      * Poistaa listasta enemm√§t kuin k esiintym√§√§ samaa alkiota
      * @param L muokattava lista
      */
    public static <E> void poistaMonet(TraLinkedList<E> L, int k) {

        // teht√§v√§ X1 t√§h√§n jos k√§yt√§t TraLinkedList:√§

    	    	
        return;
    } // poistaMonet()

    /**
      * X2
      * Poistaa listasta enemm√§t kuin k esiintym√§√§ samaa alkiota
      * @param L muokattava lista
      * 
      * Kari Eronen
      * 1.10.2012
      * Aikavaativuus lineaarinen = listan pituus |L|
      * 
      * Algoritmi k‰y l‰pi LinkedListaa yksi kerrallaan. Jos seuraavan paikan
      *  sis‰lt‰m‰ arvo on sama kuin edellisen, se lis‰‰ ko. arvon lukum‰‰r‰muuttujaa (x) 
      *  yhdell‰. Jos se ei ole sama arvo, lukum‰‰r‰ksi asetetaan uudelleen yksi.
      *  Jos x > k ( = suurin sallittu lukum‰‰r‰), kyseisen paikan arvo listassa poistetaan. Whilen loopin lopussa 
      *  siirryt‰‰n listassa eteenp‰in seuraavan tarkasteltavaan (indeksimuuttuja i).
      *  T‰t‰ toistetaan niin kauan kunnes lista on l‰pik‰yty.
      *  
      *  Mielest‰ni ratkaisun pit‰isi toimia. Ainakin testisyˆtteill‰ n‰ytti toimivan
      *  hyvin. En nyt ‰kkiselt‰‰n keksi paljoa parannettavaa. Siit‰ en ole varma, miten 
      *  ratkaisu toimii jos lukuja on todella paljon ja luvut ovat isoja. Meneekˆ silloin jotenkin sekaisin?
      *  N‰in ollen equals pit‰isi korvata jollain toisella ja muutenkin kehitt‰‰ arvojen hallintaa.
      */
    
    public static <E> void poistaMonet(LinkedList<E> L, int k) {

        // teht√§v√§ X1 t√§h√§n jos k√§yt√§t java.util.LinkedList:i√§.

    	int x = 0;
    	E edellinen = null;
    	
    	
    	int i = 0;
    	while (i < L.size()) {
    	
    		E seuraava = L.get(i);
    		
    		if (seuraava.equals(edellinen)) {
    			x++;
    		}
    		else   {
    			x = 1;
    		}
    		
    		if (x > k) {
    			
    			
    			L.remove(i);
    			
    		
    		}
    		else {
    			
    		
    			edellinen = seuraava;
    			i++;
    		}
    		
    	}
    	
    		
    		
    		
        return; 
    } // poistaMonet()



} // class

