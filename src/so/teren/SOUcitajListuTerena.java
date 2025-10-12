/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package so.teren;

import so.*;
import domain.OpstaKlasa;
import domain.Teren;
import java.util.List;

/**
 *
 * @author Marko
 */
public class SOUcitajListuTerena extends ApstraktnaSO {
    private List<OpstaKlasa> lista;

    @Override
    protected void validacija(Object param) throws Exception {
        if (!(param instanceof Teren)) {
            throw new Exception("Neispravan parametar za ucitavanje liste.");
        }
    }

    @Override
    protected void operacija(Object param) throws Exception {
        lista = dbb.ucitajIzBaze((Teren) param);
    }

    public List<OpstaKlasa> getLista() {
        return lista;
    }
}
