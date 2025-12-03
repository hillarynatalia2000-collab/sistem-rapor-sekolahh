package com.example.si_swa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Transient;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class WaliKelas extends Person implements ClassroomActions {
    @Column(name = "id_wali_kelas")
    private Integer idWaliKelas;

    @Column(name = "nama_kelas_walian")
    private String namaKelasWalian;

    @Column(name = "jumlah_siswa_walian")
    private int jumlahSiswaWalian;

    @Column(name = "nip", unique = true)
    private String nip;

    @Column(name = "status_konfirmasi")
    private Boolean statusKonfirmasi;

    @Transient
    private List<Siswa> daftarSiswa;

    public WaliKelas() {
        super();
        this.daftarSiswa = new ArrayList<>();
        this.statusKonfirmasi = Boolean.FALSE;
        this.idWaliKelas = 0; // Default value untuk menghindari null
        this.setRole("walikelas");
    }

    public WaliKelas(String n, Date t, String a, String nt, String nkw, int jsw, String nip, Integer idWaliKelas, Boolean statusKonfirmasi) {
        super(n, t, a, nt, null, null, null, "walikelas");
        this.namaKelasWalian = nkw;
        this.jumlahSiswaWalian = jsw;
        this.nip = nip;
        this.idWaliKelas = idWaliKelas;
        this.statusKonfirmasi = statusKonfirmasi != null ? statusKonfirmasi : Boolean.FALSE;
        this.daftarSiswa = new ArrayList<>();
    }

    public void setNama(String n) {
        super.setNama(n);
    }

    public void setTanggalLahir(Date t) {
        super.setTanggalLahir(t);
    }

    public void setNomorTelepon(String nt) {
        super.setNomorTelepon(nt);
    }

    public void setNamaKelasWalian(String nkw) {
        this.namaKelasWalian = nkw;
    }

    public void setJumlahSiswaWalian(int jsw) {
        this.jumlahSiswaWalian = jsw;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public void setIdWaliKelas(Integer idWaliKelas) {
        this.idWaliKelas = idWaliKelas;
    }

    public void setStatusKonfirmasi(Boolean statusKonfirmasi) {
        this.statusKonfirmasi = statusKonfirmasi != null ? statusKonfirmasi : Boolean.FALSE;
    }

    public String getNama() {
        return super.getNama();
    }

    public Date getTanggalLahir() {
        return super.getTanggalLahir();
    }

    public String getNomorTelepon() {
        return super.getNomorTelepon();
    }

    public String getNamaKelasWalian() {
        return namaKelasWalian;
    }

    public int getJumlahSiswaWalian() {
        return jumlahSiswaWalian;
    }

    public String getNip() {
        return nip;
    }

    public Integer getIdWaliKelas() {
        return idWaliKelas;
    }

    public Boolean getStatusKonfirmasi() {
        return statusKonfirmasi != null ? statusKonfirmasi : Boolean.FALSE;
    }

    public void mendaftarSiswa(Siswa siswa) {
        if (siswa != null && daftarSiswa != null) {
            if (!daftarSiswa.contains(siswa)) {
                daftarSiswa.add(siswa);
                this.jumlahSiswaWalian = daftarSiswa.size();
            }
        }
    }

    public void membuatNilai(double nilai) {
        // Wali kelas membuat nilai untuk siswa
        // Implementasi bisa dikembangkan lebih lanjut
    }

    public void editProfil(String newName, String newClass, double newScore) {
        this.setNama(newName);
        this.namaKelasWalian = newClass;
        // newScore bisa digunakan untuk validasi atau logging
    }

    public String cekProfil() {
        StringBuilder profil = new StringBuilder();
        profil.append("PROFIL WALI KELAS\n");
        profil.append("================\n");
        profil.append("Nama: ").append(this.getNama()).append("\n");
        profil.append("NIP: ").append(this.nip != null ? this.nip : "Belum diisi").append("\n");
        profil.append("Kelas Walian: ").append(this.namaKelasWalian).append("\n");
        profil.append("Jumlah Siswa: ").append(this.jumlahSiswaWalian).append("\n");
        profil.append("Status Konfirmasi: ").append(Boolean.TRUE.equals(statusKonfirmasi) ? "Terkonfirmasi" : "Belum Terkonfirmasi").append("\n");
        return profil.toString();
    }

    public List<Siswa> cekDataSiswa() {
        if (daftarSiswa == null) {
            daftarSiswa = new ArrayList<>();
        }
        return daftarSiswa;
    }

    public double menghitungNilaiRataRataKelas() {
        if (daftarSiswa == null || daftarSiswa.isEmpty()) {
            return 0.0;
        }
        double totalNilai = 0.0;
        int count = 0;
        for (Siswa siswa : daftarSiswa) {
            if (siswa != null && siswa.getNilai() != null) {
                totalNilai += siswa.getNilai();
                count++;
            }
        }
        return count > 0 ? totalNilai / count : 0.0;
    }

    @Override
    public String toString() {
        return "WaliKelas{" +
                "idWaliKelas=" + idWaliKelas +
                ", nama='" + this.getNama() + '\'' +
                ", nip='" + nip + '\'' +
                ", namaKelasWalian='" + namaKelasWalian + '\'' +
                ", jumlahSiswaWalian=" + jumlahSiswaWalian +
                ", statusKonfirmasi=" + statusKonfirmasi +
                '}';
    }

    @Override
    public void mengelolaData(Object data) {
        // Implementasi untuk mengelola data wali kelas
        if (data instanceof Siswa) {
            mendaftarSiswa((Siswa) data);
        }
    }

    @Override
    public String membuatLaporan() {
        StringBuilder laporan = new StringBuilder();
        laporan.append("LAPORAN WALI KELAS\n");
        laporan.append("==================\n");
        laporan.append("Nama: ").append(this.getNama()).append("\n");
        laporan.append("Kelas: ").append(this.namaKelasWalian).append("\n");
        laporan.append("Jumlah Siswa: ").append(this.jumlahSiswaWalian).append("\n");
        laporan.append("Nilai Rata-rata Kelas: ").append(menghitungNilaiRataRataKelas()).append("\n");
        return laporan.toString();
    }

    @Override
    public void mengupdateNilaiRaport(double nilai) {
        // Wali kelas tidak mengupdate nilai raport secara langsung
        // Method ini bisa digunakan untuk validasi atau logging
        membuatNilai(nilai);
    }

    @Override
    public double menghitungNilaiRataRata() {
        return menghitungNilaiRataRataKelas();
    }

    @Override
    public boolean validasiLogin(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        return username.equals(this.getUsername()) && this.checkPassword(password);
    }
}