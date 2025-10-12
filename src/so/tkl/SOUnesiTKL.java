/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package so.tkl;

import domain.TKL;
import so.ApstraktnaSO;

/**
 *
 * @author Marko
 */
public class SOUnesiTKL extends ApstraktnaSO{
    private Object rezultat;

    @Override
    protected void validacija(Object param) throws Exception {
        if (!(param instanceof TKL)) {
            throw new Exception("Neispravan objekat za kreiranje. Poslat je " + param.getClass());
        }
    }

    @Override
    protected void operacija(Object param) throws Exception {
        rezultat = dbb.kreiraj((TKL) param);
    }

    public Object getRezultat() {
        return rezultat;
    }
}
