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
public class SOIzmeniRadnika extends ApstraktnaSO {
    boolean rezultat;
    @Override
    protected void validacija(Object param) throws Exception {
        if (!(param instanceof Radnik)) {
            throw new Exception("Neispravan objekat za izmenu.");
        }
    }

    @Override
    protected void operacija(Object param) throws Exception {
        rezultat = dbb.promeni((OpstaKlasa) param);
    }
    
    public boolean getRezultat(){
        return rezultat;
    }
}
