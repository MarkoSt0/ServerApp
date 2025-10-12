/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package so.mesto;

import domain.Mesto;
import so.*;
import domain.OpstaKlasa;
import java.util.List;

/**
 *
 * @author Marko
 */
public class SOUcitajListuMesta extends ApstraktnaSO {
    private List<OpstaKlasa> lista;

    @Override
    protected void validacija(Object param) throws Exception {
        if (!(param instanceof Mesto)) {
            throw new Exception("Neispravan parametar za ucitavanje liste.");
        }
    }

    @Override
    protected void operacija(Object param) throws Exception {
        lista = dbb.ucitajIzBaze((Mesto) param);
    }

    public List<OpstaKlasa> getLista() {
        return lista;
    }
}
