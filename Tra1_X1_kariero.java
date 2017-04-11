import fi.joensuu.cs.tra.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Random;


public class kariero {

    public static void main(String[] args) {

        // defaults
        int vertices = 40;
        int edges = 50;
        int piiri = 7;

        if (args.length > 0)
            vertices = Integer.valueOf(args[0]);

        if (args.length > 1)
            edges = Integer.valueOf(args[1]);

        int seed = vertices+edges;

        if (args.length > 2)
            seed = Integer.valueOf(args[2]);

        // piirin laajuus
        if (args.length > 3)
            piiri = Integer.valueOf(args[3]);


        Graph graph;
        kariero y = new kariero();
        
        System.out.println("\nVerkko1: ");
        graph = GraphMaker.createGraph(vertices, edges, seed);
        System.out.println("2-jakoinenV: " + TraII_13_X1_test.onkoKaksiJakoinenValkoisillaKaarilla(graph));
        System.out.println("Karin kaksijakoinen ohjelma");
        System.out.println("2-jakoinenV: " + TraII_13_X1_test.onkoKaksiJakoinenValkoisillaKaarilla(graph));
        y.merkitseKaksiJakoiseksi(graph);
        
        //System.out.println(graph.toString());
        //System.out.println(GraphMaker.toString(graph, 0));

        
        
        // annetaan kaarille painot
        Random rnd = new Random(seed);
        for (Edge e : graph.edges())
            e.setWeight(rnd.nextInt(15)+1);
        System.out.println(GraphMaker.toString(graph, 1));

        // haetaan kunkin solmun l‰hipiirit
        System.out.println("\nL‰hipiirit (et‰isyys max " + piiri + ")");
        for (Vertex v : graph.vertices()) {
            System.out.println("Solmu " + v + " : " +
                               y.lahiPiiri(graph, v, piiri));
        }

        // aloitussolmun l‰hipiirit 1-15
        Vertex s = graph.vertexIterator().next();
        System.out.println();
        //System.out.println(graph);
        for (int i = 1; i < 25; i++)
            System.out.println("et = " + i + " : " + y.lahiPiiri(graph, s, i));




        System.out.print("\nYhten√§inen: ");
        System.out.println("\nAloitussolmu on:" + s);
        System.out.println("Onko yhtenainen ensin: " + y.yhtenainen(graph));
        y.yhtenaista(graph);
        System.out.println("Onko yhtenainen nyt: " + y.yhtenainen(graph));
        
        System.out.print("Keh√§inen:   ");
        System.out.println(y.kehainen(graph));
        
        System.out.println("2-jakoinenV: " + TraII_13_X1_test.onkoKaksiJakoinenValkoisillaKaarilla(graph));
        

        System.out.println("\n2-jakoinen: ");
        graph = GraphMaker.createBiPartie(vertices, vertices, edges, seed);

        System.out.println(GraphMaker.toString(graph, 0));
        System.out.println("2-jakoinen: " + y.onkoKaksiJakoinen(graph));
        //System.out.println(graph.toString());
        y.merkitseKaksiJakoiseksi(graph);
        System.out.println("oma ohjelma - tee kaksijakoiseksi");
        
        //System.out.println("2-jakoinen: " + y.onkoKaksiJakoinen(graph));
        
        //System.out.println(graph.toString());
        //y.raportoiValkeatJaMustatKaaret(graph);
        System.out.println("2-jakoinenV: " + TraII_13_X1_test.onkoKaksiJakoinenValkoisillaKaarilla(graph));


        System.out.println("\nLis‰t‰‰n kaaria");
        // rikotaan 2-jakoisuus lis‰‰m‰ll‰ kumpaankin kaarijoukkoon
        // vaariaKaaria kpl sis‰isi‰ kaaria.
        // vaariaKaaria saa olla max vertices-1
        int vaariaKaaria = 2;   // siis 2 tarkoittaa 4 kaarta
        if (vaariaKaaria > vertices-1)
            vaariaKaaria = vertices-1;
        Vertex[] solmut = GraphMaker.getVertexArray(graph);
        for (int i = 0; i < vaariaKaaria; i++) {
            if (! solmut[i].isAdjacent(solmut[i+1]))
                solmut[i].addEdge(solmut[i+1]);
            if (! solmut[i+vertices].isAdjacent(solmut[i+vertices+1]))
                solmut[i+vertices].addEdge(solmut[i+vertices+1]);
        }
        //System.out.println(graph.toString());
        System.out.println("2-jakoinenV: " + TraII_13_X1_test.onkoKaksiJakoinenValkoisillaKaarilla(graph));
        // testataan taas
        //System.out.println("2-jakoinen: " + y.onkoKaksiJakoinen(graph));
        
        
        y.merkitseKaksiJakoiseksi(graph);
        System.out.println("karin kaksijakoinen");
        //System.out.println("2-jakoinen: " + y.onkoKaksiJakoinen(graph));
        
        //System.out.println(graph.toString());
        //y.raportoiValkeatJaMustatKaaret(graph);
        System.out.println("2-jakoinenV: " + TraII_13_X1_test.onkoKaksiJakoinenValkoisillaKaarilla(graph));

    }

    boolean yhtenainen(Graph g) {
        // kaikki valkoisiksi
        varita(g, Graph.WHITE);

        // syvyyssyyntainen lpikynti jostain solmusta
        Vertex w = g.firstVertex(); // tai g.vertices().getFirst();
        dfsColor(w, Graph.BLACK);

        for (Vertex v : g.vertices())
            if (v.getColor() == Graph.WHITE)
                return false;
        return true;
    }

    // syvyyssyynainen lpikynti vritten vrill c
    void dfsColor(Vertex v, int color) {
        v.setColor(color);

        for (Vertex w : v.neighbors())
            if (w.getColor() != color)
                dfsColor(w, color);
    }




    // yhtenaisten komponenttien m‰‰r‰
    int komponentteja(Graph g) {
        varita(g, Graph.WHITE);
        int n = 0;
        for (Vertex v : g.vertices())
            if (v.getColor() == Graph.WHITE) {
                dfsColor(v, Graph.BLACK);
                n++;
            }
        return n;
    } // komponentit()

    // verkko annetun vriseksi
    void varita(AbstractGraph g, int c) {
        for (Vertex v : g.vertices())
            v.setColor(c);
    }

    // taydenna ep‰yhten‰inen suuntaamaton verkko yhten‰iseksi
    
    void yhtenaista(Graph g) {

        varita(g, Graph.WHITE);

        // syvyyssyyntainen lpikynti jostain solmusta
        Vertex w = g.firstVertex(); // tai g.vertices().getFirst();
        dfsColor(w, Graph.BLACK);

        	for (Vertex v : g.vertices()) {
        		if (v.getColor() == Graph.WHITE) {
        			//Toinen komponentti lˆytyy, yhdist‰ solmut toisiina
        			g.firstVertex().addEdge(v);
        			dfsColor(v,Graph.BLACK);
        
        		}
        	}
    }

    	
    	
    	
        // toista...
        // etsi kaksi solmua jotka ovat eri komponenteissa
        // ja lis‰‰ kaari niiden v‰lille
        

    


    // Teht‰v‰ 11
    // Onko verkossa keh‰ vai ei
  
    boolean kehainen(Graph g) {
        varita(g, Graph.WHITE);

        // TODO
        return false;
    }

    // Teht‰v‰ 13
    // palauttaa solmun v l‰hipiirin (polun paino max x) joukkona
    Set<Vertex> lahiPiiri(Graph g, Vertex v, float x) {

        GraphMaker.varita(g, Graph.WHITE);

        Set<Vertex> S = new Set<Vertex>();

        // vihje: kun tarvitset kaarten painoja, k‰yt‰ 
        // kaarten l‰pik‰ynti‰ ja kaaresta toinen p‰‰:
        for (Edge e : v.edges()) {
            Vertex w = e.getEndPoint(v);
            lahiPiiri_r(w, S, x - e.getWeight());
        }

        return S;
    }

    // varsinainen rekursiivinen algoritmi
    void lahiPiiri_r(Vertex v, Set<Vertex> S, float x) {

        // TODO
    }





    // Teht‰v‰ X1
    // algoritmi merkitsee mustalla kaikki ne kaaret jotka rikkovat
    // verkon kaksijakoisuutta vastaan (ei ylim‰‰r‰isi‰).
    
    /*RATKAISU: Algoritmi toimii siten , ett‰ etenee vuorotellen
     * 0/1-jakoj‰‰mill‰ v‰rj‰ten solmuja rekursiossa. Sitten kun
     * edet‰‰n verkossa ja tavataan naapurisolmu, joka on samanv‰rinen
     * niin niiden v‰linen kaari merkataan mustaksi.
     * 
     * mielest‰ni ei ole toimiva. Se toimii ihan hyvin kun menn‰‰n
     * eteenp‰in "puhtaassa" verkossa merkaten solmuja vuorotellen
     * mustaksi ja punaiseksi. Sitten kun pit‰‰ palata rekursiossa 
     * takaisinp‰in, tulee ongelmia. Minulle ei ole ihan selv‰‰
     * jos algoritmissa on foreach-toisto ja samalla rekursiokutsu,
     * miten homma etenee. Yritin hahmottaa t‰t‰ paperilla moneen
     * kertaan mutta ei oikein selvinnyt. Lis‰ksi en saanut mill‰‰n
     * syntym‰‰n ei-kaksijakoista verkkoa GraphMaker:lla. 
     * TraII_13_X1_test.onkoKaksiJakoinenValkoisillaKaarilla(graph))-
     * kutsut palauttivat aina true ? Lis‰ksi myˆs en ole ihan
     * varma tuosta kaaren v‰rj‰‰misest‰ mustaksi - Meneekˆ se oikeasti
     * noin.
     * 
     * 
     * Aikavaativuus:
     * Algoritmi k‰y l‰pi jokaisen solmun (n kpl) kahteen kertaan (
     * rekursiossa menness‰ ja tullessa takaisin). Lis‰ksi
     * tarkistetaan jokainen solmun v‰li kertaalleen = kaarien m‰‰r‰ e.
     * Eli aikavaativuudeksi tulee O(2*n + e)
     * 
     * 
     * 
     * Parannusehdotukset:
     * Kuten aikaisemmin totesin, mielest‰ni ei ole toimiva
     * ratkaisu.
     * Luulen ett‰ ajattelen asiaa liian monimutkaisesti.
     * Ainakin onkoKaksiJakoinen-metodin mukaan ratkaisu on
     * yksinkertaisempi. En ole myˆsk‰‰n ihan varma, miten palataan 
     * rekursiossa taaksep‰in esim. silloin kun on merkattu kaari
     * poistettavaksi. Myˆskin tuo (if-elseif-else)-rakenne on varmaan
     * t‰ss‰ turha. Sen voisi yksinkertaistaa jotenkin.
     * Loppui aika kesken ja olisin tarvinnut apuja siin‰, miten
     * tuo rekursio palaa takaisinp‰in verkossa ja menee sitten 
     * eteenp‰in.
     * 
     * 
     * 
     * 
     */
    
    
    
    void merkitseKaksiJakoiseksi(Graph g) {
    	// pohjav‰ritys-v‰ritt‰‰ kaikki solmut v‰rill‰ 2= harmaa
    	varita(g, 2);


    	//v‰ritt‰‰ kaikki kaaret valkoiset, jotta kaksijakoisuus voidaan
    	//havaita Simon ohjelmalla
    	
    	for (Edge e : g.edges())
            e.setColor(Graph.WHITE);
    	
    	
    	//l‰htee k‰ym‰‰n verkon solmuja yksi kerrallaan
        
    	
    	//k‰yd‰‰n l‰pi kaikki verkon solmut, myˆs eri komponenteissa
    	for (Vertex v : g.vertices()) {
            
        	//jos solmun v‰ri on harmaa=l‰pik‰ym‰tˆn
        	//k‰yd‰‰n l‰pi kaikki solmut, myˆs eri komponenteissa
    		if (v.getColor() == 2) { 
        		
        		//menn‰‰n aliohjelmaan, jossa parametreina solmu ja 0=BLACK
                 		dfs2varitaMerkitse(v, 0);
        	}
        }
    }
//Koska aloitussolmu on musta, rekursion palatessa siihen ja lˆyt‰ess‰ edellisen
  //myˆs mustaksi, lˆytyy ylim‰‰r‰inen
//Yksitt‰isen komponentin l‰pik‰ynti
  void dfs2varitaMerkitse(Vertex v, int c) {
    
	  
    	//aliohjelmassa 1. k‰sittelyss‰ oleva solmu merkataan mustaksi
    	//1. kierroksella aloitussolmu v
    	// Tehd‰‰n kaikille komponentissa ,alkaen aloitussolmusta
    for (Vertex w : v.neighbors()) {
    	//for-each toisto kaikille harmaille, ei-k‰sitellyille(musta-punainen v‰rj‰ys)
    	
    		// jakoj‰‰nnˆs c=0/1 rekursiossa	
    	if (c == 0) {
    				//jos lˆytyy harmaita, k‰sittelem‰ttˆmi‰
    				if (v.getColor() == 2) {
		    		v.setColor(0); //asetetaan mustaksi, 1. kierroksella aloitussolmu 0
		    		//menn‰‰n eteenp‰in rekursiossa seuraavalle kierrokselle
		    		//c saa arvoksi joko 0/1= jakoj‰‰nnˆs(musta/valkoinen)
		    		dfs2varitaMerkitse(w, (c+1)%2);
    		
    				}
    			
	        	//jos seuraava naapuri(voi olla myˆs alku), on samanv‰rinen
    			//ja siis k‰sitelty , jolloin rikkoo kaksijakoisuutta
    			//verrattuna edelliseen
    				else if (v.getColor() == w.getColor()) {
		    			
				    //merkataan ko. kaari poistettavaksi, v‰rill‰ musta
				    //palataan takaisinp‰in rekursiossa alkuun
    					
    					
    				Edge poistettavakaari = v.getEdge(w);
				    poistettavakaari.setColor(0);
				    //rekursio palaa takaisinp‰in ?
				    //dfs2varitaMerkitse(w, (c+1)%2);
				    
				    
		    		}
    			
    				//palataan takaisin rekursiossa ? 
    				
    				else {
    				
    				}
		    				
    	}
    			
    	// c = 1
    	else {
    				
    				//lˆyt‰‰ 1. uudestaan ?
    				if (w.getColor() == v.getColor()) {
    					//lˆytyi samanv‰rinen,poistettava kaari --> poista
    				//for-silmukassa eteenp‰in -- MITEN
    				Edge poistettavakaari = v.getEdge(w);
    				poistettavakaari.setColor(0);
    				
    				dfs2varitaMerkitse(w, (c+1)%2);
    				
    					
    				}
    				//menn‰‰n seuraavalle kierrokselle , eteenp‰in verkossa
    				
    				
    				
    				else if (v.getColor() == 2) {
    				v.setColor(1); //asetetaan valkoiseksi
		    		dfs2varitaMerkitse(w, (c+1)%2);
		    		
    				
    				}
    				else { //menn‰‰n for-silmukassa eteenp‰in
    					
    				}
    				
    				
    				
    				
    				
    				
    	}    	
		    	
		    	
    }  //for-toisto
   } //dfs2varitamerkitse
     
    
	    			
            		
        
        
        //return true;
        //muuten jatkaa seuraavalle kierrokselle
    

    // osaratkaisu teht‰v‰‰n X1
    // varmistaa v‰ritt‰m‰ll‰ kahdella v‰rill‰ onko
    // verkko 2-jakoinen vai ei
    	
    	
    	
    	
    	
    boolean onkoKaksiJakoinen(Graph g) {

        varita(g, 2);

        for (Vertex v : g.vertices())
            if (v.getColor() == 2)
                if (! dfs2varita(v, 0))
                    return false;

        return true;
    }

    boolean dfs2varita(Vertex v, int c) {
        
    	//c=0/1 , musta/valkoinen 
    	v.setColor(c);

        for (Vertex w : v.neighbors()) {

            if (w.getColor() == c)
                return false;
            else if (w.getColor() == 2) {
                if (! dfs2varita(w, (c+1)%2))
                    return false;
            }
        }

        return true;
    }

    // toimii kuten onkoKaksiJakoinen, mutta huomioi vain valkoiset kaaret
    // siirretty omaksi luokakseen joka lˆytyy k‰‰nnettyn‰ www-sivulta
    // kopio se k‰‰nnetty samaan hakemistoon
    // boolean onkoKaksiJakoinenValkoisillaKaarilla(Graph g) {}
    

    // toinen testimetodi
    void raportoiValkeatJaMustatKaaret(Graph g) {
        int n = 0 ,nv = 0, nm = 0;
        for (Edge e : g.edges()) {
            n++;
            if (e.getColor() == Graph.WHITE)
                nv++;
            if (e.getColor() == Graph.BLACK)
                nm++;
        }
        System.out.println("Kaaria=" + n + ", valkeita=" + nv + ", mustia=" + nm);
    }




}
