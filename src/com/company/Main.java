package com.company;

import com.company.classes.Reiziger;
import com.company.dao.ReizigerDAO;
import com.company.dao.ReizigerDAOPsql;

import java.sql.*;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws SQLException {

//        String url = "jdbc:postgresql://localhost/ov-chipkaart";
//        Properties props = new Properties();
//        props.setProperty("user","postgres");
//        props.setProperty("password","admin");
//        Connection conn = DriverManager.getConnection(url, props);
//
//        PreparedStatement statement = conn.prepareStatement("SELECT * FROM reiziger");
//        ResultSet resultSet = statement.executeQuery();
//
//        System.out.println("Alle reizigers:");
//        while (resultSet.next()) {
//            String id = resultSet.getString("reiziger_id");
//            String voorletters = resultSet.getString("voorletters").replaceAll("", ".").substring(1);
//            String tussenvoegsel = resultSet.getString("tussenvoegsel") == null ?
//                    "" : resultSet.getString("tussenvoegsel");
//            String achternaam = resultSet.getString("achternaam");
//            String geboortedatum = resultSet.getString("geboortedatum" )== null ?
//                    "" : String.format("(%s)",resultSet.getString("geboortedatum"));
//
//            String output = String.format("#%s: %s %s %s %s\n", id, voorletters, tussenvoegsel, achternaam, geboortedatum).replaceAll("\\s+", " ");
//            System.out.println("\t" + output);
//        }
        ReizigerDAO reizigerDAO = new ReizigerDAOPsql();
        testReizigerDAO(reizigerDAO);
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
        System.out.println("\nAlle reizigers voor delete operatie:");
        System.out.println(rdao.findAll());
        rdao.delete(mark);
        System.out.println("\nNa de delete operatie:");
        System.out.println(rdao.findAll());

    }

}
