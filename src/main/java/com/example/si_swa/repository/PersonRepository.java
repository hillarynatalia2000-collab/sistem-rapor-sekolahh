package com.example.si_swa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.si_swa.entity.Person;
import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Person findByNomorTelepon(String nomorTelepon);
    Person findByUsername(String username);
    
    @Query("SELECT p FROM Person p WHERE p.role = 'siswa'")
    List<Person> findAllByRoleSiswa();
    
    @Query("SELECT p FROM Person p WHERE p.role = 'walikelas'")
    List<Person> findAllByRoleWalikelas();
}
