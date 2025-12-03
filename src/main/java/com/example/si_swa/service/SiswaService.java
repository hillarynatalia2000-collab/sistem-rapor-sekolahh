package com.example.si_swa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.si_swa.entity.Siswa;
import com.example.si_swa.entity.Person;
import com.example.si_swa.repository.SiswaRepository;
import com.example.si_swa.repository.PersonRepository;
import java.util.ArrayList;
import java.util.List;

@Service
public class SiswaService {
    @Autowired
    private SiswaRepository siswaRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MataPelajaranService mataPelajaranService;

    public List<Siswa> getAllSiswa() {
        List<Siswa> siswaFromTable = siswaRepository.findAllWithJoin();
        if (siswaFromTable == null) {
            siswaFromTable = new ArrayList<>();
        }
        
        java.util.Map<Long, Siswa> siswaMap = new java.util.HashMap<>();
        for (Siswa s : siswaFromTable) {
            if (s != null && s.getId() != null) {
                String role = s.getRole();
                if (role == null || "siswa".equalsIgnoreCase(role)) {
                    if (s.getIdSiswa() == null) {
                        s.setIdSiswa(0);
                    }
                    siswaMap.put(s.getId(), s);
                }
            }
        }
        
        List<Person> personList = personRepository.findAllByRoleSiswa();
        List<Siswa> result = new ArrayList<>();
        
        for (Person p : personList) {
            if (p != null && "siswa".equalsIgnoreCase(p.getRole())) {
                Siswa s = siswaMap.get(p.getId());
                if (s == null) {
                    s = new Siswa();
                    s.setId(p.getId());
                    s.setNama(p.getNama());
                    s.setTanggalLahir(p.getTanggalLahir());
                    s.setAlamat(p.getAlamat());
                    s.setNomorTelepon(p.getNomorTelepon());
                    s.setUsername(p.getUsername());
                    s.setPassword(p.getPassword());
                    s.setEmail(p.getEmail());
                    s.setRole("siswa");
                    s.setStatusKonfirmasi(false);
                    s.setIdSiswa(0);
                } else {
                    if (s.getNama() == null || s.getNama().isEmpty()) s.setNama(p.getNama());
                    if (s.getTanggalLahir() == null) s.setTanggalLahir(p.getTanggalLahir());
                    if (s.getAlamat() == null || s.getAlamat().isEmpty()) s.setAlamat(p.getAlamat());
                    if (s.getNomorTelepon() == null || s.getNomorTelepon().isEmpty()) s.setNomorTelepon(p.getNomorTelepon());
                    if (s.getUsername() == null || s.getUsername().isEmpty()) s.setUsername(p.getUsername());
                    if (s.getPassword() == null || s.getPassword().isEmpty()) s.setPassword(p.getPassword());
                    if (s.getEmail() == null || s.getEmail().isEmpty()) s.setEmail(p.getEmail());
                    if (s.getRole() == null || s.getRole().isEmpty()) s.setRole("siswa");
                }
                result.add(s);
            }
        }
        
        return result;
    }

    public Siswa addSiswa(Siswa obj) {
        Long id = null;
        obj.setId(id);
        if (obj.getIdSiswa() == null) {
            obj.setIdSiswa(0);
        }
        return siswaRepository.save(obj);
    }

    public Siswa addSiswaFromPerson(long personId, String nis, String kelas) {
        Person p = personRepository.findById(personId).orElse(null);
        if (p == null) return null;
        siswaRepository.insertSiswaRow(p.getId(), nis, kelas);
        Siswa s = new Siswa();
        s.setId(p.getId());
        s.setNama(p.getNama());
        s.setTanggalLahir(p.getTanggalLahir());
        s.setAlamat(p.getAlamat());
        s.setNomorTelepon(p.getNomorTelepon());
        s.setNis(nis);
        s.setKelas(kelas);
        s.setUsername(p.getUsername());
        s.setPassword(p.getPassword());
        s.setEmail(p.getEmail());
        s.setRole("siswa");
        s.setStatusKonfirmasi(false);
        s.setIdSiswa(0);
        return siswaRepository.save(s);
    }

    public Siswa getSiswaById(long id) {
        Siswa s = siswaRepository.findByIdWithJoin(id);
        if (s == null) {
            s = siswaRepository.findById(id).orElse(null);
        }
        if (s != null && (s.getRole() == null || !"siswa".equalsIgnoreCase(s.getRole()))) {
            return null;
        }
        if (s != null && s.getIdSiswa() == null) {
            s.setIdSiswa(0);
        }
        return s;
    }

    public Siswa updateSiswa(long id, Siswa obj) {
        Siswa existing = siswaRepository.findById(id).orElse(null);
        if (existing == null) return null;
        
        // Update field dari Person
        if (obj.getNama() != null && !obj.getNama().trim().isEmpty()) {
            existing.setNama(obj.getNama());
        }
        if (obj.getTanggalLahir() != null) {
            existing.setTanggalLahir(obj.getTanggalLahir());
        }
        if (obj.getAlamat() != null && !obj.getAlamat().trim().isEmpty()) {
            existing.setAlamat(obj.getAlamat());
        }
        if (obj.getNomorTelepon() != null && !obj.getNomorTelepon().trim().isEmpty()) {
            existing.setNomorTelepon(obj.getNomorTelepon());
        }
        if (obj.getEmail() != null && !obj.getEmail().trim().isEmpty()) {
            existing.setEmail(obj.getEmail());
        }
        
        // Update field dari Siswa
        if (obj.getNis() != null && !obj.getNis().trim().isEmpty()) {
        existing.setNis(obj.getNis());
        }
        if (obj.getKelas() != null && !obj.getKelas().trim().isEmpty()) {
        existing.setKelas(obj.getKelas());
        }
        if (obj.getNilai() != null) {
            existing.setNilai(obj.getNilai());
        }
        
        existing.setId(id);
        return siswaRepository.save(existing);
    }

    public void deleteSiswa(long id) {
        siswaRepository.deleteSiswaRowById(id);
    }

    public Siswa findSiswaByNIS(String nis) {
        Siswa s = siswaRepository.findByNis(nis);
        if (s != null && s.getIdSiswa() == null) {
            s.setIdSiswa(0);
        }
        return s;
    }

    public void assignMapel(long siswaId, java.util.List<String> mapelIds) {
        Siswa s = siswaRepository.findById(siswaId).orElse(null);
        if (s == null) return;
        if (s.getIdSiswa() == null) {
            s.setIdSiswa(0);
        }
        
        s.getMataPelajaran().clear();
        
        if (mapelIds != null && !mapelIds.isEmpty()) {
            for (String mapelId : mapelIds) {
                if (mapelId != null && !mapelId.trim().isEmpty()) {
                    com.example.si_swa.entity.MataPelajaran mp = mataPelajaranService.getById(mapelId.trim());
                    if (mp != null) {
                        s.getMataPelajaran().add(mp);
                    }
                }
            }
        }
        
        java.util.List<String> idList = new java.util.ArrayList<>();
        for (com.example.si_swa.entity.MataPelajaran mp : s.getMataPelajaran()) {
            if (mp != null && mp.getIdMataPelajaran() != null) {
                idList.add(mp.getIdMataPelajaran());
            }
        }
        s.setMapelList(String.join(",", idList));
        
        siswaRepository.save(s);
    }

    public boolean validasiRangeNilai(Float nilai) {
        if (nilai == null) return false;
        return nilai >= 0f && nilai <= 100f;
    }

    @org.springframework.transaction.annotation.Transactional
    public Siswa updateStatusKonfirmasi(long id, boolean status) {
        Person p = personRepository.findById(id).orElse(null);
        if (p == null || !"siswa".equalsIgnoreCase(p.getRole())) {
            return null;
        }
        
        Siswa s = siswaRepository.findById(id).orElse(null);
        
        if (status) {
            if (s == null) {
                String kelas = "Belum Ditentukan";
                try {
                    siswaRepository.insertSiswaRow(p.getId(), null, kelas);
                } catch (Exception e) {
                }
            }
            siswaRepository.updateStatusKonfirmasiById(id, true);
            s = siswaRepository.findById(id).orElse(null);
        } else {
            if (s != null) {
                siswaRepository.updateStatusKonfirmasiById(id, false);
                s = siswaRepository.findById(id).orElse(null);
            }
        }
        
        if (s != null && s.getIdSiswa() == null) {
            s.setIdSiswa(0);
        }
        
        return s;
    }

    public List<Siswa> getSiswaBelumKonfirmasi() {
        List<Siswa> all = getAllSiswa();
        List<Siswa> result = new java.util.ArrayList<>();
        for (Siswa s : all) {
            if (s != null && !s.getStatusKonfirmasi()) {
                result.add(s);
            }
        }
        return result;
    }

    public List<Siswa> getSiswaSudahKonfirmasi() {
        List<Siswa> all = getAllSiswa();
        List<Siswa> result = new java.util.ArrayList<>();
        for (Siswa s : all) {
            if (s != null && s.getStatusKonfirmasi()) {
                result.add(s);
            }
        }
        return result;
    }
}
