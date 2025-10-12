/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package so.radnik;

import domain.OpstaKlasa;
import domain.Radnik;
import so.ApstraktnaSO;

/**
 *
 * @author Marko
 */
public class SOUnesiRadnika extends ApstraktnaSO{
    private Object rezultat;

    @Override
    protected void validacija(Object param) throws Exception {
        if (!(param instanceof Radnik)) {
            throw new Exception("Neispravan objekat za kreiranje.");
        }
    }

    @Override
    protected void operacija(Object param) throws Exception {
        rezultat = dbb.kreiraj((OpstaKlasa) param);
    }

    public Object getRezultat() {
        return rezultat;
    }
}
