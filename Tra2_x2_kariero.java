// LottoHashFile_pohja.java SJ

// Lottorivien tallennus tiedostossa hajautuksella

import java.io.RandomAccessFile;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Vector;
import java.util.PriorityQueue;
import java.util.Comparator;

public class kariero
    implements Iterable<LottoRivi>
{

    protected int headerLength = 8+8;  // size of header in the file (two longs)
    protected int blockSize = LottoRivi.KOKO;      // size of each element
    protected int extraSpace = 20;      // # of extra space at end of file
    protected long maxElements = 100;          // main hash size
    protected static final LottoRivi emptyData = LottoRivi.EIRIVI;    // placeholder for empty slots

    // talletustiedosto
    protected RandomAccessFile file;
	

    // avaa olemassaolevan tiedoston
    public kariero(String fileName) {
        try {
            synchronized(this) {
                file = new RandomAccessFile(fileName, "rw");
                // read header
                maxElements = file.readLong();
                extraSpace = (int) file.readLong();
            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    // luo uuden tiedoston
    public kariero(String fileName, int max) {
        maxElements = max;
        try {
            synchronized(this) {
                file = new RandomAccessFile(fileName, "rw");

                // write headers
                file.writeLong(maxElements);
                file.writeLong(extraSpace);

                // file size
                file.setLength(headerLength + blockSize * (maxElements + extraSpace));
                file.seek(headerLength);
               
                // fill an array with empty data
                int writeblocklen = extraSpace;
                byte[] blockData = new byte[blockSize * writeblocklen];
                for (int i = 0; i < writeblocklen; i++)
                    emptyData.toBytes(blockData, i*blockSize);

                // fill the file with empty data
                for (long i = 0; i < maxElements + extraSpace; i += writeblocklen) {
                    // last block may be incomplete
                    if (i + writeblocklen > maxElements + extraSpace)
                        writeblocklen = (int) (maxElements + extraSpace - i);
                    file.write(blockData, 0, writeblocklen*blockSize);
                }
                

            }
           
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    // lis√§√§ hajautukseen uuden lottorivin
    // jos lottorivi on siell√§ jo ennest√§√§n, kasvattaa pelien lukum√§√§r√§√§

    // X2
    /* metodin kuvaus:
     * Metodi lis‰‰ lottorivin randomaccessfile-tiedostoon. Rivin paikka etsit‰‰n 
     * Suljetulla hajautuksella. Myˆs ko. rivin lukum‰‰r‰ lis‰t‰‰n tiedostoon. Jos 
     * rivi on jo olemassa, vain lukum‰‰r‰‰ p‰ivitet‰‰n. 
     * 
     * 
     * Aikavaativuus:
     * L‰hes vakioaikaista. Metodi koostuu vakioaikaisista seek, read,write -operaatioista.
     * Myˆs tavutaulukkojen luonti ja hajautusosoitteen haku on vakioaikaista.
     * 
     * 
     * Ongelmat:
     * Metodi ei toimi vaaditulla tavalla. Ensimm‰isen rivin lis‰ys onnistuu (rivi + lukum‰‰r‰)
     * mutta sen j‰lkeen en saanut hajautusta toimimaan niin ett‰ jos lis‰yksen
     * paikka sattuu samaan kohtaan, lis‰ys tehd‰‰n oikealle puolelle seuraavaan
     * tyhj‰‰n paikkaan. Samoin puuttuu toiminnallisuus siit‰ ett‰ jos ko. rivi
     * on jo olemassa, vain lukum‰‰r‰‰ p‰ivitet‰‰n. 
     * N‰it‰ ongelmia olen yritt‰nyt korjata , korjausyritykset ovat koodissa kommentoituna.
     * Mielest‰ni olen oikeilla j‰ljill‰ mutta nyt loppui aika kesken.
     * 
     */
    
    
    public void lisaa( LottoRivi uusiLottoRivi)  {

    	
    	
    	/* T‰ss‰ on yritetty katsoa onko olemassa ko. rivi‰
    	
    	if (file sis‰lt‰‰ (uusiLottoRivi.getPelit())) {
        	// pit‰‰ lˆyt‰‰ ko. rivin paikka ja kirjoittaa sinne- lis‰t‰ loppuun lukum‰‰r‰‰ yhdell‰
        	//miten se etsittiink‰‰n
    		
        	file.read();
        	Ja sitten lopettaa metodi kokonaan.
        }
        
        else { //jos ko. rivi‰ ei aikaisemmin ole
        	
    	*/
    	
    	// home address - hajautuksen tuottama satunnainen  osoite
        long addr = uusiLottoRivi.hashCode() % maxElements;
        
        
        
        //System.out.println("hajautuksen osoite on" + addr);
        
        if (addr < 0)
            addr = -addr;

        // varataan riitt√§v√§sti tilaa lohkon lukemiseen
        byte olddata[] = new byte[blockSize];

        synchronized(this) {
            try {
            	
            	/*
            	 * seek
public void seek(long pos)
 
Throws: IOException - if pos is less than 0 or if an I/O error occurs.
            	 */
            	
            	
                
            	long kirjoituspaikka =  headerLength + addr*blockSize;
            	
            	
            	
            	
   /*         //T‰ss‰ on yritetty katsoa tyhj‰‰ tilaa kirjoitukselle oikealle p‰in
            	
            	//byte buffer [] = new byte[blockSize];
            	
            	
            	

    Nyt on bufferissa ‰sken luettu data
    T‰ss‰ voisi esim. luoda sen LottoRivin bufferista ja kysy‰ silt‰, onko numeroita eli ei
    Jos lˆytyy niin jatketaan, jos ei niin silloin t‰ss‰ on oikea kohta eli seekilla blockSizen verran takasin p‰in ja siihen kirjoitus

            	/*
            	//Tehd‰‰n niin kauan kunnes kirjoituspaikka-alue on tyhj‰
           	
            	while (file.read(buffer, 0, blockSize) == 0) {
            	if (buffer.equals(emptyData)) {	
            		
            		
            		
            		kirjoituspaikka = kirjoituspaikka - blockSize;
           
           */      
            		
            	file.seek(kirjoituspaikka);
                long lottorivilong = uusiLottoRivi.getLong();
                file.read(olddata);
                file.writeLong(lottorivilong);
           
                file.seek(kirjoituspaikka + blockSize/2);
                long lottorivilkm = uusiLottoRivi.getPelit();
                file.read(olddata);
                file.writeLong(lottorivilkm);
                	
                	
                	
                file.close();
                
                
                	
            	
            
            
            	
            
                
                
                
            	
            	
                // TODO
            	

            } catch (IOException e) {
                System.err.println(e);
                return;
            }
            
        }
        
    }
        
    
        
        

        // jos oli t√§ynn√§, niin uudelleenhajautetaan
        //rehash();

        //lisaa(uusiLottoRivi);
    




    // palauttaa pelien lukum√§√§r√§n annetulle riville
    // X2
    /*Metodin kuvaus:
     * Metodi palauttaa random access filest‰ annetun lottorivin lukum‰‰r‰n.
     * Se ei siis kirjoita sinne mit‰‰n. Lukum‰‰r‰ lˆytyy rivin lopusta.
     * 
     * Mielest‰ni methodin pit‰isi toimia niin ett‰ tiedostoa k‰yd‰‰n l‰pi
     * rivi rivilt‰ ja jos vastaavuus lˆytyy, rivin lopusta palautetaan lukum‰‰r‰.
     * 
     * 
     * 
     * Aikavaativuus:
     * L‰hes vakioaikaista.
     * 
     * 
     * 
     * Ongelmat:
     * Minulla oli kaksi l‰hestymistapaa ratkaista t‰m‰ ongelma. 
     * Ensimm‰inen oli hajautuksessa saadun addr:n perusteella etsi‰. Mielest‰ni
     * t‰m‰ ei toimi t‰ss‰.
     * 
     * Toinen vaihtoehto oli k‰yd‰ l‰pi rivi rivilt‰ tiedostoa, mutta en saanut sit‰k‰‰n
     * toimimaan. Minulle j‰i ep‰selv‰ksi miten getPelit-metodi toimii ? 
     * 
     * 
     *
     * 
     * 
     * 
     */
    
    
    public long lukuMaara(LottoRivi rivi) {
        // home address
        long addr = rivi.hashCode() % maxElements;
        if (addr < 0)
            addr = -addr;
        
        long lkm = 0;
        
        
        
        

        try {
        	
        	file.seek(addr);
        	
        	byte largeBlock[] = new byte[blockSize];
        	
        	lkm = file.read(largeBlock, 0, blockSize);
        	
        	System.out.println( + lkm);
        	
        	// haetaan oikea kohta
                    	
            // luetaan lohko,  etsit√§√§n siit√§ oikeaa rivi√§
            // palautetaan 0 jos t√∂rm√§t√§√§n tyhj√§√§n
            // palautetaan lukum√§√§r√§ jos l√∂ytyi oikea

            // ....
        	
        	/*
        	long koko = file.length();
        	
        	long nblkm = koko/blockSize;
        	
        	    //byte buffer [] = new byte[blockSize];
                //file.read(buffer, 0, blockSize);
                //int nb = 0; // ...

                for (int i = 0; i < nblkm-1; i++) {
                	byte buffer [] = new byte[blockSize];
                    file.read(buffer, 0, blockSize);
                	
                	LottoRivi val = new LottoRivi(largeBlock, blockSize);
                    if (rivi.equals(emptydata)) { - jos vastaavuutta ei lˆydy lottorivin osalta 
                        lkm = 0;
                    } else if (rivi.equals(rivi?)) - jos vastaavuus lottorivin osalta
                        
                    	lkm = val.getPelit();
                    
                }
*/
                file.close();
                
       
                

        } catch (IOException e) {
            System.err.println(e);
        }
		
		return lkm;
        
    }


    protected void rehash() {

        // uudelleenhajauttaa hajatustaulun

        // kama aputiedostoon l√§pik√§ynnill√§
        // aputiedostosta takaisin
    	
    	

        // TODO


    }   // rehash()




    // close file
    public void close() {
        try {
            file.close();
            file = null;
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    // Iterable -interface:
    public Iterator<LottoRivi> iterator() {
        return new RHFIterator(this);
    }

    // koko sis√§ll√∂n tulostus
    public void print() {
        int i = 0;
        for (LottoRivi s : this) {
            System.out.print(s + " ");
            i++;
        }
        System.out.println("\n" + i + " alkiota");
    }



    // actual iterator implemention
    private class RHFIterator implements Iterator<LottoRivi> {

        kariero RHF = null;

        long filepos;   // current position in the file
        LottoRivi next = null; // last read LottoRivi
        byte [] buffer;
        int bufferpos;
        int bufferlength;

        // init iteration
        public RHFIterator(kariero hf) {
            RHF = hf;
            filepos = RHF.headerLength;
            bufferlength = extraSpace * blockSize;
            buffer = new byte[bufferlength];
            bufferpos = bufferlength;
        }


        // find next occurence
        public boolean hasNext() {

            while (true) {

                if (bufferpos >= bufferlength) {
                    // buffer is empty, read more
                    try {
                        // onko tiedosto jo kokonaan k√§yty l√§pi?
                        if (filepos >= RHF.file.length())
                            return false;
                        // seek to previous position
                        int nb;
                        synchronized (RHF) {
                            RHF.file.seek(filepos);
                            nb = RHF.file.read(buffer, 0, bufferlength);
                        } // sync
                        if (nb <= 0)
                            return false;
                        filepos += nb;
                        bufferlength = nb;
                        bufferpos = 0;
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                } // if
                // now buffer has data
                while (bufferpos < bufferlength) {
                    next = new LottoRivi(buffer, bufferpos);
                    bufferpos += blockSize;
                    if (!next.equals(emptyData))
                        return true;
                } // while bufferpos
            } // while true
        }

        public LottoRivi next() {
            return next;
        }

        public void remove() {
            throw new RuntimeException("RHFIterator.remove() not implemented");
        }

    } // class RHFIterator


}
