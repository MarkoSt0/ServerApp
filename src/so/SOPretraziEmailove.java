/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package so;

import domain.Korisnik;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Marko
 */
public class SOPretraziEmailove extends ApstraktnaSO {
    private List<String> rezultati;

    @Override
    protected void validacija(Object param) throws Exception {
        if (!(param instanceof String)) {
            throw new Exception("Neispravan objekat za pretragu.");
        }
    }

    @Override
    protected void operacija(Object param) throws Exception {
        List<Object> obj = dbb.vratiPredloge(new Korisnik(), param);
        rezultati = obj.stream()
                           .map(o -> (String) o)
                           .collect(Collectors.toList());
    }

    public List<String> getRezultati() {
        return rezultati;
    }
}