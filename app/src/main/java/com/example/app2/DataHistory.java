package com.example.app2;

public class DataHistory {

    String id, nama, harga, kategori, lamaPesan, biaya, tanggalBooking, jamBooking, pemesan;

    public DataHistory(){

    }
    public DataHistory(String id, String nama, String harga, String kategori, String lamaPesan, String biaya, String tanggalBooking, String jamBooking, String pemesan){
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.lamaPesan = lamaPesan;
        this.biaya = biaya;
        this.tanggalBooking = tanggalBooking;
        this.jamBooking = jamBooking;
        this.pemesan = pemesan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getLamaPesan() {
        return lamaPesan;
    }

    public void setLamaPesan(String lamaPesan) {
        this.lamaPesan = lamaPesan;
    }

    public String getBiaya() {
        return biaya;
    }

    public void setBiaya(String biaya) {
        this.biaya = biaya;
    }

    public String getTanggalBooking() {
        return tanggalBooking;
    }

    public void setTanggalBooking(String tanggalBooking) {
        this.tanggalBooking = tanggalBooking;
    }

    public String getJamBooking() {
        return jamBooking;
    }

    public void setJamBooking(String jamBooking) {
        this.jamBooking = jamBooking;
    }

    public String getPemesan() {
        return pemesan;
    }

    public void setPemesan(String pemesan) {
        this.pemesan = pemesan;
    }
}
