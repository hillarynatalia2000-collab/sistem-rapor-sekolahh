package com.example.si_swa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "nilai_mata_pelajaran")
public class NilaiMataPelajaran {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nilai_mata_pelajaran")
    private int idNilaiMataPelajaran;

    @Column(name = "id_siswa")
    private int idSiswa;

    @Column(name = "id_mata_pelajaran")
    private String idMataPelajaran;

    @Column(name = "nilai")
    private Float nilai;

    public NilaiMataPelajaran() {
    }

    public NilaiMataPelajaran(int idNilaiMataPelajaran, int idSiswa, String idMataPelajaran, Float nilai) {
        this.idNilaiMataPelajaran = idNilaiMataPelajaran;
        this.idSiswa = idSiswa;
        this.idMataPelajaran = idMataPelajaran;
        this.nilai = nilai;
    }

    public int getIdNilaiMataPelajaran() {
        return idNilaiMataPelajaran;
    }

    public void setIdNilaiMataPelajaran(int idNilaiMataPelajaran) {
        this.idNilaiMataPelajaran = idNilaiMataPelajaran;
    }

    public int getIdSiswa() {
        return idSiswa;
    }

    public void setIdSiswa(int idSiswa) {
        this.idSiswa = idSiswa;
    }

    public String getIdMataPelajaran() {
        return idMataPelajaran;
    }

    public void setIdMataPelajaran(String idMataPelajaran) {
        this.idMataPelajaran = idMataPelajaran;
    }

    public Float getNilai() {
        return nilai;
    }

    public void setNilai(Float nilai) {
        this.nilai = nilai;
    }

    @Override
    public String toString() {
        return "NilaiMataPelajaran{" +
                "idNilaiMataPelajaran=" + idNilaiMataPelajaran +
                ", idSiswa=" + idSiswa +
                ", idMataPelajaran=" + idMataPelajaran +
                ", nilai=" + nilai +
                '}';
    }
}

