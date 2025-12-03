package com.example.si_swa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.si_swa.entity.Raport;
import com.example.si_swa.entity.MataPelajaran;
import com.example.si_swa.entity.NilaiMataPelajaran;
import com.example.si_swa.repository.RaportRepository;
import com.example.si_swa.service.MataPelajaranService;
import com.example.si_swa.service.NilaiMataPelajaranService;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Service
public class RaportService {
    @Autowired
    private RaportRepository raportRepository;

    @Autowired
    private MataPelajaranService mataPelajaranService;

    @Autowired
    private NilaiMataPelajaranService nilaiMataPelajaranService;

    public List<Raport> getAll() {
        List<Raport> list = raportRepository.findAll();
        for (Raport r : list) {
            loadNilaiFromTable(r);
        }
        return list;
    }

    @Transactional
    public Raport add(Raport obj) {
        obj.setId(null);
        Raport saved = raportRepository.save(obj);
        if (obj.getNilai() != null && !obj.getNilai().isEmpty() && obj.getSiswaId() != null) {
            saveNilaiToTable(obj.getSiswaId().intValue(), obj.getNilai());
        }
        return saved;
    }

    public Raport getById(long id) {
        Raport r = raportRepository.findById(id).orElse(null);
        if (r != null) {
            loadNilaiFromTable(r);
        }
        return r;
    }

    @Transactional
    public Raport update(long id, Raport obj) {
        Raport existing = raportRepository.findById(id).orElse(null);
        if (existing == null) return null;
        existing.setSiswaId(obj.getSiswaId());
        existing.setWaliKelasId(obj.getWaliKelasId());
        existing.setNomorRapot(obj.getNomorRapot());
        existing.setTanggalDiterbitkan(obj.getTanggalDiterbitkan());
        existing.setCatatanWaliKelas(obj.getCatatanWaliKelas());
        if (obj.getNilai() != null && !obj.getNilai().isEmpty()) {
            for (java.util.Map.Entry<MataPelajaran, Integer> e : obj.getNilai().entrySet()) {
                existing.tambahNilai(e.getKey(), e.getValue());
            }
        }
        if (existing.getNilai() != null && !existing.getNilai().isEmpty()) {
            float sum = 0f; int count = 0;
            for (Integer v : existing.getNilai().values()) {
                if (v != null) { sum += v; count++; }
            }
            float avg = count > 0 ? sum / count : 0f;
            String status = avg >= 75f ? "Naik" : "Tidak Naik";
            existing.setStatusKenaikanKelas(status);
        } else {
            existing.setStatusKenaikanKelas(obj.getStatusKenaikanKelas());
        }
        Raport saved = raportRepository.save(existing);
        if (obj.getNilai() != null && !obj.getNilai().isEmpty() && obj.getSiswaId() != null) {
            saveNilaiToTable(obj.getSiswaId().intValue(), obj.getNilai());
        }
        return saved;
    }

    public void delete(long id) { raportRepository.deleteById(id); }

    public String generateNomorRapot(java.util.Date date) {
        java.util.Date d = date != null ? date : new java.util.Date();
        String ymd = new java.text.SimpleDateFormat("yyyyMMdd").format(d);
        int rnd = new java.security.SecureRandom().nextInt(9000) + 1000;
        return "RPT-" + ymd + "-" + rnd;
    }

    @Transactional
    public Raport createRaport(Long waliKelasId, Long siswaId, String nomorRapot, Date tanggalDiterbitkan, String catatanWaliKelas, String statusKenaikanKelas, java.util.Map<String, Integer> nilaiMap) {
        String nr = (nomorRapot != null && nomorRapot.trim().length() > 0) ? nomorRapot : generateNomorRapot(tanggalDiterbitkan);
        Raport r = new Raport(nr, tanggalDiterbitkan);
        r.setSiswaId(siswaId);
        r.setWaliKelasId(waliKelasId);
        r.setCatatanWaliKelas(catatanWaliKelas);
        
        if (nilaiMap != null && !nilaiMap.isEmpty()) {
            for (java.util.Map.Entry<String, Integer> e : nilaiMap.entrySet()) {
                String mapelId = e.getKey();
                Integer nilai = e.getValue();
                if (mapelId != null && nilai != null) {
                    MataPelajaran mp = mataPelajaranService.getById(mapelId);
                    if (mp != null) {
                        r.tambahNilai(mp, nilai);
                    }
                }
            }
        }
        
        float sum = 0f; int count = 0;
        if (r.getNilai() != null && !r.getNilai().isEmpty()) {
            for (Integer v : r.getNilai().values()) {
                if (v != null) { sum += v; count++; }
            }
        }
        float avg = count > 0 ? sum / count : 0f;
        String status = avg >= 75f ? "Naik" : "Tidak Naik";
        r.setStatusKenaikanKelas(status);
        Raport saved = raportRepository.save(r);
        
        if (r.getNilai() != null && !r.getNilai().isEmpty() && siswaId != null) {
            saveNilaiToTable(siswaId.intValue(), r.getNilai());
        }
        
        return saved;
    }

    private void saveNilaiToTable(int idSiswa, java.util.HashMap<MataPelajaran, Integer> nilaiMap) {
        nilaiMataPelajaranService.deleteBySiswaId(idSiswa);
        
        List<NilaiMataPelajaran> nilaiList = new ArrayList<>();
        for (java.util.Map.Entry<MataPelajaran, Integer> entry : nilaiMap.entrySet()) {
            MataPelajaran mp = entry.getKey();
            Integer nilai = entry.getValue();
            if (mp != null && mp.getIdMataPelajaran() != null && nilai != null) {
                NilaiMataPelajaran nmp = new NilaiMataPelajaran();
                nmp.setIdSiswa(idSiswa);
                nmp.setIdMataPelajaran(mp.getIdMataPelajaran());
                nmp.setNilai(nilai.floatValue());
                nilaiList.add(nmp);
            }
        }
        if (!nilaiList.isEmpty()) {
            nilaiMataPelajaranService.saveAll(nilaiList);
        }
    }

    public List<Raport> findBySiswa(Long siswaId) {
        List<Raport> list = raportRepository.findBySiswaId(siswaId);
        for (Raport r : list) {
            loadNilaiFromTable(r);
        }
        return list;
    }

    private void loadNilaiFromTable(Raport r) {
        if (r != null && r.getSiswaId() != null) {
            List<com.example.si_swa.entity.NilaiMataPelajaran> nilaiList = nilaiMataPelajaranService.getBySiswaId(r.getSiswaId().intValue());
            for (com.example.si_swa.entity.NilaiMataPelajaran nmp : nilaiList) {
                if (nmp.getIdMataPelajaran() != null) {
                    MataPelajaran mp = mataPelajaranService.getById(nmp.getIdMataPelajaran());
                    if (mp != null && nmp.getNilai() != null) {
                        r.tambahNilai(mp, nmp.getNilai().intValue());
                    }
                }
            }
        }
    }
}