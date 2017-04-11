// traI_12_x3_pohja.java SJ

// komentorivilt√§ erikokoisia satunnaisia puita

import fi.joensuu.cs.tra.*;
import java.util.Random;

public class kariero {

	public static void main(String[] args) {

        // aloitetaan vakioidulla esimerkkipuulla
		BTree<Integer> puu = exampleBTree();

		System.out.println("Sisajarjestyksessa:");
		inorderPrint(puu);

		System.out.println("Esimerkkipuun l√§pik√§ynti sis√§j√§rjestyksess√§ takaperin:");

        BTreeNode<Integer> n = inorderLast(puu);
        while (n != null) {
            System.out.print(n.getElement() + " ");
            n = inorderPrevious(puu, n);
        }
		System.out.println();

        // luodaan satunnainen puu, t√§ll√§kertaa sis√§lt√§en merkkijonoja
        BTree<String> puu2 = new  BTree<String>();

		System.out.println("Satunnainen puu:");
		int N = 18;
		if (args.length > 0)
			N = Integer.valueOf(args[0]);

		Random r = new Random(42+N);
		Integer x;
		for (int i = 0; i < N; i++) {
			x = r.nextInt(N*3);
			System.out.print(x + " ");
			inorderInsert(puu2, "(" + x + ")");
		}
		System.out.println();

		System.out.println("Sisajarjestyksessa:");
		inorderPrint(puu2);

		System.out.println("Esimerkkipuun l√§pik√§ynti sis√§j√§rjestyksess√§ takaperin:");

        BTreeNode<String> n2 = inorderLast(puu2);
        while (n2 != null) {
            System.out.print(n2.getElement() + " ");
            n2 = inorderPrevious(puu2, n2);
        }
		System.out.println();

		
	} // main()

	/* Luo pienen sisajarjestyn esimerkkipuun
	                 
	       10        
	    __/  \__     
	   5        15   
	  / \      /  \  
	 3   8   12    18
	  \      /\      
	   4    11 13    
	                 
	*/
	public static BTree<Integer> exampleBTree() {

		BTree<Integer> T = new BTree<Integer>();

		// juuri
		T.setRoot(new BTreeNode<Integer>(10));

		BTreeNode<Integer> n = T.getRoot();

		// juuren lapset
		n.setLeftChild(new BTreeNode<Integer>(5));
		n.setRightChild(new BTreeNode<Integer>(15));

		// vasen haara
		BTreeNode<Integer> l = n.getLeftChild();

		l.setLeftChild(new BTreeNode<Integer>(3));
		l.setRightChild(new BTreeNode<Integer>(8));

		l.getLeftChild().setRightChild(new BTreeNode<Integer>(4));

		// oikea haara
		l = n.getRightChild();

		l.setLeftChild(new BTreeNode<Integer>(12));
		l.setRightChild(new BTreeNode<Integer>(18));

		l.getLeftChild().setLeftChild(new BTreeNode<Integer>(11));
		l.getLeftChild().setRightChild(new BTreeNode<Integer>(13));


		System.out.println("                 ");
		System.out.println("       10        ");
		System.out.println("    __/  \\__     ");
		System.out.println("   5        15   ");
		System.out.println("  / \\      /  \\  ");
		System.out.println(" 3   8   12    18");
		System.out.println("  \\      /\\      ");
		System.out.println("   4    11 13    ");
		System.out.println("                 ");

		return T;

	} // exampleBTree()


    // Lis√§ys sis√§j√§rjestettyy bin√§√§ripuuhun
    // t√§ss√§ samalla esimerkki miten (hankalasti) Javan parametroitujen
    // tyyppien yli/aliluokkavaatimukset merkit√§√§n jotta k√§√§nt√§j√§ ei niist√§
    // varoita
	public static <E extends Comparable<? super E>> boolean inorderInsert(BTree<E> T, E x) {
		BTreeNode<E> n = T.getRoot();
		if (n == null) {
			T.setRoot(new BTreeNode<E>(x));
			return true;
		}

		while (true) {

			if (x.compareTo(n.getElement()) == 0)
				// x jo puussa, ei lisata
				return false;
			
			else if (x.compareTo(n.getElement()) < 0) {
				// x edeltaa n:n alkiota
				if (n.getLeftChild() == null) {
					// lisayskohta loydetty
					n.setLeftChild(new BTreeNode<E>(x));
					return true;
				} else
					n = n.getLeftChild();
			} else {
				// x suurempi kuin n
				if (n.getRightChild() == null) {
					// lisayskohta loydetty
					n.setRightChild(new BTreeNode<E>(x));
					return true;
				} else
					n = n.getRightChild();
			}
		} // while

	} // inorderInsert()

    public static int solmunKorkeus(BTreeNode n) {

        // null:n "korkeus" on -1 jotta lehtisolmun korkeus
        // on max(-1,-1)+1 = 0
        if (n == null)
            return -1;

        // solmun korkeus on maksimi lasten korkeuksista + 1
        return java.lang.Math.max(solmunKorkeus(n.getLeftChild()),
                                  solmunKorkeus(n.getRightChild())) + 1;
    }


	// Tulostus sisajarjestyksessa rekursiivisesti
	public static void inorderPrint(BTree T) {
		inorderPrintBranch(T.getRoot());
		System.out.println();
	} // inorderPrint()


	public static void inorderPrintBranch(BTreeNode n) {
		if (n == null)
			return;

		inorderPrintBranch(n.getLeftChild());
		System.out.print(n.getElement() + " ");
		inorderPrintBranch(n.getRightChild());

	} // inorderPrintBranch()


    /**
      * X3
      * Palauttaa bin√§√§ripuun sis√§j√§rjestyksess√§ viimeisen solmun,
      * siis puun oikeanpuoleisimman
      * @param T puu
      * @return viimeinen solmu
      * 
      * Kari Eronen
      * 8.10.2012
      * X3
      * Juuri tiedet‰‰n. Jos juurella ei ole oikeaa lasta, juuri on vastaus.
       * Muuten menn‰‰n  oikealle, haetaan oikea lapsi niin kauan kunnes
         * ei lˆydy en‰‰ oikeaa lasta. Palautetaan viimeinen , joka on vastaus = 
         * sis‰j‰rjestyksess‰ viimeinen.
         * 
         * Aikavaativuus: log (n) - puun korkeus. Algoritmi hakee vain oikean 
         * haaran alinta lasta, ei k‰y kaikkia solmuja l‰pi.
         * 
         * Toimivuus: Mielest‰ni ratkaisu toimii. Ainakin esimerkkisyˆtteill‰ n‰in
         * tapahtui.
         * 
         * Parannettavaa: En lˆyd‰ t‰st‰ juurikaan parannettavaa algoritmi-tasolla.
         *  Ehk‰ koodia voi jotenkin optimoida viel‰. 
         */
    public static <E> BTreeNode<E> inorderLast(BTree<E> T) {
        // X3a t√§h√§n
    	BTreeNode<E> n = T.getRoot(); // juuri tiedet‰‰n , asetetaan aluksi n = juuri
        if (n == null) {     // jos juuri on null, palautetaan null.
        	return null;
        }
        BTreeNode<E> x = n.getRightChild();
        while (x != null) {
            n = x;
            x = n.getRightChild(); // katsotaan lˆytyykˆ oikea lapsi
        }
        return n;
        
    } // inorderLast()

    /**
      * X3
      * Palauttaa bin√§√§ripuun annettua solmua sis√§j√§rjestyksess√§ 
      * edelt√§v√§n solmun,
      * @param T puu
      * @param n solmu jonka edelt√§j√§ palautetaan
      * @return edelt√§j√§solmu tai null jollei edelt√§j√§√§ ole
      * On kolme vaihtoehtoa lˆyt‰‰ edelt‰j‰.
      * 
      * Kari Eronen
      * 8.10.2012
      * X3-teht‰v‰
      * 
      * Vaihtoehto1; Edelt‰j‰ on vasemman lapsen oikean puolimmainen lapsi
      * vaihtoehto2; Edelt‰j‰ on joko suoraan is‰ tai sen is‰ ylˆsp‰in, joka ei ole kuin
      * oikea lapsi jollekin (haettu edelt‰j‰).
      * Vaihtoehto 3; Edelt‰j‰‰ ei ole. T‰m‰ on mahdollista silloin kun annettu n
      * on mahdollisimman vasemmalla, sis‰j‰rjestyksess‰ ensimm‰inen.
      * 
      *  Aikavaativuus; solmujen lukum‰‰r‰  O(n). Algoritmi k‰y l‰pi kaikki solmut
      *  yksitellen.
      *  
      *  Toimivuus: Mielest‰ni ratkaisu toimii. Ainakin esimerkkisyˆtteill‰ n‰in tapahtui.
      *  
      *  Parannettavaa; En lˆyd‰ t‰st‰ juuri parannettavaa algoritmitasolla.
      *   Ehk‰ koodia voi jotenkin optimoida.
      *  
      *  
      * 
      */
    public static <E> BTreeNode<E> inorderPrevious(BTree<E> T, BTreeNode<E> n) {

        BTreeNode<E> y = null;
        BTreeNode<E> x = n.getLeftChild();
        
        
        if ( x != null ) {   // vaihtoehto 1
        	while (x.getRightChild() != null){
        		x = x.getRightChild();
        	}
        return x;
        }
        else {  // vaihtoehto 2
        	y = n.getParent();
        	x = n;
        	while ((y != null) && (x == y.getLeftChild())){
        		x = y;
        		y = y.getParent();
        	}
        }
        return y;                  // palauttaa edelt‰j‰n
    }
    

	
} // class traI_12_x3_pohja
