package com.example.si_swa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.si_swa.entity.Raport;
import java.util.List;

public interface RaportRepository extends JpaRepository<Raport, Long> {
    List<Raport> findBySiswaId(Long siswaId);
}