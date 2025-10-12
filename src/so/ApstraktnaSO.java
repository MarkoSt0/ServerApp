/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package so;

import db.DatabaseBroker;

/**
 *
 * @author Marko
 */
public abstract class ApstraktnaSO {

    protected DatabaseBroker dbb;

    public ApstraktnaSO() {
        dbb = new DatabaseBroker();
    }

    public final void izvrsi(Object param) throws Exception {
        try {
            dbb.connect();
            validacija(param);
            operacija(param);
            dbb.commit();
        } catch (Exception e) {
            dbb.rollback();
            throw new Exception("Greška u transakciji: " + e.getMessage(), e);
        } finally {
            dbb.disconnect();
        }
    }

    protected abstract void validacija(Object param) throws Exception;
    protected abstract void operacija(Object param) throws Exception;
}
