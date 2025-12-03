package com.example.si_swa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.List;
import com.example.si_swa.entity.WaliKelas;
import com.example.si_swa.service.WaliKelasService;
import com.example.si_swa.service.PersonService;
import com.example.si_swa.service.SiswaService;

@Controller
public class WaliKelasController {
    @Autowired
    private WaliKelasService service;

    @Autowired
    private PersonService personService;

    @Autowired
    private SiswaService siswaService;

    @GetMapping(value={"/walikelas", "/walikelas/"})
    public String page(Model model, jakarta.servlet.http.HttpSession session) {
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        // Hanya admin yang bisa akses
        if (!"admin".equals(allowedMenu)) {
            return "redirect:/login";
        }
        
        List<WaliKelas> list = service.getAllWaliKelasWithCounts();
        List<com.example.si_swa.entity.Person> personList = personService.getAllPerson();
        List<Long> wkIds = new java.util.ArrayList<>();
        for (WaliKelas wk : list) {
            if (wk != null && wk.getId() != null) {
                wkIds.add(wk.getId());
            }
        }
        List<Long> siswaIds = new java.util.ArrayList<>();
        for (com.example.si_swa.entity.Siswa s : siswaService.getAllSiswa()) {
            if (s != null && s.getId() != null) {
                siswaIds.add(s.getId());
            }
        }
        model.addAttribute("wkList", list);
        model.addAttribute("wkInfo", new WaliKelas());
        model.addAttribute("personList", personList);
        model.addAttribute("wkIds", wkIds);
        model.addAttribute("siswaIds", siswaIds);
        model.addAttribute("allowedMenu", "admin");
        return "walikelas.html";
    }

    @GetMapping("/walikelas/{id}")
    public String getRec(Model model, @PathVariable("id") long id, jakarta.servlet.http.HttpSession session) {
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        // Hanya admin yang bisa akses
        if (!"admin".equals(allowedMenu)) {
            return "redirect:/login";
        }
        
        List<WaliKelas> list = service.getAllWaliKelasWithCounts();
        WaliKelas rec = service.getWaliKelasById(id);
        if (rec == null) {
            return "redirect:/walikelas";
        }
        
        List<com.example.si_swa.entity.Person> personList = personService.getAllPerson();
        List<Long> wkIds = new java.util.ArrayList<>();
        for (WaliKelas wk : list) {
            if (wk != null && wk.getId() != null) {
                wkIds.add(wk.getId());
            }
        }
        List<Long> siswaIds = new java.util.ArrayList<>();
        for (com.example.si_swa.entity.Siswa s : siswaService.getAllSiswa()) {
            if (s != null && s.getId() != null) {
                siswaIds.add(s.getId());
            }
        }
        model.addAttribute("wkList", list);
        model.addAttribute("wkRec", rec);
        model.addAttribute("wkInfo", rec);
        model.addAttribute("personList", personList);
        model.addAttribute("wkIds", wkIds);
        model.addAttribute("siswaIds", siswaIds);
        model.addAttribute("allowedMenu", "admin");
        return "walikelas.html";
    }

    @PostMapping(value={"/walikelas/submit", "/walikelas/submit/", "/walikelas/submit/{id}"}, params={"add"})
    public String add(@ModelAttribute("wkInfo") WaliKelas wkInfo, org.springframework.web.servlet.mvc.support.RedirectAttributes ra,
                      jakarta.servlet.http.HttpSession session) {
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        if (!"admin".equals(allowedMenu)) {
            ra.addFlashAttribute("error", "Anda tidak memiliki izin untuk menambah wali kelas");
            return "redirect:/walikelas";
        }
        if (wkInfo.getNama() == null || wkInfo.getNama().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Nama harus diisi");
            return "redirect:/walikelas";
        }
        
        if (wkInfo.getTanggalLahir() == null) {
            ra.addFlashAttribute("error", "Tanggal lahir harus diisi");
            return "redirect:/walikelas";
        }
        
        if (wkInfo.getNomorTelepon() == null || wkInfo.getNomorTelepon().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Nomor telepon harus diisi");
            return "redirect:/walikelas";
        }
        
        if (wkInfo.getAlamat() == null || wkInfo.getAlamat().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Alamat harus diisi");
            return "redirect:/walikelas";
        }
        
        if (wkInfo.getUsername() == null || wkInfo.getUsername().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Username harus diisi");
            return "redirect:/walikelas";
        }
        
        com.example.si_swa.entity.Person existingPerson = personService.getByUsername(wkInfo.getUsername());
        if (existingPerson != null) {
            ra.addFlashAttribute("error", "Username sudah digunakan");
            return "redirect:/walikelas";
        }
        
        if (wkInfo.getPassword() == null || wkInfo.getPassword().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Password harus diisi");
            return "redirect:/walikelas";
        }
        
        if (wkInfo.getEmail() == null || wkInfo.getEmail().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Email harus diisi");
            return "redirect:/walikelas";
        }
        
        if (wkInfo.getNamaKelasWalian() == null || wkInfo.getNamaKelasWalian().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Nama Kelas Walian harus diisi");
            return "redirect:/walikelas";
        }
        
        if (wkInfo.getRole() == null || wkInfo.getRole().trim().isEmpty()) {
            wkInfo.setRole("walikelas");
        }
        
        wkInfo.setStatusKonfirmasi(false);
        service.addWaliKelas(wkInfo);
        return "redirect:/walikelas";
    }

    @PostMapping(value="/walikelas/submit/{id}", params={"edit"})
    public String edit(@ModelAttribute("wkInfo") WaliKelas wkInfo, @PathVariable("id") long id, 
                       org.springframework.web.servlet.mvc.support.RedirectAttributes ra,
                       jakarta.servlet.http.HttpSession session) {
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        if (!"admin".equals(allowedMenu)) {
            ra.addFlashAttribute("error", "Anda tidak memiliki izin untuk mengedit wali kelas");
            return "redirect:/walikelas";
        }
        
        WaliKelas existing = service.getWaliKelasById(id);
        if (existing == null) {
            ra.addFlashAttribute("error", "Wali Kelas tidak ditemukan");
            return "redirect:/walikelas";
        }
        
        // Admin bisa edit semua field termasuk nama dan kelas
        if (wkInfo.getNama() != null && !wkInfo.getNama().trim().isEmpty()) {
            existing.setNama(wkInfo.getNama());
        }
        existing.setTanggalLahir(wkInfo.getTanggalLahir());
        existing.setAlamat(wkInfo.getAlamat());
        existing.setNomorTelepon(wkInfo.getNomorTelepon());
        if (wkInfo.getNamaKelasWalian() != null && !wkInfo.getNamaKelasWalian().trim().isEmpty()) {
            existing.setNamaKelasWalian(wkInfo.getNamaKelasWalian());
        }
        if (wkInfo.getNip() != null && !wkInfo.getNip().trim().isEmpty()) {
            existing.setNip(wkInfo.getNip());
        }
        
        service.updateWaliKelas(id, existing);
        return "redirect:/walikelas";
    }

    @PostMapping(value="/walikelas/submit/{id}", params={"delete"})
    public String delete(@PathVariable("id") long id, org.springframework.web.servlet.mvc.support.RedirectAttributes ra,
                         jakarta.servlet.http.HttpSession session) {
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        if (!"admin".equals(allowedMenu)) {
            ra.addFlashAttribute("error", "Anda tidak memiliki izin untuk menghapus wali kelas");
            return "redirect:/walikelas";
        }
        
        WaliKelas waliKelas = service.getWaliKelasById(id);
        if (waliKelas == null) {
            ra.addFlashAttribute("error", "Wali Kelas tidak ditemukan");
            return "redirect:/walikelas";
        }
        
        service.deleteWaliKelas(id);
        ra.addFlashAttribute("success", "Wali Kelas " + waliKelas.getNama() + " berhasil dihapus");
        return "redirect:/walikelas";
    }

    @PostMapping(value={"/walikelas/konfirmasi/{id}/accept", "/walikelas/konfirmasi/{id}/accept/"})
    public String acceptWaliKelas(@PathVariable("id") long id, org.springframework.web.servlet.mvc.support.RedirectAttributes ra, jakarta.servlet.http.HttpSession session) {
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        if (!"admin".equals(allowedMenu)) {
            ra.addFlashAttribute("error", "Anda tidak memiliki izin untuk mengkonfirmasi wali kelas");
            return "redirect:/walikelas";
        }

        WaliKelas waliKelas = service.updateStatusKonfirmasi(id, true);
        if (waliKelas != null) {
            ra.addFlashAttribute("success", "Wali Kelas " + waliKelas.getNama() + " berhasil dikonfirmasi");
        } else {
            ra.addFlashAttribute("error", "Gagal mengkonfirmasi wali kelas");
        }
        return "redirect:/walikelas";
    }

    @PostMapping(value={"/walikelas/konfirmasi/{id}/reject", "/walikelas/konfirmasi/{id}/reject/"})
    public String rejectWaliKelas(@PathVariable("id") long id, org.springframework.web.servlet.mvc.support.RedirectAttributes ra, jakarta.servlet.http.HttpSession session) {
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        if (!"admin".equals(allowedMenu)) {
            ra.addFlashAttribute("error", "Anda tidak memiliki izin untuk menolak konfirmasi wali kelas");
            return "redirect:/walikelas";
        }

        WaliKelas waliKelas = service.updateStatusKonfirmasi(id, false);
        if (waliKelas != null) {
            ra.addFlashAttribute("success", "Konfirmasi Wali Kelas " + waliKelas.getNama() + " telah ditolak");
        } else {
            ra.addFlashAttribute("error", "Gagal menolak konfirmasi wali kelas");
        }
        return "redirect:/walikelas";
    }
}