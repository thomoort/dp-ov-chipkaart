package com.company.dao;

import com.company.classes.Reiziger;
import org.checkerframework.checker.units.qual.A;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ReizigerDAOPsql implements ReizigerDAO {

    Connection conn;

    /**
     * Bij het aanroepen van de constructer zonder parameter, wordt de connectie hier aangemaakt
     * @throws SQLException
     */
    public ReizigerDAOPsql() throws SQLException {
        String url = "jdbc:postgresql://localhost/ov-chipkaart";
        Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","admin");

        conn = DriverManager.getConnection(url, props);
    }

    /**
     * Als er een Connection wordt meegegeven, dan wordt die gebruikt.
     * @param conn
     */
    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Reiziger reiziger) throws SQLException {
        PreparedStatement ps = null;
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
        while (rs.next()) {
            Reiziger reizigerObj = createReiziger(rs);
            return reizigerObj;
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

        return reiziger;
    }
}
