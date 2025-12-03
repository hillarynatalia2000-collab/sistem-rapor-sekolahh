package com.example.si_swa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import java.util.Date;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Admin extends Person implements ClassroomActions {
    @Column(name = "id_admin", unique = true)
    private String idAdmin;

    public Admin() {
        super();
    }

    public Admin(String n, Date t, String a, String nt, String u, String p, String e, String role, String idAdmin) {
        super(n, t, a, nt, u, p, e, role);
        this.idAdmin = idAdmin;
    }

    public void setIdAdmin(String idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getIdAdmin() {
        return idAdmin;
    }

    @Override
    public void mengelolaData(Object data) {
        // Implementasi untuk mengelola data admin
    }

    @Override
    public String membuatLaporan() {
        return "Laporan Admin";
    }

    @Override
    public void mengupdateNilaiRaport(double nilai) {
        // Admin tidak mengupdate nilai raport secara langsung
        // Method ini bisa digunakan untuk validasi atau logging
    }

    @Override
    public double menghitungNilaiRataRata() {
        // Admin tidak menghitung nilai rata-rata
        // Return 0.0 sebagai default
        return 0.0;
    }

    @Override
    public boolean validasiLogin(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        return username.equals(this.getUsername()) && this.checkPassword(password);
    }

    public void konfirmasiPendaftaranSiswa(Siswa siswa, boolean status) {
        if (siswa != null) {
            // Implementasi konfirmasi pendaftaran siswa
            // Bisa digunakan untuk mengaktifkan/menonaktifkan siswa
        }
    }

    public void konfirmasiPendaftaranWaliKelas(WaliKelas waliKelas, boolean status) {
        if (waliKelas != null) {
            // Implementasi konfirmasi pendaftaran wali kelas
            // Bisa digunakan untuk mengaktifkan/menonaktifkan wali kelas
        }
    }
}

