package com.example.si_swa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.si_swa.entity.Admin;
import com.example.si_swa.entity.Person;
import com.example.si_swa.repository.AdminRepository;
import com.example.si_swa.repository.PersonRepository;
import java.util.List;
import java.util.UUID;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PersonRepository personRepository;
    
    public List<Admin> getAllAdmin() {
        return adminRepository.findAll();
    }
    
    public Admin addAdmin(Admin obj) {
        obj.setId(null);
        if (obj.getIdAdmin() == null || obj.getIdAdmin().trim().isEmpty()) {
            // Generate ID admin otomatis jika tidak ada
            obj.setIdAdmin("ADM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        return adminRepository.save(obj);
    }
    
    @org.springframework.transaction.annotation.Transactional
    public Admin addAdminFromPerson(long personId, String idAdmin) {
        Person p = personRepository.findById(personId).orElse(null);
        if (p == null) return null;
        
        // Cek apakah admin sudah ada
        Admin existing = adminRepository.findById(personId).orElse(null);
        if (existing != null) {
            return existing; // Admin sudah ada, return yang sudah ada
        }
        
        // Generate ID admin otomatis jika tidak ada
        if (idAdmin == null || idAdmin.trim().isEmpty()) {
            idAdmin = "ADM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
        
        // Insert menggunakan native SQL
        try {
            adminRepository.insertAdminRow(p.getId(), idAdmin);
        } catch (Exception e) {
            // Jika insert gagal (misalnya karena sudah ada), coba ambil dari database
            existing = adminRepository.findById(personId).orElse(null);
            if (existing != null) {
                return existing;
            }
            // Jika masih null, throw exception
            throw new RuntimeException("Gagal membuat admin: " + e.getMessage(), e);
        }
        
        // Ambil admin yang baru di-insert dari database (tidak perlu save lagi)
        Admin a = adminRepository.findById(personId).orElse(null);
        if (a == null) {
            // Jika masih null setelah insert, buat entity baru (tidak perlu save karena sudah di-insert via native SQL)
            a = new Admin();
            a.setId(p.getId());
            a.setNama(p.getNama());
            a.setTanggalLahir(p.getTanggalLahir());
            a.setAlamat(p.getAlamat());
            a.setNomorTelepon(p.getNomorTelepon());
            a.setUsername(p.getUsername());
            a.setPassword(p.getPassword());
            a.setEmail(p.getEmail());
            a.setRole("admin");
            a.setIdAdmin(idAdmin);
        }
        
        return a;
    }
    
    public Admin getAdminById(long id) {
        return adminRepository.findById(id).orElse(null);
    }
    
    public Admin getAdminByUsername(String username) {
        Person p = personRepository.findByUsername(username);
        if (p == null || !"admin".equalsIgnoreCase(p.getRole())) {
            return null;
        }
        return adminRepository.findById(p.getId()).orElse(null);
    }
    
    public Admin updateAdmin(long id, Admin obj) {
        Admin existing = adminRepository.findById(id).orElse(null);
        if (existing == null) return null;
        
        if (obj.getNama() != null) existing.setNama(obj.getNama());
        if (obj.getTanggalLahir() != null) existing.setTanggalLahir(obj.getTanggalLahir());
        if (obj.getAlamat() != null) existing.setAlamat(obj.getAlamat());
        if (obj.getNomorTelepon() != null) existing.setNomorTelepon(obj.getNomorTelepon());
        if (obj.getUsername() != null) existing.setUsername(obj.getUsername());
        if (obj.getPassword() != null) existing.setPassword(obj.getPassword());
        if (obj.getEmail() != null) existing.setEmail(obj.getEmail());
        if (obj.getIdAdmin() != null && !obj.getIdAdmin().trim().isEmpty()) {
            existing.setIdAdmin(obj.getIdAdmin());
        }
        
        return adminRepository.save(existing);
    }
    
    public void deleteAdmin(long id) {
        adminRepository.deleteById(id);
    }
}

