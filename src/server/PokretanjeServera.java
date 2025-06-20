/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marko
 */
public class PokretanjeServera extends Thread{
    FrmServer s;
    ServerSocket ss = null;
    
    
    public PokretanjeServera(FrmServer s){
        this.s = s;
    }
    
    @Override
    public void run() {
        ss = null;
        try {
            ss = new ServerSocket(9000);
            s.pokrenut();
            while(true){
                Socket socket = ss.accept();
                System.out.println("Klijent se povezao");
                s.prijavljenKorisnik();
                //ovde ce biti obrada podataka
                ObradaZahteva o = new ObradaZahteva(socket, s);
                o.start();
            }
        } catch (IOException ex) {
            s.zaustavljen();
//            Logger.getLogger(PokretanjeServera.class.getName()).log(Level.SEVERE, null, ex);}
        }
//        }finally {
//            if (ss != null && !ss.isClosed()) {
//                try {
//                    ss.close();
//                    s.nijePokrenut();
//                    s.nemaKorisnika();
//                } catch (IOException e) {
//                    Logger.getLogger(PokretanjeServera.class.getName()).log(Level.SEVERE, "Greška pri zatvaranju server socket-a", e);
//                }
//            }
//        }
    }
    public void stopServer(){
        if(ss != null){
            try {
                ss.close();
                ss = null;
            } catch (IOException ex) {
                System.out.println("Neuspesno zatvaranje porta");
            }
        }
    }
    
}
