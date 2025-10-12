/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package so;

import domain.Teren;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Marko
 */
public class SOPretraziSatnice extends ApstraktnaSO {
    private List<List<LocalTime>> rezultati;

    @Override
    protected void validacija(Object param) throws Exception {
        if(!(param instanceof List)) {
            throw new Exception("Neispravan objekat za pretragu.");
        }
    }

    @Override
    protected void operacija(Object param) throws Exception {
        List<Object> input = (List<Object>) param;
        List<Object> obj = dbb.vratiPredloge((Teren) input.get(0), (LocalDate) input.get(1));
        rezultati = obj.stream()
            .map(o -> (List<LocalTime>) o)
            .collect(Collectors.toList());
    }

    public List<List<LocalTime>> getRezultati() {
        return rezultati;
    }
}