package com.example.si_swa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.example.si_swa.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByIdAdmin(String idAdmin);
    
    @Modifying
    @Transactional
    @Query(value = "INSERT IGNORE INTO admin (id, id_admin) VALUES (?1, ?2)", nativeQuery = true)
    void insertAdminRow(Long id, String idAdmin);
}

