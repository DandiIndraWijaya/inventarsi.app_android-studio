package com.example.chatapps.model;


public class data_barang {

    //Deklarasi Variable
    private String NamaBarang;
    private String KodeBarang;
    private String Deskripsi;
    private String Peminjam;
    private String WaktuPinjam;
    private String TenggatWaktu;
    private String Gambar;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNamaBarang() {
        return NamaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.NamaBarang = namaBarang;
    }

    public String getKodeBarang() {
        return KodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.KodeBarang = kodeBarang;
    }

    public String getDeskripsi() {
        return Deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.Deskripsi = deskripsi;
    }

    public String getPeminjam() {
        return Peminjam;
    }

    public void setPeminjam(String peminjam) {
        this.Peminjam = peminjam;
    }

    public String getWaktuPinjam() {
        return WaktuPinjam;
    }

    public void setWaktuPinjam(String waktuPinjam) {
        this.WaktuPinjam = waktuPinjam;
    }

    public String getTenggatWaktu() {
        return TenggatWaktu;
    }

    public void setTenggatWaktu(String tenggatWaktu) {
        this.TenggatWaktu = tenggatWaktu;
    }

    public String getGambar() {
        return Gambar;
    }

    public void setGambar(String gambar) {
        this.Gambar = gambar;
    }

    //Membuat Konstuktor kosong untuk membaca data snapshot
    public data_barang(){
    }

    //Konstruktor dengan beberapa parameter, untuk mendapatkan Input Data dari User
    public data_barang(String Nama_Barang, String Kode_Barang, String Deskripsi, String Peminjam, String WaktuPinjam, String TenggatWaktu, String Gambar) {
        this.NamaBarang = Nama_Barang;
        this.KodeBarang = Kode_Barang;
        this.Deskripsi = Deskripsi;
        this.Peminjam = Peminjam;
        this.WaktuPinjam = WaktuPinjam;
        this.TenggatWaktu = TenggatWaktu;
        this.Gambar = Gambar;
    }
}
