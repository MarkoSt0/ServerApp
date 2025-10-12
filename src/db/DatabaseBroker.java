package db;

import domain.OpstaKlasa;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import properties.DBConfigReader;


public class DatabaseBroker {
    private Connection connection;

    public void connect() throws SQLException {
        try {
            Properties props = DBConfigReader.loadProperties();
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);

        } catch (SQLException ex) {
            System.out.println("Greska! Konekcija sa bazom nije uspostavljena!\n" + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        } catch (IOException ex) {
            Logger.getLogger(DatabaseBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disconnect() throws SQLException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.out.println("Greska! Konekcija sa bazom nije uspesno raskinuta!\n" + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
    }
    
    public void commit() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.commit();
        }
    }

    public void rollback() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.rollback();
        }
    }
    
    
    
    public List<OpstaKlasa> ucitajIzBaze(OpstaKlasa opsti) throws SQLException {
        List<OpstaKlasa> lista = new ArrayList<>();
        try {
            String upit = opsti.vratiUpitZaUcitavanje();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(upit);
            while(rs.next()){
                OpstaKlasa novaInstanca = opsti.napraviInstancu();
                novaInstanca.postaviVrednosti(rs);
                lista.add(novaInstanca);
            }
            return lista;
        } catch (SQLException ex) {
            System.out.println("Greska! Neuspesno ucitavanje objekata iz baze!"  + ex.getMessage());
            throw ex;
        }
    }
    
    public Object kreiraj(OpstaKlasa opsti) throws SQLException {
        String upit = opsti.vratiUpitZaKreiranje();
        try (PreparedStatement st = connection.prepareStatement(upit, Statement.RETURN_GENERATED_KEYS)) {
            st.executeUpdate();
            System.out.println("Objekat je uspesno ucitan u bazu!");

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    opsti.postaviGenerisaniKljuc(rs.getLong(1));
                    return opsti;
                }
            }
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                System.out.println("Objekat vec postoji!");
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Objekat nije uspesno unesen u bazu! " + e.getMessage());
            throw new SQLException("Greška pri ubacivanju objekta u bazu", e);
        }
    }
    
    public Object pretrazi(OpstaKlasa opsti) throws SQLException{
        try {
            String query = opsti.vratiUpitZaPretragu();
            PreparedStatement st = connection.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                opsti.postaviVrednosti(rs);
                return opsti;
            }
            return null;
        } catch (SQLException ex) {
            System.out.println("Greska!" + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
    
    public boolean promeni(OpstaKlasa opsti) throws SQLException{
        try {
            String query = opsti.vratiUpitZaIzmenu();
            PreparedStatement st = connection.prepareStatement(query);
            st.execute();
            return true;
        } catch (SQLException ex) {
            System.out.println("Greska prilikom izmene mesta!");
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean obrisi(OpstaKlasa opsti) throws SQLException{
        try {
            String query = opsti.vratiUpitZaBrisanje();
            Statement st = connection.createStatement();
            st.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            System.out.println("Greska prilikom brisanja sloga!");
            ex.printStackTrace();
            return false;
        }
    }
    
    // dodatna sistemska operacija - ucitava email-ove korisnika i vraca satnice dostupnih terena
    public List<Object> vratiPredloge(OpstaKlasa ok, Object param) throws SQLException {
        List<Object> rezultat = new ArrayList<>();
        String upit = ok.vratiUpitZaSpecijalnuPretragu(ok,param);
        try (PreparedStatement ps = connection.prepareStatement(upit);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rezultat.add(ok.vratiObjekatIzRs(rs));
            }
        }
        return rezultat;
    }
}
