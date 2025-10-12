/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package so.rezervacija;

import domain.Rezervacija;
import domain.StavkaRezervacije;
import so.ApstraktnaSO;

/**
 *
 * @author Marko
 */
public class SOUnesiRezervaciju extends ApstraktnaSO{
    private Object rezultat;
    
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
        Rezervacija rez = (Rezervacija) param;
        double ukupnaCena = 0;
        for (StavkaRezervacije sr : rez.getStavkaRezervacije()) {
            ukupnaCena += sr.getIznos();
        }
        rez.postaviUkupanIznos(ukupnaCena);
        rezultat = dbb.kreiraj(rez);
        Rezervacija r = (Rezervacija) rezultat;
        for (StavkaRezervacije stavkaRezervacije : r.getStavkaRezervacije()) {
            stavkaRezervacije.setRezervacija(r);
            dbb.kreiraj(stavkaRezervacije);
        }
    }

    public Object getRezultat() {
        return rezultat;
    }
}
