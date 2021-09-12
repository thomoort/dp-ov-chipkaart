package com.company.classes;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Reiziger {

    private int id = 0;
    private String voorletters = null;
    private String achternaam = null;
    private String tussenvoegsel = null;
    private Date geboortedatum = null;

    public Reiziger() {

    }

    public Reiziger(int id, String voorletters, String achternaam) {
        this.id = id;
        this.voorletters = voorletters;
        this.achternaam = achternaam;
    }

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
    }

    public Reiziger(int id, String voorletters, String achternaam, Date geboortedatum) {
        this.id = id;
        this.voorletters = voorletters;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam,  Date geboortedatum) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    @Override
    public String toString() {
        String GBdatum = getGeboortedatum() == null ? "" : new SimpleDateFormat("dd/MM/yyyy").format(getGeboortedatum());
        String tussen = getTussenvoegsel() == null ? "" : getTussenvoegsel();

        return String.format("#%s: %s %s %s %s\n",
                getId(), getVoorletters(), tussen, getAchternaam(), GBdatum).replaceAll("\\s+", " ");
    }
}
