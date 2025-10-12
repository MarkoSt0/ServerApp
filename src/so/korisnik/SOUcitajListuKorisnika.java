/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package so.korisnik;

import domain.Korisnik;
import domain.OpstaKlasa;
import java.util.List;
import so.ApstraktnaSO;

/**
 *
 * @author Marko
 */
public class SOUcitajListuKorisnika extends ApstraktnaSO{
    private List<OpstaKlasa> lista;

    @Override
    protected void validacija(Object param) throws Exception {
        if (!(param instanceof Korisnik)) {
            throw new Exception("Neispravan parametar za ucitavanje liste.");
        }
    }

    @Override
    protected void operacija(Object param) throws Exception {
        lista = dbb.ucitajIzBaze((Korisnik) param);
    }

    public List<OpstaKlasa> getLista() {
        return lista;
    }
}
