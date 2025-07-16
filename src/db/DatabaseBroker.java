package db;
import domain.OpstaKlasa;
import domain.Radnik;
import domain.StavkaRezervacije;
import domain.TeniskiKlub;
import domain.Teren;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class DatabaseBroker {
    private Connection connection;

    public void connect() throws SQLException {
        try {
            String url = "jdbc:mysql://localhost:3306/aplikacija";
            String user = "root";
            String password = "root";
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);

        } catch (SQLException ex) {
            System.out.println("Greska! Konekcija sa bazom nije uspostavljena!\n" + ex.getMessage());
            ex.printStackTrace();
            throw ex;
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
    public Radnik prijavaRadnika(Radnik radnik) throws SQLException{
        try {
            String upit = """
                          SELECT * FROM radnik r
                          JOIN teniski_klub k ON (r.teniski_klub = k.id_klub)
                          WHERE r.korisnicko_ime = ? AND r.sifra = ?""";
            PreparedStatement st = connection.prepareStatement(upit);
            st.setString(1, radnik.getKorisnickoIme());
            st.setString(2, radnik.getSifra());
            
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                radnik.setId(rs.getLong("id"));
                radnik.setIme(rs.getString("ime"));
                radnik.setPrezime(rs.getString("prezime"));
                TeniskiKlub klub = new TeniskiKlub(rs.getLong("id_klub"), rs.getString("naziv_kluba"), 
                        rs.getString("adresa"), rs.getString("mail_kluba"));
                radnik.setTeniskiKlub(klub);
                System.out.println("Uspesno pronadjen radnik");
                return radnik;
            }
            throw new SQLException("radnik ne postoji u bazi!");
        } catch (SQLException e) {
            System.out.println("Greska! Neuspesno prijavljivanje na sistem, " + e.getMessage());
            throw e;
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
            connection.commit();
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
            connection.commit();
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
            connection.commit();
            return true;
        } catch (SQLException ex) {
            System.out.println("Greska prilikom brisanja sloga!");
            ex.printStackTrace();
            return false;
        }
    }
    
    // metoda ucitava koje su satnice za izabrani teren na formi zauzete da ne bi prikazalo kao dostupne
    public List<List<LocalTime>> ucitajSatnice(Teren teren, LocalDate datum) throws SQLException{
        List<List<LocalTime>> satnice = new ArrayList<>();
        try {
            String upit = "SELECT vreme_od, vreme_do FROM stavka_rezervacije WHERE datum = ? AND id_teren = ? ORDER BY vreme_od";
            PreparedStatement st = connection.prepareStatement(upit);
            st.setDate(1, Date.valueOf(datum));
            st.setLong(2, teren.getIdTeren());
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                List<LocalTime> pocetakKraj = new ArrayList<>();
                pocetakKraj.add(rs.getTime("vreme_od").toLocalTime());
                pocetakKraj.add(rs.getTime("vreme_do").toLocalTime());
                satnice.add(pocetakKraj);
            }
            return satnice;
        } catch (SQLException ex) {
            throw ex;
        }
    }

    public boolean unesiStavkeRezervacije(List<StavkaRezervacije> stavke) throws SQLException{
        try {
            boolean obrisano = obrisi(new StavkaRezervacije(stavke.get(0).getRezervacija()));
            if(obrisano){
                double ukupnaCenaRezervacije = 0;
                String upit = "INSERT INTO stavka_rezervacije (id_rezervacija, rb, vreme_od, vreme_do, datum, iznos, id_teren) VALUES (?,?,?,?,?,?,?)";
                PreparedStatement st = connection.prepareStatement(upit);
                for (StavkaRezervacije stavkaRezervacije : stavke) {
                    st.setLong(1, stavkaRezervacije.getRezervacija().getIdRezervacija());
                    st.setLong(2, stavkaRezervacije.getRb());
                    st.setTime(3, Time.valueOf(stavkaRezervacije.getVremeOd()));
                    st.setTime(4, Time.valueOf(stavkaRezervacije.getVremeDo()));
                    st.setDate(5, Date.valueOf(stavkaRezervacije.getDatum()));
                    st.setDouble(6, stavkaRezervacije.getIznos());
                    st.setInt(7, stavkaRezervacije.getTeren().getIdTeren());
                    ukupnaCenaRezervacije += stavkaRezervacije.getIznos();
                    st.addBatch();
                }
                st.executeBatch();
                connection.commit();
                return true;
            }
            return false;
            
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw e;
            }
            throw ex;
        }
    }

    public List<String> vratiPredlogeZaEmail(String input) throws SQLException{
        List<String> emails = new ArrayList<>();
        String query = "SELECT mail FROM korisnik WHERE mail LIKE ? LIMIT 10";

        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, input + "%");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                emails.add(rs.getString("mail"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return emails;
    }
}
