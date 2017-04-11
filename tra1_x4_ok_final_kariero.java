
// Pohja TRAI X4:een / SJ

import java.util.NoSuchElementException;
import java.util.Iterator;



/**
  * Abstraktin tietotyypin rengas toteutus dynaamisesti.
  * Ei tarjoa asemaa, vaan getNext() -operaatio tarjoaa
  * oman mielens√§ mukaan aina seuraavia alkioita
  */


// vaihda luokan nimeksi oman spostin alkuosa
public class kariero<E> implements Iterable<E>

{

    // lis√§√§ j√§seni√§ ja metodeja oman toteutuksen mukaan

	
    //m‰‰ritell‰‰n RingNodet
 
	
	private RingNode<E> current;
	private RingNode<E> last;
	private RingNode<E> first;
   	

    private int size = 0;

    /**
      * Luo uuden tyhj√§n renkaan
      */
    
    
    
    public kariero() {
        
    	 
    	Object current = null;
        
        //RingNode<E> EOL;
		// lis√§√§ tarvittaessa
        //first = EOL;
        //last = EOL;
    }

    /**
      * Alkioiden m√§√§r√§ renkaassa.
      * @return Alkioiden m√§√§r√§n.
      */
    public int size() {
        return size;
    }

    /**
      * Onko rengas tyhj√§
      * @return true jos rengas on tyhj√§, muuten false.
      */
    public boolean isEmpty() {
        return (size == 0);
    }

    /**
     * GETNEXT
      * Palauttaa seuraavan alkion renkaasta.
      * Alussa palauttaa jonkin alkion.
      * Toistuvasti kutsuttaessa palauttaa kaikkia alkioita vuorotellen.
      * @return Yhden renkaan alkion.
      * @throws NoSuchElementException jollei renkaassa ole alkoita.
      * 
      * Kari Eronen: 
      * 22.10.2012
      *
      * kaksisuuntainen dynaamisesti linkitetty rengas toimii samalla tavalla kuin lista
      * paitsi ett‰ viimeisest‰ on linkitys ensimm‰iseen (next) ja ensimm‰isest‰ viimeiseen
      * (prev).
      * Toistuvasti kutsuttaessa kutsutaan vuorotellen kaikkia alkioita j‰rjestyksess‰
      * ja menn‰‰n eteenp‰in.
      * 
      * Toimivuus: Mielest‰ni ratkaisu toimii kaikkien kolmen vaaditun toiminnalli-
      * suuden osalta (add, getNext,removePrev).
      * 
      * Aikavaativuus: Kaikki operaatiot ovat vakioaikaisia , O(1).
      * 
      * 
      * Parannusehdotukset:
      * En t‰h‰n h‰t‰‰n keksi parannettavaa. Ehk‰ koodia voisi jotenkin optimoida
      * v‰h‰n viel‰.
      * 
      * 
      * 
      */
    public E getNext() {
        if (size == 0)
            throw new NoSuchElementException("Cannot next() from empty Ring");

        //jos renkaassa on yksi tai enemm‰n
        //laittaa currentiksi siit‰ seuraavan
        current = current.next;  
        // t√§m√§ t√§ss√§ vain esimerkkin√§, muuta tarpeen mukaan
        //palauttaa currentin
        
        return current.elem;

    }

    /**ADD
      * Lis√§√§ renkaaseen alkion.
      * @param Lis√§tt√§v√§ alkio
      * 
      * Kari Eronen
      * 22.10.2012
      * TOIMINNALLISUUS
      * Lis‰‰ renkaaseen j‰rjestyksess‰ alkion viimeisen per‰‰n ja tekee uudet
      * linkitykset. Jos rengas on tyhj‰ ennen lis‰yst‰ niin lis‰‰ alkuun ensimm‰isen.
      * 
      * AIKAVAATIVUUS
      * Kaikki operaatiot ovat vakioaikaisia , O(1).
      * 
      * PARANNUSEHDOTUKSET
      * Mielest‰ni ADD toimii. En keksi siihen parannettavaa. Ehk‰ koodia voisi
      * jotenkin optimoida.
      * 
      * 
      */
    public void add(E x) {

        // luodaan uusi rengassolmu
        RingNode<E> n = new RingNode<E>(x);
//jos on tyhj‰, niin ensimm‰inen ja viimeinen on sama
        if (size == 0) { 
        n.prev = n;
        n.next = n; 
        	//current=n;
            // lis√§t√§√§n ensimm√§inen alkio
        	
        	first=last=current=n;
        	
            // X4
        }
//jos ei ole tyhj‰, mihin lis‰t‰‰n niin lis‰ys tehd‰‰n loppuun
         
        	 else {
    
        	//lis‰ys tehd‰‰n loppuun
        		 
        	n.next = first;   //viitataan renkaan alkuun, jossa ensimm‰inen
        	n.prev = last;    //Sen j‰lkeen laittaa lis‰tt‰v‰n edellisen last:ksi.
        	first.prev = n;    // ensimm‰ist‰ edelt‰v‰ on itse lis‰tty n.
        	last.next=n;       //Aikaisemmin m‰‰r‰ty last:n seuraaja on n.
        
        	last = n;           //lopuksi muutetaan last n:ksi.
        	
            // on jo aiemmin alkioita
            // lis‰t‰‰n loppuun
        	

             // X4

         }
        
        size++;            // kasvatetaan kokoa yhdell‰
    
}
    
    /**REMOVEPREV
      * Poistaa ja palauttaa renkaasta sen alkion joka edellisell√§ kerralla on next():ll√§ palautettu
      * Jollei next()i√§ ole kutsuttu, poistaa jonkin alkion.
      * @return Poistettu alkio.
      * @throws NoSuchElementException jollei renkaassa ole alkoita
      * 
      * KARI ERONEN
      * 22.10.2012
      * 
      * TOIMINNALLISUUS
      * poistetaan edellisell‰ kerralla next();ll‰ kutsuttu alkio
      * poistaa currentin. Jos viimeksi on kutsuttu add,
      * poistaa sen mik‰ on viimeksi lis‰tty.
      * AIKAVAATIVUUS
      * Kaikki operaatiot ovat vakioaikaisia , O(1).
      * 
      * PARANNUSEHDOTUKSET
      * Mielest‰ni ADD toimii. En keksi siihen parannettavaa. Ehk‰ koodia voisi
      * jotenkin optimoida.
      * 
      */
    public E removePrev() {
        if (size == 0)
            throw new NoSuchElementException("Cannot remove from empty Ring");

        // t√§m√§ t√§ss√§ vain esimerkkin√§, muuta tarpeen mukaan
        E x = current.elem;

        if (size == 1) {  // rengas yhden mittainen
            // poistetaan t‰m‰ yksi alkio
        	//Kaikki viittaukset null:iksi.
            first=last=current=null;

        } else  {  

            // X4
        	//poistetaan edellisell‰ kerralla next();ll‰ kutsuttu alkio
        	
        	/*poistaa currentin. Jos viimeksi on kutsuttu add,
        	 *  poistaa sen mik‰ on viimeksi lis‰tty
        	 */
        
        //currentin edelt‰j‰n seuraaja on currentin seuraaja	
        	
        current.prev.next=current.next;
        
        //currentin seuraajan edelt‰j‰ on currentin edelt‰j‰
        
        current.next.prev=current.prev;
        
        //siirt‰‰ currentin edelt‰j‰‰n
        
        current = current.prev;
        }
        
        //Lopuksi v‰hennet‰‰n kokoa ja palautetaan poistettu alkio
        size--;
        return x;
    }   // removePrev()


    // sis√§luokka rengassolmuille - rengassolmujen luokka
    // Rengassolmu toimii samalla tavalla kuin lista paitsi ett‰ 
    // kaksisuuntaisessa rengassolmussa on viittaus viimeisest‰ ensimm‰iseen (next)
    // ja ensimm‰isest‰ viimeiseen (prev).
    
    class RingNode<E> {

        protected E elem = null;
        protected RingNode<E> next = null;
        protected RingNode<E> prev = null;
        
        public final RingNode EOL = null;
        // lis√§√§ j√§seni√§ ja metodeja oman toteutuksen mukaan

        protected RingNode(E x) {
            elem = x;
            prev = EOL;
            next = EOL;
        }
        public RingNode<E> prev(){
        	return prev;
        }
        public RingNode<E> next(){
        	return next;
        }
        public E getElement(){
        	return elem;
        }

    } // class RingNode



    // poista kommentit kun teet teht√§v√§√§ 45
    
    // Teht√§v√§ 45


    // Iterable:n toteutus
    public Iterator<E> iterator() {
        return new RingIter();
    }

    private class RingIter implements Iterator<E> {

        
    	   //private RingNode<E> current;
    	   
    	
    	
    	// aina pit√§√§ l√∂yty√§ seuraava jos alkioita on
        public boolean hasNext() {
            return (size > 0);
        }

        public E next() {
        	return getNext();
            
        }

        public void remove() {
        	removePrev();
        }
    } // class RingIter

    // \Teht√§v√§ 45
    


} // class

