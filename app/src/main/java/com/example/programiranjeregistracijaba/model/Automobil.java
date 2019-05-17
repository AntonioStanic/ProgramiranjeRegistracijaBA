package com.example.programiranjeregistracijaba.model;

public class Automobil {
    public String ime;
    public String prezime;
    public String marka;
    public String broj_tablica;
    public String registriran_do;
    public String osoba_id;

    public Automobil(String ime, String prezime, String marka, String broj_tablica, String registriran_do, String osoba_id) {
        this.ime = ime;
        this.prezime = prezime;
        this.marka = marka;
        this.broj_tablica = broj_tablica;
        this.registriran_do = registriran_do;
        this.osoba_id = osoba_id;
    }

    public Automobil() {
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getBroj_tablica() {
        return broj_tablica;
    }

    public void setBroj_tablica(String broj_tablica) {
        this.broj_tablica = broj_tablica;
    }

    public String getRegistriran_do() {
        return registriran_do;
    }

    public void setRegistriran_do(String registriran_do) {
        this.registriran_do = registriran_do;
    }

    public String getOsoba_id(){
        return osoba_id;
    }
    public void setOsoba_id(String osooba_id){
        this.osoba_id = osooba_id;}
}
