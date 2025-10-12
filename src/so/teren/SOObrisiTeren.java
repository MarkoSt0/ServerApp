/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package so.teren;

import domain.OpstaKlasa;
import domain.Teren;
import so.ApstraktnaSO;

/**
 *
 * @author Marko
 */
public class SOObrisiTeren extends ApstraktnaSO {
    boolean rezultat;
    @Override
    protected void validacija(Object param) throws Exception {
        if (!(param instanceof Teren)) {
            throw new Exception("Neispravan objekat za brisanje.");
        }
    }

    @Override
    protected void operacija(Object param) throws Exception {
        rezultat = dbb.obrisi((OpstaKlasa) param);
    }
    
    public boolean getRezultat(){
        return rezultat;
    }
}
