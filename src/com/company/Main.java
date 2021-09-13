package com.company;

import com.company.classes.Adres;
import com.company.classes.Reiziger;
import com.company.dao.AdresDAO;
import com.company.dao.AdresDAOPsql;
import com.company.dao.ReizigerDAO;
import com.company.dao.ReizigerDAOPsql;
import org.checkerframework.checker.units.qual.A;

import java.sql.*;
import java.util.List;
import java.util.Properties;

public class Main {

    private static Connection conn;

    public static void main(String[] args) throws SQLException {
        setConnection();
        ReizigerDAO reizigerDAO = new ReizigerDAOPsql(getConnection());
        AdresDAO adresDAO = new AdresDAOPsql(getConnection());

        testReizigerDAO(reizigerDAO);
        testAdresDAO(adresDAO, reizigerDAO);
    }

    private static void setConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost/ov-chipkaart";
        Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","admin");

        conn = DriverManager.getConnection(url, props);
    }

    private static Connection getConnection() {
        if (conn == null) {
            System.out.println("De connectie bestaat niet, er is iets fout gegaan dus het programma sluit nu af.");
            System.exit(0);
        }
        return conn;
    }

    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
     * @throws SQLException
     */
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        Reiziger mark = new Reiziger(78, "M", "", "Rutte", java.sql.Date.valueOf(gbdatum));
        rdao.save(mark);
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers");

        //Update sietske's tussenvoegsel, wissel tussen 'van' en geen tussenvoegsel
        System.out.println("\n-----\n[Test] Update sietske's tussenvoegsel, wissel tussen 'van' en geen tussenvoegsel\n");
        Reiziger sietske_update = rdao.findById(sietske.getId());
        String tussen = sietske_update.getTussenvoegsel() == null ? "van" : null;
        System.out.println("Voor update:\t" + sietske_update);
        sietske_update.setTussenvoegsel(tussen);
        rdao.update(sietske_update);
        System.out.println("Na update:\t\t" + rdao.findById(sietske.getId()));

        //Zoek bij geboortedatum
        System.out.println("\n-----\n[Test] Zoek reizigers met geboortedatum 1981-03-14\n");
        List<Reiziger> reizigersGB= rdao.findByGbdatum(gbdatum);
        for (Reiziger r : reizigersGB) {
            System.out.println(r);
        }

        //Verwijder reiziger M. Rutte (78) van DB
        System.out.println("\n-----\n[Test] Verwijder M.Rutte (ID: 78) van het systeem");
        System.out.println("\nFindById 78 voor delete operatie:");
        System.out.println(rdao.findById(78));
        rdao.delete(mark);
        System.out.println("\nNa de delete operatie:");
        System.out.println(rdao.findById(78));

    }

    private static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao) throws SQLException {
        Reiziger reiziger = new Reiziger(99, "A", "B", "C", java.sql.Date.valueOf("1900-01-01"));
        rdao.save(reiziger);
        System.out.println("\n\n ----------------- [AdresDAO TEST] ---------------------");
        System.out.println("Testen van AdresDAO met reiziger:");
        System.out.println(reiziger);

        System.out.println("\n-----\n[Test] aanmaken en opslaan van adres:");
        Adres adres = new Adres(99, "0000AA", "0", "1st Street", "Pangea", reiziger.getId());
        adao.save(adres);
        System.out.println("Adres: " + adao.findById(99));
        System.out.println("Reiziger: " + rdao.findById(99));

        System.out.println("\n-----\n[Test] Updaten adres postcode van 0000AA naar 1111BB");
        adres.setPostcode("1111BB");
        adao.update(adres);
        System.out.println("Adres: " + adao.findById(99));
        System.out.println("Reiziger: " + rdao.findById(99));

        System.out.println("\n-----\n[Test] Vind alle adressen");
        System.out.println(adao.findAll());

        System.out.println("\n-----\n[Test] Vind adres via reiziger");
        System.out.println(adao.findByReiziger(reiziger) + ", adress van: " + reiziger.toStringNoAdres());

        System.out.println("\n-----\n[Test] Adres verwijderd, findById test & reiziger get adres");
        adao.delete(adres);
        System.out.println(adao.findById(adres.getId()));
        System.out.println(reiziger.getAdres());
        rdao.delete(reiziger);
    }

}
