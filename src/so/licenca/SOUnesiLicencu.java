/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package so.licenca;

import domain.Licenca;
import so.ApstraktnaSO;

/**
 *
 * @author Marko
 */
public class SOUnesiLicencu extends ApstraktnaSO{
    private Object rezultat;

    @Override
    protected void validacija(Object param) throws Exception {
        if (!(param instanceof Licenca)) {
            throw new Exception("Neispravan objekat za kreiranje. Poslat je " + param.getClass());
        }
    }

    @Override
    protected void operacija(Object param) throws Exception {
        rezultat = dbb.kreiraj((Licenca) param);
    }

    public Object getRezultat() {
        return rezultat;
    }
}
