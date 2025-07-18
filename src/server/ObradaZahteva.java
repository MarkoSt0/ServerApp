/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import communication.Operation;
import communication.Receiver;
import communication.Request;
import communication.Response;
import communication.Sender;
import db.DatabaseBroker;
import domain.OpstaKlasa;
import domain.Radnik;
import domain.StavkaRezervacije;
import domain.Teren;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marko
 */
public class ObradaZahteva extends Thread{
    Socket socket;
    FrmServer forma;
    boolean kraj = false;
    Receiver receiver;
    Sender sender;
    DatabaseBroker dbb;

    public ObradaZahteva(Socket s, FrmServer forma) {
        this.socket = s;
        this.forma = forma;
        dbb = new DatabaseBroker();
        try {
            dbb.connect();
        } catch (SQLException ex) {
            Logger.getLogger(PokretanjeServera.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    @Override
    public void run() {
        receiver = new Receiver(socket);
        sender = new Sender(socket);
        while(!kraj){
            try {
                System.out.println("Cekam zahtev...");
                Request request = (Request) receiver.receive();
                System.out.println("Zahtev primljen: " + request.getOperation());
                Response response = new Response();
                Operation operation = request.getOperation();
                if(null != operation)switch (operation) {
                    case LOGIN -> {
                        Radnik radnik = (Radnik) request.getArgument();
                        try {
                            Radnik r = dbb.prijavaRadnika(radnik);
                            response.setResult(r);
                        } catch (SQLException e) {
                            response.setEx(e);
                        }   
                        sender.send(response);
                    }
                    case KREIRAJ -> {
                        Object object = request.getArgument();
                        try {
                            Object result = dbb.kreiraj((OpstaKlasa)object);
                            response.setResult(result);
                        } catch (SQLException e) {
                            response.setEx(e);
                        }   
                        sender.send(response);
                    }
                    case UCITAJ -> {
                        OpstaKlasa opsti = (OpstaKlasa) request.getArgument();
                        try {
                            List<OpstaKlasa> lista = dbb.ucitajIzBaze(opsti);
                            response.setResult(lista);
                        } catch (SQLException e) {
                            response.setEx(e);
                        }       
                        sender.send(response);
                    }
                    case PRETRAZI -> {
                        OpstaKlasa opsti = (OpstaKlasa) request.getArgument();
                        try {
                            OpstaKlasa objekat = (OpstaKlasa) dbb.pretrazi(opsti);
                            response.setResult(objekat);
                        } catch (SQLException e) {
                            response.setEx(e);
                        }       
                        sender.send(response);
                    }
                    case PROMENI -> {
                        OpstaKlasa opsti = (OpstaKlasa) request.getArgument();
                        try {
                            boolean result = dbb.promeni(opsti);
                            response.setResult(result);
                        } catch (SQLException e) {
                            response.setEx(e);
                        }       
                        sender.send(response);
                    }
                    case OBRISI -> {
                        OpstaKlasa opsti = (OpstaKlasa) request.getArgument();
                        try {
                            boolean result = dbb.obrisi(opsti);
                            response.setResult(result);
                        } catch (SQLException e) {
                            response.setEx(e);
                        }       
                        sender.send(response);
                    }
                    case VRATI_PREDLOGE -> {
                        String input = (String) request.getArgument();
                        try {
                            List<String> emails = dbb.vratiPredlogeZaEmail(input);
                            response.setResult(emails);
//                            System.out.println(emails);
                        } catch (SQLException e) {
                            response.setEx(e);
                        }       
                        sender.send(response);
                    }case UCITAJ_SATNICE -> {
                        List<Object> input = (List<Object>) request.getArgument();
                        try {
                            Teren teren = (Teren) input.get(0);
                            LocalDate datum = (LocalDate) input.get(1);
                            List<List<LocalTime>> satnice = dbb.ucitajSatnice(teren, datum);
                            response.setResult(satnice);
                        } catch (SQLException e) {
                            response.setEx(e);
                        }       
                        sender.send(response);
                    }case UCITAJ_STAVKE -> {
                        List<StavkaRezervacije> stavke = (List<StavkaRezervacije>) request.getArgument();
                        try {
                            System.out.println("Sistem je primio zahtev i ulazi u izmenu stavki");
                            boolean result = dbb.izmeniStavkeZaRezervaciju((List<StavkaRezervacije>) stavke);
//                            boolean result = dbb.unesiStavkeRezervacije((List<StavkaRezervacije>) stavke); //kreiranje
                            response.setResult(result);
                        } catch (SQLException e) {
                            response.setEx(e);
                        }       
                        sender.send(response);
                    }
                }

            } catch (Exception ex) {
                System.out.println("Korisnik je napustio sistem");
                this.forma.nemaKorisnika();
                kraj = true;
                ex.printStackTrace();
            }
        }
    }
    
    
}
