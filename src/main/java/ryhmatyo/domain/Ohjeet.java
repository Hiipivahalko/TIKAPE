
package ryhmatyo.domain;

import java.util.ArrayList;
import java.util.List;


public class Ohjeet {
    
    private List<String> mitat;

    public Ohjeet() {
        this.mitat = new ArrayList<>();
        
    }
    
    public void lisaaMitta(String mitta) {
        mitat.add(mitta);
    }
    
    public List<String> luoMitat() {
        lisaaMitta("litra");
        lisaaMitta("desilitra");
        lisaaMitta("centtilitra");
        lisaaMitta("millilitra");
        lisaaMitta("loraus");
        lisaaMitta("tyjäys");
        lisaaMitta("puristus");
        lisaaMitta("hippunen");
        lisaaMitta("kpl");
        lisaaMitta("tee lusikallinen");
        lisaaMitta("tippa");
        
        return mitat;
    }

    public List<String> getMitat() {
        return mitat;
    }
    
    
}
