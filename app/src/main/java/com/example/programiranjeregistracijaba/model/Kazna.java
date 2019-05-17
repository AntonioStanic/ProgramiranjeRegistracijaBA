package com.example.programiranjeregistracijaba.model;

public class Kazna {
    private String slika;
    private String broj_tablica;
    private String opis;
    private String iznos;



    public Kazna() {
    }

    public Kazna(String broj_tablica, String iznos, String opis,String slika) {
        this.slika = slika;
        this.broj_tablica = broj_tablica;
        this.opis = opis;
        this.iznos = iznos;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public String getBroj_tablica() {
        return broj_tablica;
    }

    public void setTablice(String broj_tablica) {
        this.broj_tablica = broj_tablica;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getIznos() {
        return iznos;
    }

    public void setIznos(String iznos) {
        this.iznos = iznos;
    }
}
