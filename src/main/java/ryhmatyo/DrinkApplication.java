
package ryhmatyo;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import ryhmatyo.dao.AnnosDao;
import ryhmatyo.dao.AnnosRaakaAineDao;
import ryhmatyo.dao.RaakaAineDao;
import ryhmatyo.database.Database;
import ryhmatyo.domain.Annos;
import ryhmatyo.domain.AnnosRaakaAine;
import ryhmatyo.domain.Ohjeet;
import ryhmatyo.domain.RaakaAine;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;


public class DrinkApplication {

    public static void main(String[] args) {
        
        Spark.staticFileLocation("/static");
        
        Database database = new Database("jdbc:sqlite:drink.db");
        AnnosDao annos = new AnnosDao(database, "Annos");
        RaakaAineDao raakaAine = new RaakaAineDao(database, "RaakaAine");
        AnnosRaakaAineDao annosRaaka = new AnnosRaakaAineDao(database);
        Ohjeet ohje = new Ohjeet();
        
        ohje.luoMitat();
        
        Spark.get("/etusivu", (req, res) -> {
            HashMap map = new HashMap<>();
            
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/info", (req, res) -> { 
            HashMap map = new HashMap<>();
            
            return new ModelAndView(map, "info");
        }, new ThymeleafTemplateEngine());
        
        //etusivu ja kaikkien juomien listaus
        Spark.get("/annokset", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annokset", annos.findAll());
            
            
            return new ModelAndView(map, "annokset");
        }, new ThymeleafTemplateEngine());
        
        //yhden juoman sivu
        Spark.get("/annokset/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer annosId = Integer.parseInt(req.params(":id"));
            Annos aa = annos.findOne(annosId);
            map.put("annos", aa);
            map.put("raakaAineet", annosRaaka.tulostaAnnoksenOhjeet(annosId));
            map.put("drinkki", aa.getTarina());
            
            return new ModelAndView(map, "annos");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/annokset/:id/muokkaus", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer annosId = Integer.parseInt(req.params(":id"));
            Annos aa = annos.findOne(annosId);
            map.put("annos", aa);
            map.put("raakaAineet", annosRaaka.tulostaAnnoksenOhjeet(annosId));
            map.put("raaka", raakaAine.AnnoksenAineOsat(annosId));
            
            return new ModelAndView(map, "annosMuokkaus");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/annokset/muokkaus", (req, res) -> {
            Integer annosid = Integer.parseInt(req.queryParams("ainesID"));
            raakaAine.delete(Integer.parseInt(req.queryParams("raakaAineID")));
            
            res.redirect("/annokset/" + annosid + "/muokkaus");
            return "";
        });
        
        Spark.post("/annokset/tarina", (req, res) -> {
            String tarina = req.queryParams("tarina");
            System.out.println(tarina);
            String drinkki = req.queryParams("drinkki");
            System.out.println(drinkki);
            Integer id = annos.findByName(drinkki).getId();
            System.out.println(id);
            annos.talletaTarina(tarina, drinkki);
            
            res.redirect("/annokset/" + id);
            return "";
        });
        
        //raaka-aine sivu
        Spark.get("/raakaAineet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaAineet", raakaAine.findAll());
            
            
            return new ModelAndView(map, "raakaAineet");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/raakaAineet/muokkaus", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaAineet", raakaAine.findAll());
            
            
            return new ModelAndView(map, "raakaAineetMuokkaus");
        }, new ThymeleafTemplateEngine());
        
        //raaka-aineen poisto
        Spark.post("/raakaAineet/muokkaus", (req, res) -> {
            Integer raakaAineId = Integer.parseInt(req.queryParams("raakaAineId"));
            raakaAine.delete(raakaAineId);
            
            res.redirect("/raakaAineet/muokkaus");
            return "";
        });
        
        //raaka-aineen lisäys
        Spark.post("/raakaAineet/uusi", (req, res) -> {
            String uusiAine = req.queryParams("aine");
            RaakaAine aine = new RaakaAine(-1, uusiAine);
            raakaAine.saveOrUpdate(aine);
            
            
            res.redirect("/raakaAineet");
            return "";
        });
        
        Spark.get("/uusiAnnos", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annokset", annos.findAll());
            map.put("raakaAineet", raakaAine.findAll());
            map.put("mitat", ohje.getMitat());
            
            return new ModelAndView(map, "uusiAnnos");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/uusiAnnos/uusi", (req, res) -> {
            System.out.println("joo");
            String drinkki = req.queryParams("drinkki");
            Annos a = new Annos(-1, drinkki, "");
            annos.saveOrUpdate(a);
            
            res.redirect("/uusiAnnos");
            return "";
        });
        
        //lisätään drinkille raaka-aine
        Spark.post("/uusiAnnos/lisaaAine", (req, res) -> {
            Integer raakaId = Integer.parseInt(req.queryParams("aine"));
            Integer annoId = Integer.parseInt(req.queryParams("annos2"));
            System.out.println(raakaId);
            
            Integer maara = Integer.parseInt(req.queryParams("maara"));
            //System.out.println(maara);
            String mitta = req.queryParams("ohje");
            //System.out.println(maara);
            AnnosRaakaAine ar = new AnnosRaakaAine(-1, annoId, raakaId, maara, mitta);
            annosRaaka.saveOrUpdate(ar);
            
            
            res.redirect("/uusiAnnos");
            return "";
        });
        
        //poistetaan drinkki
        Spark.post("uusiAnnos/poisto", (req, res) -> {
            Integer annosid = Integer.parseInt(req.queryParams("drinkkiId"));
            annos.delete(annosid);
            
            res.redirect("/uusiAnnos");
            return "";
        });
    }
    
}
