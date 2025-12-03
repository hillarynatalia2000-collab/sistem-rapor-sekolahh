package com.example.si_swa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.si_swa.entity.MataPelajaran;

public interface MataPelajaranRepository extends JpaRepository<MataPelajaran, String> {
    MataPelajaran findByKode(String kode);
}