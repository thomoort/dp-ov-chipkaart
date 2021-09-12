package com.company;

import java.sql.*;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws SQLException {

        String url = "jdbc:postgresql://localhost/ov-chipkaart";
        Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","admin");
        Connection conn = DriverManager.getConnection(url, props);

        PreparedStatement statement = conn.prepareStatement("SELECT * FROM reiziger");
        ResultSet resultSet = statement.executeQuery();

        System.out.println("Alle reizigers:");
        while (resultSet.next()) {
            String id = resultSet.getString("reiziger_id");
            String voorletters = resultSet.getString("voorletters").replaceAll("", ".").substring(1);
            String tussenvoegsel = resultSet.getString("tussenvoegsel") == null ?
                    "" : resultSet.getString("tussenvoegsel");
            String achternaam = resultSet.getString("achternaam");
            String geboortedatum = resultSet.getString("geboortedatum" )== null ?
                    "" : String.format("(%s)",resultSet.getString("geboortedatum"));

            String output = String.format("#%s: %s %s %s %s\n", id, voorletters, tussenvoegsel, achternaam, geboortedatum).replaceAll("\\s+", " ");
            System.out.println("\t" + output);
        }
    }
}
