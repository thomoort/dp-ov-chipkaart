package com.company.dao;

import com.company.classes.Adres;
import com.company.classes.Reiziger;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ReizigerDAOPsql implements ReizigerDAO {

    private Connection conn;
    private AdresDAO adao;

    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
        adao = new AdresDAOPsql(this.conn, this);
    }

    //Snelle oplossing om van beide DAO's elkaar te kunnen aanroepen zonder oneidig nieuwe instanties van elkaar te maken
    public ReizigerDAOPsql(Connection conn, AdresDAO adao) {
        this.conn = conn;
        this.adao = adao;
    }

    @Override
    public boolean save(Reiziger reiziger) throws SQLException {
        PreparedStatement ps;
        if (reiziger.getId() > 0) {
            ps  = conn.prepareStatement("INSERT INTO reiziger (reiziger_id, voorletters, achternaam, tussenvoegsel, geboortedatum) VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, reiziger.getId());
            ps.setString(2, reiziger.getVoorletters());
            ps.setString(3, reiziger.getAchternaam());
            ps.setString(4, reiziger.getTussenvoegsel());
            ps.setDate(5, reiziger.getGeboortedatum());
        } else {
            ps  = conn.prepareStatement("INSERT INTO reiziger (voorletters, achternaam, tussenvoegsel, geboortedatum) VALUES (?, ?, ?, ?)");
            ps.setString(1, reiziger.getVoorletters());
            ps.setString(2, reiziger.getAchternaam());
            ps.setString(3, reiziger.getTussenvoegsel());
            ps.setDate(4, reiziger.getGeboortedatum());
        }

        try {
            int rs = ps.executeUpdate();
            return rs > 0;
        } catch (PSQLException e) {
            System.out.println("\n ERROR: Reiziger met ID bestaat, reiziger niet opgeslagen");
        }
        return false;
    }

    @Override
    public boolean update(Reiziger reiziger) throws SQLException {
        PreparedStatement ps  = conn.prepareStatement("" +
                "UPDATE reiziger SET voorletters = ?, achternaam = ?, tussenvoegsel = ?, geboortedatum = ? WHERE reiziger_id = ?");
        ps.setString(1, reiziger.getVoorletters());
        ps.setString(2, reiziger.getAchternaam());
        ps.setString(3, reiziger.getTussenvoegsel());
        ps.setDate(4, reiziger.getGeboortedatum());
        ps.setInt(5, reiziger.getId());

        int rs = ps.executeUpdate();
        return rs > 0;
    }

    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE FROM reiziger WHERE reiziger_id = ?");
        ps.setInt(1, reiziger.getId());

        int rs = ps.executeUpdate();
        return rs > 0;
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM reiziger WHERE reiziger_id = ?");
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return createReiziger(rs);
        }
        return null;
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) throws SQLException {
        List<Reiziger> reizigers = new ArrayList<>();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM reiziger WHERE geboortedatum = ?");
        ps.setDate(1, Date.valueOf(datum));
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Reiziger reizigerObj = createReiziger(rs);
            reizigers.add(reizigerObj);
        }

        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        List<Reiziger> reizigers = new ArrayList<>();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM reiziger");
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            Reiziger reizigerObj = createReiziger(rs);
            reizigers.add(reizigerObj);
        }

        return reizigers;
    }

    public void setReizigerAdres(Adres adres) throws SQLException {
        Reiziger reiziger = findById(adres.getReizigerId());
        reiziger.setAdres(adres);
    }

    private Reiziger createReiziger(ResultSet rs) throws SQLException {
        Reiziger reiziger = new Reiziger();
        reiziger.setId(rs.getInt("reiziger_id"));
        reiziger.setVoorletters(rs.getString("voorletters"));
        reiziger.setAchternaam(rs.getString("achternaam"));

        String tussenvoegsel = rs.getString("tussenvoegsel");
        if (tussenvoegsel != null) {
            reiziger.setTussenvoegsel(tussenvoegsel);
        }

        Date geboortedatum = rs.getDate("geboortedatum");
        if (geboortedatum != null) {
            reiziger.setGeboortedatum(geboortedatum);
        }

        Adres adres = adao.findByReiziger(reiziger);
        if (adres != null) {
            reiziger.setAdres(adres);
        }

        return reiziger;
    }
}
