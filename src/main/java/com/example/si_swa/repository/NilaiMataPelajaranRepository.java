package com.example.si_swa.repository;

import com.example.si_swa.entity.NilaiMataPelajaran;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NilaiMataPelajaranRepository extends JpaRepository<NilaiMataPelajaran, Integer> {
    List<NilaiMataPelajaran> findByIdSiswa(int idSiswa);
    
    List<NilaiMataPelajaran> findByIdSiswaAndIdMataPelajaran(int idSiswa, String idMataPelajaran);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM NilaiMataPelajaran n WHERE n.idSiswa = :idSiswa")
    void deleteByIdSiswa(@Param("idSiswa") int idSiswa);
}

