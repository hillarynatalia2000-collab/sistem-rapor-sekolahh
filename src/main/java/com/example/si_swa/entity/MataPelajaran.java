package com.example.si_swa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.List;

@Entity
@jakarta.persistence.Table(name = "mata_pelajaran")
public class MataPelajaran {
    @Id
    @Column(name = "id_mata_pelajaran")
    private String idMataPelajaran;

    @Column(name = "nama")
    private String nama;

    @Column(name = "kode", unique = true)
    private String kode;

    @Column(name = "jumlah_jam")
    private int jumlahJam;

    @jakarta.persistence.Transient
    private List<Siswa> siswa;

    public MataPelajaran() {}

    public MataPelajaran(String n, String k, int j, String id) {
        this.nama = n;
        this.kode = k;
        this.jumlahJam = j;
        this.idMataPelajaran = id;
    }

    public String getIdMataPelajaran() {
        return idMataPelajaran;
    }

    public void setIdMataPelajaran(String id) {
        this.idMataPelajaran = id;
    }

    public void setNama(String n) {
        this.nama = n;
    }

    public void setKode(String k) {
        this.kode = k;
    }

    public void setJumlahJam(int j) {
        this.jumlahJam = j;
    }

    public String getNama() {
        return nama;
    }

    public String getKode() {
        return kode;
    }

    public int getJumlahJam() {
        return jumlahJam;
    }

    public List<Siswa> getSiswa() {
        return siswa;
    }

    public void setSiswa(List<Siswa> s) {
        this.siswa = s;
    }

    @Override
    public String toString() {
        return "MataPelajaran{" +
                "idMataPelajaran='" + idMataPelajaran + '\'' +
                ", nama='" + nama + '\'' +
                ", kode='" + kode + '\'' +
                ", jumlahJam=" + jumlahJam +
                '}';
    }
}