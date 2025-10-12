/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package so.korisnik;

import domain.Korisnik;
import domain.OpstaKlasa;
import so.ApstraktnaSO;

/**
 *
 * @author Marko
 */
public class SOPretraziKorisnika extends ApstraktnaSO{
    private Object rezultat;
    
    @Override
    protected void validacija(Object param) throws Exception {
        if (!(param instanceof Korisnik)) {
            throw new Exception("Neispravan objekat za pretragu.");
        }
    }

    @Override
    protected void operacija(Object param) throws Exception {
        rezultat = dbb.pretrazi((OpstaKlasa) param);
    }

    public Object getRezultat() {
        return rezultat;
    }
}
