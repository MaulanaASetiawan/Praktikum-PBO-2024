package com.example.gui;

import javafx.beans.property.*;
public class Hewan {
    private final IntegerProperty id;
    private String nama;
    private String jenis;
    private String habitat;
    private String populasi;
    private String tanggal;
    private String image;


    public Hewan(int id, String nama,  String jenis, String habitat,  String populasi, String tanggal, String image) {
        this.id = new SimpleIntegerProperty(id);
        this.nama = nama;
        this.jenis = jenis;
        this.habitat = habitat;
        this.populasi = populasi;
        this.tanggal = tanggal;
        this.image = image;
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getPopulasi() {
        return populasi;
    }

    public void setPopulasi(String populasi) {
        this.populasi = populasi;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
