/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package so.rezervacija;

import so.*;
import domain.OpstaKlasa;
import domain.Rezervacija;
import domain.StavkaRezervacije;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Marko
 */
public class SOUcitajListuRezervacija extends ApstraktnaSO {
    private List<OpstaKlasa> lista;

    @Override
    protected void validacija(Object param) throws Exception {
        if (!(param instanceof Rezervacija)) {
            throw new Exception("Neispravan parametar za ucitavanje liste.");
        }
    }

    @Override
    protected void operacija(Object param) throws Exception {
        try {
            lista = dbb.ucitajIzBaze((Rezervacija) param);
            List<Rezervacija> lRez = lista.stream()
                              .map(o -> (Rezervacija) o)
                              .toList();
            
            for (Rezervacija rezervacija : lRez) {
                List<OpstaKlasa> lStavki = dbb.ucitajIzBaze(new StavkaRezervacije(rezervacija));
                List<StavkaRezervacije> lS = lStavki.stream()
                              .map(s -> (StavkaRezervacije) s)
                              .collect(Collectors.toList());
                rezervacija.setStavkaRezervacije(lS);
            }
            lista = lRez.stream()
                              .map(o -> (OpstaKlasa) o)
                              .toList();
        } catch (SQLException e) {
            lista = null;
            throw new Exception("Greška u transakciji: " + e.getMessage(), e);
        }
        
    }

    public List<OpstaKlasa> getLista() {
        return lista;
    }
}
