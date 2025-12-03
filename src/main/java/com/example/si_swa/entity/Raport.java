package com.example.si_swa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.HashMap;
import jakarta.persistence.Transient;

@Entity
@jakarta.persistence.Table(name = "raport")
public class Raport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "siswa_id")
    private Long siswaId;

    @Column(name = "wali_kelas_id")
    private Long waliKelasId;

    @Column(name = "nomor_rapot")
    private String nomorRapot;

    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "tanggal_diterbitkan")
    private Date tanggalDiterbitkan;

    @Column(name = "catatan_wali_kelas")
    private String catatanWaliKelas;

    @Column(name = "status_kenaikan_kelas")
    private String statusKenaikanKelas;

    @Column(name = "nilai_mapel")
    private String nilaiMapel;

    @Transient
    private HashMap<MataPelajaran, Integer> nilai;

    public Raport() {
        this.nilai = new HashMap<>();
    }

    public Raport(String n, Date t) {
        this.nomorRapot = n;
        this.tanggalDiterbitkan = t;
        this.nilai = new HashMap<>();
    }

    // Backward compatibility - untuk database
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSiswaId() { return siswaId; }
    public void setSiswaId(Long siswaId) { this.siswaId = siswaId; }

    public Long getWaliKelasId() { return waliKelasId; }
    public void setWaliKelasId(Long waliKelasId) { this.waliKelasId = waliKelasId; }

    public String getNomorRapot() {
        return nomorRapot;
    }

    public void setNomorRapot(String n) {
        this.nomorRapot = n;
    }

    public Date getTanggalDiterbitkan() {
        return tanggalDiterbitkan;
    }

    public void setTanggalDiterbitkan(Date t) {
        this.tanggalDiterbitkan = t;
    }

    public HashMap<MataPelajaran, Integer> getNilai() {
        if (nilai == null) {
            nilai = new HashMap<>();
        }
        return nilai;
    }

    public void tambahNilai(MataPelajaran m, Integer n) {
        if (m != null && n != null) {
            if (nilai == null) {
                nilai = new HashMap<>();
            }
            nilai.put(m, n);
        }
    }

    public Integer getNilai(MataPelajaran m) {
        if (nilai == null || m == null) {
            return null;
        }
        return nilai.get(m);
    }

    public void setCatatanWaliKelas(String c) {
        this.catatanWaliKelas = c;
    }

    public String getCatatanWaliKelas() {
        return catatanWaliKelas;
    }

    public void setStatusKenaikanKelas(String s) {
        this.statusKenaikanKelas = s;
    }

    public String getStatusKenaikanKelas() {
        return statusKenaikanKelas;
    }

    public void cetakRapot() {
        System.out.println("========================================");
        System.out.println("            RAPORT SISWA");
        System.out.println("========================================");
        System.out.println("Nomor Raport: " + (nomorRapot != null ? nomorRapot : "-"));
        System.out.println("Tanggal Diterbitkan: " + (tanggalDiterbitkan != null ? new java.text.SimpleDateFormat("dd-MM-yyyy").format(tanggalDiterbitkan) : "-"));
        System.out.println("----------------------------------------");
        System.out.println("NILAI MATA PELAJARAN:");
        if (nilai != null && !nilai.isEmpty()) {
            for (java.util.Map.Entry<MataPelajaran, Integer> entry : nilai.entrySet()) {
                MataPelajaran mp = entry.getKey();
                Integer nilaiMapel = entry.getValue();
                if (mp != null) {
                    System.out.println("  - " + mp.getNama() + " (" + mp.getKode() + "): " + (nilaiMapel != null ? nilaiMapel : "-"));
    }
            }
        } else {
            System.out.println("  - Belum ada nilai");
        }
        System.out.println("----------------------------------------");
        System.out.println("Catatan Wali Kelas: " + (catatanWaliKelas != null && !catatanWaliKelas.trim().isEmpty() ? catatanWaliKelas : "-"));
        System.out.println("Status Kenaikan Kelas: " + (statusKenaikanKelas != null ? statusKenaikanKelas : "-"));
        System.out.println("========================================");
    }
}