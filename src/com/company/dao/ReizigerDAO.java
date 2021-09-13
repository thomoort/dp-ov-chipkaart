package com.company.dao;

import com.company.classes.Adres;
import com.company.classes.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface ReizigerDAO {

    /**
     * In deze interface heb ik SQLExceptions deel gemaakt van de methodes,
     * aangezien we SQL in de implementatie gebruiken en dit veel try/catch bespaard.
     */

    boolean save(Reiziger reiziger) throws SQLException;

    boolean update(Reiziger reiziger) throws SQLException;

    boolean delete(Reiziger reiziger) throws SQLException;

    Reiziger findById(int id) throws SQLException;

    List<Reiziger> findByGbdatum(String datum) throws SQLException;

    List<Reiziger> findAll() throws SQLException;

    void setReizigerAdres(Adres adres) throws SQLException;

}
