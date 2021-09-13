package com.company.dao;

import com.company.classes.Adres;
import com.company.classes.Reiziger;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {

    private Connection conn;
    private ReizigerDAO rdao;

    public AdresDAOPsql(Connection conn) {
        this.conn = conn;
        this.rdao = new ReizigerDAOPsql(conn, this);
    }

    //Snelle oplossing om van beide DAO's elkaar te kunnen aanroepen zonder oneidig nieuwe instanties van elkaar te maken
    public AdresDAOPsql(Connection conn, ReizigerDAOPsql rdao) {
        this.conn = conn;
        this.rdao = rdao;
    }

    @Override
    public boolean save(Adres adres) throws SQLException {
        if (!checkReizigerBestaat(adres.getReizigerId())) {
            return false;
        }

        PreparedStatement ps;
        if (adres.getId() > 0) {
            ps = conn.prepareStatement("INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setInt(1, adres.getId());
            ps.setString(2, adres.getPostcode());
            ps.setString(3, adres.getHuisnummer());
            ps.setString(4, adres.getStraat());
            ps.setString(5, adres.getWoonplaats());
            ps.setInt(6, adres.getReizigerId());
        } else {
            ps = conn.prepareStatement("INSERT INTO adres (postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, adres.getPostcode());
            ps.setString(2, adres.getHuisnummer());
            ps.setString(3, adres.getStraat());
            ps.setString(4, adres.getWoonplaats());
            ps.setInt(5, adres.getReizigerId());
        }

        try {
            int rs = ps.executeUpdate();
            rdao.setReizigerAdres(adres);

            return rs > 0;
        } catch (PSQLException e) {
            System.out.println("\n ERROR: Adres met ID bestaat, adres niet opgeslagen");
        }

        return false;
    }

    @Override
    public boolean update(Adres adres) throws SQLException {
        if (!checkReizigerBestaat(adres.getReizigerId())) {
            return false;
        }

        PreparedStatement ps = conn.prepareStatement("UPDATE adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? WHERE adres_id = ?");
        ps.setString(1, adres.getPostcode());
        ps.setString(2, adres.getHuisnummer());
        ps.setString(3, adres.getStraat());
        ps.setString(4, adres.getWoonplaats());
        ps.setInt(5, adres.getReizigerId());
        ps.setInt(6, adres.getId());

        int rs = ps.executeUpdate();
        rdao.setReizigerAdres(adres);

        return rs > 0;
    }

    @Override
    public boolean delete(Adres adres) throws SQLException {
        if (!checkReizigerBestaat(adres.getReizigerId())) {
            return false;
        }

        PreparedStatement ps = conn.prepareStatement("DELETE FROM adres WHERE adres_id = ?");
        ps.setInt(1, adres.getId());

        int rs = ps.executeUpdate();
        rdao.setReizigerAdres(adres);

        return rs > 0;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM adres WHERE reiziger_id = ?");
        ps.setInt(1, reiziger.getId());

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return createAdres(rs);
        }
        return null;
    }

    @Override
    public Adres findById(int id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM adres WHERE adres_id = ?");
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return createAdres(rs);
        }
        return null;
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        List<Adres> adresList = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM adres");

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            adresList.add(createAdres(rs));
        }
        return adresList;
    }

    /**
     * Methode om een reiziger object te maken via de reizigerDAO.
     * Hiermee kunnen we het adres van de reiziger setten, updaten of verwwijderen.
     * @param reiziger_id van het adres.
     * @return Boolean true als reiziger bestaat, false als niet bestaat.
     */
    private boolean checkReizigerBestaat(int reiziger_id) throws SQLException {
        Reiziger reiziger = rdao.findById(reiziger_id);
        if (reiziger == null) {
            System.out.println("ERROR: Adres refereert naar ongeldig reiziger id: " + reiziger_id);
            return false;
        }
        return true;
    }

    private Adres createAdres(ResultSet rs) throws SQLException {
        return new Adres(rs.getInt("adres_id"), rs.getString("postcode"),
                rs.getString("huisnummer"), rs.getString("straat"),
                rs.getString("woonplaats"), rs.getInt("reiziger_id"));
    }
}
