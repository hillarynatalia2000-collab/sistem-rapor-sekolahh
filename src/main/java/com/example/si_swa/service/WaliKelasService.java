package com.example.si_swa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.si_swa.entity.WaliKelas;
import com.example.si_swa.repository.WaliKelasRepository;
import com.example.si_swa.repository.PersonRepository;
import com.example.si_swa.entity.Person;
import com.example.si_swa.service.SiswaService;
import java.util.List;

@Service
public class WaliKelasService {
    @Autowired
    private WaliKelasRepository wkRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SiswaService siswaService;

    public List<WaliKelas> getAllWaliKelas() {
        List<Person> personList = personRepository.findAllByRoleWalikelas();
        List<WaliKelas> result = new java.util.ArrayList<>();
        
        for (Person p : personList) {
            if (p != null && "walikelas".equalsIgnoreCase(p.getRole())) {
                WaliKelas w = wkRepository.findById(p.getId()).orElse(null);
                if (w == null) {
                    w = new WaliKelas();
                    w.setId(p.getId());
                    w.setNama(p.getNama());
                    w.setTanggalLahir(p.getTanggalLahir());
                    w.setAlamat(p.getAlamat());
                    w.setNomorTelepon(p.getNomorTelepon());
                    w.setUsername(p.getUsername());
                    w.setPassword(p.getPassword());
                    w.setEmail(p.getEmail());
                    w.setRole("walikelas");
                    w.setStatusKonfirmasi(false);
                    w.setIdWaliKelas(0);
                } else {
                    if (w.getIdWaliKelas() == null) {
                        w.setIdWaliKelas(0);
                    }
                    if (w.getStatusKonfirmasi() == null) {
                        w.setStatusKonfirmasi(Boolean.FALSE);
                    }
                }
                result.add(w);
            }
        }
        
        return result;
    }

    public List<WaliKelas> getAllWaliKelasWithCounts() {
        List<WaliKelas> list = getAllWaliKelas();
        List<com.example.si_swa.entity.Siswa> siswaList = siswaService.getAllSiswa();
        for (WaliKelas wk : list) {
            String kelas = wk.getNamaKelasWalian();
            int count = 0;
            if (Boolean.TRUE.equals(wk.getStatusKonfirmasi()) 
                && kelas != null 
                && !kelas.trim().isEmpty() 
                && !kelas.trim().equalsIgnoreCase("Belum Ditentukan")) {
                for (com.example.si_swa.entity.Siswa s : siswaList) {
                    if (s != null 
                        && s.getKelas() != null 
                        && !s.getKelas().trim().isEmpty() 
                        && s.getKelas().trim().equals(kelas.trim()) 
                        && s.getStatusKonfirmasi()) {
                        count++;
                    }
                }
            }
            int prev = wk.getJumlahSiswaWalian();
            wk.setJumlahSiswaWalian(count);
            if (prev != count && wk.getId() != null) {
                try {
                wkRepository.save(wk);
                } catch (Exception e) {
                }
            }
        }
        return list;
    }

    public WaliKelas addWaliKelas(WaliKelas obj) {
        obj.setId(null);
        if (obj.getIdWaliKelas() == null) {
            obj.setIdWaliKelas(0); // Default value untuk wali kelas baru
        }
        return wkRepository.save(obj);
    }

    public WaliKelas addWaliKelasFromPerson(long personId, String namaKelasWalian) {
        Person p = personRepository.findById(personId).orElse(null);
        if (p == null) return null;
        if (getWaliKelasById(personId) != null) return null;
        if (siswaService.getSiswaById(personId) != null) return null;
        wkRepository.insertWaliKelasRow(p.getId(), namaKelasWalian, 0);
        WaliKelas w = new WaliKelas();
        w.setId(p.getId());
        w.setNama(p.getNama());
        w.setTanggalLahir(p.getTanggalLahir());
        w.setAlamat(p.getAlamat());
        w.setNomorTelepon(p.getNomorTelepon());
        w.setNamaKelasWalian(namaKelasWalian);
        w.setJumlahSiswaWalian(0);
        w.setUsername(p.getUsername());
        w.setPassword(p.getPassword());
        w.setEmail(p.getEmail());
        w.setRole("walikelas");
        w.setStatusKonfirmasi(false);
        w.setIdWaliKelas(0); // Default value untuk wali kelas yang belum dikonfirmasi
        return wkRepository.save(w);
    }

    public WaliKelas getWaliKelasById(long id) {
        WaliKelas w = wkRepository.findById(id).orElse(null);
        if (w != null && w.getIdWaliKelas() == null) {
            w.setIdWaliKelas(0);
        }
        return w;
    }

    public WaliKelas findByNomorTelepon(String tel) {
        WaliKelas w = wkRepository.findByNomorTelepon(tel);
        if (w != null && w.getIdWaliKelas() == null) {
            w.setIdWaliKelas(0);
        }
        return w;
    }

    public WaliKelas updateWaliKelas(long id, WaliKelas obj) {
        WaliKelas existing = wkRepository.findById(id).orElse(null);
        if (existing == null) return null;
        existing.setTanggalLahir(obj.getTanggalLahir());
        existing.setAlamat(obj.getAlamat());
        existing.setNomorTelepon(obj.getNomorTelepon());
        existing.setNamaKelasWalian(obj.getNamaKelasWalian());
        if (obj.getNip() != null && !obj.getNip().trim().isEmpty()) {
            existing.setNip(obj.getNip());
        }
        if (existing.getIdWaliKelas() == null) {
            existing.setIdWaliKelas(0);
        }
        return wkRepository.save(existing);
    }

    public void deleteWaliKelas(long id) { wkRepository.deleteWaliKelasRowById(id); }

    @org.springframework.transaction.annotation.Transactional
    public WaliKelas updateStatusKonfirmasi(long id, boolean status) {
        Person p = personRepository.findById(id).orElse(null);
        if (p == null || !"walikelas".equalsIgnoreCase(p.getRole())) {
            return null;
        }
        
        WaliKelas w = wkRepository.findById(id).orElse(null);
        
        if (status) {
            if (w == null) {
                String kelas = "Belum Ditentukan";
                try {
                    wkRepository.insertWaliKelasRow(p.getId(), kelas, 0);
                    wkRepository.flush();
                    w = wkRepository.findById(id).orElse(null);
                } catch (Exception e) {
                    w = wkRepository.findById(id).orElse(null);
                    if (w == null) {
                        return null;
                    }
                }
            }
            if (w != null) {
                wkRepository.updateStatusKonfirmasiById(id, true);
                wkRepository.flush();
                w = wkRepository.findById(id).orElse(w);
            }
        } else {
            if (w != null) {
                wkRepository.updateStatusKonfirmasiById(id, false);
                wkRepository.flush();
                w = wkRepository.findById(id).orElse(w);
            } else {
                String kelas = "Belum Ditentukan";
                try {
                    wkRepository.insertWaliKelasRow(p.getId(), kelas, 0);
                    wkRepository.updateStatusKonfirmasiById(id, false);
                    wkRepository.flush();
                    w = wkRepository.findById(id).orElse(null);
                } catch (Exception e) {
                    return null;
                }
            }
        }
        
        if (w != null && w.getIdWaliKelas() == null) {
            w.setIdWaliKelas(0);
        }
        
        return w;
    }
}