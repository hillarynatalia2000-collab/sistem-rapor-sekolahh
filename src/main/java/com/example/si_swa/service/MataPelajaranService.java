package com.example.si_swa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.si_swa.entity.MataPelajaran;
import com.example.si_swa.repository.MataPelajaranRepository;
import java.util.List;

@Service
public class MataPelajaranService {
    @Autowired
    private MataPelajaranRepository mataPelajaranRepository;

    public List<MataPelajaran> getAll() {
        return mataPelajaranRepository.findAll();
    }

    public MataPelajaran add(MataPelajaran obj) {
        return mataPelajaranRepository.save(obj);
    }

    public MataPelajaran getById(String id) {
        return mataPelajaranRepository.findById(id).orElse(null);
    }

    public MataPelajaran update(String id, MataPelajaran obj) {
        MataPelajaran existing = mataPelajaranRepository.findById(id).orElse(null);
        if (existing == null) return null;
        existing.setNama(obj.getNama());
        existing.setKode(obj.getKode());
        existing.setJumlahJam(obj.getJumlahJam());
        return mataPelajaranRepository.save(existing);
    }

    public void delete(String id) {
        mataPelajaranRepository.deleteById(id);
    }

    public List<MataPelajaran> findByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) return java.util.Collections.emptyList();
        return mataPelajaranRepository.findAllById(ids);
    }

    public MataPelajaranRepository getRepository() {
        return mataPelajaranRepository;
    }
}