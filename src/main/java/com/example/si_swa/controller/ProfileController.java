package com.example.si_swa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.si_swa.service.PersonService;
import com.example.si_swa.service.SiswaService;
import com.example.si_swa.service.WaliKelasService;
import com.example.si_swa.service.AdminService;
import com.example.si_swa.entity.Person;
import com.example.si_swa.entity.Siswa;
import com.example.si_swa.entity.WaliKelas;
import com.example.si_swa.entity.Admin;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ProfileController {
    @Autowired
    private PersonService personService;

    @Autowired
    private SiswaService siswaService;

    @Autowired
    private WaliKelasService waliKelasService;
    
    @Autowired
    private AdminService adminService;

    @GetMapping(value={"/profile", "/profile/"})
    public String profilePage(Model model, jakarta.servlet.http.HttpSession session) {
        if (session == null) {
            return "redirect:/login";
        }
        
        Object allowedMenuObj = session.getAttribute("allowedMenu");
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        if (allowedMenu == null || allowedMenu.trim().isEmpty()) {
            return "redirect:/login";
        }

        Person person = null;
        Siswa siswa = null;
        WaliKelas waliKelas = null;
        Admin admin = null;
        Long personId = null;

        Object sid = session.getAttribute("siswaId");
        Object wid = session.getAttribute("waliKelasId");
        Object loginName = session.getAttribute("loginName");
        Object adminUsernameObj = session.getAttribute("adminUsername");

        if (sid != null) {
            Long siswaId = sid instanceof Long ? (Long) sid : (sid != null ? Long.valueOf(sid.toString()) : null);
            if (siswaId != null) {
                siswa = siswaService.getSiswaById(siswaId);
                if (siswa != null) {
                    person = personService.getPersonById(siswa.getId());
                    personId = siswa.getId();
                }
            }
        } else if (wid != null) {
            Long waliKelasId = wid instanceof Long ? (Long) wid : (wid != null ? Long.valueOf(wid.toString()) : null);
            if (waliKelasId != null) {
                waliKelas = waliKelasService.getWaliKelasById(waliKelasId);
                if (waliKelas != null) {
                    person = personService.getPersonById(waliKelas.getId());
                    personId = waliKelas.getId();
                }
            }
        } else if ("admin".equals(allowedMenu)) {
            // User adalah admin - cari berdasarkan username dari session
            if (adminUsernameObj != null) {
                String adminUsername = adminUsernameObj.toString();
                if (adminUsername != null && !adminUsername.trim().isEmpty()) {
                    person = personService.getByUsername(adminUsername.trim());
                    if (person != null) {
                        personId = person.getId();
                        admin = adminService.getAdminById(personId);
                    }
                }
            }
            // Fallback: jika tidak ditemukan berdasarkan username, cari berdasarkan nama
            if (person == null && loginName != null) {
                String loginNameStr = loginName.toString();
                if (loginNameStr != null && !loginNameStr.trim().isEmpty()) {
                    java.util.List<Person> allPersons = personService.getAllPerson();
                    for (Person p : allPersons) {
                        if (p != null && "admin".equalsIgnoreCase(p.getRole()) && loginNameStr.equals(p.getNama())) {
                            person = p;
                            personId = p.getId();
                            admin = adminService.getAdminById(personId);
                            if (p.getUsername() != null) {
                                session.setAttribute("adminUsername", p.getUsername());
                            }
                            break;
                        }
                    }
                }
            }
            // Fallback kedua: jika masih null, cari admin pertama yang ada (untuk recovery)
            if (person == null) {
                java.util.List<Person> allPersons = personService.getAllPerson();
                for (Person p : allPersons) {
                    if (p != null && "admin".equalsIgnoreCase(p.getRole())) {
                        person = p;
                        personId = p.getId();
                        admin = adminService.getAdminById(personId);
                        if (p.getUsername() != null) {
                            session.setAttribute("adminUsername", p.getUsername());
                        }
                        if (p.getNama() != null) {
                            session.setAttribute("loginName", p.getNama());
                        }
                        break;
                    }
                }
            }
        }
        
        // Fallback terakhir: jika person masih null, coba cari berdasarkan loginName dan allowedMenu
        if (person == null && loginName != null) {
            String loginNameStr = loginName.toString();
            if (loginNameStr != null && !loginNameStr.trim().isEmpty()) {
                java.util.List<Person> allPersons = personService.getAllPerson();
                for (Person p : allPersons) {
                    if (p != null && p.getNama() != null && loginNameStr.equals(p.getNama())) {
                        // Cek role sesuai allowedMenu
                        if (("siswa".equals(allowedMenu) && "siswa".equalsIgnoreCase(p.getRole())) ||
                            ("all".equals(allowedMenu) && "walikelas".equalsIgnoreCase(p.getRole())) ||
                            ("admin".equals(allowedMenu) && "admin".equalsIgnoreCase(p.getRole()))) {
                            person = p;
                            personId = p.getId();
                            if ("siswa".equals(allowedMenu)) {
                                siswa = siswaService.getSiswaById(personId);
                            } else if ("all".equals(allowedMenu)) {
                                waliKelas = waliKelasService.getWaliKelasById(personId);
                            } else if ("admin".equals(allowedMenu)) {
                                admin = adminService.getAdminById(personId);
                                if (p.getUsername() != null) {
                                    session.setAttribute("adminUsername", p.getUsername());
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }

        if (person == null) {
            return "redirect:/login";
        }
        
        if ("admin".equals(allowedMenu) && admin == null && personId != null) {
            admin = adminService.getAdminById(personId);
        }

        model.addAttribute("person", person);
        model.addAttribute("siswa", siswa);
        model.addAttribute("waliKelas", waliKelas);
        model.addAttribute("admin", admin);
        model.addAttribute("personId", personId);
        model.addAttribute("allowedMenu", allowedMenu);

        return "profile.html";
    }

    @PostMapping(value={"/profile/update", "/profile/update/"})
    public String updateProfile(
            @RequestParam(value = "nama", required = false) String nama,
            @RequestParam(value = "tanggalLahir", required = false) String tanggalLahirStr,
            @RequestParam(value = "alamat", required = false) String alamat,
            @RequestParam(value = "nomorTelepon", required = false) String nomorTelepon,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "passwordConfirm", required = false) String passwordConfirm,
            RedirectAttributes ra,
            jakarta.servlet.http.HttpSession session) {
        
        if (session == null) {
            ra.addFlashAttribute("error", "Session tidak valid");
            return "redirect:/login";
        }
        
        Object allowedMenuObj = session.getAttribute("allowedMenu");
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        if (allowedMenu == null || allowedMenu.trim().isEmpty()) {
            ra.addFlashAttribute("error", "Anda harus login terlebih dahulu");
            return "redirect:/login";
        }

        Person person = null;
        Long personId = null;

        Object sid = session.getAttribute("siswaId");
        Object wid = session.getAttribute("waliKelasId");
        Object loginName = session.getAttribute("loginName");
        Object adminUsernameObj = session.getAttribute("adminUsername");

        if (sid != null) {
            Long siswaId = sid instanceof Long ? (Long) sid : (sid != null ? Long.valueOf(sid.toString()) : null);
            if (siswaId != null) {
                Siswa siswa = siswaService.getSiswaById(siswaId);
                if (siswa != null) {
                    person = personService.getPersonById(siswa.getId());
                    personId = siswa.getId();
                }
            }
        } else if (wid != null) {
            Long waliKelasId = wid instanceof Long ? (Long) wid : (wid != null ? Long.valueOf(wid.toString()) : null);
            if (waliKelasId != null) {
                WaliKelas waliKelas = waliKelasService.getWaliKelasById(waliKelasId);
                if (waliKelas != null) {
                    person = personService.getPersonById(waliKelas.getId());
                    personId = waliKelas.getId();
                }
            }
        } else if ("admin".equals(allowedMenu)) {
            // User adalah admin - cari berdasarkan username dari session
            if (adminUsernameObj != null) {
                String adminUsername = adminUsernameObj.toString();
                if (adminUsername != null && !adminUsername.trim().isEmpty()) {
                    person = personService.getByUsername(adminUsername.trim());
                    if (person != null) {
                        personId = person.getId();
                    }
                }
            }
            // Fallback: jika tidak ditemukan berdasarkan username, cari berdasarkan nama
            if (person == null && loginName != null) {
                String loginNameStr = loginName.toString();
                if (loginNameStr != null && !loginNameStr.trim().isEmpty()) {
                    java.util.List<Person> allPersons = personService.getAllPerson();
                    for (Person p : allPersons) {
                        if (p != null && "admin".equalsIgnoreCase(p.getRole()) && loginNameStr.equals(p.getNama())) {
                            person = p;
                            personId = p.getId();
                            if (p.getUsername() != null) {
                                session.setAttribute("adminUsername", p.getUsername());
                            }
                            break;
                        }
                    }
                }
            }
            // Fallback kedua: jika masih null, cari admin pertama yang ada (untuk recovery)
            if (person == null) {
                java.util.List<Person> allPersons = personService.getAllPerson();
                for (Person p : allPersons) {
                    if (p != null && "admin".equalsIgnoreCase(p.getRole())) {
                        person = p;
                        personId = p.getId();
                        if (p.getUsername() != null) {
                            session.setAttribute("adminUsername", p.getUsername());
                        }
                        if (p.getNama() != null) {
                            session.setAttribute("loginName", p.getNama());
                        }
                        break;
                    }
                }
            }
        }
        
        // Fallback terakhir: jika person masih null, coba cari berdasarkan loginName dan allowedMenu
        if (person == null && loginName != null) {
            String loginNameStr = loginName.toString();
            if (loginNameStr != null && !loginNameStr.trim().isEmpty()) {
                java.util.List<Person> allPersons = personService.getAllPerson();
                for (Person p : allPersons) {
                    if (p != null && p.getNama() != null && loginNameStr.equals(p.getNama())) {
                        // Cek role sesuai allowedMenu
                        if (("siswa".equals(allowedMenu) && "siswa".equalsIgnoreCase(p.getRole())) ||
                            ("all".equals(allowedMenu) && "walikelas".equalsIgnoreCase(p.getRole())) ||
                            ("admin".equals(allowedMenu) && "admin".equalsIgnoreCase(p.getRole()))) {
                            person = p;
                            personId = p.getId();
                            if ("admin".equals(allowedMenu) && p.getUsername() != null) {
                                session.setAttribute("adminUsername", p.getUsername());
                            }
                            break;
                        }
                    }
                }
            }
        }

        if (person == null) {
            ra.addFlashAttribute("error", "User tidak ditemukan");
            return "redirect:/profile";
        }

        if (nama != null && !nama.trim().isEmpty()) {
            person.setNama(nama.trim());
        }
        
        if (alamat != null && !alamat.trim().isEmpty()) {
            person.setAlamat(alamat.trim());
        }
        
        if (nomorTelepon != null && !nomorTelepon.trim().isEmpty()) {
            if (!nomorTelepon.matches("^0[0-9]{9,13}$")) {
                ra.addFlashAttribute("error", "Nomor telepon harus diawali 0 dan 10-14 digit");
                return "redirect:/profile";
            }
            person.setNomorTelepon(nomorTelepon.trim());
        }
        
        if (email != null && !email.trim().isEmpty()) {
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                ra.addFlashAttribute("error", "Format email tidak valid");
                return "redirect:/profile";
            }
            person.setEmail(email.trim());
        }
        
        if (username != null && !username.trim().isEmpty()) {
            Person existingByUsername = personService.getByUsername(username.trim());
            if (existingByUsername != null && !existingByUsername.getId().equals(personId)) {
                ra.addFlashAttribute("error", "Username sudah digunakan");
                return "redirect:/profile";
            }
            person.setUsername(username.trim());
        }
        
        if (password != null && !password.trim().isEmpty()) {
            if (passwordConfirm == null || !password.equals(passwordConfirm)) {
                ra.addFlashAttribute("error", "Password dan konfirmasi password tidak sama");
                return "redirect:/profile";
            }
            if (password.length() < 6) {
                ra.addFlashAttribute("error", "Password minimal 6 karakter");
                return "redirect:/profile";
            }
            person.setPassword(password.trim());
        }
        
        if (tanggalLahirStr != null && !tanggalLahirStr.trim().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date tanggalLahir = sdf.parse(tanggalLahirStr.trim());
                person.setTanggalLahir(tanggalLahir);
            } catch (Exception e) {
                ra.addFlashAttribute("error", "Format tanggal lahir tidak valid (yyyy-MM-dd)");
                return "redirect:/profile";
            }
        }

        personService.updatePerson(person.getId(), person);
        
        if (nama != null && !nama.trim().isEmpty() && session != null) {
            session.setAttribute("loginName", nama.trim());
        }
        
        if (username != null && !username.trim().isEmpty() && session != null && "admin".equals(allowedMenu)) {
            session.setAttribute("adminUsername", username.trim());
        }

        ra.addFlashAttribute("success", "Profile berhasil diperbarui");
        return "redirect:/profile";
    }
}

