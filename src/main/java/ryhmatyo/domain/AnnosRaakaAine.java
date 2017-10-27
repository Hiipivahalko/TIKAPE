
package ryhmatyo.domain;


public class AnnosRaakaAine {
    
    Integer id;
    Integer annos_id;
    Integer raakaAine_id;
    Integer maara;
    String ohje;

    public AnnosRaakaAine(Integer id, Integer annos_id, Integer raakaAine_id, Integer maara, String ohje) {
        this.annos_id = annos_id;
        this.raakaAine_id = raakaAine_id;
        this.maara = maara;
        this.ohje = ohje;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
    

    public Integer getAnnos_id() {
        return annos_id;
    }

    public Integer getRaakaAine_id() {
        return raakaAine_id;
    }

    
    public Integer getMaara() {
        return maara;
    }

    public String getOhje() {
        return ohje;
    }
    
}
