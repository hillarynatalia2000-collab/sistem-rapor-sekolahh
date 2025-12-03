package com.example.si_swa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.example.si_swa.entity.Siswa;

public interface SiswaRepository extends JpaRepository<Siswa, Long> {
    Siswa findByNis(String nis);

    Siswa findByNomorTelepon(String nomorTelepon);

    @org.springframework.data.jpa.repository.Query("SELECT s FROM Siswa s WHERE s.id = ?1")
    Siswa findByIdWithJoin(Long id);
    
    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT s FROM Siswa s LEFT JOIN FETCH s.mataPelajaran")
    java.util.List<Siswa> findAllWithJoin();
    
    @org.springframework.data.jpa.repository.Query("SELECT s FROM Siswa s")
    java.util.List<Siswa> findAllSiswa();
    
    @Query(value = "SELECT s.*, p.* FROM siswa s INNER JOIN person p ON s.id = p.id WHERE p.role = 'siswa'", nativeQuery = true)
    java.util.List<Object[]> findAllSiswaNative();

    @Modifying
    @Transactional
    @Query(value = "INSERT IGNORE INTO siswa (id, nis, kelas, status_konfirmasi) VALUES (?1, NULLIF(?2, ''), ?3, false)", nativeQuery = true)
    void insertSiswaRow(Long id, String nis, String kelas);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM siswa WHERE id = ?1", nativeQuery = true)
    void deleteSiswaRowById(Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE siswa SET status_konfirmasi = ?2 WHERE id = ?1", nativeQuery = true)
    void updateStatusKonfirmasiById(Long id, boolean status);
}
