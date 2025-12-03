package com.example.si_swa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.si_swa.entity.Person;
import com.example.si_swa.repository.PersonRepository;
import java.util.List;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public List<Person> getAllPerson() {
        return personRepository.findAll();
    }

    public Person addPerson(Person obj) {
        Long id = null;
        obj.setId(id);
        return personRepository.save(obj);
    }

    public Person getPersonById(long id) {
        return personRepository.findById(id).orElse(null);
    }

    public Person getByNomorTelepon(String tel) {
        return personRepository.findByNomorTelepon(tel);
    }

    public Person getByUsername(String username) {
        return personRepository.findByUsername(username);
    }

    public Person updatePerson(long id, Person obj) {
        Person existing = personRepository.findById(id).orElse(null);
        if (existing == null) return null;
        if (obj.getNama() != null) existing.setNama(obj.getNama());
        if (obj.getTanggalLahir() != null) existing.setTanggalLahir(obj.getTanggalLahir());
        if (obj.getAlamat() != null) existing.setAlamat(obj.getAlamat());
        if (obj.getNomorTelepon() != null) existing.setNomorTelepon(obj.getNomorTelepon());
        if (obj.getUsername() != null) existing.setUsername(obj.getUsername());
        if (obj.getPassword() != null) existing.setPassword(obj.getPassword());
        if (obj.getEmail() != null) existing.setEmail(obj.getEmail());
        return personRepository.save(existing);
    }

    public void deletePerson(long id) {
        personRepository.deleteById(id);
    }
}
