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
import domain.Korisnik;
import domain.Mesto;
import domain.Radnik;
import domain.Rezervacija;
import domain.Teren;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import so.SOPretraziEmailove;
import so.SOPretraziSatnice;
import so.korisnik.SOIzmeniKorisnika;
import so.korisnik.SOObrisiKorisnika;
import so.korisnik.SOPretraziKorisnika;
import so.korisnik.SOUcitajListuKorisnika;
import so.korisnik.SOUnesiKorisnika;
import so.licenca.SOUnesiLicencu;
import so.mesto.SOIzmeniMesto;
import so.mesto.SOObrisiMesto;
import so.mesto.SOPretraziMesto;
import so.mesto.SOUcitajListuMesta;
import so.mesto.SOUnesiMesto;
import so.radnik.SOIzmeniRadnika;
import so.radnik.SOObrisiRadnika;
import so.radnik.SOPretraziRadnika;
import so.radnik.SOUnesiRadnika;
import so.rezervacija.SOIzmeniRezervaciju;
import so.rezervacija.SOObrisiRezervaciju;
import so.rezervacija.SOPretraziRezervaciju;
import so.rezervacija.SOUcitajListuRezervacija;
import so.rezervacija.SOUnesiRezervaciju;
import so.teren.SOIzmeniTeren;
import so.teren.SOObrisiTeren;
import so.teren.SOPretraziTeren;
import so.teren.SOUcitajListuTerena;
import so.teren.SOUnesiTeren;
import so.tkl.SOUcitajTKL;
import so.tkl.SOUnesiTKL;

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
        while(!socket.isClosed()){
            try {
                Request request = (Request) receiver.receive();
                Response response = new Response();
                Operation operation = request.getOperation();
                Object object = request.getArgument();
                if(null != operation){
                    
                    switch (operation) {
                        case KREIRAJ_TKL -> {
                            try {
                                SOUnesiTKL so = new SOUnesiTKL();
                                so.izvrsi(object);
                                response.setResult(so.getRezultat());
                            } catch (SQLException e) {
                                response.setEx(e);
                            }   
                        }
                        case KREIRAJ_LICENCU -> {
                            try {
                                SOUnesiLicencu so = new SOUnesiLicencu();
                                so.izvrsi(object);
                                response.setResult(so.getRezultat());
                            } catch (SQLException e) {
                                response.setEx(e);
                            }   
                        }
                        case UCITAJ_TKL -> {
                            try {
                                SOUcitajTKL so = new SOUcitajTKL();
                                so.izvrsi(object);
                                response.setResult(so.getLista());
                            } catch (SQLException e) {
                                response.setEx(e);
                            }       
                        }
                        case VRATI_PREDLOGE -> {
                            try {
                                SOPretraziEmailove so = new SOPretraziEmailove();
                                so.izvrsi(object);
                                response.setResult(so.getRezultati());
                            } catch (SQLException e) {
                                response.setEx(e);
                            }       
                        }case UCITAJ_SATNICE -> {
                            try {
                                SOPretraziSatnice so = new SOPretraziSatnice();
                                so.izvrsi(object);
                                response.setResult(so.getRezultati());
                            } catch (SQLException e) {
                                response.setEx(e);
                            }       
                        }
                        case KREIRAJ_KORISNIKA -> {
                            response = kreirajKorisnika((Korisnik) object);
                        }case OBRISI_KORISNIKA -> {
                            response = obrisiKorisnika((Korisnik) object);
                        }case PRETRAZI_KORISNIKA -> {
                            response = pretraziKorisnika((Korisnik) object);
                        }case PROMENI_KORISNIKA -> {
                            response = izmeniKorisnika((Korisnik) object);
                        } case KREIRAJ_RADNIKA -> {
                            response = kreirajRadnika((Radnik) object);
                        }case OBRISI_RADNIKA -> {
                            response = obrisiRadnika((Radnik) object);
                        }case PRETRAZI_RADNIKA -> {
                            response = pretraziRadnika((Radnik) object);
                        }case PROMENI_RADNIKA -> {
                            response = izmeniRadnika((Radnik) object);
                        } case KREIRAJ_TEREN -> {
                            response = kreirajTeren((Teren) object);
                        }case OBRISI_TEREN -> {
                            response = obrisiTeren((Teren) object);
                        }case PRETRAZI_TEREN -> {
                            response = pretraziTeren((Teren) object);
                        }case PROMENI_TEREN -> {
                            response = izmeniTeren((Teren) object);
                        } case KREIRAJ_MESTO -> {
                            response = kreirajMesto((Mesto) object);
                        }case OBRISI_MESTO -> {
                            response = obrisiMesto((Mesto) object);
                        }case PRETRAZI_MESTO -> {
                            response = pretraziMesto((Mesto) object);
                        }case PROMENI_MESTO -> {
                            response = izmeniMesto((Mesto) object);
                        } case KREIRAJ_REZERVACIJU -> {
                            response = kreirajRezervaciju((Rezervacija) object);
                        }case OBRISI_REZERVACIJU -> {
                            response = obrisiRezervaciju((Rezervacija) object);
                        }case PRETRAZI_REZERVACIJU -> {
                            response = pretraziRezervaciju((Rezervacija) object);
                        }case PROMENI_REZERVACIJU -> {
                            response = izmeniRezervaciju((Rezervacija) object);
                        }case UCITAJ_LISTU_REZERVACIJA -> {
                            response = ucitajListuRezervacija((Rezervacija) object);
                        }case UCITAJ_LISTU_MESTA -> {
                            response = ucitajListuMesta((Mesto) object);
                        }case UCITAJ_LISTU_TERENA-> {
                            response = ucitajListuTerena((Teren) object);
                        }case UCITAJ_LISTU_KORISNIKA-> {
                            response = ucitajListuKorisnika((Korisnik) object);
                        }
                    }
                    sender.send(response);
                }
                

            } catch (Exception ex) {
                System.out.println("Korisnik je napustio sistem");
                this.forma.nemaKorisnika();
                try {
                    socket.close();
                } catch (IOException ex1) {
                    Logger.getLogger(ObradaZahteva.class.getName()).log(Level.SEVERE, null, ex1);
                }
                ex.printStackTrace();
            }
        }
    }
    
    private Response kreirajKorisnika(Korisnik k) {
        Response response = new Response();
        try {
            SOUnesiKorisnika so = new SOUnesiKorisnika();
            so.izvrsi(k);
            response.setResult(so.getRezultat());
        }  catch (Exception ex) {
            response.setEx(ex);
        }
        return response;
    }
    
    private Response obrisiKorisnika(Korisnik k) {
        Response response = new Response();
        try {
            SOObrisiKorisnika so = new SOObrisiKorisnika();
            so.izvrsi(k);
            response.setResult(so.getRezultat());
        } catch (Exception ex) {
            response.setEx(ex);
        }    
        return response;
    }
    
    private Response pretraziKorisnika(Korisnik k) {
        Response response = new Response();
        try {
            SOPretraziKorisnika so = new SOPretraziKorisnika();
            so.izvrsi(k);
            response.setResult(so.getRezultat());
        } catch (Exception ex) {
            response.setEx(ex);
        }    
        return response;
    }
    
    private Response izmeniKorisnika(Korisnik k) {
        Response response = new Response();
        try {
            SOIzmeniKorisnika so = new SOIzmeniKorisnika();
            so.izvrsi(k);
            response.setResult(so.getRezultat());
        } catch (Exception ex) {
            response.setEx(ex);
        }    
        return response;
    }
    
//    --------------------------------------------------------------------------
    
    private Response kreirajRadnika(Radnik r) {
        Response response = new Response();
        try {
            SOUnesiRadnika so = new SOUnesiRadnika();
            so.izvrsi(r);
            response.setResult(so.getRezultat());
        }  catch (Exception ex) {
            response.setEx(ex);
        }
        return response;
    }
    
    private Response obrisiRadnika(Radnik r) {
        Response response = new Response();
        try {
            SOObrisiRadnika so = new SOObrisiRadnika();
            so.izvrsi(r);
            response.setResult(so.getRezultat());
        } catch (Exception ex) {
            response.setEx(ex);
        }    
        return response;
    }
    
    private Response pretraziRadnika(Radnik r) {
        Response response = new Response();
        try {
            SOPretraziRadnika so = new SOPretraziRadnika();
            so.izvrsi(r);
            response.setResult(so.getRezultat());
        } catch (Exception ex) {
            response.setEx(ex);
        }    
        return response;
    }
    
    private Response izmeniRadnika(Radnik r) {
        Response response = new Response();
        try {
            SOIzmeniRadnika so = new SOIzmeniRadnika();
            so.izvrsi(r);
            response.setResult(so.getRezultat());
        } catch (Exception ex) {
            response.setEx(ex);
        }    
        return response;
    }
    
    //    --------------------------------------------------------------------------
    
    private Response kreirajTeren(Teren x) {
        Response response = new Response();
        try {
            SOUnesiTeren so = new SOUnesiTeren();
            so.izvrsi(x);
            response.setResult(so.getRezultat());
        }  catch (Exception ex) {
            response.setEx(ex);
        }
        return response;
    }
    
    private Response obrisiTeren(Teren x) {
        Response response = new Response();
        try {
            SOObrisiTeren so = new SOObrisiTeren();
            so.izvrsi(x);
            response.setResult(so.getRezultat());
        } catch (Exception ex) {
            response.setEx(ex);
        }    
        return response;
    }
    
    private Response pretraziTeren(Teren x) {
        Response response = new Response();
        try {
            SOPretraziTeren so = new SOPretraziTeren();
            so.izvrsi(x);
            response.setResult(so.getRezultat());
        } catch (Exception ex) {
            response.setEx(ex);
        }    
        return response;
    }
    
    private Response izmeniTeren(Teren x) {
        Response response = new Response();
        try {
            SOIzmeniTeren so = new SOIzmeniTeren();
            so.izvrsi(x);
            response.setResult(so.getRezultat());
        } catch (Exception ex) {
            response.setEx(ex);
        }    
        return response;
    }
    
    //    --------------------------------------------------------------------------
    
    private Response kreirajMesto(Mesto x) {
        Response response = new Response();
        try {
            SOUnesiMesto so = new SOUnesiMesto();
            so.izvrsi(x);
            response.setResult(so.getRezultat());
        }  catch (Exception ex) {
            response.setEx(ex);
        }
        return response;
    }
    
    private Response obrisiMesto(Mesto x) {
        Response response = new Response();
        try {
            SOObrisiMesto so = new SOObrisiMesto();
            so.izvrsi(x);
            response.setResult(so.getRezultat());
        } catch (Exception ex) {
            response.setEx(ex);
        }    
        return response;
    }
    
    private Response pretraziMesto(Mesto x) {
        Response response = new Response();
        try {
            SOPretraziMesto so = new SOPretraziMesto();
            so.izvrsi(x);
            response.setResult(so.getRezultat());
        } catch (Exception ex) {
            response.setEx(ex);
        }    
        return response;
    }
    
    private Response izmeniMesto(Mesto x) {
        Response response = new Response();
        try {
            SOIzmeniMesto so = new SOIzmeniMesto();
            so.izvrsi(x);
            response.setResult(so.getRezultat());
        } catch (Exception ex) {
            response.setEx(ex);
        }    
        return response;
    }
    
    //    --------------------------------------------------------------------------
    
    private Response kreirajRezervaciju(Rezervacija x) {
        Response response = new Response();
        try {
            SOUnesiRezervaciju so = new SOUnesiRezervaciju();
            so.izvrsi(x);
            response.setResult(so.getRezultat());
        }  catch (Exception ex) {
            response.setEx(ex);
        }
        return response;
    }
    
    private Response obrisiRezervaciju(Rezervacija x) {
        Response response = new Response();
        try {
            SOObrisiRezervaciju so = new SOObrisiRezervaciju();
            so.izvrsi(x);
            response.setResult(so.getRezultat());
        } catch (Exception ex) {
            response.setEx(ex);
        }    
        return response;
    }
    
    private Response pretraziRezervaciju(Rezervacija x) {
        Response response = new Response();
        try {
            SOPretraziRezervaciju so = new SOPretraziRezervaciju();
            so.izvrsi(x);
            response.setResult(so.getRezultat());
        } catch (Exception ex) {
            response.setEx(ex);
        }    
        return response;
    }
    
    private Response izmeniRezervaciju(Rezervacija x) {
        Response response = new Response();
        try {
            SOIzmeniRezervaciju so = new SOIzmeniRezervaciju();
            so.izvrsi(x);
            response.setResult(so.getRezultat());
        } catch (Exception ex) {
            response.setEx(ex);
        }    
        return response;
    }
    
    //    --------------------------------------------------------------------------
    
    private Response ucitajListuRezervacija(Rezervacija x) {
        Response response = new Response();
        try {
            SOUcitajListuRezervacija so = new SOUcitajListuRezervacija();
            so.izvrsi(x);
            response.setResult(so.getLista());
        } catch (Exception ex) {
            response.setEx(ex);
        }    
        return response;
    }
    
    private Response ucitajListuMesta(Mesto x) {
        Response response = new Response();
        try {
            SOUcitajListuMesta so = new SOUcitajListuMesta();
            so.izvrsi(x);
            response.setResult(so.getLista());
        } catch (Exception ex) {
            response.setEx(ex);
        }    
        return response;
    }
    
    private Response ucitajListuTerena(Teren x) {
        Response response = new Response();
        try {
            SOUcitajListuTerena so = new SOUcitajListuTerena();
            so.izvrsi(x);
            response.setResult(so.getLista());
        } catch (Exception ex) {
            response.setEx(ex);
        }    
        return response;
    }

    private Response ucitajListuKorisnika(Korisnik x) {
        Response response = new Response();
        try {
            SOUcitajListuKorisnika so = new SOUcitajListuKorisnika();
            so.izvrsi(x);
            response.setResult(so.getLista());
        } catch (Exception ex) {
            response.setEx(ex);
        }    
        return response;
    }
    
}
