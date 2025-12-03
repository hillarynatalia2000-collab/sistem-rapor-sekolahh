package com.example.si_swa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@jakarta.persistence.Table(name = "person")
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int idPerson;

    @Column(name = "nama")
    private String nama;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "tanggal_lahir")
    private Date tanggalLahir;

    @Column(name = "alamat")
    private String alamat;

    @Column(name = "nomor_telepon")
    private String nomorTelepon;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    private String role;

    public Person() {
    }

    public Person(String n, Date t, String a, String nt, String u, String p, String e, String role) {
        this.nama = n;
        this.tanggalLahir = t;
        this.alamat = a;
        this.nomorTelepon = nt;
        this.username = u;
        this.password = p;
        this.email = e;
        this.role = role;
    }

    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }

    public int getIdPerson() {
        return idPerson;
    }

    // Backward compatibility - keep getId() for existing code
    public Long getId() {
        return (long) idPerson;
    }

    public void setId(Long id) {
        this.idPerson = id != null ? id.intValue() : 0;
    }

    public void setNama(String n) {
        this.nama = n;
    }

    public String getNama() {
        return nama;
    }

    public void setTanggalLahir(Date t) {
        this.tanggalLahir = t;
    }

    public Date getTanggalLahir() {
        return tanggalLahir;
    }

    public void setAlamat(String a) {
        this.alamat = a;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setNomorTelepon(String nt) {
        this.nomorTelepon = nt;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setUsername(String u) {
        this.username = u;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String p) {
        this.password = p;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String e) {
        this.email = e;
    }

    public String getEmail() {
        return email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public boolean isUsernameValid() {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        // Username harus minimal 3 karakter dan hanya mengandung alphanumeric
        return username.length() >= 3 && username.matches("^[a-zA-Z0-9]+$");
    }

    public boolean checkPassword(String inputPassword) {
        if (password == null || inputPassword == null) {
            return false;
        }
        return password.equals(inputPassword);
    }

    public void abstrakMethod() {}

    @Override
    public String toString() {
        return "Person{" +
                "idPerson=" + idPerson +
                ", nama='" + nama + '\'' +
                ", tanggalLahir=" + tanggalLahir +
                ", alamat='" + alamat + '\'' +
                ", nomorTelepon='" + nomorTelepon + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public int generateId() {
        // Generate ID berdasarkan timestamp atau random
        // Untuk implementasi sederhana, bisa menggunakan kombinasi timestamp
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }
}
