package com.company.dao;

import com.company.classes.Adres;
import com.company.classes.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface AdresDAO {

    //todo verwijder indien beter naar void ipv bool
    boolean save(Adres adres) throws SQLException;

    boolean update(Adres adres) throws SQLException;

    boolean delete(Adres adres) throws SQLException;

    Adres findByReiziger(Reiziger reiziger) throws SQLException;

    Adres findById(int id) throws SQLException;

    List<Adres> findAll() throws SQLException;

}
