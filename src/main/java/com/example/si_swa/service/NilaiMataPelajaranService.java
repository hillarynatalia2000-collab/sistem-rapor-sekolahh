package com.example.si_swa.service;

import com.example.si_swa.entity.NilaiMataPelajaran;
import com.example.si_swa.repository.NilaiMataPelajaranRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NilaiMataPelajaranService {
    @Autowired
    private NilaiMataPelajaranRepository nilaiMataPelajaranRepository;

    public List<NilaiMataPelajaran> getBySiswaId(int idSiswa) {
        return nilaiMataPelajaranRepository.findByIdSiswa(idSiswa);
    }

    public NilaiMataPelajaran save(NilaiMataPelajaran nilai) {
        return nilaiMataPelajaranRepository.save(nilai);
    }

    public void saveAll(List<NilaiMataPelajaran> nilaiList) {
        nilaiMataPelajaranRepository.saveAll(nilaiList);
    }

    public void deleteBySiswaId(int idSiswa) {
        nilaiMataPelajaranRepository.deleteByIdSiswa(idSiswa);
    }

    public NilaiMataPelajaran getByIdSiswaAndIdMataPelajaran(int idSiswa, String idMataPelajaran) {
        List<NilaiMataPelajaran> list = nilaiMataPelajaranRepository.findByIdSiswaAndIdMataPelajaran(idSiswa, idMataPelajaran);
        return list.isEmpty() ? null : list.get(0);
    }
}

