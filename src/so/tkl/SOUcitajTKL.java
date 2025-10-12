/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package so.tkl;

import so.*;
import domain.OpstaKlasa;
import domain.TKL;
import java.util.List;

/**
 *
 * @author Marko
 */
public class SOUcitajTKL extends ApstraktnaSO {
    private List<OpstaKlasa> lista;

    @Override
    protected void validacija(Object param) throws Exception {
        if (!(param instanceof TKL)) {
            throw new Exception("Neispravan parametar za ucitavanje liste.");
        }
    }

    @Override
    protected void operacija(Object param) throws Exception {
        lista = dbb.ucitajIzBaze((TKL) param);
    }

    public List<OpstaKlasa> getLista() {
        return lista;
    }
}
