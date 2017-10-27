
package ryhmatyo.domain;


public class Annos extends AbstractNamedObject {
    
    private String tarina;
    
    public Annos(Integer id, String name, String tarina) {
        super(id, name);
        this.tarina = tarina;
    }

    public String getTarina() {
        return tarina;
    }
    

    @Override
    public String toString() {
        return super.getNimi();
    }
    
    
    

    
    
    
}
