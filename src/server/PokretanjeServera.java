/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Marko
 */
public class PokretanjeServera extends Thread{
    FrmServer s;
    ServerSocket ss = null;
    volatile boolean pokrenuto = true;
    
    
    public PokretanjeServera(FrmServer s){
        this.s = s;
    }
    
    @Override
    public void run() {
        ss = null;
        try {
            ss = new ServerSocket(9000);
            s.pokrenut();
            while(pokrenuto){
                try {
                    Socket socket = ss.accept();
                    System.out.println("Klijent se povezao");
                    s.prijavljenKorisnik();
                    //ovde ce biti obrada podataka
                    ObradaZahteva o = new ObradaZahteva(socket, s);
                    o.start();
                } catch (IOException e) {
                    if(!pokrenuto){
                        System.out.println("Greska pri prihvatanju konekcije");
                    }else{
                        System.out.println("Server socket je zatvoren!");
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Greska pri pokretanju servera");
            s.zaustavljen();
        }finally {
            if (ss != null && !ss.isClosed()) {
                try {
                    ss.close();
                } catch (IOException e) {
                    System.out.println("Greska pri zatvaranju ServerSocket-a" + e.getMessage());
                }
            }
            s.nijePokrenut();
            s.nemaKorisnika();
        }
    }
    public void stopServer(){
        pokrenuto = false;
        try {
            new Socket("localhost", 9000).close();
        } catch (IOException ex) {
            
        }
    }
    
    
}
