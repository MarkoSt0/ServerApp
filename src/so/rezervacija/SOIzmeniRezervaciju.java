/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package so.rezervacija;

import domain.Rezervacija;
import domain.StavkaRezervacije;
import java.sql.SQLException;
import so.ApstraktnaSO;

/**
 *
 * @author Marko
 */
public class SOIzmeniRezervaciju extends ApstraktnaSO {
    boolean rezultat;
    @Override
    protected void validacija(Object param) throws Exception {
        if (!(param instanceof Rezervacija)) {
            throw new Exception("Neispravan objekat za kreiranje.");
        }
        
        Rezervacija r = (Rezervacija) param;

        if (r.getStavkaRezervacije().isEmpty()) {
            throw new Exception("Rezervacija mora da sadrzi bar jednu stavku!");
        }
    }

    @Override
    protected void operacija(Object param) throws Exception {
        try {
            double ukupnaCena = 0;
            Rezervacija r = (Rezervacija) param;
            
            for (StavkaRezervacije sr : r.getStavkaRezervacije()) {
                switch (sr.getStatus()) {
                    case NOVA:
                        dbb.kreiraj(sr);
                        ukupnaCena += sr.vratiIznos();
                        break;
                    case IZMENJENA:
                        dbb.promeni(sr);
                        ukupnaCena += sr.vratiIznos();
                        break;
                    case OBRISANA:
                        dbb.obrisi(sr);
                        break;
                    case NEPROMENJENA:
                        ukupnaCena += sr.vratiIznos();
                        break;
                }
            }

            
            if (r != null) {
                r.postaviUkupanIznos(ukupnaCena);
                dbb.promeni(r);
            }
            

            rezultat = true;
        } catch (SQLException e) {
            rezultat = false;
            throw new Exception("Greška u transakciji: " + e.getMessage(), e);
        }
    }
    
    public boolean getRezultat(){
        return rezultat;
    }
}
