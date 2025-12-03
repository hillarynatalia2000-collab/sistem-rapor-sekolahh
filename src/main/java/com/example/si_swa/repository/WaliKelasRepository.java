package com.example.si_swa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.example.si_swa.entity.WaliKelas;

public interface WaliKelasRepository extends JpaRepository<WaliKelas, Long> {
    WaliKelas findByNomorTelepon(String nomorTelepon);
    @Modifying
    @Transactional
    @Query(value = "INSERT IGNORE INTO wali_kelas (id, nama_kelas_walian, jumlah_siswa_walian, status_konfirmasi) VALUES (?1, ?2, ?3, false)", nativeQuery = true)
    void insertWaliKelasRow(Long id, String namaKelasWalian, int jumlahSiswaWalian);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM wali_kelas WHERE id = ?1", nativeQuery = true)
    void deleteWaliKelasRowById(Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE wali_kelas SET status_konfirmasi = ?2 WHERE id = ?1", nativeQuery = true)
    void updateStatusKonfirmasiById(Long id, boolean status);
}