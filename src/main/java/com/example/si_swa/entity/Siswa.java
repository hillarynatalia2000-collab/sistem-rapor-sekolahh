package com.example.si_swa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Transient;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Siswa extends Person implements ClassroomActions {
    @Column(name = "id_siswa")
    private Integer idSiswa;

    @Column(name = "nis", unique = true)
    private String nis;

    @Column(name = "kelas")
    private String kelas;

    @Column(name = "nilai")
    private Float nilai;

    @ManyToMany
    @JoinTable(
        name = "siswa_mata_pelajaran",
        joinColumns = @JoinColumn(name = "siswa_id"),
        inverseJoinColumns = @JoinColumn(name = "mata_pelajaran_id")
    )
    private List<MataPelajaran> mataPelajaran;

    @Column(name = "status_konfirmasi")
    private boolean statusKonfirmasi;

    @Column(name = "nilai_list")
    private String nilaiList;

    @Column(name = "nilai_mapel")
    private String nilaiMapel;

    @Column(name = "mapel_list")
    private String mapelList;

    public Siswa() {
        super();
        this.mataPelajaran = new ArrayList<>();
        this.statusKonfirmasi = false;
        this.idSiswa = 0; // Default value untuk menghindari null
        this.setRole("siswa");
    }

    public Siswa(String n, Date t, String a, String nt, String nis, String k, Float nilai, List<MataPelajaran> mataPelajaran, Integer idSiswa, boolean statusKonfirmasi) {
        super(n, t, a, nt, null, null, null, "siswa");
        this.nis = nis;
        this.kelas = k;
        this.nilai = nilai;
        this.mataPelajaran = mataPelajaran != null ? mataPelajaran : new ArrayList<>();
        this.idSiswa = idSiswa;
        this.statusKonfirmasi = statusKonfirmasi;
    }

    public void setIdSiswa(Integer idSiswa) {
        this.idSiswa = idSiswa;
    }

    public Integer getIdSiswa() {
        return idSiswa;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public Float getNilai() {
        return nilai;
    }

    public void setNilai(Float nilai) {
        this.nilai = nilai;
    }

    public List<MataPelajaran> getMataPelajaran() {
        if (mataPelajaran == null) {
            mataPelajaran = new ArrayList<>();
        }
        return mataPelajaran;
    }

    public void setMataPelajaran(List<MataPelajaran> mataPelajaran) {
        this.mataPelajaran = mataPelajaran != null ? mataPelajaran : new ArrayList<>();
    }

    public boolean getStatusKonfirmasi() {
        return statusKonfirmasi;
    }

    public void setStatusKonfirmasi(boolean statusKonfirmasi) {
        this.statusKonfirmasi = statusKonfirmasi;
    }

    @Transient
    public java.util.List<Long> getMapelIds() {
        java.util.List<Long> out = new java.util.ArrayList<>();
        String src = mapelList;
        if (src == null || src.trim().isEmpty()) return out;
        String[] parts = src.split(",");
        for (String p : parts) {
            try {
                Long v = Long.parseLong(p.trim());
                out.add(v);
            } catch (Exception ignored) {}
        }
        return out;
    }

    public void setMapelIds(java.util.List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            this.mapelList = "";
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Long id : ids) {
            if (id == null) continue;
            if (sb.length() > 0) sb.append(',');
            sb.append(id);
        }
        this.mapelList = sb.toString();
    }

    public String getMapelList() {
        return mapelList != null ? mapelList : "";
    }

    public void setMapelList(String mapelList) {
        this.mapelList = mapelList != null ? mapelList : "";
    }

    @jakarta.persistence.Transient
    public java.util.Map<Long, Float> getNilaiPerMapel() {
        java.util.Map<Long, Float> out = new java.util.HashMap<>();
        String src = nilaiMapel;
        if (src == null || src.trim().isEmpty()) return out;
        String[] parts = src.split(",");
        for (String p : parts) {
            String[] kv = p.split(":");
            if (kv.length != 2) continue;
            try {
                Long mid = Long.parseLong(kv[0].trim());
                Float val = Float.parseFloat(kv[1].trim());
                out.put(mid, val);
            } catch (Exception ignored) {}
        }
        return out;
    }

    public void setNilaiPerMapel(java.util.Map<Long, Float> map) {
        if (map == null || map.isEmpty()) { this.nilaiMapel = ""; return; }
        StringBuilder sb = new StringBuilder();
        for (java.util.Map.Entry<Long, Float> e : map.entrySet()) {
            Long k = e.getKey(); Float v = e.getValue();
            if (k == null || v == null) continue;
            if (sb.length() > 0) sb.append(',');
            sb.append(k).append(':').append(v);
        }
        this.nilaiMapel = sb.toString();
    }

    @Transient
    public Float getNilaiRataRata() {
        java.util.Map<Long, Float> map = getNilaiPerMapel();
        if (map != null && !map.isEmpty()) {
            float sum = 0f; int count = 0;
            for (Float v : map.values()) { if (v != null) { sum += v; count++; } }
            if (count > 0) return sum / count;
        }
        // Jika ada nilai langsung, gunakan nilai tersebut
        if (this.nilai != null) {
            return this.nilai;
        }
        return null;
    }


    public void editSiswa(String newName, String newClass, Float newScore) {
        this.setNama(newName);
        this.kelas = newClass;
        this.nilai = newScore;
    }

    public Float cekNilai() {
        return this.nilai;
    }

    public List<MataPelajaran> cekMataPelajaran() {
        return getMataPelajaran();
    }

    public String cetakRaport() {
        StringBuilder raport = new StringBuilder();
        raport.append("RAPORT SISWA\n");
        raport.append("============\n");
        raport.append("Nama: ").append(this.getNama()).append("\n");
        raport.append("NIS: ").append(this.nis).append("\n");
        raport.append("Kelas: ").append(this.kelas).append("\n");
        raport.append("Nilai: ").append(this.nilai != null ? this.nilai : "Belum ada").append("\n");
        raport.append("Mata Pelajaran:\n");
        if (mataPelajaran != null && !mataPelajaran.isEmpty()) {
            for (MataPelajaran mp : mataPelajaran) {
                raport.append("- ").append(mp.getNama()).append(" (").append(mp.getKode()).append(")\n");
            }
        } else {
            raport.append("- Belum ada mata pelajaran\n");
        }
        raport.append("Status Konfirmasi: ").append(statusKonfirmasi ? "Terkonfirmasi" : "Belum Terkonfirmasi").append("\n");
        return raport.toString();
    }

    public boolean validasiNIS(String nis) {
        if (nis == null || nis.trim().isEmpty()) {
            return false;
        }
        if (nis.length() < 4 || nis.length() > 20) {
            return false;
        }
        return nis.matches("^[0-9]+$");
    }

    public boolean validasiNomorTelepon(String tel) {
        if (tel == null || tel.trim().isEmpty()) {
            return false;
        }
        if (!tel.startsWith("0")) {
            return false;
        }
        int len = tel.length();
        return len >= 10 && len <= 14;
    }

    public boolean validasiRangeNilai(Float nilai) {
        if (nilai == null) {
            return false;
        }
        return nilai >= 0.0f && nilai <= 100.0f;
    }

    @Override
    public void mengelolaData(Object data) {}

    @Override
    public String membuatLaporan() {
        return "Laporan Siswa";
    }

    @Override
    public void mengupdateNilaiRaport(double nilai) {
        this.nilai = (float) nilai;
    }

    @Override
    public double menghitungNilaiRataRata() {
        Float rataRata = getNilaiRataRata();
        if (rataRata != null) {
            return rataRata.doubleValue();
        }
        return 0.0;
    }

    @Override
    public boolean validasiLogin(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        return username.equals(this.getUsername()) && this.checkPassword(password);
    }
}